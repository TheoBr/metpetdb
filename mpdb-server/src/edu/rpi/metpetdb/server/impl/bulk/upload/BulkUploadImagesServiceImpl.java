package edu.rpi.metpetdb.server.impl.bulk.upload;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.media.jai.RenderedOp;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.validation.InvalidImageException;
import edu.rpi.metpetdb.client.error.validation.PropertyRequiredException;
import edu.rpi.metpetdb.client.model.BulkUploadResult;
import edu.rpi.metpetdb.client.model.BulkUploadResultCount;
import edu.rpi.metpetdb.client.model.Grid;
import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.ImageOnGrid;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.XrayImage;
import edu.rpi.metpetdb.client.service.bulk.upload.BulkUploadImagesService;
import edu.rpi.metpetdb.server.ImageUploadServlet;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.bulk.upload.ImageParser;
import edu.rpi.metpetdb.server.dao.impl.GridDAO;
import edu.rpi.metpetdb.server.dao.impl.ImageDAO;
import edu.rpi.metpetdb.server.dao.impl.ImageOnGridDAO;
import edu.rpi.metpetdb.server.dao.impl.SampleDAO;
import edu.rpi.metpetdb.server.dao.impl.SubsampleDAO;
import edu.rpi.metpetdb.server.dao.impl.XrayImageDAO;

