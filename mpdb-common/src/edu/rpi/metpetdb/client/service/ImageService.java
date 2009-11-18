package edu.rpi.metpetdb.client.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;

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

public interface ImageService extends RemoteService {
	Results<Image> allImages(long subsampleId, PaginationParameters p)
			throws MpDbException;
	Image details(long id) throws MpDbException;
	List<Image> details(List<Long> ids) throws MpDbException;
	Map<Object,Boolean> allImageIds(long subsampleId) throws MpDbException;
	Image saveImage(Image image) throws ValidationException,
			LoginRequiredException, MpDbException;
	XrayImage saveImage(XrayImage xrayimg) throws ValidationException,
			LoginRequiredException, MpDbException;
	ImageOnGrid saveImageOnGrid(ImageOnGrid iog) throws ValidationException,
			LoginRequiredException, MpDbException;
	void delete(Image i);
	ImageOnGrid rotate(ImageOnGrid iog, int degrees);
	void makePublicBySubsampleId(ArrayList<Subsample> subsamples) throws ValidationException, MpDbException;
	void makePublicBySampleId(ArrayList<Sample> samples) throws ValidationException, MpDbException;
	void makeMobileImages()throws ValidationException, MpDbException;
	long getPublicationCount();	
	long getPublicCount();	
	long getPrivateCount();
}
