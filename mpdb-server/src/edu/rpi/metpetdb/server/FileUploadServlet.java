package edu.rpi.metpetdb.server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import edu.rpi.metpetdb.server.security.PasswordEncrypter;

public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String baseFolder = "bulkUpload";

	// private static final String baseFolder =
	// MpDbServlet.fileProps.getProperty("bulk.path");

	protected void doPost(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException {
		response.setContentType("text/plain");
		try {
			final FileItem uploadItem = getFileItem(request);
			if (uploadItem == null) {
				response.getWriter().write("NO-SCRIPT-DATA");
				return;
			}
			final String hash = writeFile(uploadItem);

			DataStore
					.open()
					.createSQLQuery(
							"INSERT INTO uploaded_files(hash, filename, time) VALUES(:hash, :filename, NOW())")
					.setParameter("hash", hash).setParameter("filename",
							uploadItem.getName()).executeUpdate();
			response.getWriter().write(baseFolder + "/" + hash);

		} catch (final IOException ioe) {
			throw new IllegalStateException(ioe.getMessage());

		}

	}

	@SuppressWarnings( { "unchecked" })
	private FileItem getFileItem(final HttpServletRequest request) {
		final FileItemFactory factory = new DiskFileItemFactory();
		final ServletFileUpload upload = new ServletFileUpload(factory);

		try {
			final List<FileItem> items = upload.parseRequest(request);
			final Iterator<FileItem> it = items.iterator();
			while (it.hasNext()) {
				final FileItem item = it.next();
				if (!item.isFormField()
						&& "bulkUpload".equals(item.getFieldName())) {
					return item;
				}
			}
		} catch (final FileUploadException e) {
			return null;
		}

		return null;
	}

	private static String writeFile(final FileItem uploadItem) {
		final byte[] file = uploadItem.get();
		final String checksum = PasswordEncrypter.calculateChecksum(file);
		final String filename = checksum;
		// String temp = "";
		// try {
		// temp = path.getCanonicalPath();
		// } catch (IOException ioe) {
		// throw new IllegalStateException(ioe.getMessage());
		// }
		try {
			final FileOutputStream writer = new FileOutputStream(baseFolder
					+ "/" + filename);
			writer.write(file);
			writer.close();
			return filename;
		} catch (final IOException ioe) {
			throw new IllegalStateException("filePath=" + baseFolder
					+ " IO error: " + ioe.getMessage());
		}
	}
}
