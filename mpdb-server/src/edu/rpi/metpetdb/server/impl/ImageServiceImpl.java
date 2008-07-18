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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.media.jai.ImageLayout;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.RasterFactory;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.CompositeDescriptor;

import org.hibernate.Query;
import org.hibernate.exception.ConstraintViolationException;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.GridDTO;
import edu.rpi.metpetdb.client.model.ImageDTO;
import edu.rpi.metpetdb.client.model.ImageOnGridDTO;
import edu.rpi.metpetdb.client.model.XrayImageDTO;
import edu.rpi.metpetdb.client.service.ImageService;
import edu.rpi.metpetdb.server.ImageUploadServlet;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.model.Element;
import edu.rpi.metpetdb.server.model.Grid;
import edu.rpi.metpetdb.server.model.Image;
import edu.rpi.metpetdb.server.model.ImageOnGrid;
import edu.rpi.metpetdb.server.model.Sample;
import edu.rpi.metpetdb.server.model.Subsample;
import edu.rpi.metpetdb.server.model.XrayImage;

public class ImageServiceImpl extends MpDbServlet implements ImageService {
	private static final long serialVersionUID = 1L;
	private static String baseFolder = "";

	public ImageDTO details(final long id) throws NoSuchObjectException {
		final ImageDTO i = cloneBean(byId("Image", id));
		return i;
	}

	public List<ImageDTO> allImages(final long subsampleId)
			throws NoSuchObjectException {
		final List<Image> images = byKey("Image", "subsampleId", subsampleId);
		return cloneBean(images);
	}
	public ImageDTO saveImage(ImageDTO image) throws ValidationException,
			LoginRequiredException {
		// oc.validate(ImageDTO);
		// if (ImageDTO.getSample().getOwner().getId() != currentUser())
		// throw new SecurityException("Cannot modify images you don't own.");
		Image i = mergeBean(image);
		i = save(i);
		commit();
		return cloneBean(i);
	}

	public XrayImageDTO saveImage(XrayImageDTO xrayimg)
			throws ValidationException, LoginRequiredException {
		XrayImage i = mergeBean(xrayimg);
		i = save(i);
		commit();
		return cloneBean(i);
	}

	public XrayImage save(XrayImage img) throws ValidationException,
			LoginRequiredException {
		replaceSample(img);
		replaceElement(img);

		try {
			if (img.mIsNew())
				insert(img);
			else
				img = update(merge(img));
		} catch (ConstraintViolationException cve) {
			throw cve;
		}
		return img;
	}

	public Image save(Image img) throws ValidationException,
			LoginRequiredException {
		replaceSample(img);

		try {
			if (img.mIsNew())
				insert(img);
			else
				img = update(merge(img));
		} catch (ConstraintViolationException cve) {
			throw cve;
		}
		return img;
	}

	protected void save(final Collection<ImageDTO> images)
			throws ValidationException, LoginRequiredException {
		for (ImageDTO image : images) {
			if (image instanceof XrayImageDTO) {
				XrayImage i = mergeBean(image);
				save(i);
			} else {
				Image i = mergeBean(image);
				save(i);
			}
		}
		commit();
	}

	public ImageOnGridDTO saveImageOnGrid(ImageOnGridDTO iogDTO)
			throws ValidationException, LoginRequiredException {
		ImageOnGrid iog = mergeBean(iogDTO);
		try {
			if (iog.mIsNew())
				insert(iog);
			else
				iog = update(merge(iog));
			commit();
			return cloneBean(iog);
		} catch (ConstraintViolationException cve) {
			throw cve;
		}
	}

	protected ImageOnGridDTO saveIncompleteImageOnGrid(ImageOnGridDTO iogDTO)
			throws ValidationException, LoginRequiredException {
		// First save the image
		Image i = mergeBean(iogDTO.getImage());
		i = save(i);
		iogDTO.setImage((ImageDTO) cloneBean(i));

		// Set the grid, either find the appropriate old one, or a new one
		GridDTO g = iogDTO.getImage().getSubsample().getGrid();
		if (g == null) {
			// Create New Grid
			g = new GridDTO();
			g.setSubsample(iogDTO.getImage().getSubsample());
			g = saveGrid(g);
		}
		iogDTO.setGrid(g);

		// Save ImageOnGrid
		ImageOnGrid iog = mergeBean(iogDTO);
		try {
			if (iog.mIsNew())
				insert(iog);
			else
				iog = update(merge(iog));

			return cloneBean(iog);
		} catch (ConstraintViolationException cve) {
			throw cve;
		}
	}

	// Code duplication isn't fun, but seems to be necessary from time to time
	protected GridDTO saveGrid(GridDTO grid) throws LoginRequiredException {
		if (grid.getSubsample().getSample().getOwner().getId() != currentUser())
			throw new SecurityException("Cannot modify grids you don't own.");
		Grid g = mergeBean(grid);
		try {
			if (g.mIsNew())
				insert(g);
			else
				g = (Grid) update(merge(g));
			return cloneBean(g);
		} catch (ConstraintViolationException cve) {
			throw cve;
		}
	}

