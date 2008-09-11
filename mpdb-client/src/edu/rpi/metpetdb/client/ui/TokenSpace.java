package edu.rpi.metpetdb.client.ui;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.HistoryListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisDTO;
import edu.rpi.metpetdb.client.model.GridDTO;
import edu.rpi.metpetdb.client.model.ProjectDTO;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.model.SubsampleDTO;
import edu.rpi.metpetdb.client.model.UserDTO;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.TokenHandler.IKey;
import edu.rpi.metpetdb.client.ui.TokenHandler.LKey;
import edu.rpi.metpetdb.client.ui.TokenHandler.SKey;
import edu.rpi.metpetdb.client.ui.TokenHandler.Screen;
import edu.rpi.metpetdb.client.ui.bulk.upload.BulkUploadPanel;
import edu.rpi.metpetdb.client.ui.html.Introduction;
import edu.rpi.metpetdb.client.ui.image.browser.ImageBrowserDetails;
import edu.rpi.metpetdb.client.ui.image.browser.ImageListViewer;
import edu.rpi.metpetdb.client.ui.objects.details.ChemicalAnalysisDetails;
import edu.rpi.metpetdb.client.ui.objects.details.ProjectDetails;
import edu.rpi.metpetdb.client.ui.objects.details.SampleDetails;
import edu.rpi.metpetdb.client.ui.objects.details.SubsampleDetails;
import edu.rpi.metpetdb.client.ui.objects.list.SampleListEx;
import edu.rpi.metpetdb.client.ui.objects.list.UserProjectsListEx;
import edu.rpi.metpetdb.client.ui.objects.list.UserSamplesList;
import edu.rpi.metpetdb.client.ui.permission.PermissionDenied;
import edu.rpi.metpetdb.client.ui.search.Search;
import edu.rpi.metpetdb.client.ui.user.Confirmation;
import edu.rpi.metpetdb.client.ui.user.EditUserProfile;
import edu.rpi.metpetdb.client.ui.user.UserDetails;
import edu.rpi.metpetdb.client.ui.user.UserRegistrationPanel;

/**
 * History token handler.
 * <p>
 * Responsible for creating history tokens for application states as well as
 * parsing a history token and setting the correct application state for that
 * token. One stop shopping for all states supported! (No, that is probably not
 * the best idea. This class is likely to get large.)
 * </p>
 */
public class TokenSpace implements HistoryListener {
	private static String currentToken = new String();
	public static final TokenSpace INSTANCE = new TokenSpace();
	private static final Map<String, TokenHandler> handlers = new HashMap<String, TokenHandler>();
	private static final TokenHandler sampleDetails = new LKey(
			LocaleHandler.lc_entity.TokenSpace_Sample_Details()) {
		public long get(final Object obj) {
			return ((SampleDTO) obj).getId();
		}

		public void execute(final long id) {
			show(new SampleDetails().showById(id));
		}
	};
	private static final TokenHandler userDetails = new SKey(
			LocaleHandler.lc_entity.TokenSpace_User_Details()) {
		public String get(final Object obj) {
			return ((UserDTO) obj).getEmailAddress();
		}

		public void execute(final String emailAddress) {
			show(new UserDetails(emailAddress));
		}
	};
	private static final TokenHandler projectDetails = new IKey(
			LocaleHandler.lc_entity.TokenSpace_Project_Details()) {
		public int get(final Object obj) {
			return ((ProjectDTO) obj).getId();
		}

		public void execute(final int id) {
			show(new ProjectDetails().showById(id));
		}
	};
	private static final TokenHandler subsampleDetails = new LKey(
			LocaleHandler.lc_entity.TokenSpace_Subsample_Details()) {
		public long get(final Object obj) {
			return ((SubsampleDTO) obj).getId();
		}

		public void execute(final long id) {
			show(new SubsampleDetails().showById(id));
		}
	};
	private static final TokenHandler subsampleEdit = new LKey(
			LocaleHandler.lc_entity.TokenSpace_Edit_Subsample()) {
		public long get(final Object obj) {
			return ((SubsampleDTO) obj).getId();
		}

		public void execute(final long id) {
			show(new SubsampleDetails().edit(id));
		}
	};
	private static final TokenHandler imageBrowserDetails = new LKey(
			LocaleHandler.lc_entity.TokenSpace_ImageBroswer_Details()) {
		public long get(final Object obj) {
			return ((GridDTO) obj).getId();
		}

		public void execute(final long id) {
			show(new ImageBrowserDetails().showById(id));
		}
	};
	private static final TokenHandler chemicalAnalysisDetails = new LKey(
			LocaleHandler.lc_entity.TokenSpace_ChemicalAnalysis_Details()) {
		public long get(final Object obj) {
			return ((ChemicalAnalysisDTO) obj).getId();
		}

		public void execute(final long id) {
			show(new ChemicalAnalysisDetails().showById(id));
		}
	};
	private static final TokenHandler ImageListViewer = new LKey(
			LocaleHandler.lc_entity.TokenSpace_ImageListViewer()) {
		public long get(final Object obj) {
			return ((SubsampleDTO) obj).getId();
		}

		public void execute(final long id) {
			show(new ImageListViewer(id, true));
		}
	};
	public static final Screen register = new Screen(LocaleHandler.lc_entity
			.TokenSpace_Register()) {
		public void executeToken(final String args) {
			show(new UserRegistrationPanel());
		}
	};

