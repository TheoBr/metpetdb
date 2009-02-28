package edu.rpi.metpetdb.client.ui;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.HistoryListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.Grid;
import edu.rpi.metpetdb.client.model.Project;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.TokenHandler.IKey;
import edu.rpi.metpetdb.client.ui.TokenHandler.LKey;
import edu.rpi.metpetdb.client.ui.TokenHandler.SKey;
import edu.rpi.metpetdb.client.ui.TokenHandler.Screen;
import edu.rpi.metpetdb.client.ui.bulk.upload.BulkUploadPanel;
import edu.rpi.metpetdb.client.ui.commands.LoggedInServerOp;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.html.Homepage;
import edu.rpi.metpetdb.client.ui.image.browser.ImageBrowserDetails;
import edu.rpi.metpetdb.client.ui.image.browser.ImageListViewer;
import edu.rpi.metpetdb.client.ui.objects.details.ChemicalAnalysisDetails;
import edu.rpi.metpetdb.client.ui.objects.details.ProjectDetails;
import edu.rpi.metpetdb.client.ui.objects.details.SampleDetails;
import edu.rpi.metpetdb.client.ui.objects.details.SubsampleDetails;
import edu.rpi.metpetdb.client.ui.objects.list.SampleList;
import edu.rpi.metpetdb.client.ui.objects.list.SampleListEx;
import edu.rpi.metpetdb.client.ui.objects.list.UserProjectsListEx;
import edu.rpi.metpetdb.client.ui.objects.list.UserSamplesList;
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
			return ((Sample) obj).getId();
		}

		public void execute(final long id) {
			show(new SampleDetails().showById(id));
		}
	};
	private static final TokenHandler userDetails = new SKey(
			LocaleHandler.lc_entity.TokenSpace_User_Details()) {
		public String get(final Object obj) {
			return ((User) obj).getEmailAddress();
		}

		public void execute(final String emailAddress) {
			show(new UserDetails(emailAddress));
		}
	};
	private static final TokenHandler projectDetails = new IKey(
			LocaleHandler.lc_entity.TokenSpace_Project_Details()) {
		public int get(final Object obj) {
			return ((Project) obj).getId();
		}

		public void execute(final int id) {
			show(new ProjectDetails().showById(id));
		}
	};
	private static final TokenHandler subsampleDetails = new LKey(
			LocaleHandler.lc_entity.TokenSpace_Subsample_Details()) {
		public long get(final Object obj) {
			return ((Subsample) obj).getId();
		}

		public void execute(final long id) {
			show(new SubsampleDetails().showById(id));
		}
	};
	private static final TokenHandler subsampleEdit = new LKey(
			LocaleHandler.lc_entity.TokenSpace_Edit_Subsample()) {
		public long get(final Object obj) {
			return ((Subsample) obj).getId();
		}

		public void execute(final long id) {
			show(new SubsampleDetails().edit(id));
		}
	};
	private static final TokenHandler imageBrowserDetails = new LKey(
			LocaleHandler.lc_entity.TokenSpace_ImageBroswer_Details()) {
		public long get(final Object obj) {
			return ((Grid) obj).getId();
		}

		public void execute(final long id) {
			show(new ImageBrowserDetails().showById(id));
		}
	};
	private static final TokenHandler chemicalAnalysisDetails = new LKey(
			LocaleHandler.lc_entity.TokenSpace_ChemicalAnalysis_Details()) {
		public long get(final Object obj) {
			return ((ChemicalAnalysis) obj).getId();
		}

		public void execute(final long id) {
			show(new ChemicalAnalysisDetails().showById(id));
		}
	};
	private static final TokenHandler ImageListViewer = new LKey(
			LocaleHandler.lc_entity.TokenSpace_ImageListViewer()) {
		public long get(final Object obj) {
			return ((Subsample) obj).getId();
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

	public static final Screen home = new Screen(
			LocaleHandler.lc_entity.TokenSpace_Home()) {
		public void executeToken(final String args) {
			show(new Homepage());
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

	public static final Screen editProfile = new Screen(LocaleHandler.lc_entity
			.TokenSpace_Edit_Profile()) {
		public void executeToken(final String args) {
			new ServerOp<User>() {
				public void begin() {
					MpDb.user_svc.beginEditMyProfile(this);
				}

				public void onSuccess(final User result) {
					show(new EditUserProfile(((User) result)));
				}
			}.begin();
		}
	};

	public static final Screen allSamples = new Screen(LocaleHandler.lc_entity
			.TokenSpace_All_Samples()) {
		public void executeToken(final String args) {
			show(new SampleList() {
				@Override
				public void update(PaginationParameters p,
						AsyncCallback<Results<Sample>> ac) {
					MpDb.sample_svc.allPublicSamples(p, ac);
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
						AsyncCallback<Results<Sample>> ac) {
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
			return ((Project) obj).getId();
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
			return ((Sample) obj).getId();
		}

		public void execute(final long id) {
			new ServerOp<Sample>() {
				public void begin() {
					MpDb.sample_svc.details(id, this);
				}

				public void onSuccess(final Sample result) {
					show(new SubsampleDetails().createNew(result));
				}
			}.begin();
		}
	};

	private static final TokenHandler newChemicalAnalysis = new LKey(
			LocaleHandler.lc_entity.TokenSpace_Enter_ChemicalAnalysis()) {
		public long get(final Object obj) {
			return ((Subsample) obj).getId();
		}

		public void execute(final long id) {
			new ServerOp<Subsample>() {
				public void begin() {
					MpDb.subsample_svc.details(id, this);
				}

				public void onSuccess(final Subsample result) {
					show(new ChemicalAnalysisDetails().createNew(result));
				}
			}.begin();
		}
	};
	private static final TokenHandler createImageMap = new LKey(
			LocaleHandler.lc_entity.TokenSpace_Create_Image_Map()) {
		public long get(final Object obj) {
			return ((Subsample) obj).getId();
		}

		public void execute(final long id) {
			show(new ImageBrowserDetails().createNew(id));
		}
	};
	public static final Screen confirmation = new Screen("ConfirmationCode") {
		public void executeToken(final String args) {
			new LoggedInServerOp() {
				@Override
				public void command() {
					show(new Confirmation().fill(args));
				}
			}.execute();
		}
	};
	public static final Screen rebuildSearchIndex = new Screen("RebuildSearchIndex") {
		public void executeToken(final String args) {
			new LoggedInServerOp() {
				@Override
				public void command() {
					new ServerOp<Void>() {
						public void begin() {
							MpDb.search_svc.rebuildSearchIndex(this);
						}

						public void onSuccess(Void result) {
							Window.alert("done regenerating indicies");
						}
					}.begin();
				}
			}.execute();
		}
	};
	static {
		register(sampleDetails);
		register(userDetails);
		register(projectDetails);
		register(subsampleDetails);
		register(subsampleEdit);
		register(register);
		register(home);
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
		register(newSubsample);
		register(newChemicalAnalysis);
		register(createImageMap);
		register(confirmation);
		register(rebuildSearchIndex);

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

	public static String detailsOf(final Sample s) {
		return sampleDetails.makeToken(s);
	}

	public static String detailsOf(final User u) {
		return userDetails.makeToken(u);
	}

	public static String detailsOf(final Subsample s) {
		return subsampleDetails.makeToken(s);
	}

	public static String detailsOf(final Grid g) {
		return imageBrowserDetails.makeToken(g);
	}

	public static String detailsOf(final ChemicalAnalysis ma) {
		return chemicalAnalysisDetails.makeToken(ma);
	}

	public static String edit(final Subsample s) {
		return subsampleEdit.makeToken(s);
	}

	public static String listOf(final Project p) {
		return projectSamples.makeToken(p);
	}

	public static String ViewOf(final Subsample p) {
		return ImageListViewer.makeToken(p);
	}

	public static String createNewSubsample(final Sample s) {
		return newSubsample.makeToken(s);
	}

	public static String createNewChemicalAnalysis(final Subsample s) {
		return newChemicalAnalysis.makeToken(s);
	}

	public static String createNewImageMap(final Subsample s) {
		return createImageMap.makeToken(s);
	}

	public static String samplesOf(final Project p) {
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