public class BulkUploadImagesServiceImpl extends BulkUploadService implements
		BulkUploadImagesService {
	private static final long serialVersionUID = 1L;

	private void setRealImage(ZipFile zp, Image img, String prefix)
			throws IOException {
		// Get Image Data from Zip
		ZipEntry ze = zp.getEntry(prefix + img.getFilename());
		final byte[] imgData = getBytesFromInputStream(zp.getInputStream(ze));

		// Save Image Data, in various forms, to the server
		RenderedOp ro = ImageUploadServlet.loadImage(imgData);
		img.setChecksum(ImageUploadServlet.generateFullsize(ro, false));
		img.setChecksum64x64(ImageUploadServlet.generate64x64(ro, false));
		img.setChecksumHalf(ImageUploadServlet.generateHalf(ro, false));
		img.setWidth(ro.getWidth());
		img.setHeight(ro.getHeight());
	}
	private ZipEntry getSpreadsheetName(InputStream is) throws IOException {
		ZipInputStream zis = new ZipInputStream(is);
		ZipEntry ent;
		final ArrayList<ZipEntry> discoveredSpreadsheets = new ArrayList<ZipEntry>();
		while ((ent = zis.getNextEntry()) != null) {
			String entryName = ent.getName();

			// Ignore anything that is in a' hidden' directory
			if (entryName.startsWith("__") || entryName.startsWith("."))
				continue;

			// Implicit assumption that there will only be _one_ xls spreadsheet
			if (entryName.toLowerCase().contains(".xls")) {
				discoveredSpreadsheets.add(ent);
			}
		}
		// if there is only one spreadsheet then use that one
		if (discoveredSpreadsheets.size() == 1)
			return discoveredSpreadsheets.get(0);
		else {
			// otherwise find the one that matches the name' image_upload.xls'
			for (ZipEntry ze : discoveredSpreadsheets) {
				if (ze.getName().toLowerCase().contains("image_upload.xls")) {
					return ze;
				}
			}
		}
		return null;
	}

	private byte[] getBytesFromInputStream(InputStream is) throws IOException {
		// Read Data into byte sequence
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] tmpSpace = new byte[2048];
		int amount = 0;
		while ((amount = is.read(tmpSpace, 0, tmpSpace.length)) > -1)
			baos.write(tmpSpace, 0, amount);
		return baos.toByteArray();
	}

	public BulkUploadResult parser(String fileOnServer, boolean save)
			throws InvalidFormatException, LoginRequiredException {
		final BulkUploadResult results = new BulkUploadResult();
		try {
			if (save) {
				currentSession()
						.createSQLQuery(
								"UPDATE uploaded_files SET user_id = :user_id WHERE hash = :hash")
						.setParameter("user_id", currentUser()).setParameter(
								"hash", fileOnServer).executeUpdate();
			}
			// Find the Excel Spreadsheet
			FileInputStream is = new FileInputStream(MpDbServlet
					.getFileUploadPath()
					+ fileOnServer);
			ZipEntry spreadsheet = getSpreadsheetName(is);
			if (spreadsheet == null)
				throw new IllegalStateException("No Excel Spreasheet found");
			String spreadsheetPrefix = (new File(spreadsheet.getName()))
					.getParent();
			if (spreadsheetPrefix == null)
				spreadsheetPrefix = "";
			else
				spreadsheetPrefix += File.separator;

			ZipFile zp = new ZipFile(MpDbServlet.getFileUploadPath()
					+ fileOnServer);
			final ImageParser ip = new ImageParser(zp
					.getInputStream(spreadsheet));
			ip.parse();
			final Map<Integer, Image> images = ip.getImages();
			final Map<Integer, ImageOnGrid> imagesOnGrid = ip.getImagesOnGrid();
			final BulkUploadResultCount ssResultCount = new BulkUploadResultCount();
			final BulkUploadResultCount imgResultCount = new BulkUploadResultCount();
			User u = new User();
			u.setId(currentUser());
			// Keeps track of existing/new subsample names for each sample
			final Map<String, Collection<String>> subsampleNames = new HashMap<String, Collection<String>>();
			// maps a sample alias to an actual sample
			final Map<String, Sample> samples = new HashMap<String, Sample>();
			// maps a sample alias + subsample name to an actual subsample
			final Map<String, Subsample> subsamples = new HashMap<String, Subsample>();
			final SubsampleDAO ssDAO = new SubsampleDAO(this.currentSession());
			final SampleDAO sDAO = new SampleDAO(this.currentSession());
			final ImageDAO dao = new ImageDAO(this.currentSession());
			final Iterator<Integer> imgRows = images.keySet().iterator();
			while(imgRows.hasNext()) {
				final int row = imgRows.next();
				final Image img = images.get(row);
				// Confirm the filename is in the zip
				if (zp.getEntry(spreadsheetPrefix + img.getFilename()) == null) {
					results.addError(row, new InvalidImageException(
							spreadsheetPrefix + img.getFilename()));
				} else {
					try {
						// see if our sample exists
						Sample s = img.getSample();
						s.setOwner(u);
						img.getSubsample().setOwner(u);
						img.getSubsample().setPublicData(false);
						try {
							// if we don't have this sample already loaded check
							// for it in the database
							if (!samples.containsKey(s.getAlias())) {
								s = sDAO.fill(s);
								samples.put(s.getAlias(), s);
								subsampleNames.put(s.getAlias(),
										new HashSet<String>());
							} else {
								s = samples.get(s.getAlias());
							}
						} catch (DAOException e) {
							// There is no sample we have to add an error
							// Every Image needs a sample so add an error
							results.addError(row,
									new PropertyRequiredException("Sample"));
							continue;
						}
						Subsample ss = (img.getSubsample());
						ss.setSample(s);
						if (ss != null) {
							// if we don't have the name stored already we need
							// to load the subsample
							if (!subsampleNames.get(s.getAlias()).contains(
									ss.getName())) {
								try {
									doc.validate(ss);
									ss = ssDAO.fill(ss);
									subsamples.put(s.getAlias() + ss.getName(),
											ss);
									img.setSubsample(ss);
									ssResultCount.incrementOld();
								} catch (DAOException e) {
									// Means it is new because we could not find
									// it
									ssResultCount.incrementFresh();
									if (save) {
										try {
											ss = ssDAO.save(ss);
										} catch (DAOException e1) {
											results.addError(row, e1);
										}
										subsamples.put(s.getAlias()
												+ ss.getName(), ss);
										img.setSubsample(ss);
									}
								}
								subsampleNames.get(img.getSample().getAlias())
										.add(img.getSubsample().getName());
							} else {
								img.setSubsample(subsamples.get(s.getAlias()
										+ ss.getName()));
							}
						} else {
							// Every Image needs a subsample so add an error
							results.addError(row,
									new PropertyRequiredException("Subsample"));
						}
						doc.validate(img);
						if (dao.isNew(img))
							imgResultCount.incrementFresh();
						else
							imgResultCount.incrementOld();
						if (save) {
							setRealImage(zp, img, spreadsheetPrefix);
							try {
								if (img instanceof XrayImage) {
									(new XrayImageDAO(this.currentSession()))
											.save((XrayImage) img);
								} else {
									(new ImageDAO(this.currentSession()))
											.save(img);
								}
							} catch (DAOException e1) {
								results.addError(row, e1);
							}
						}
					} catch (ValidationException e) {
						results.addError(row, e);
						imgResultCount.incrementInvalid();
					}
				}
			}

			final Iterator<Integer> iogRows = imagesOnGrid.keySet().iterator();
			while(iogRows.hasNext()) {
				final int row = iogRows.next();
				final ImageOnGrid iog = imagesOnGrid.get(row);
				Image img = iog.getImage();
				// Confirm the filename is in the zip
				if (zp.getEntry(spreadsheetPrefix + img.getFilename()) == null) {
					results.addError(row, new InvalidImageException(
							spreadsheetPrefix + img.getFilename()));
				}
				try {
					doc.validate(img);
					if (save) {
						setRealImage(zp, iog.getImage(), spreadsheetPrefix);

						// Set image information for the Grid Copy
						iog.setGchecksum(iog.getImage().getChecksum());
						iog
								.setGchecksum64x64(iog.getImage()
										.getChecksum64x64());
						iog.setGchecksumHalf(iog.getImage().getChecksumHalf());
						iog.setGheight(iog.getImage().getHeight());
						iog.setGwidth(iog.getImage().getWidth());
						try {
							saveIncompleteImageOnGrid(iog);
						} catch (MpDbException e) {
							results.addError(0, e);
						}

					}
				} catch (ValidationException e) {
					results.addError(row, e);
				}
			}
			results.addResultCount("Subsamples", ssResultCount);
			results.addResultCount("Images", imgResultCount);
			results.setHeaders(ip.getHeaders());
			if (save && results.getErrors().isEmpty()) {
				try {
					commit();
				} catch (DAOException e1) {
					results.addError(0, e1);
				}
			}
		} catch (final IOException ioe) {
			throw new IllegalStateException(ioe.getMessage());
		}
		return results;
	}
	protected ImageOnGrid saveIncompleteImageOnGrid(ImageOnGrid iog)
			throws ValidationException, LoginRequiredException, DAOException {
		// First save the image
		doc.validate(iog.getImage());
		Image i = (iog.getImage());
		i = (new ImageDAO(this.currentSession())).save(i);
		iog.setImage((Image) (i));

		// Set the grid, either find the appropriate old one, or a new one
		Grid g = iog.getImage().getSubsample().getGrid();
		if (g == null) {
			// Create New Grid
			g = new Grid();
			g.setSubsample(iog.getImage().getSubsample());

			if (g.getSubsample().getSample().getOwner().getId() != currentUser())
				throw new SecurityException(
						"Cannot modify grids you don't own.");
			g = (new GridDAO(this.currentSession())).save(g);
		}
		iog.setGrid(g);

		// Save ImageOnGrid
		iog = (new ImageOnGridDAO(this.currentSession())).save(iog);
		return (iog);
	}
}
