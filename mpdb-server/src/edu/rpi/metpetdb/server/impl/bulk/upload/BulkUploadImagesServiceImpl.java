package edu.rpi.metpetdb.server.impl.bulk.upload;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.media.jai.RenderedOp;

import edu.rpi.metpetdb.client.error.ImageRuntimeException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.bulk.upload.InvalidSpreadSheetException;
import edu.rpi.metpetdb.client.error.dao.GenericDAOException;
import edu.rpi.metpetdb.client.error.validation.ImageNotUploadedException;
import edu.rpi.metpetdb.client.error.validation.InvalidImageException;
import edu.rpi.metpetdb.client.error.validation.PropertyRequiredException;
import edu.rpi.metpetdb.client.model.Grid;
import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.ImageOnGrid;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.XrayImage;
import edu.rpi.metpetdb.client.model.bulk.upload.BulkUploadResult;
import edu.rpi.metpetdb.client.model.bulk.upload.BulkUploadResultCount;
import edu.rpi.metpetdb.client.service.bulk.upload.BulkUploadImagesService;
import edu.rpi.metpetdb.server.ImageUploadServlet;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.bulk.upload.BulkUploadImage;
import edu.rpi.metpetdb.server.bulk.upload.ImageParser;
import edu.rpi.metpetdb.server.dao.impl.GeoReferenceDAO;
import edu.rpi.metpetdb.server.dao.impl.GridDAO;
import edu.rpi.metpetdb.server.dao.impl.ImageDAO;
import edu.rpi.metpetdb.server.dao.impl.ImageOnGridDAO;
import edu.rpi.metpetdb.server.dao.impl.SampleDAO;
import edu.rpi.metpetdb.server.dao.impl.SubsampleDAO;
import edu.rpi.metpetdb.server.dao.impl.XrayImageDAO;

