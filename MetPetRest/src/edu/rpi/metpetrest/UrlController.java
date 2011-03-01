package edu.rpi.metpetrest;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import edu.rpi.metpetrest.dao.ChemicalAnalysisDAOImpl;
import edu.rpi.metpetrest.dao.PublicationDAOImpl;
import edu.rpi.metpetrest.dao.SampleDAOImpl;
import edu.rpi.metpetrest.dao.UserDAOImpl;
import edu.rpi.metpetrest.model.EarthChemModel;
import edu.rpi.metpetrest.model.EarthChemSample;
import edu.rpi.metpetrest.model.PublicationData;
import edu.rpi.metpetrest.model.SampleData;
import edu.rpi.metpetrest.model.UserData;
import edu.rpi.metpetrest.model.UserSampleData;
import edu.rpi.metpetrest.sitemap.model.SitemapModel;
import edu.rpi.metpetrest.sitemap.model.Url;

@Controller
public class UrlController {

	private SampleDAOImpl sampleDAO = null;

	private PublicationDAOImpl publicationDAO = null;

	private UserDAOImpl userDAO = null;

	private ChemicalAnalysisDAOImpl chemicalAnalysisDAO = null;

	private static final String uriRoot = "http://metpetdb.rpi.edu/metpetweb/";

	private static final DateFormatter formatter = new DateFormatter(
			"yyyy-MM-dd");

	Logger logger = LoggerFactory.getLogger(UrlController.class);

	public UrlController() {

	}

	@RequestMapping(value = "/chemical_analyses/{subSampleId}/{startRowNum}/{endRowNum}", method = RequestMethod.GET)
	public ModelAndView getChemicalAnalyses(
			@PathVariable("subSampleId") String subSampleId,
			@PathVariable("startRowNum") String startRowNum,
			@PathVariable("endRowNum") String endRowNum) {

		ModelAndView mav = new ModelAndView();
		mav.setViewName("chemicalAnalyses");
		mav.setView(new MappingJacksonJsonView());
		MappingJacksonJsonView myView = (MappingJacksonJsonView) mav.getView();

		myView.setContentType("text/javascript");

		List<Map<String, String>> jsonChemAnalyses = chemicalAnalysisDAO
				.getAllChemicalAnalyses(subSampleId, Long.valueOf(startRowNum),
						Long.valueOf(endRowNum));

		mav.addObject("chemicalAnalyses", jsonChemAnalyses);

		return mav;
	}

	@RequestMapping(value = "/sitemap", method = RequestMethod.GET)
	public ModelAndView getSitemap() {
		SitemapModel sitemap = new SitemapModel();

		for (SampleData sample : sampleDAO
				.findPublicSampleDataOwnedByPublication()) {
			sitemap.addUrl(new Url(uriRoot + "#sample/" + sample.getSampleId(),
					formatter.print(new Date(), Locale.ENGLISH), "always",
					"0.5"));
		}

		ModelAndView mav = new ModelAndView("sitemapView",
				BindingResult.MODEL_KEY_PREFIX + "siteMap", sitemap);

		return mav;
	}

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public ModelAndView getAllUsers() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("user");

		List<UserData> users = userDAO.getAllUsersExceptPUBLICATION();

		for (UserData currentUser : users) {
			mav.addObject(currentUser.getUserId().toString(), currentUser);
		}

