package edu.rpi.metpetdb.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.validation.DatabaseObjectConstraints;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraints;
import edu.rpi.metpetdb.client.service.ChemicalAnalysisService;
import edu.rpi.metpetdb.client.service.ChemicalAnalysisServiceAsync;
import edu.rpi.metpetdb.client.service.ConstantsService;
import edu.rpi.metpetdb.client.service.ConstantsServiceAsync;
import edu.rpi.metpetdb.client.service.GeoReferenceService;
import edu.rpi.metpetdb.client.service.GeoReferenceServiceAsync;
import edu.rpi.metpetdb.client.service.ImageBrowserService;
import edu.rpi.metpetdb.client.service.ImageBrowserServiceAsync;
import edu.rpi.metpetdb.client.service.ImageService;
import edu.rpi.metpetdb.client.service.ImageServiceAsync;
import edu.rpi.metpetdb.client.service.ImageTypeService;
import edu.rpi.metpetdb.client.service.ImageTypeServiceAsync;
import edu.rpi.metpetdb.client.service.MetamorphicGradeService;
import edu.rpi.metpetdb.client.service.MetamorphicGradeServiceAsync;
import edu.rpi.metpetdb.client.service.MetamorphicRegionService;
import edu.rpi.metpetdb.client.service.MetamorphicRegionServiceAsync;
import edu.rpi.metpetdb.client.service.MpDbGenericService;
import edu.rpi.metpetdb.client.service.MpDbGenericServiceAsync;
import edu.rpi.metpetdb.client.service.ProjectService;
import edu.rpi.metpetdb.client.service.ProjectServiceAsync;
import edu.rpi.metpetdb.client.service.RecaptchaRemoteService;
import edu.rpi.metpetdb.client.service.RecaptchaRemoteServiceAsync;
import edu.rpi.metpetdb.client.service.ReferenceService;
import edu.rpi.metpetdb.client.service.ReferenceServiceAsync;
import edu.rpi.metpetdb.client.service.RegionService;
import edu.rpi.metpetdb.client.service.RegionServiceAsync;
import edu.rpi.metpetdb.client.service.SampleCommentService;
import edu.rpi.metpetdb.client.service.SampleCommentServiceAsync;
import edu.rpi.metpetdb.client.service.SampleService;
import edu.rpi.metpetdb.client.service.SampleServiceAsync;
import edu.rpi.metpetdb.client.service.SearchService;
import edu.rpi.metpetdb.client.service.SearchServiceAsync;
import edu.rpi.metpetdb.client.service.SubsampleService;
import edu.rpi.metpetdb.client.service.SubsampleServiceAsync;
import edu.rpi.metpetdb.client.service.UserService;
import edu.rpi.metpetdb.client.service.UserServiceAsync;
import edu.rpi.metpetdb.client.service.bulk.upload.BulkUploadChemicalAnalysesService;
import edu.rpi.metpetdb.client.service.bulk.upload.BulkUploadChemicalAnalysesServiceAsync;
import edu.rpi.metpetdb.client.service.bulk.upload.BulkUploadImagesService;
import edu.rpi.metpetdb.client.service.bulk.upload.BulkUploadImagesServiceAsync;
import edu.rpi.metpetdb.client.service.bulk.upload.BulkUploadReferencesService;
import edu.rpi.metpetdb.client.service.bulk.upload.BulkUploadReferencesServiceAsync;
import edu.rpi.metpetdb.client.service.bulk.upload.BulkUploadSampleService;
import edu.rpi.metpetdb.client.service.bulk.upload.BulkUploadSampleServiceAsync;
import edu.rpi.metpetdb.client.ui.commands.VoidServerOp;

/**
 * Client side async service proxies and global constants.
 * <p>
 * <i><b>Only available on the client side.</b></i>
 * </p>
 * <p>
 * The server side GWT runtime is unable to create service proxies, and is
 * equally unable to load locale files.
 * </p>
 */
public class MpDb {
	public static final ConstantsServiceAsync constants_svc;

	public static final ProjectServiceAsync project_svc;

	public static final SampleServiceAsync sample_svc;

	public static final UserServiceAsync user_svc;

	public static final SubsampleServiceAsync subsample_svc;

