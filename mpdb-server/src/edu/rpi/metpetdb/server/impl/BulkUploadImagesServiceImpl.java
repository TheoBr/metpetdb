package edu.rpi.metpetdb.server.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.media.jai.RenderedOp;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.error.InvalidImageException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.ImageDTO;
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
				try {
					for (ImageDTO img : images) {
						// Get Image Data from Zip
						ZipEntry ze = zp.getEntry(img.getFilename());
						final byte[] imgData = new byte[(int) ze.getSize()];
						zp.getInputStream(ze).read(imgData);

						// Save Image Data, in various forms, to the server
						RenderedOp ro = ImageUploadServlet.loadImage(imgData);
						img.setChecksum(ImageUploadServlet.generateFullsize(ro,
								false));
						img.setChecksum64x64(ImageUploadServlet.generate64x64(
								ro, false));
						img.setChecksumHalf(ImageUploadServlet.generateHalf(ro,
								false));
						img.setWidth(ro.getWidth());
						img.setHeight(ro.getHeight());

						// Save ImageDTO
						saveImage(img);
					}
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
	public static void setBaseFolder(String baseFolder) {
		BulkUploadImagesServiceImpl.baseFolder = baseFolder;
	}
}
