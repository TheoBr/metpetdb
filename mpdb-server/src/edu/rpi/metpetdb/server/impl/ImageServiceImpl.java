package edu.rpi.metpetdb.server.impl;

import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.SampleModel;
import java.awt.image.renderable.ParameterBlock;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.XrayImage;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.service.ImageService;
import edu.rpi.metpetdb.server.ImageUploadServlet;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.impl.ChemicalAnalysisDAO;
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
	
	public List<Image> details(final List<Long> ids) throws MpDbException {
		List<Image> images = new ArrayList<Image>();
		for (Long id : ids) {
			Image i = new Image();
			i.setId(id);
			i = (new ImageDAO(this.currentSession())).fill(i);
			images.add(i);
		}
		return images;
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
	
	public List<ImageOnGrid> rotate(List<ImageOnGrid> iogs, int degrees) {
		for (ImageOnGrid iog : iogs) {
			iog = rotate(iog, degrees);
		}
		return iogs;
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
					(double) degrees, iog.getImage().getWidth(), iog.getImage().getHeight());

			final File halfFile = new File(baseFolder
					+ Image.getServerPath(iog.getImage().getChecksumHalf()));
			final FileInputStream halfStream = new FileInputStream(halfFile);
			final byte[] halfBytes = new byte[(int) halfFile.length()];
			halfStream.read(halfBytes);
			final RenderedOp halfsizeImage = ImageUploadServlet
					.loadImage(halfBytes);
			final RenderedOp halfsizeRotated = rotate(halfsizeImage,
					(double) degrees, halfsizeImage.getWidth(),
					halfsizeImage.getHeight());
			deleteRotatedImages(iog);
			iog.setGchecksum(ImageUploadServlet.generateFullsize(rotatedImage,
					true));
			iog.setGchecksumHalf(ImageUploadServlet.generateFullsize(
					halfsizeRotated, true));
			iog.setGchecksum64x64(ImageUploadServlet.generate64x64(
					rotatedImage, true));
			iog.setGwidth(rotatedImage.getWidth());
			iog.setGheight(rotatedImage.getHeight());
			iog.setAngle(degrees);
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

	public Results<Image> allImages(long subsampleId,
			PaginationParameters p) throws MpDbException {
		return new ImageDAO(currentSession()).getAllBySubsampleId(p, subsampleId);
	}
	
	/**
	 * used for pagination tables to select all/public/private
	 */
	public Map<Object,Boolean> allImageIds(long subsampleId) throws MpDbException {
		Map<Object,Boolean> ids = new HashMap<Object,Boolean>();
		for (Object[] row : new ImageDAO(this.currentSession()).getIdsBySubsampleId(subsampleId)){
			ids.put(row[0], (Boolean) row[1]);
		}
		return ids;
	}
	
	public void makePublicBySubsampleId(ArrayList<Subsample> subsamples) throws ValidationException, MpDbException {
		final ImageDAO dao = new ImageDAO(this.currentSession());
		List<Image> imageList = new ArrayList<Image>();
		for(Subsample ss: subsamples){
			imageList = dao.getBySubsampleId(ss.getId());
			for(Image i: imageList){
				i.setPublicData(true);
				doc.validate(i);
				dao.save(i);
			}
		}
		commit();
	}
	
	public void makePublicBySampleId(ArrayList<Sample> samples) throws ValidationException, MpDbException {
		final ImageDAO dao = new ImageDAO(this.currentSession());
		List<Image> imageList = new ArrayList<Image>();
		for(Sample s: samples){
			imageList = dao.getBySampleId(s.getId());
			for(Image i: imageList){
				i.setPublicData(true);
				doc.validate(i);
				dao.save(i);
			}
		}
		commit();
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

	public void makeMobileImages()throws ValidationException, MpDbException {
		final ImageDAO dao = new ImageDAO(this.currentSession());
		List <Image> imageList = dao.getImagesWithoutMobile();
		for(Image i : imageList) {
			if (i.getSubsample() != null)
				i.getSubsample().getId();
			if (i.getSample() != null)
				i.getSample().getId();
			String checksum= i.getChecksum();
			if (checksum != null) {
				checksum = checksum.replaceAll("\\\\|/", "");

				final String folder = checksum.substring(0, 2);
				final String subfolder = checksum.substring(2, 4);
				final String filename = checksum.substring(4, checksum.length());

				final File imagePath = new File(baseFolder + "/" + folder + "/"
						+ subfolder + "/" + filename);

				try{
					final FileInputStream reader = new FileInputStream(imagePath);
					//BufferedInputStream input = null;
					//input = new BufferedInputStream(new FileInputStream(imagePath));
					final byte[] imgData = getBytesFromInputStream(reader);
					RenderedOp ro = ImageUploadServlet.loadImage(imgData);
					i.setChecksumMobile(ImageUploadServlet.generateMobileVersion(ro,false));
					System.out.print("Created checksum for image: " + i.getId() + " : " + i.getChecksumMobile());
					dao.save(i);
				} catch (IOException ioe){
					System.out.print(ioe.getMessage());
				}
			}
		}
		commit();
	}
	
	public long getPublicCount() {
		return new ImageDAO(this.currentSession()).getPublicCount();
	}
	
	public long getPrivateCount() {
		return new ImageDAO(this.currentSession()).getPrivateCount();
	}
	
	public long getPublicationCount() {
		return new ImageDAO(this.currentSession()).getPublicationCount();
	}
}