package edu.rpi.metpetdb.server.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.media.jai.RenderedOp;

import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.error.InvalidImageException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.ImageDTO;
import edu.rpi.metpetdb.client.model.ImageOnGridDTO;
import edu.rpi.metpetdb.client.service.BulkUploadImagesService;
import edu.rpi.metpetdb.server.ImageUploadServlet;
import edu.rpi.metpetdb.server.bulk.upload.sample.ImageParser;

public class BulkUploadImagesServiceImpl extends ImageServiceImpl implements
		BulkUploadImagesService {
	private static final long serialVersionUID = 1L;
	public static String baseFolder;
	static final String spreadsheet_name = "main.xls";

	public Map<Integer, String[]> getHeaderMapping(final String fileOnServer)
			throws InvalidFormatException {
		try {
			ZipFile zp = new ZipFile(baseFolder + "/" + fileOnServer);

			final ImageParser ip = new ImageParser(zp.getInputStream(zp
					.getEntry(spreadsheet_name)));

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

			ZipFile zp = new ZipFile(baseFolder + "/" + fileOnServer);

			final ImageParser ip = new ImageParser(zp.getInputStream(zp
					.getEntry(spreadsheet_name)));
			try {
				ip.initialize();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				throw new IllegalStateException(
						"Programmer Error: No Such Method");
			}

			ip.parse();

			final List<ImageDTO> images = ip.getImages();
			final List<ImageOnGridDTO> imagesOnGrid = ip.getImagesOnGrid();

			Integer i = 2;
			// Find Valid, new Images
			Integer[] img_breakdown = {
					0, 0, 0
			};

			// Include images that are for the grid
			for (ImageOnGridDTO iog : imagesOnGrid)
				images.add(iog.getImage());

			for (ImageDTO img : images) {
				// Confirm the filename is in the zip
				if (zp.getEntry(img.getFilename()) != null) {
					img_breakdown[1]++;
				}

				// There's no doc.validate() for images?
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
			LoginRequiredException {
		final Map<Integer, ValidationException> errors = new HashMap<Integer, ValidationException>();

		try {
			currentSession()
					.createSQLQuery(
							"UPDATE uploaded_files SET user_id = :user_id WHERE hash = :hash")
					.setParameter("user_id", currentUser()).setParameter(
							"hash", fileOnServer).executeUpdate();

			ZipFile zp = new ZipFile(baseFolder + "/" + fileOnServer);

			final ImageParser ip = new ImageParser(zp.getInputStream(zp
					.getEntry(spreadsheet_name)));
			try {
				ip.initialize();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				throw new IllegalStateException(
						"Programmer Error: No Such Method");
			}

			ip.parse();

			final List<ImageDTO> images = ip.getImages();
			final List<ImageOnGridDTO> imagesOnGrid = ip.getImagesOnGrid();

			Integer i = 2;
			for (ImageDTO img : images) {
				// Confirm the filename is in the zip
				if (zp.getEntry(img.getFilename()) == null) {
					final String err = "Filename (" + img.getFilename()
							+ ") not found in zip";
					errors.put(new Integer(i), new InvalidImageException(err));
				}
				// There's no doc.validate() for images?

				++i;
			}

			if (errors.isEmpty()) {
				final List<ImageDTO> imagesToSave = new LinkedList<ImageDTO>();

				try {
					for (ImageOnGridDTO iog : imagesOnGrid) {
						// Populate Image, Mark to save with rest of images
						ImageDTO img = uploadImages(zp, iog.getImage());
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

					for (ImageDTO img : images) {
						ImageDTO img_s = uploadImages(zp, img);
						imagesToSave.add(img_s);
					}

					// Finally, Save the Images
					save(imagesToSave);

					return null;
				} catch (final ValidationException e) {
					throw new IllegalStateException(
							"Objects passed and subsequently failed validation (ImageDTO).");
				}
			}
		} catch (final IOException ioe) {
			throw new IllegalStateException(ioe.getMessage());
		}

		return errors;
	}

	private ImageDTO uploadImages(ZipFile zp, ImageDTO img) throws IOException {
		// Get Image Data from Zip
		ZipEntry ze = zp.getEntry(img.getFilename());
		final byte[] imgData = new byte[(int) ze.getSize()];
		zp.getInputStream(ze).read(imgData);

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
}
