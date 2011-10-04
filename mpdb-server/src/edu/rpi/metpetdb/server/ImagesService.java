package edu.rpi.metpetdb.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ImagesService extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(final HttpServletRequest request,
			final HttpServletResponse response) {
		String baseFolder = getBaseFolder();
		String checksum = request.getParameter("checksum");
		if (checksum != null) {
			checksum = checksum.replaceAll("\\\\|/", "");

			final String folder = checksum.substring(0, 2);
			final String subfolder = checksum.substring(2, 4);
			final String filename = checksum.substring(4, checksum.length());

			final File file = new File(baseFolder + '/' + folder + '/'
					+ subfolder + '/' + filename);
			if (file.exists() && file.canRead()) {
				BufferedInputStream input = null;
				BufferedOutputStream output = null;
				try {
					input = new BufferedInputStream(new FileInputStream(file));
					int contentLength = input.available();
					
					response.setContentLength(contentLength);
					
					response.setHeader("Content-disposition",
							"inline; filename=\"" + filename + "\"");
					output = new BufferedOutputStream(response
							.getOutputStream());

					while (contentLength-- > 0) {
						output.write(input.read());
					}
					output.flush();
				} catch (IOException ioe) {

				} finally {
					try {
						if (output != null)
							output.close();
						if (input != null)
							input.close();
					} catch (IOException ioe) {

					}
				}
			}
		}

	}

	public String getBaseFolder() {
		final String propFile = "files.properties";
		final InputStream i = ImageUploadServlet.class.getClassLoader()
				.getResourceAsStream(propFile);
		if (i == null)
			throw new IllegalStateException("No " + propFile + " found");
		final Properties props = new Properties();
		try {
			props.load(i);
			i.close();
		} catch (IOException ioe) {
			throw new IllegalStateException("Cannot load " + propFile, ioe);
		}

		return props.getProperty("images.path");
	}
}