	public static final ImageBrowserServiceAsync imageBrowser_svc;

	public static final ImageServiceAsync image_svc;

	public static final ChemicalAnalysisServiceAsync chemicalAnalysis_svc;
	
	public static final MpDbGenericServiceAsync mpdbGeneric_svc;

	public static final BulkUploadSampleServiceAsync bulkUploadSamples_svc;

	public static final BulkUploadChemicalAnalysesServiceAsync bulkUploadChemicalAnalyses_svc;

	public static final BulkUploadImagesServiceAsync bulkUploadImages_svc;
	
	public static final BulkUploadReferencesServiceAsync bulkUploadReferences_svc;

	public static final SearchServiceAsync search_svc;

	public static final RegionServiceAsync region_svc;

	public static final ImageTypeServiceAsync imageType_svc;

	public static final ReferenceServiceAsync reference_svc;

	public static final MetamorphicGradeServiceAsync metamorphicGrade_svc;
	
	public static final MetamorphicRegionServiceAsync metamorphicRegion_svc;
	
	public static final SampleCommentServiceAsync sampleComment_svc;
	
	public static final GeoReferenceServiceAsync geoReference_svc;
	
	public static final RecaptchaRemoteServiceAsync recaptcha_svc;

	public static DatabaseObjectConstraints doc;

	public static ObjectConstraints oc;

	protected static User currentUser;

	public static final String WIKI_URL = "http://wiki.cs.rpi.edu/trac/metpetdb";
	public static final String JAVADOC_URL = "http://samana.cs.rpi.edu:8080/metpetwebtst/api/index.html";
	public static final String JUNIT_URL = "http://samana.cs.rpi.edu:8080/metpetwebtst/reports/index.html";
	public static final String BUG_REPORT_URL = "http://metpetdb.rpi.edu/mantis/";
	/*public static final String METPETDB_HELP_URL = GWT.getHostPageBaseURL() + "help/helphome.html";
	public static final String HOW_TO_BULK_UPLOAD_URL = GWT.getHostPageBaseURL() + "help/HowToUploadData.html";
	public static final String HOW_TO_VIEW_DATA_URL = GWT.getHostPageBaseURL() +"help/HowToViewData.html";
	public static final String HOW_TO_SEARCH_METPETDB_URL = GWT.getHostPageBaseURL() +"help/Search.html";
	public static final String VIDEO_TUTORIALS_URL = GWT.getHostPageBaseURL() +"help/VideoTutorials.html";
	public static final String LISTS_OF_DATA_TYPES_URL = GWT.getHostPageBaseURL() +"help/DataTypeIndex.html";*/
	public static final String METPETDB_HELP_URL = "http://wiki.cs.rpi.edu/trac/metpetdb/wiki";
	public static final String HOW_TO_BULK_UPLOAD_URL = "http://wiki.cs.rpi.edu/trac/metpetdb/wiki/HowToUploadData";
	public static final String HOW_TO_VIEW_DATA_URL = "http://wiki.cs.rpi.edu/trac/metpetdb/wiki/HowToViewData";
	public static final String HOW_TO_SEARCH_METPETDB_URL = "http://wiki.cs.rpi.edu/trac/metpetdb/wiki/Search";
	public static final String VIDEO_TUTORIALS_URL = "http://wiki.cs.rpi.edu/trac/metpetdb/wiki/VideoTutorials";
	public static final String LISTS_OF_DATA_TYPES_URL = "http://wiki.cs.rpi.edu/trac/metpetdb/wiki/Lists";
	public static final String ABOUT_METPETDB_URL = "http://wiki.cs.rpi.edu/trac/metpetdb/wiki/About";
	public static final String DOWNLOAD_IPHONE_APP_URL = "http://appshopper.com/link/metpetdb";
	public static final String IPHONE_APP_WIKI = "http://wiki.cs.rpi.edu/trac/metpetdb/wiki/IphoneApp";


	// public static final HtmlFactory factory;

