package edu.rpi.metpetdb.server;

import java.awt.image.renderable.ParameterBlock;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.sun.media.jai.codec.JPEGEncodeParam;

import edu.rpi.metpetdb.server.security.PasswordEncrypter;

public class ImageUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String baseFolder = "";

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain");

		FileItem uploadItem = getFileItem(request);
		if (uploadItem == null) {
			response.getWriter().write("NO-SCRIPT-DATA");
			return;
		}

		if (!uploadItem.getContentType().substring(0, 5).equals("image")) {
			response.getWriter().write("Not a valid image file.");
			return;
		}
		response.getWriter().write("{OK}" + writeFiles(uploadItem).toString());
	}

	private ArrayList<String> writeFiles(FileItem uploadItem) {
		ArrayList<String> al = new ArrayList<String>();
		RenderedOp ro = loadImage(uploadItem);
		// final String originalChecksum = writeFile(uploadItem.get());
		al.add(generateFullsize(ro, false));
		al.add(generate64x64(ro, false));
		al.add(generateMobileVersion(ro, false));
		al.add(generateHalf(ro, false));
		al.add(String.valueOf(ro.getWidth()));
		al.add(String.valueOf(ro.getHeight()));
		al.add(uploadItem.getName());
		return al;
	}

	public static String writeFile(final byte[] image) {
		final String checksum = PasswordEncrypter.calculateChecksum(image);
		final String folder = checksum.substring(0, 2);
		final String subfolder = checksum.substring(2, 4);
		final String filename = checksum.substring(4, checksum.length());
		final String imagePath = baseFolder + "/" + folder + "/" + subfolder;
		final File path = new File(imagePath);
		String temp = "";
		path.mkdirs();
		try {
			temp = path.getCanonicalPath();
		} catch (IOException ioe) {
			throw new IllegalStateException(ioe.getMessage());
		}
		try {
			final FileOutputStream writer = new FileOutputStream(imagePath
					+ "/" + filename);
			writer.write(image);
			writer.close();
			return checksum;
		} catch (IOException ioe) {
			throw new IllegalStateException("temp=" + temp + " canwrite="
					+ path.canWrite() + " imagePath=" + imagePath
					+ " IO error: " + ioe.getMessage());
		}
	}

	public static String generate64x64(final RenderedOp ro,
			final boolean doAsPng) {
		final float scale = (float) 64.0 / ro.getWidth();
		final RenderedOp small = scale(ro, scale, scale);
		final JPEGEncodeParam encodeParam = new JPEGEncodeParam();
		encodeParam.setQuality(1.0f);
		final ParameterBlock pb = new ParameterBlock();
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		pb.addSource(small);
		if (!doAsPng)
			pb.add(bs).add("jpeg").add(encodeParam);
		else
			pb.add(bs).add("png");
		JAI.create("encode", pb);
		final byte[] bytes = bs.toByteArray();
		try {
			bs.close();
		} catch (IOException ioe) {

		}
		return writeFile(bytes);
	}
	public static String generateMobileVersion(final RenderedOp ro,
			final boolean doAsPng){
		//if the width of the original image is less than 320, leave the image its original size
		final RenderedOp mobile;
		if(ro.getWidth()<320.0) 
		{
			mobile= scale(ro, ro.getWidth(), ro.getHeight());
		}
		else
		{
			final float scale= (float) 1280.0 / ro.getWidth();
			mobile= scale(ro, scale, scale);
		}
		final JPEGEncodeParam encodeParam = new JPEGEncodeParam();
		encodeParam.setQuality(1.0f);
		final ParameterBlock pb= new ParameterBlock();
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		pb.addSource(mobile);
		if(!doAsPng)
			pb.add(bs).add("jpeg").add(encodeParam);
		else
			pb.add(bs).add("png");
		JAI.create("encode", pb);
		final byte[] bytes = bs.toByteArray();
		try {
			bs.close();
		} catch (IOException ioe) {

		}
		return writeFile(bytes);
	}

	public static String generateHalf(final RenderedOp ro, final boolean doAsPng) {
		final RenderedOp half = scale(ro, .5f, .5f);
		final JPEGEncodeParam encodeParam = new JPEGEncodeParam();
		encodeParam.setQuality(0.75f);
		final ParameterBlock pb = new ParameterBlock();
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		pb.addSource(half.getRendering());
		if (!doAsPng)
			pb.add(bs).add("jpeg").add(encodeParam);
		else
			pb.add(bs).add("png");
		JAI.create("encode", pb);
		final byte[] bytes = bs.toByteArray();
		try {
			bs.close();
		} catch (IOException ioe) {

		}
		return writeFile(bytes);
	}

	public static String generateFullsize(RenderedOp ro, final boolean doAsPng) {
		final JPEGEncodeParam encodeParam = new JPEGEncodeParam();
		encodeParam.setQuality(1.0f);
		final ParameterBlock pb = new ParameterBlock();
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		pb.addSource(ro);
		if (!doAsPng)
			pb.add(bs).add("jpeg").add(encodeParam);
		else
			pb.add(bs).add("png");
		ro = JAI.create("encode", pb);
		final byte[] bytes = bs.toByteArray();
		try {
			bs.close();
		} catch (IOException ioe) {

		}
		return writeFile(bytes);
	}

	public static RenderedOp scale(final RenderedOp source, float xscale,
			float yscale) {
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(source);
		pb.add(xscale);
		pb.add(yscale);
		pb.add(0.0f);
		pb.add(0.0f);
		pb.add(Interpolation.getInstance(Interpolation.INTERP_NEAREST));
		return JAI.create("scale", pb);
	}

	public static RenderedOp loadImage(FileItem uploadItem) {
		try {
			RenderedOp ro = JAI.create("stream", new ByteArraySeekableStream(
					uploadItem.get()));
			return ro;
		} catch (IOException ioe) {
			throw new IllegalStateException("IO error: " + ioe.getMessage());
		}
	}

	public static RenderedOp loadImage(final byte[] file) {
		try {
			return JAI.create("stream", new ByteArraySeekableStream(file));
		} catch (IOException ioe) {
			throw new IllegalStateException("IO error: " + ioe.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	private FileItem getFileItem(HttpServletRequest request) {
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);

		try {
			List items = upload.parseRequest(request);
			Iterator it = items.iterator();
			while (it.hasNext()) {
				FileItem item = (FileItem) it.next();
				if (!item.isFormField()
						&& "imageUpload".equals(item.getFieldName())) {
					return item;
				}
			}
		} catch (FileUploadException e) {
			return null;
		}

		return null;
	}

	public static String getBaseFolder() {
		return baseFolder;
	}

	public static void setBaseFolder(String baseFolder) {
		ImageUploadServlet.baseFolder = baseFolder;
	}
}
