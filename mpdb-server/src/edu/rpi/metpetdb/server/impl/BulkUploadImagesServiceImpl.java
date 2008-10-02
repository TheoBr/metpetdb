package edu.rpi.metpetdb.server.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.ImageOnGrid;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.service.BulkUploadImagesService;
import edu.rpi.metpetdb.server.ImageUploadServlet;
import edu.rpi.metpetdb.server.bulk.upload.ImageParser;
import edu.rpi.metpetdb.server.dao.impl.SubsampleDAO;

public class BulkUploadImagesServiceImpl extends ImageServiceImpl implements
		BulkUploadImagesService {
	private static final long serialVersionUID = 1L;
	public static String baseFolder;

	public Map<Integer, String[]> getHeaderMapping(final String fileOnServer)
			throws InvalidFormatException {
		try {
			// Find the Excel Spreadsheet
			FileInputStream is = new FileInputStream(baseFolder + "/"
					+ fileOnServer);
			ZipEntry spreadsheet = getSpreadsheetName(is);
			if (spreadsheet == null)
				throw new IllegalStateException("No Excel Spreasheet found");

			ZipFile zp = new ZipFile(baseFolder + "/" + fileOnServer);
			final ImageParser ip = new ImageParser(zp
					.getInputStream(spreadsheet));

			try {
				ip.initialize();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				throw new IllegalStateException(
						"Programmer Error: No Such Method");
			}

			return ip.getHeaders();
		} catch (final IOException ioe) {
			throw new IllegalStateException(ioe.getMessage());
		}
	}

	public Map<String, Integer[]> getAdditions(final String fileOnServer)
			throws InvalidFormatException, LoginRequiredException {
		final Map<String, Integer[]> newAdditions = new TreeMap<String, Integer[]>();

		try {
			currentSession()
					.createSQLQuery(
							"UPDATE uploaded_files SET user_id = :user_id WHERE hash = :hash")
					.setParameter("user_id", currentUser()).setParameter(
							"hash", fileOnServer).executeUpdate();

			// Find the Excel Spreadsheet
			FileInputStream is = new FileInputStream(baseFolder + "/"
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

			ZipFile zp = new ZipFile(baseFolder + "/" + fileOnServer);
			final ImageParser ip = new ImageParser(zp
					.getInputStream(spreadsheet));
			try {
				ip.initialize();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				throw new IllegalStateException(
						"Programmer Error: No Such Method");
			}

			ip.parse();

			final List<Image> images = ip.getImages();
			final List<ImageOnGrid> imagesOnGrid = ip.getImagesOnGrid();

			Integer i = 2;
			// Find Valid, new Images
			Integer[] img_breakdown = {
					0, 0, 0
			};

			// Include images that are for the grid
			for (ImageOnGrid iog : imagesOnGrid)
				images.add(iog.getImage());

			for (Image img : images) {
				// Confirm the filename is in the zip
				if (zp.getEntry(spreadsheetPrefix + img.getFilename()) == null) {
					img_breakdown[0]++;
				} else {
					try {
						doc.validate(img);
						img_breakdown[1]++;
					} catch (ValidationException e) {
						img_breakdown[0]++;
					}
				}

				++i;
			}

			newAdditions.put("Subsample_images", img_breakdown);
		} catch (final IOException ioe) {
			throw new IllegalStateException(ioe.getMessage());
		}

		return newAdditions;
	}
	public Map<Integer, ValidationException> saveImagesFromZip(
			final String fileOnServer) throws InvalidFormatException,
			LoginRequiredException, DAOException {
		final Map<Integer, ValidationException> errors = new HashMap<Integer, ValidationException>();

		try {
			currentSession()
					.createSQLQuery(
							"UPDATE uploaded_files SET user_id = :user_id WHERE hash = :hash")
					.setParameter("user_id", currentUser()).setParameter(
							"hash", fileOnServer).executeUpdate();

			// Find the Excel Spreadsheet
			FileInputStream is = new FileInputStream(baseFolder + "/"
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

			ZipFile zp = new ZipFile(baseFolder + "/" + fileOnServer);
			final ImageParser ip = new ImageParser(zp
					.getInputStream(spreadsheet));
			try {
				ip.initialize();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				throw new IllegalStateException(
						"Programmer Error: No Such Method");
			}

			ip.parse();

			final List<Image> images = ip.getImages();
			final List<ImageOnGrid> imagesOnGrid = ip.getImagesOnGrid();

			Integer i = 2;
			User u = new User();
			u.setId(currentUser());
			SubsampleDAO ssDAO = new SubsampleDAO(this.currentSession());
			for (Image img : images) {
				// Confirm the filename is in the zip
				if (zp.getEntry(spreadsheetPrefix + img.getFilename()) == null) {
					errors.put(new Integer(i), new InvalidImageException(
							spreadsheetPrefix + img.getFilename()));
				}
				try {
					Image img_s = uploadImages(zp, img, spreadsheetPrefix);
					Subsample ss = (img_s.getSubsample());
					ss.getSample().setOwner(u);

					try {
						ssDAO.fill(ss);
					} catch (SubsampleNotFoundException daoe) {
						doc.validate(ss);
						ss = ssDAO.save(ss);
					}
					img_s.setSubsample((Subsample) (ss));
					img_s.getSubsample().getSample().setOwner(u);
					if (img.getSample() == null)
						img.setSample(new Sample());
					img_s.getSample().setOwner(u);
					doc.validate(img_s);
				} catch (ValidationException e) {
					errors.put(i, e);
				}

				++i;
			}

			for (ImageOnGrid iog : imagesOnGrid) {
				Image img = iog.getImage();
				// Confirm the filename is in the zip
				if (zp.getEntry(spreadsheetPrefix + img.getFilename()) == null) {
					errors.put(new Integer(i), new InvalidImageException(
							spreadsheetPrefix + img.getFilename()));
				}
				try {
					doc.validate(img);
				} catch (ValidationException e) {
					errors.put(i, e);
				}

				++i;
			}

			if (errors.isEmpty()) {
				final List<Image> imagesToSave = new LinkedList<Image>();

				try {
					for (ImageOnGrid iog : imagesOnGrid) {
						// Populate Image, Mark to save with rest of images
						Image img = uploadImages(zp, iog.getImage(),
								spreadsheetPrefix);
						img.getSubsample().getSample().setOwner(u);
						if (img.getSample() == null)
							img.setSample(new Sample());
						img.getSample().setOwner(u);
						iog.setImage(img);

						// Set image information for the Grid Copy
						iog.setGchecksum(img.getChecksum());
						iog.setGchecksum64x64(img.getChecksum64x64());
						iog.setGchecksumHalf(img.getChecksumHalf());
						iog.setGheight(img.getHeight());
						iog.setGwidth(img.getWidth());

						// Save
						saveIncompleteImageOnGrid(iog);
					}

				
					for (Image img : images) {
						Image img_s = uploadImages(zp, img, spreadsheetPrefix);
						Subsample ss = (img_s.getSubsample());
						ss.getSample().setOwner(u);

						try {
							ssDAO.fill(ss);
						} catch (SubsampleNotFoundException daoe) {
							doc.validate(ss);
							ss = ssDAO.save(ss);
						}
						img_s.setSubsample((Subsample) (ss));
						img_s.getSubsample().getSample().setOwner(u);
						if (img.getSample() == null)
							img.setSample(new Sample());
						img_s.getSample().setOwner(u);
						imagesToSave.add(img_s);
					}

					// Finally, Save the Images
					save(imagesToSave);

					return null;
				} catch (final ValidationException e) {
					throw new IllegalStateException(
							"Objects passed and subsequently failed validation (Image).");
				}
			}
		} catch (final IOException ioe) {
			throw new IllegalStateException(ioe.getMessage());
		}

		return errors;
	}
	private Image uploadImages(ZipFile zp, Image img, String prefix)
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
		return img;
	}

	public static void setBaseFolder(String baseFolder) {
		BulkUploadImagesServiceImpl.baseFolder = baseFolder;
	}

	private ZipEntry getSpreadsheetName(InputStream is) throws IOException {
		ZipInputStream zis = new ZipInputStream(is);
		ZipEntry ent;
		while ((ent = zis.getNextEntry()) != null) {
			String entryName = ent.getName();

			// Ignore anything that is in a' hidden' directory
			if (entryName.startsWith("__") || entryName.startsWith("."))
				continue;

			// Implicit assumption that there will only be _one_ xls spreadsheet
			if (entryName.contains(".xls")) {
				return ent;
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
}