public class BulkUploadImagesServiceImpl extends BulkUploadService implements
		BulkUploadImagesService {
	private static final long serialVersionUID = 1L;
	
	private ZipEntry getZipEntry(final ZipFile zp, final Image img, final String prefix) {
		// Get Image Data from Zip
		final String imageName = prefix + img.getFilename();
		ZipEntry ze = zp.getEntry(imageName);
		if (ze == null) {
			//try a search without comparing case
			final Enumeration<? extends ZipEntry> entries = zp.entries();
			while(entries.hasMoreElements()) {
				final ZipEntry next = entries.nextElement();
				if (next.getName().equalsIgnoreCase(imageName)) {
					ze = next;
				}
			}
		}
		return ze;
	}

	private void setRealImage(ZipFile zp, Image img, String prefix)
			throws IOException, ImageRuntimeException {
		
		final byte[] imgData = getBytesFromInputStream(zp.getInputStream(getZipEntry(zp, img, prefix)));

		// Save Image Data, in various forms, to the server
		try {
			RenderedOp ro = ImageUploadServlet.loadImage(imgData);
			img.setChecksum(ImageUploadServlet.generateFullsize(ro, false));
			img.setChecksum64x64(ImageUploadServlet.generate64x64(ro, false));
			img.setChecksumHalf(ImageUploadServlet.generateHalf(ro, false));
			img.setChecksumMobile(ImageUploadServlet.generateMobileVersion(ro,false));
			img.setWidth(ro.getWidth());
			img.setHeight(ro.getHeight());
		}
		catch (RuntimeException e){
			throw new ImageRuntimeException();
		}
	}
	private ZipEntry getSpreadsheetName(InputStream is) throws IOException, InvalidSpreadSheetException {
		ZipInputStream zis = new ZipInputStream(is);
		ZipEntry ent;
		final ArrayList<ZipEntry> discoveredSpreadsheets = new ArrayList<ZipEntry>();
		while ((ent = zis.getNextEntry()) != null) {
			String entryName = ent.getName();
			String[] entryNameSplit = entryName.split("/");
			// Ignore any subdirectories
			if (entryNameSplit.length > 1){
				continue;
			}

			// Ignore anything that is in a' hidden' directory
			for (String s : entryNameSplit) {
				if (s.startsWith("__") || entryName.startsWith("."))
					continue;
			}

			// Implicit assumption that there will only be _one_ xls spreadsheet
			if (entryName.toLowerCase().contains(".xls")) {
				discoveredSpreadsheets.add(ent);
			}
		}
		// if there is only one spreadsheet then use that one
		if (discoveredSpreadsheets.size() == 1)
			return discoveredSpreadsheets.get(0);
		else if (discoveredSpreadsheets.size() > 1){
			throw new InvalidSpreadSheetException(discoveredSpreadsheets.size() + " spreadsheets were found " +
					"in the root directory. There must be one and only one.");
		} else
			throw new InvalidSpreadSheetException("No spreadsheet was found in the root directory. " +
					"There must be one and only one.");
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

	@Override
	public void parserImpl(String fileOnServer, boolean save,
			BulkUploadResult results, SampleDAO sampleDao, SubsampleDAO ssDao,
			GeoReferenceDAO geoDao,
			Map<String, Collection<String>> subsampleNames,
			Map<String, Sample> samples, Map<String, Subsample> subsamples)
			throws FileNotFoundException, MpDbException, LoginRequiredException {
		final BulkUploadResultCount ssResultCount = new BulkUploadResultCount();
		final BulkUploadResultCount imgResultCount = new BulkUploadResultCount();
		// Find the Excel Spreadsheet
		FileInputStream is = new FileInputStream(MpDbServlet
				.getFileUploadPath()
				+ fileOnServer);
		ZipEntry spreadsheet;
		try {
			spreadsheet = getSpreadsheetName(is);
		} catch (IOException ioe) {
			throw new GenericDAOException(ioe.getMessage());
		}
		String spreadsheetPrefix = (new File(spreadsheet.getName()))
				.getParent();
		if (spreadsheetPrefix == null)
			spreadsheetPrefix = "";
		else
			spreadsheetPrefix += File.separator;

		ZipFile zp;
		ImageParser ip;
		try {
			zp = new ZipFile(MpDbServlet.getFileUploadPath() + fileOnServer);
			ip = new ImageParser(zp.getInputStream(spreadsheet));
		} catch (IOException ioe) {
			throw new GenericDAOException(ioe.getMessage());
		}

		ip.parse();
		results.setHeaders(ip.getHeaders());
		final Map<Integer, BulkUploadImage> images = ip.getBulkUploadImages();
		final Iterator<Integer> imgRows = images.keySet().iterator();
		// Handle any existing errors found
		addErrors(ip, results);
		addWarnings(ip, results);
		final ImageDAO imgDao = new ImageDAO(currentSession());
		final XrayImageDAO xrayDao = new XrayImageDAO(currentSession());
		while (imgRows.hasNext()) {
			final int row = imgRows.next();
			final Image img = images.get(row).getImage();
			initObject(img);
			// Confirm the filename is in the zip
			if (getZipEntry(zp, img, spreadsheetPrefix)== null) {
				results.addError(row, new ImageNotUploadedException(
						spreadsheetPrefix + img.getFilename()));
			} else {
				try {
					// see if our sample exists
					if (img.getSample() == null && img.getSubsample() == null) {
						// Image needs a sample or a subsample to continue
						results.addError(row, new PropertyRequiredException(
								"Sample or Subsample"));
						continue;
					}
					Sample s = img.getSample();
					img.setSample(checkForSample(s, samples, sampleDao, results,
							subsampleNames, ssDao, subsamples, row));
					if (img.getSample().getId() == 0)
						continue;
					if (img.getSample() != null && img.getSubsample() == null) {
						img.setPublicData(img.getSample().isPublicData());
						// we are adding a sample image
					} else {
						// we are adding an image to a subsample
						Subsample ss = (img.getSubsample());
						ss.setSample(s);
						img.setSample(null);
						if (ss != null && ss.getName() != null) {
							// if we don't have the name stored already we
							// need
							// to load the subsample
							img.setSubsample(checkForSubsample(s, ss, samples,
									ssDao, results, subsampleNames, row,
									subsamples, ssResultCount, save));
							img.setPublicData(img.getSubsample().isPublicData());
						} else {
							// Every Image needs a subsample so add an error
							results.addError(row,
									new PropertyRequiredException("Subsample"));
						}
					}
					doc.validate(img);
					if (imgDao.isNew(img))
						imgResultCount.incrementFresh();
					else
						imgResultCount.incrementOld();
					if (save) {
						setRealImage(zp, img, spreadsheetPrefix);
						if (img instanceof XrayImage) {
							xrayDao.save((XrayImage) img);
						} else {
							imgDao.save(img);
						}
					}
				} catch (Exception e) {
					results.addError(row, getNiceException(e));
					imgResultCount.incrementInvalid();
				}
			}
		}
//ignore images on grid for now
//		final Iterator<Integer> iogRows = images.keySet().iterator();
//		while (iogRows.hasNext()) {
//			final int row = iogRows.next();
//			final ImageOnGrid iog = images.get(row).getImageOnGrid();
//			Image img = iog.getImage();
//			// Confirm the filename is in the zip
//			if (zp.getEntry(spreadsheetPrefix + img.getFilename()) == null) {
//				results.addError(row, new InvalidImageException(
//						spreadsheetPrefix + img.getFilename()));
//			}
//			try {
//				doc.validate(img);
//				if (save) {
//					setRealImage(zp, iog.getImage(), spreadsheetPrefix);
//
//					// Set image information for the Grid Copy
//					iog.setGchecksum(iog.getImage().getChecksum());
//					iog.setGchecksum64x64(iog.getImage().getChecksum64x64());
//					iog.setGchecksumHalf(iog.getImage().getChecksumHalf());
//					iog.setGheight(iog.getImage().getHeight());
//					iog.setGwidth(iog.getImage().getWidth());
//					saveIncompleteImageOnGrid(iog);
//				}
//			} catch (Exception e) {
//				results.addError(row, getNiceException(e));
//			}
//		}
		results.addResultCount("Subsamples", ssResultCount);
		results.addResultCount("Images", imgResultCount);
	}

	protected ImageOnGrid saveIncompleteImageOnGrid(ImageOnGrid iog)
			throws ValidationException, LoginRequiredException, MpDbException {
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
			g = (new GridDAO(this.currentSession())).save(g);
		}
		iog.setGrid(g);

		// Save ImageOnGrid
		iog = (new ImageOnGridDAO(this.currentSession())).save(iog);
		return (iog);
	}

}