	public static final Screen introduction = new Screen(
			LocaleHandler.lc_entity.TokenSpace_Introduction()) {
		public void executeToken(final String args) {
			show(new Introduction());
		}
	};
	public static final Screen bulkUpload = new Screen(LocaleHandler.lc_entity
			.TokenSpace_Bulk_Upload()) {
		public void executeToken(final String args) {
			show(new BulkUploadPanel());
		}
	};
	public static final Screen search = new Screen(LocaleHandler.lc_entity
			.TokenSpace_Search()) {
		public void executeToken(final String args) {
			show(new Search().createNew());
		}
	};

	public static final Screen permissionDenied = new Screen(
			LocaleHandler.lc_entity.TokenSpace_Permission_Denied()) {
		public void executeToken(final String args) {
			show(new PermissionDenied());
		}
	};
	public static final Screen editProfile = new Screen(LocaleHandler.lc_entity
			.TokenSpace_Edit_Profile()) {
		public void executeToken(final String args) {
			new ServerOp<UserDTO>() {
				public void begin() {
					MpDb.user_svc.beginEditMyProfile(this);
				}

				public void onSuccess(final UserDTO result) {
					show(new EditUserProfile(((UserDTO) result)));
				}
			}.begin();
		}
	};

	public static final Screen allSamples = new Screen(LocaleHandler.lc_entity
			.TokenSpace_All_Samples()) {
		public void executeToken(final String args) {
			show(new SampleListEx() {
				@Override
				public void update(PaginationParameters p,
						AsyncCallback<Results<SampleDTO>> ac) {
					MpDb.sample_svc.all(p, ac);
				}
			});
		}
	};

	public static final Screen allPublicSamples = new Screen(
			LocaleHandler.lc_entity.TokenSpace_All_Public_Samples()) {
		public void executeToken(final String args) {
			show(new SampleListEx() {

				@Override
				public void update(PaginationParameters p,
						AsyncCallback<Results<SampleDTO>> ac) {
					MpDb.sample_svc.allPublicSamples(p, ac);
				}

			});
		}
	};

	public static final Screen allProjects = new Screen(LocaleHandler.lc_entity
			.TokenSpace_All_Projects()) {
		public void executeToken(final String args) {
			new LoggedInServerOp() {
				public void command() {
					show(new UserProjectsListEx().display());
				}
			}.begin();
		}
	};

	private static final TokenHandler projectSamples = new LKey(
			LocaleHandler.lc_entity.TokenSpace_Project_Samples()) {
		public long get(final Object obj) {
			return ((ProjectDTO) obj).getId();
		}

		public void execute(final long id) {
			new LoggedInServerOp() {
				public void command() {
					show(new UserSamplesList().display(id));
				}
			}.begin();
		}
	};

	public static final Screen samplesForUser = new Screen(
			LocaleHandler.lc_entity.TokenSpace_Samples_For_User()) {
		public void executeToken(final String args) {
			new LoggedInServerOp() {
				public void command() {
					show(new UserSamplesList().display());
				}
			}.begin();

		}
	};

	public static final Screen enterSample = new Screen(LocaleHandler.lc_entity
			.TokenSpace_Enter_Sample()) {
		public void executeToken(final String args) {
			new LoggedInServerOp() {
				public void command() {
					show(new SampleDetails().createNew());
				}
			}.begin();
		}
	};

	public static final Screen newProject = new Screen(LocaleHandler.lc_entity
			.TokenSpace_New_Project()) {
		public void executeToken(final String args) {
			new LoggedInServerOp() {
				public void command() {
					show(new ProjectDetails().createNew());
				}
			}.begin();
		}
	};

	private static final TokenHandler newSubsample = new LKey(
			LocaleHandler.lc_entity.TokenSpace_Enter_Subsample()) {
		public long get(final Object obj) {
			return ((SampleDTO) obj).getId();
		}

		public void execute(final long id) {
			new ServerOp<SampleDTO>() {
				public void begin() {
					MpDb.sample_svc.details(id, this);
				}

				public void onSuccess(final SampleDTO result) {
					show(new SubsampleDetails().createNew(result, null));
				}
			}.begin();
		}
	};

