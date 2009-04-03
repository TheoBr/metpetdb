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
import java.util.List;

import javax.media.jai.ImageLayout;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.RasterFactory;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.CompositeDescriptor;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.ImageOnGrid;
import edu.rpi.metpetdb.client.model.XrayImage;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.service.ImageService;
import edu.rpi.metpetdb.server.ImageUploadServlet;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.impl.ImageDAO;
import edu.rpi.metpetdb.server.dao.impl.ImageOnGridDAO;
import edu.rpi.metpetdb.server.dao.impl.XrayImageDAO;

public class ImageServiceImpl extends MpDbServlet implements ImageService {
	private static final long serialVersionUID = 1L;
	private static String baseFolder = "";

	public Image details(final long id) throws MpDbException {
		Image i = new Image();
		i.setId(id);
		i = (new ImageDAO(this.currentSession())).fill(i);

		return (i);
	}

	public List<Image> allImages(final long subsampleId) throws MpDbException {
		final List<Image> images = (new ImageDAO(this.currentSession()))
				.getBySubsampleId(subsampleId);
		return (images);
	}

	public Image saveImage(Image image) throws ValidationException,
			LoginRequiredException, MpDbException {
		doc.validate(image);
		Image i = (image);

		i = (new ImageDAO(this.currentSession())).save(i);

		commit();
		return (i);
	}

	public XrayImage saveImage(XrayImage xrayimg) throws ValidationException,
			LoginRequiredException, MpDbException {
		doc.validate(xrayimg);
		XrayImage i = (xrayimg);
		i = (new XrayImageDAO(this.currentSession())).save(i);
		commit();
		return (i);
	}

	public ImageOnGrid saveImageOnGrid(ImageOnGrid iog)
			throws ValidationException, LoginRequiredException, MpDbException {
		iog = (new ImageOnGridDAO(this.currentSession())).save(iog);
		commit();
		return (iog);
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
			iog.setGchecksum(ImageUploadServlet.generateFullsize(rotatedImage,
					true));
			iog.setGchecksumHalf(ImageUploadServlet.generateFullsize(
					halfsizeRotated, true));
			iog.setGchecksum64x64(ImageUploadServlet.generate64x64(
					rotatedImage, true));
			iog.setGwidth(rotatedImage.getWidth());
			iog.setGheight(rotatedImage.getHeight());
			stream.close();
			halfStream.close();
			// TODO: This many exceptions just ignored scares me
			// perhaps a justifying comment is in order?
			try {
				saveImageOnGrid(iog);

			} catch (LoginRequiredException lre) {

			} catch (ValidationException ve) {

			} catch (MpDbException daoe) {

			}
		} catch (FileNotFoundException fnfe) {

		} catch (IOException ioe) {

		}
		return iog;
	}

	public RenderedOp rotate(RenderedOp Image, double ang, float width,
			float height) {
		//
		// Create a constant 1-band byte Image to represent the alpha
		// channel. It has the source dimensions and is filled with
		// 255 to indicate that the entire source is opaque.
		ParameterBlock pb = new ParameterBlock();
		pb.add(width).add(height);
		pb.add(new Byte[] {
			new Byte((byte) 0xFF)
		});
		RenderedOp alpha = JAI.create("constant", pb);

		// Combine the source and alpha images such that the source Image
		// occupies the first band(s) and the alpha Image the last band.
		// RenderingHints are used to specify the destination SampleModel and
		// ColorModel.
		pb = new ParameterBlock();
		int numBands = Image.getSampleModel().getNumBands();
		pb.addSource(Image).addSource(Image);
		pb.add(alpha).add(alpha).add(Boolean.FALSE);
		pb.add(CompositeDescriptor.DESTINATION_ALPHA_LAST);
		SampleModel sm = RasterFactory.createComponentSampleModel(Image
				.getSampleModel(), DataBuffer.TYPE_BYTE, Image.getTileWidth(),
				Image.getTileHeight(), numBands + 1);
		ColorSpace cs = ColorSpace
				.getInstance(numBands == 1 ? ColorSpace.CS_GRAY
						: ColorSpace.CS_sRGB);
		ColorModel cm = RasterFactory.createComponentColorModel(
				DataBuffer.TYPE_BYTE, cs, true, false, Transparency.BITMASK);
		ImageLayout il = new ImageLayout();
		il.setSampleModel(sm).setColorModel(cm);
		RenderingHints rh = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, il);
		RenderedOp srca = JAI.create("composite", pb, rh);
		//
		// Rotate the source+alpha Image.

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

	public static String getBaseFolder() {
		return baseFolder;
	}

	public static void setBaseFolder(String baseFolder) {
		ImageServiceImpl.baseFolder = baseFolder;
	}

	public Results<Image> allForImageMap(long subsampleId,
			PaginationParameters p) throws MpDbException {
		return new ImageDAO(currentSession()).getAllBySubsampleId(p, subsampleId);
	}
}