		return mav;
	}

	@RequestMapping(value = "/publication", method = RequestMethod.GET)
	public ModelAndView getAllPublications() {

		ModelAndView mav = new ModelAndView();
		mav.setViewName("publication");

		List<PublicationData> publications = publicationDAO
				.getAllPublications();

		Collections.sort(publications);

		for (PublicationData currentPublication : publications) {
			mav.addObject(currentPublication.getReferenceId(),
					currentPublication);
		}

		return mav;
	}

	@RequestMapping(value = "/usersample", method = RequestMethod.GET)
	public ModelAndView getSamplesForUser(String userId) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("usersample");

		List<UserSampleData> samples = sampleDAO.getSamplesForUser(userId);

		for (UserSampleData currentSample : samples) {
			mav.addObject(currentSample.getSampleId().toString(), currentSample);
		}

		return mav;
	}

	@RequestMapping(value = "/publicationsample", method = RequestMethod.GET)
	public ModelAndView getSamplesForPublication(String referenceId) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("publicationsample");

		List<SampleData> samples = sampleDAO
				.getSamplesForPublication(referenceId);

		for (SampleData currentSample : samples) {
			mav.addObject(currentSample.getSampleId().toString(), currentSample);
		}

		return mav;

	}

	@RequestMapping(value = "/sample", method = RequestMethod.GET)
	public ModelAndView getSampleUrls() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("sampleUrls");

		for (SampleData sample : sampleDAO
				.findPublicSampleDataOwnedByPublication()) {
			mav.addObject(uriRoot + "#sample/" + sample.getSampleId(), sample);
		}

		return mav;
	}

	@RequestMapping(value = "/samples", method = RequestMethod.GET)
	public ModelAndView getSamples() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("samples");

		for (String sampleNumber : sampleDAO.getSamples()) {
			mav.addObject(sampleNumber, new SampleData(sampleNumber));
		}

		return mav;
	}

	@RequestMapping(value = "/content", method = RequestMethod.GET)
	public ModelAndView getUrls() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("urls");

		for (Long chemicalAnalysisId : sampleDAO
				.findPublicChemicalAnalysisIdsOwnedByPublication()) {
			mav.addObject("chemical_analysis_" + chemicalAnalysisId.toString(),
					uriRoot + "#ChemicalAnalysisDetails/" + chemicalAnalysisId);
		}

		for (Long subSampleId : sampleDAO
				.findPublicSubsampleIdsOwnedByPublication()) {
			mav.addObject("subsample_" + subSampleId.toString(), uriRoot
					+ "#subsample/" + subSampleId);
		}

		for (Long sampleId : sampleDAO.findPublicSampleIdsOwnedByPublication()) {
			mav.addObject("sample_" + sampleId.toString(), uriRoot + "#sample/"
					+ sampleId);
		}

		return mav;
	}

	@RequestMapping(value = "/earthchem_samples", method = RequestMethod.GET)
	public ModelAndView getEarthChemSamples() {

		logger.info("grabbing earth chem samples...");

		EarthChemModel samples = new EarthChemModel();

		for (String sampleNumber : sampleDAO.getSamples()) {
			samples.addEarthChemSample(this.getSample(sampleNumber));
		}

		// logger.info("hit/miss" +
		// sampleDAO.getCache().getLiveCacheStatistics().getCacheHitCount() +
		// "/" +
		// sampleDAO.getCache().getLiveCacheStatistics().getCacheMissCount());
		// logger.info("evicted/expired" +
		// sampleDAO.getCache().getLiveCacheStatistics().getEvictedCount() + "/"
		// + sampleDAO.getCache().getLiveCacheStatistics().getExpiredCount());
		// logger.info("imhit/immiss" +
		// sampleDAO.getCache().getLiveCacheStatistics().getInMemoryHitCount() +
		// "/" +
		// sampleDAO.getCache().getLiveCacheStatistics().getInMemoryMissCount());

		ModelAndView mav = new ModelAndView("earthChemSamplesView",
				BindingResult.MODEL_KEY_PREFIX + "earthChemSamples", samples);

		logger.info("done grabbing earth chem samples");
		return mav;

	}

	private EarthChemSample getSample(String sampleNumber) {

		EarthChemSample earthChemSample = sampleDAO
				.getEarthChemSample(sampleNumber);

		return earthChemSample;
	}

	@RequestMapping(value = "/earthchem_sample/{sampleNumber}", method = RequestMethod.GET)
	public ModelAndView getEarthChemSample(
			@PathVariable("sampleNumber") String sampleNumber) {

		ModelAndView mav = new ModelAndView("earthChemView",
				BindingResult.MODEL_KEY_PREFIX + "earthChemSample",
				this.getSample(sampleNumber));
		return mav;
	}

	public SampleDAOImpl getSampleDAO() {
		return sampleDAO;
	}

	public void setSampleDAO(SampleDAOImpl sampleDAO) {
		this.sampleDAO = sampleDAO;
	}

	public PublicationDAOImpl getPublicationDAO() {
		return publicationDAO;
	}

	public void setPublicationDAO(PublicationDAOImpl publicationDAO) {
		this.publicationDAO = publicationDAO;
	}

	public UserDAOImpl getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAOImpl userDAO) {
		this.userDAO = userDAO;
	}

	public ChemicalAnalysisDAOImpl getChemicalAnalysisDAO() {
		return chemicalAnalysisDAO;
	}

	public void setChemicalAnalysisDAO(
			ChemicalAnalysisDAOImpl chemicalAnalysisDAO) {
		this.chemicalAnalysisDAO = chemicalAnalysisDAO;
	}

}