	private static final TokenHandler newChemicalAnalysis = new LKey(
			LocaleHandler.lc_entity.TokenSpace_Enter_ChemicalAnalysis()) {
		public long get(final Object obj) {
			return ((SubsampleDTO) obj).getId();
		}

		public void execute(final long id) {
			new ServerOp<SubsampleDTO>() {
				public void begin() {
					MpDb.subsample_svc.details(id, this);
				}

				public void onSuccess(final SubsampleDTO result) {
					show(new ChemicalAnalysisDetails().createNew(result));
				}
			}.begin();
		}
	};
	private static final TokenHandler createImageMap = new LKey(
			LocaleHandler.lc_entity.TokenSpace_Create_Image_Map()) {
		public long get(final Object obj) {
			return ((SubsampleDTO) obj).getId();
		}

		public void execute(final long id) {
			show(new ImageBrowserDetails().createNew(id));
		}
	};
	public static final Screen confirmation = new Screen("ConfirmationCode") {
		public void executeToken(final String args) {
				show(new Confirmation().fill(args));
		}
	};
	static {
		register(sampleDetails);
		register(userDetails);
		register(projectDetails);
		register(subsampleDetails);
		register(subsampleEdit);
		register(register);
		register(introduction);
		register(allSamples);
		register(allPublicSamples);
		register(samplesForUser);
		register(projectSamples);
		register(editProfile);
		register(imageBrowserDetails);
		register(chemicalAnalysisDetails);
		register(enterSample);
		register(newProject);
		register(bulkUpload);
		register(search);
		register(ImageListViewer);
		register(allProjects);
		register(permissionDenied);
		register(newSubsample);
		register(newChemicalAnalysis);
		register(createImageMap);
		register(confirmation);

		// DefaultPaginationBehavior
		register(new TokenHandler.NoOp("previousPage"));
		register(new TokenHandler.NoOp("page"));
		register(new TokenHandler.NoOp("nextPage"));
		register(new TokenHandler.NoOp(""));
		register(new TokenHandler.NoOp("null"));
		register(new TokenHandler.NoOp("#"));
		register(new TokenHandler.NoOp(null));
	}

	private static void register(final TokenHandler h) {
		handlers.put(h.getName(), h);
	}

	private static void show(final Widget content) {
		MetPetDBApplication.show(content);
		((Breadcrumbs) MetPetDBApplication.getFromBreadCrumbs())
				.update(currentToken);
	}

	private static void show(final Widget content, final String token) {
		MetPetDBApplication.show(content);
	}

	// private static void showIntroduction() {
	// // show(MetPetDBApplication.introduction);
	// show(RootPanel.get("screen-Introduction"));
	// }

	public static String detailsOf(final SampleDTO s) {
		return sampleDetails.makeToken(s);
	}

	public static String detailsOf(final UserDTO u) {
		return userDetails.makeToken(u);
	}

	public static String detailsOf(final SubsampleDTO s) {
		return subsampleDetails.makeToken(s);
	}

	public static String detailsOf(final GridDTO g) {
		return imageBrowserDetails.makeToken(g);
	}

	public static String detailsOf(final ChemicalAnalysisDTO ma) {
		return chemicalAnalysisDetails.makeToken(ma);
	}

	public static String edit(final SubsampleDTO s) {
		return subsampleEdit.makeToken(s);
	}

	public static String listOf(final ProjectDTO p) {
		return projectSamples.makeToken(p);
	}

	public static String ViewOf(final SubsampleDTO p) {
		return ImageListViewer.makeToken(p);
	}

	public static String createNewSubsample(final SampleDTO s) {
		return newSubsample.makeToken(s);
	}

	public static String createNewChemicalAnalysis(final SubsampleDTO s) {
		return newChemicalAnalysis.makeToken(s);
	}

	public static String createNewImageMap(final SubsampleDTO s) {
		return createImageMap.makeToken(s);
	}

	public static String samplesOf(final ProjectDTO p) {
		return projectSamples.makeToken(p);
	}

	public static void dispatch(final String historyToken) {
		try {
			internalDispatch(historyToken);
		} catch (Throwable re) {
			ErrorHandler.dispatch(re);
		}
	}

	private static void internalDispatch(final String historyToken)
			throws NoSuchObjectException {
		final int sepIdx = historyToken.indexOf(TokenHandler.sep);
		final String name, args;

		if (sepIdx > 0) {
			name = historyToken.substring(0, sepIdx);
			args = historyToken.substring(sepIdx + 1);
		} else {
			name = historyToken;
			args = "";
		}

		TokenHandler h = (TokenHandler) handlers.get(name);
		if (h == null && args.length() == 0) {
			// Is this a predefined panel that exists as pure HTML?
			// If so we should find it and display that node.
			//
			final RootPanel p = RootPanel.get("screen-" + name);
			if (p != null) {
				register(new Screen(name) {
					public void executeToken(final String args) {
						show(p);
					}
				});
			}
		}
		if (h == null)
			throw new NoSuchObjectException("Page", historyToken);
		h.executeToken(args);
	}

	public void onHistoryChanged(final String historyToken) {
		currentToken = historyToken;
		dispatch(historyToken);
		MetPetDBApplication.dispatchCurrentPageChanged();
	}

	private TokenSpace() {
	}
}