	static {
		constants_svc = (ConstantsServiceAsync) bindService(GWT
				.create(ConstantsService.class), "constants");

		project_svc = (ProjectServiceAsync) bindService(GWT
				.create(ProjectService.class), "project");

		user_svc = (UserServiceAsync) bindService(
				GWT.create(UserService.class), "user");

		sample_svc = (SampleServiceAsync) bindService(GWT
				.create(SampleService.class), "sample");

		subsample_svc = (SubsampleServiceAsync) bindService(GWT
				.create(SubsampleService.class), "subsample");

		imageBrowser_svc = (ImageBrowserServiceAsync) bindService(GWT
				.create(ImageBrowserService.class), "imageBrowser");

		image_svc = (ImageServiceAsync) bindService(GWT
				.create(ImageService.class), "image");

		chemicalAnalysis_svc = (ChemicalAnalysisServiceAsync) bindService(GWT
			.create(ChemicalAnalysisService.class), "chemicalAnalysis");
		
		mpdbGeneric_svc = (MpDbGenericServiceAsync) bindService(GWT
				.create(MpDbGenericService.class), "mpdbGeneric");

		bulkUploadSamples_svc = (BulkUploadSampleServiceAsync) bindService(GWT
				.create(BulkUploadSampleService.class), "bulkUpload");

		bulkUploadChemicalAnalyses_svc = (BulkUploadChemicalAnalysesServiceAsync) bindService(
				GWT.create(BulkUploadChemicalAnalysesService.class),
				"bulkUploadChemicalAnalyses");

		bulkUploadImages_svc = (BulkUploadImagesServiceAsync) bindService(GWT
				.create(BulkUploadImagesService.class), "bulkUploadImages");
		
		bulkUploadReferences_svc = (BulkUploadReferencesServiceAsync) bindService(GWT
				.create(BulkUploadReferencesService.class), "bulkUploadReferences");

		search_svc = (SearchServiceAsync) bindService(GWT
				.create(SearchService.class), "search");

		region_svc = (RegionServiceAsync) bindService(GWT
				.create(RegionService.class), "region");

		imageType_svc = (ImageTypeServiceAsync) bindService(GWT
				.create(ImageTypeService.class), "imageType");

		reference_svc = (ReferenceServiceAsync) bindService(GWT
				.create(ReferenceService.class), "reference");

		metamorphicGrade_svc = (MetamorphicGradeServiceAsync) bindService(GWT
				.create(MetamorphicGradeService.class), "metamorphicGrade");
		
		metamorphicRegion_svc = (MetamorphicRegionServiceAsync) bindService(GWT
				.create(MetamorphicRegionService.class), "metamorphicRegion");
		
		sampleComment_svc = (SampleCommentServiceAsync) bindService(GWT
				.create(SampleCommentService.class), "sampleComment");
		
		geoReference_svc = (GeoReferenceServiceAsync) bindService(GWT
				.create(GeoReferenceService.class), "geoReference");
		
		recaptcha_svc = (RecaptchaRemoteServiceAsync) bindService(GWT.create(RecaptchaRemoteService.class), "recaptcha");

		// factory = (HtmlFactory) GWT.create(HtmlFactory.class);
	}

	protected static Object bindService(final Object svc, final String name) {
		final ServiceDefTarget e = (ServiceDefTarget) svc;
		e.setServiceEntryPoint(GWT.getModuleBaseURL() + name + ".svc");
		return svc;
	}

	public static boolean isLoggedIn() {
		return currentUser != null;
	}

	public static boolean isCurrentUser(final User u) {
		return u != null && currentUser != null
				&& u.getId() == currentUser.getId();
	}

	public static User currentUser() {
		return currentUser;
	}

	public static void setCurrentUser(final User n) {
		final User o = currentUser;
		currentUser = n;
		if (o != n
				&& (o == null || n == null || o.getId() != n.getId() || o
						.getEnabled() != n.getEnabled())) {
			MetPetDBApplication.onCurrentUserChanged(n);
			if (n != null) {

			}
		}
		// end the session if we are setting it to null
		if (n == null) {
			new VoidServerOp() {
				public void begin() {
					MpDb.user_svc.endSession(this);
				}
				@Override
				public void onSuccess() {
				}
			}.execute();
		}
	}

	// Creates user history in the left column
	// My Samples and My Projects history
	public static void createUserHistory(User n) {
		// MetPetDBApplication.clearLeftSide();

	}

	protected MpDb() {
	}
}
