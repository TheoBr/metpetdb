package edu.rpi.metpetrest.security;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import edu.rpi.metpetrest.dao.ChemicalAnalysisDAOImpl;
import edu.rpi.metpetrest.dao.SampleDAOImpl;
import edu.rpi.metpetrest.dao.UserDAOImpl;

@Controller()
public class SecureUrlController {

	private UserDAOImpl userDAO = null;

	private ChemicalAnalysisDAOImpl chemicalAnalysisDAO = null;
	
	private SampleDAOImpl sampleDAO = null;

	Logger logger = LoggerFactory.getLogger(SecureUrlController.class);

	public SecureUrlController() {

	}

	@PreAuthorize("hasRole('MEMBER')")
	@RequestMapping(value = "/secure/chemical_analyses/{subSampleId}/{startRowNum}/{endRowNum}", method = RequestMethod.GET)
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

	
	@PreAuthorize("hasRole('MEMBER')")
	@RequestMapping(value = "/secure/mysamples/{startRowNum}/{endRowNum}", method = RequestMethod.GET)
	public ModelAndView getMySamples(
			@PathVariable("startRowNum") String startRowNum,
			@PathVariable("endRowNum") String endRowNum) {

		ModelAndView mav = new ModelAndView();
		mav.setViewName("samples");
		mav.setView(new MappingJacksonJsonView());
		MappingJacksonJsonView myView = (MappingJacksonJsonView) mav.getView();

		myView.setContentType("text/javascript");

		List<Map<String, Object>> jsonSamples = sampleDAO.getMySamples(Long.valueOf(startRowNum),
						Long.valueOf(endRowNum));

		mav.addObject("samples", jsonSamples);

		return mav;
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

	public SampleDAOImpl getSampleDAO() {
		return sampleDAO;
	}

	public void setSampleDAO(SampleDAOImpl sampleDAO) {
		this.sampleDAO = sampleDAO;
	}

}
