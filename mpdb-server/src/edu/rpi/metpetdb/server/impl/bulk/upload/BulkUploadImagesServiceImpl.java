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
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.media.jai.RenderedOp;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.dao.SubsampleNotFoundException;
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

	public void commit(final String fileOnServer)
			throws InvalidFormatException, LoginRequiredException, DAOException {
		try {
			currentSession()
					.createSQLQuery(
							"UPDATE uploaded_files SET user_id = :user_id WHERE hash = :hash")
					.setParameter("user_id", currentUser()).setParameter(
							"hash", fileOnServer).executeUpdate();
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
			final List<Image> images = ip.getImages();
			final List<ImageOnGrid> imagesOnGrid = ip.getImagesOnGrid();
			User u = new User();
			u.setId(currentUser());
			SubsampleDAO ssDAO = new SubsampleDAO(this.currentSession());
			for (Image img : images) {
				setRealImage(zp, img, spreadsheetPrefix);
				Subsample ss = (img.getSubsample());
				ss.getSample().setOwner(u);
				try {
					ssDAO.fill(ss);
				} catch (SubsampleNotFoundException daoe) {
					ss = ssDAO.save(ss);
				}
				img.setSubsample((Subsample) (ss));
			}

			for (ImageOnGrid iog : imagesOnGrid) {
				// Populate Image, Mark to save with rest of images
				setRealImage(zp, iog.getImage(), spreadsheetPrefix);

				// Set image information for the Grid Copy
				iog.setGchecksum(iog.getImage().getChecksum());
				iog.setGchecksum64x64(iog.getImage().getChecksum64x64());
				iog.setGchecksumHalf(iog.getImage().getChecksumHalf());
				iog.setGheight(iog.getImage().getHeight());
				iog.setGwidth(iog.getImage().getWidth());
				saveIncompleteImageOnGrid(iog);
			}
			// Finally, Save the Images
			save(images);
		} catch (final IOException ioe) {
			throw new IllegalStateException(ioe.getMessage());
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
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

	public BulkUploadResult parser(String fileOnServer)
			throws InvalidFormatException, LoginRequiredException {
		final BulkUploadResult results = new BulkUploadResult();
		final Map<Integer, ValidationException> errors = new HashMap<Integer, ValidationException>();
		try {
			currentSession()
					.createSQLQuery(
							"UPDATE uploaded_files SET user_id = :user_id WHERE hash = :hash")
					.setParameter("user_id", currentUser()).setParameter(
							"hash", fileOnServer).executeUpdate();
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
			final List<Image> images = ip.getImages();
			final List<ImageOnGrid> imagesOnGrid = ip.getImagesOnGrid();
			int row = 2;
			final BulkUploadResultCount ssResultCount = new BulkUploadResultCount();
			final BulkUploadResultCount imgResultCount = new BulkUploadResultCount();
			User u = new User();
			u.setId(currentUser());
			// Keeps track of existing/new subsample names for each sample
			final Map<String, Collection<String>> subsampleNames = new HashMap<String, Collection<String>>();
			final SubsampleDAO ssDAO = new SubsampleDAO(this.currentSession());
			final SampleDAO sDAO = new SampleDAO(this.currentSession());
			final ImageDAO dao = new ImageDAO(this.currentSession());
			for (Image img : images) {
				// Confirm the filename is in the zip
				if (zp.getEntry(spreadsheetPrefix + img.getFilename()) == null) {
					errors.put(row, new InvalidImageException(spreadsheetPrefix
							+ img.getFilename()));
				} else {
					try {
						setRealImage(zp, img, spreadsheetPrefix);
						// see if our subsample exists

						Subsample ss = (img.getSubsample());
						if (ss != null) {
							if (!subsampleNames.containsKey(img.getSample()
									.getAlias())
									|| !subsampleNames.get(
											img.getSample().getAlias())
											.contains(ss.getName())) {
								if (!subsampleNames.containsKey(img.getSample()
										.getAlias()))
									subsampleNames.put(img.getSample()
											.getAlias(), new HashSet<String>());
								try {
									ssDAO.fill(ss);
									ssResultCount.incrementOld();
								} catch (DAOException e) {
									// Means it is new because we could not find
									// it
									ssResultCount.incrementFresh();
								}
								subsampleNames.get(
										img.getSample().getAlias()).add(
										img.getSubsample().getName());
							}
						} else {
							// Every Image needs a subsample so add an error
							errors.put(row, new PropertyRequiredException(
									"Subsample"));
						}

						// see if our sample exists
						try {
							Sample s = img.getSample();
							s.setOwner(u);
							sDAO.fill(s);
						} catch (DAOException e) {
							// There is no sample we have to add an error
							// Every Image needs a sample so add an error
							errors.put(row, new PropertyRequiredException(
									"Sample"));
						}
						doc.validate(img);
						if (dao.isNew(img))
							imgResultCount.incrementFresh();
						else
							imgResultCount.incrementOld();
					} catch (ValidationException e) {
						errors.put(row, e);
						imgResultCount.incrementInvalid();
					}
				}
				++row;
			}

			for (ImageOnGrid iog : imagesOnGrid) {
				Image img = iog.getImage();
				// Confirm the filename is in the zip
				if (zp.getEntry(spreadsheetPrefix + img.getFilename()) == null) {
					errors.put(row, new InvalidImageException(spreadsheetPrefix
							+ img.getFilename()));
				}
				try {
					doc.validate(img);
				} catch (ValidationException e) {
					errors.put(row, e);
				}
				++row;
			}
			results.addResultCount("Subsamples", ssResultCount);
			results.addResultCount("Images", imgResultCount);
			results.setHeaders(ip.getHeaders());
			results.setErrors(errors);
		} catch (final IOException ioe) {
			throw new IllegalStateException(ioe.getMessage());
		}
		return results;
	}

	protected void save(final Collection<Image> images)
			throws ValidationException, LoginRequiredException, DAOException {

		for (Image image : images) {
			if (image instanceof XrayImage) {
				(new XrayImageDAO(this.currentSession()))
						.save((XrayImage) image);
			} else {
				(new ImageDAO(this.currentSession())).save(image);
			}
		}
		commit();
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
