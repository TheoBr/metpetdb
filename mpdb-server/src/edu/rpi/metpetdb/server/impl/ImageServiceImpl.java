package edu.rpi.metpetdb.server.impl;

import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.SampleModel;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import javax.media.jai.ImageLayout;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.RasterFactory;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.CompositeDescriptor;

import org.hibernate.exception.ConstraintViolationException;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.ImageOnGrid;
import edu.rpi.metpetdb.client.service.ImageService;
import edu.rpi.metpetdb.server.ImageUploadServlet;
import edu.rpi.metpetdb.server.MpDbServlet;

public class ImageServiceImpl extends MpDbServlet implements ImageService {
	private static final long serialVersionUID = 1L;
	private static String baseFolder = "";

	public Image details(final long id) throws NoSuchObjectException {
		final Image i = (Image) byId("Image", id);
		return i;
	}

	@SuppressWarnings("unchecked")
	public ArrayList allImages(final long subsampleId)
			throws NoSuchObjectException {
		return (ArrayList) byKeySet("Image", "subsampleId", subsampleId);
	}

	public Image saveImage(Image image) throws ValidationException,
			LoginRequiredException {
		// oc.validate(image);
		// if (image.getSample().getOwner().getId() != currentUser())
		// throw new SecurityException("Cannot modify images you don't own.");

		try {
			if (image.mIsNew())
				insert(image);
			else
				image = (Image) update(merge(image));
			commit();
			SampleServiceImpl.resetSample(image.getSample());
			forgetChanges();
			return image;
		} catch (ConstraintViolationException cve) {
			throw cve;
		}
	}

	@SuppressWarnings("unchecked")
	public ImageOnGrid saveImageOnGrid(ImageOnGrid iog)
			throws ValidationException, LoginRequiredException {
		try {
			if (iog.mIsNew())
					insert(iog);
			else
				iog = (ImageOnGrid) update(merge(iog));
			commit();
			iog.getGrid().setImagesOnGrid(load(iog.getGrid().getImagesOnGrid()));
			forgetChanges();
			return iog;
		} catch (ConstraintViolationException cve) {
			throw cve;
		}
	}

	public ImageOnGrid rotate(ImageOnGrid iog, int degrees) {
		try {
			final File file = new File(baseFolder
					+ Image.getServerPath(iog.getImage().getChecksum()));
			final FileInputStream stream = new FileInputStream(file);
			final byte[] bytes = new byte[(int) file.length()];
			stream.read(bytes);
			final RenderedOp originalImage = ImageUploadServlet
					.loadImage(bytes);
			final RenderedOp rotatedImage = rotate(originalImage,
					(double) degrees, iog.getImage().getWidth(), iog.getImage()
							.getHeight());

			final File halfFile = new File(baseFolder
					+ Image.getServerPath(iog.getImage().getChecksumHalf()));
			final FileInputStream halfStream = new FileInputStream(halfFile);
			final byte[] halfBytes = new byte[(int) halfFile.length()];
			halfStream.read(halfBytes);
			final RenderedOp halfsizeImage = ImageUploadServlet
					.loadImage(halfBytes);
			final RenderedOp halfsizeRotated = rotate(halfsizeImage,
					(double) degrees, (float) halfsizeImage.getWidth(),
					(float) halfsizeImage.getHeight());
			deleteRotatedImages(iog);
			iog.setGchecksum(ImageUploadServlet.generateFullsize(
					rotatedImage, true));
			iog.setGchecksumHalf(ImageUploadServlet.generateFullsize(
					halfsizeRotated, true));
			iog.setGchecksum64x64(ImageUploadServlet.generate64x64(
					rotatedImage, true));
			iog.setGwidth(rotatedImage.getWidth());
			iog.setGheight(rotatedImage.getHeight());
			stream.close();
			halfStream.close();
			try {
				saveImageOnGrid(iog);

			} catch (LoginRequiredException lre) {

			} catch (ValidationException ve) {

			}
		} catch (FileNotFoundException fnfe) {

		} catch (IOException ioe) {

		}
		return iog;
	}

	public RenderedOp rotate(RenderedOp image, double ang, float width,
			float height) {
		//
		// Create a constant 1-band byte image to represent the alpha
		// channel. It has the source dimensions and is filled with
		// 255 to indicate that the entire source is opaque.
		ParameterBlock pb = new ParameterBlock();
		pb.add(width).add(height);
		pb.add(new Byte[]{new Byte((byte) 0xFF)});
		RenderedOp alpha = JAI.create("constant", pb);

		// Combine the source and alpha images such that the source image
		// occupies the first band(s) and the alpha image the last band.
		// RenderingHints are used to specify the destination SampleModel and
		// ColorModel.
		pb = new ParameterBlock();
		int numBands = image.getSampleModel().getNumBands();
		pb.addSource(image).addSource(image);
		pb.add(alpha).add(alpha).add(Boolean.FALSE);
		pb.add(CompositeDescriptor.DESTINATION_ALPHA_LAST);
		SampleModel sm = RasterFactory.createComponentSampleModel(image
				.getSampleModel(), DataBuffer.TYPE_BYTE, image.getTileWidth(),
				image.getTileHeight(), numBands + 1);
		ColorSpace cs = ColorSpace.getInstance(numBands == 1
				? ColorSpace.CS_GRAY
				: ColorSpace.CS_sRGB);
		ColorModel cm = RasterFactory.createComponentColorModel(
				DataBuffer.TYPE_BYTE, cs, true, false, Transparency.BITMASK);
		ImageLayout il = new ImageLayout();
		il.setSampleModel(sm).setColorModel(cm);
		RenderingHints rh = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, il);
		RenderedOp srca = JAI.create("composite", pb, rh);
		//
		// Rotate the source+alpha image.

		pb = new ParameterBlock();
		pb.addSource(srca);
		pb.add((float) (srca.getWidth()) / 2.0f);
		pb.add((float) (srca.getHeight()) / 2.0f);
		pb.add((float) Math.toRadians(ang));
		pb.add(Interpolation.getInstance(Interpolation.INTERP_BICUBIC));
		return JAI.create("rotate", pb);
	}

	public void deleteRotatedImages(final ImageOnGrid iog) {

		if (!iog.getGchecksum().equals(iog.getImage().getChecksum())) {
			new File(baseFolder + Image.getServerPath(iog.getGchecksum()))
					.delete();
			new File(baseFolder + Image.getServerPath(iog.getGchecksum64x64()))
					.delete();
			new File(baseFolder + Image.getServerPath(iog.getGchecksumHalf()))
					.delete();
		}
	}

	public void delete(Image i) {

		new File(baseFolder + Image.getServerPath(i.getChecksum())).delete();
		new File(baseFolder + Image.getServerPath(i.getChecksum64x64()))
				.delete();
		new File(baseFolder + Image.getServerPath(i.getChecksumHalf()))
				.delete();
	}
	
	public static final void resetImage(final Image i) {
		
	}
	
	@SuppressWarnings("unchecked")
	public static final void resetImage(final Set s) {
		final Iterator itr = s.iterator();
		while(itr.hasNext()) {
			resetImage((Image) itr.next());
		}
	}

	public static String getBaseFolder() {
		return baseFolder;
	}

	public static void setBaseFolder(String baseFolder) {
		ImageServiceImpl.baseFolder = baseFolder;
	}
}
