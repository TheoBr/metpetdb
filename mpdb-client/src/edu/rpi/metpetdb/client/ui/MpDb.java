package edu.rpi.metpetdb.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.UserDTO;
import edu.rpi.metpetdb.client.model.validation.DatabaseObjectConstraints;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraints;
import edu.rpi.metpetdb.client.service.BulkUploadChemicalAnalysesService;
import edu.rpi.metpetdb.client.service.BulkUploadChemicalAnalysesServiceAsync;
import edu.rpi.metpetdb.client.service.BulkUploadImagesService;
import edu.rpi.metpetdb.client.service.BulkUploadImagesServiceAsync;
import edu.rpi.metpetdb.client.service.BulkUploadService;
import edu.rpi.metpetdb.client.service.BulkUploadServiceAsync;
import edu.rpi.metpetdb.client.service.ChemicalAnalysisService;
import edu.rpi.metpetdb.client.service.ChemicalAnalysisServiceAsync;
import edu.rpi.metpetdb.client.service.ConstantsService;
import edu.rpi.metpetdb.client.service.ConstantsServiceAsync;
import edu.rpi.metpetdb.client.service.ImageBrowserService;
import edu.rpi.metpetdb.client.service.ImageBrowserServiceAsync;
import edu.rpi.metpetdb.client.service.ImageService;
import edu.rpi.metpetdb.client.service.ImageServiceAsync;
import edu.rpi.metpetdb.client.service.MpDbGenericService;
import edu.rpi.metpetdb.client.service.MpDbGenericServiceAsync;
import edu.rpi.metpetdb.client.service.ProjectService;
import edu.rpi.metpetdb.client.service.ProjectServiceAsync;
import edu.rpi.metpetdb.client.service.SampleService;
import edu.rpi.metpetdb.client.service.SampleServiceAsync;
import edu.rpi.metpetdb.client.service.SearchService;
import edu.rpi.metpetdb.client.service.SearchServiceAsync;
import edu.rpi.metpetdb.client.service.SubsampleService;
import edu.rpi.metpetdb.client.service.SubsampleServiceAsync;
import edu.rpi.metpetdb.client.service.UserService;
import edu.rpi.metpetdb.client.service.UserServiceAsync;
import edu.rpi.metpetdb.client.ui.left.side.LeftColWidget;

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

	public static final BulkUploadServiceAsync bulkUpload_svc;

	public static final BulkUploadChemicalAnalysesServiceAsync bulkUploadChemicalAnalyses_svc;

	public static final BulkUploadImagesServiceAsync bulkUploadImages_svc;

	public static final SearchServiceAsync search_svc;

	public static DatabaseObjectConstraints doc;

	public static ObjectConstraints oc;

	private static UserDTO currentUser;

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

		bulkUpload_svc = (BulkUploadServiceAsync) bindService(GWT
				.create(BulkUploadService.class), "bulkUpload");

		bulkUploadChemicalAnalyses_svc = (BulkUploadChemicalAnalysesServiceAsync) bindService(
				GWT.create(BulkUploadChemicalAnalysesService.class),
				"bulkUploadChemicalAnalyses");

		bulkUploadImages_svc = (BulkUploadImagesServiceAsync) bindService(GWT
				.create(BulkUploadImagesService.class), "bulkUploadImages");

		search_svc = (SearchServiceAsync) bindService(GWT
				.create(SearchService.class), "search");

		// factory = (HtmlFactory) GWT.create(HtmlFactory.class);
	}

	private static Object bindService(final Object svc, final String name) {
		final ServiceDefTarget e = (ServiceDefTarget) svc;
		e.setServiceEntryPoint(GWT.getModuleBaseURL() + name + ".svc");
		return svc;
	}

	public static boolean isLoggedIn() {
		return currentUser != null;
	}

	public static boolean isCurrentUser(final UserDTO u) {
		return u != null && currentUser != null
				&& u.getId() == currentUser.getId();
	}

	public static UserDTO currentUser() {
		return currentUser;
	}

	public static void setCurrentUser(final UserDTO n) {
		final UserDTO o = currentUser;
		currentUser = n;
		if (o != n && (o == null || n == null || o.getId() != n.getId())) {
			MetPetDBApplication.onCurrentUserChanged(n);
			if (n != null) {
				if (Breadcrumbs.getCurrentNode() != null) {
					if (Breadcrumbs.getCurrentNode().getLeftSide().equals(
							LocaleHandler.lc_entity.LeftSide_UserInfo())) {
						LeftColWidget.updateLeftSide(LocaleHandler.lc_entity
								.LeftSide_UserInfo());
					}
				}
			}
		}
	}

	// Creates user history in the left column
	// My Samples and My Projects history
	public static void createUserHistory(UserDTO n) {
		MetPetDBApplication.clearLeftSide();

	}

	private MpDb() {
	}
}