	public ImageOnGridDTO rotate(ImageOnGridDTO iog, int degrees) {
		try {
			final File file = new File(baseFolder
					+ ImageDTO.getServerPath(iog.getImage().getChecksum()));
			final FileInputStream stream = new FileInputStream(file);
			final byte[] bytes = new byte[(int) file.length()];
			stream.read(bytes);
			final RenderedOp originalImage = ImageUploadServlet
					.loadImage(bytes);
			final RenderedOp rotatedImage = rotate(originalImage,
					(double) degrees, iog.getImage().getWidth(), iog.getImage()
							.getHeight());

			final File halfFile = new File(baseFolder
					+ ImageDTO.getServerPath(iog.getImage().getChecksumHalf()));
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

	public RenderedOp rotate(RenderedOp ImageDTO, double ang, float width,
			float height) {
		//
		// Create a constant 1-band byte ImageDTO to represent the alpha
		// channel. It has the source dimensions and is filled with
		// 255 to indicate that the entire source is opaque.
		ParameterBlock pb = new ParameterBlock();
		pb.add(width).add(height);
		pb.add(new Byte[] {
			new Byte((byte) 0xFF)
		});
		RenderedOp alpha = JAI.create("constant", pb);

		// Combine the source and alpha images such that the source ImageDTO
		// occupies the first band(s) and the alpha ImageDTO the last band.
		// RenderingHints are used to specify the destination SampleModel and
		// ColorModel.
		pb = new ParameterBlock();
		int numBands = ImageDTO.getSampleModel().getNumBands();
		pb.addSource(ImageDTO).addSource(ImageDTO);
		pb.add(alpha).add(alpha).add(Boolean.FALSE);
		pb.add(CompositeDescriptor.DESTINATION_ALPHA_LAST);
		SampleModel sm = RasterFactory.createComponentSampleModel(ImageDTO
				.getSampleModel(), DataBuffer.TYPE_BYTE, ImageDTO
				.getTileWidth(), ImageDTO.getTileHeight(), numBands + 1);
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
		// Rotate the source+alpha ImageDTO.

		pb = new ParameterBlock();
		pb.addSource(srca);
		pb.add((float) (srca.getWidth()) / 2.0f);
		pb.add((float) (srca.getHeight()) / 2.0f);
		pb.add((float) Math.toRadians(ang));
		pb.add(Interpolation.getInstance(Interpolation.INTERP_BICUBIC));
		return JAI.create("rotate", pb);
	}

	public void deleteRotatedImages(final ImageOnGridDTO iog) {

		if (!iog.getGchecksum().equals(iog.getImage().getChecksum())) {
			new File(baseFolder + ImageDTO.getServerPath(iog.getGchecksum()))
					.delete();
			new File(baseFolder
					+ ImageDTO.getServerPath(iog.getGchecksum64x64())).delete();
			new File(baseFolder
					+ ImageDTO.getServerPath(iog.getGchecksumHalf())).delete();
		}
	}

	public void delete(ImageDTO i) {

		new File(baseFolder + ImageDTO.getServerPath(i.getChecksum())).delete();
		new File(baseFolder + ImageDTO.getServerPath(i.getChecksum64x64()))
				.delete();
		new File(baseFolder + ImageDTO.getServerPath(i.getChecksumHalf()))
				.delete();
	}

	@Deprecated
	public static final void resetImage(final ImageDTO i) {

	}

	@Deprecated
	public static final void resetImage(final Set<ImageDTO> s) {
		final Iterator<ImageDTO> itr = s.iterator();
		while (itr.hasNext()) {
			resetImage(itr.next());
		}
	}

	public static String getBaseFolder() {
		return baseFolder;
	}

	public static void setBaseFolder(String baseFolder) {
		ImageServiceImpl.baseFolder = baseFolder;
	}

	private void replaceSample(final Image img) throws ValidationException,
			LoginRequiredException {
		// TODO: once this is debugged, Null Pointer exceptions should indicate
		// when samples or subsamples are not in the database, and should be
		// handled accordingly
		final Query sample = namedQuery("Sample.byUser.byAlias");
		final Query subsample = namedQuery("Subsample.bySample.byName");

		sample.setParameter("id", currentUser());
		sample.setParameter("alias", img.getSubsample().getSample().getAlias());

		subsample.setParameter("id", ((Sample) sample.uniqueResult()).getId());
		subsample.setParameter("name", img.getSubsample().getName());

		img.setSubsample((Subsample) subsample.uniqueResult());
	}

	private void replaceElement(final XrayImage img)
			throws ValidationException, LoginRequiredException {
		final Query element = namedQuery("Element.byName");

		element.setParameter("name", img.getElement().getName());

		img.setElement((Element) element.uniqueResult());
	}
}
