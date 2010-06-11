package edu.rpi.metpetdb.client.ui;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.HistoryListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.Grid;
import edu.rpi.metpetdb.client.model.Image;
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
import edu.rpi.metpetdb.client.ui.commands.VoidLoggedInOp;
import edu.rpi.metpetdb.client.ui.html.Homepage;
import edu.rpi.metpetdb.client.ui.html.IPhoneApp;
import edu.rpi.metpetdb.client.ui.image.browser.ImageBrowserDetails;
import edu.rpi.metpetdb.client.ui.objects.details.ChemicalAnalysisDetails;
import edu.rpi.metpetdb.client.ui.objects.details.ProjectDetails;
import edu.rpi.metpetdb.client.ui.objects.details.SampleDetails;
import edu.rpi.metpetdb.client.ui.objects.details.SubsampleDetails;
import edu.rpi.metpetdb.client.ui.objects.list.ImageListViewer;
import edu.rpi.metpetdb.client.ui.objects.list.SampleListEx;
import edu.rpi.metpetdb.client.ui.project.InviteStatus;
import edu.rpi.metpetdb.client.ui.project.MyInvites;
import edu.rpi.metpetdb.client.ui.project.ProjectDescription;
import edu.rpi.metpetdb.client.ui.project.ProjectInvite;
import edu.rpi.metpetdb.client.ui.search.Search;
import edu.rpi.metpetdb.client.ui.user.Confirmation;
import edu.rpi.metpetdb.client.ui.user.EditUserProfile;
import edu.rpi.metpetdb.client.ui.user.ProjectSamplesList;
import edu.rpi.metpetdb.client.ui.user.RequestRoleChange;
import edu.rpi.metpetdb.client.ui.user.ReviewRoleChanges;
import edu.rpi.metpetdb.client.ui.user.UserDetails;
import edu.rpi.metpetdb.client.ui.user.UserProjectsListEx;
import edu.rpi.metpetdb.client.ui.user.UserRegistrationPanel;
import edu.rpi.metpetdb.client.ui.user.UserSamplesList;

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
			if (obj instanceof Sample)
				return ((Sample) obj).getId();
			else
				return Long.parseLong(obj.toString());
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
			show(new ImageListViewer() {

				@Override
				public void update(PaginationParameters p,
						AsyncCallback<Results<Image>> ac) {
					MpDb.image_svc.allImages(id, p, ac);
				}

				@Override
				public void getAllIds(AsyncCallback<Map<Object, Boolean>> ac) {
					MpDb.image_svc.allImageIds(id, ac);
				}

			});
		}
	};
	public static final Screen register = new Screen(LocaleHandler.lc_entity
			.TokenSpace_Register()) {
		public void executeToken(final String args) {
			show(new UserRegistrationPanel());
		}
	};

	public static final Screen home = new Screen(LocaleHandler.lc_entity
			.TokenSpace_Home()) {
		public void executeToken(final String args) {
			show(new Homepage());
		}
	};
	public static final Screen iphoneApp = new Screen(LocaleHandler.lc_entity
			.TokenSpace_App()) {
		public void executeToken(final String args) {
			show(new IPhoneApp());
		}
	};
	public static final Screen bulkUpload = new Screen(LocaleHandler.lc_entity
			.TokenSpace_Bulk_Upload()) {
		BulkUploadPanel bup;
		public void executeToken(final String args) {
			new VoidLoggedInOp() {
				public void command() {
					if (bup == null)
						bup = new BulkUploadPanel();
					show(bup);
				}
			}.begin();
		}
	};
	public static final Screen search = new Screen(LocaleHandler.lc_entity
			.TokenSpace_Search()) {
		Search s;
		public void executeToken(final String args) {
			if (s == null)
			{
				s = new Search() {	
				@Override
					public void onCurrentUserChanged(User whoIsIt) throws LoginRequiredException {
						s = null;
						search.executeToken("");
					}			
				};
			}
			
			//else 
			s.reload();
			show(s);
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
	private static final TokenHandler projectDetails = new IKey(
			LocaleHandler.lc_entity.TokenSpace_Project_Details()) {
		public int get(final Object obj) {
			return ((Project) obj).getId();
		}

		public void execute(final int id) {
			show(new ProjectDetails().showById(id));
		}
	};
	public static final Screen allProjects = new Screen(LocaleHandler.lc_entity
			.TokenSpace_All_Projects()) {
		public void executeToken(final String args) {
			new VoidLoggedInOp() {
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
			new VoidLoggedInOp() {
				public void command() {
					show(new ProjectSamplesList(id));
				}
			}.begin();
		}
	};
	
	private static final TokenHandler projectDescription = new IKey(
			LocaleHandler.lc_entity.TokenSpace_Project_Description()) {

		public int get(Object obj) {
			return ((Project) obj).getId();
		}
		
		public void execute(final int id) {
			new VoidLoggedInOp() {
				public void command() {
					show(new ProjectDescription().showById(id));
				}
			}.begin();
		}
	};

	public static final Screen samplesForUser = new Screen(
			LocaleHandler.lc_entity.TokenSpace_Samples_For_User()) {
		UserSamplesList list;
		public void executeToken(final String args) {
			
			new VoidLoggedInOp() {
				public void command() {
					if (list == null)
						list = new UserSamplesList();
					list.reload();
					show(list);
				}
			}.begin();

		}
	};

	public static final Screen enterSample = new Screen(LocaleHandler.lc_entity
			.TokenSpace_Enter_Sample()) {
		public void executeToken(final String args) {
			new VoidLoggedInOp() {
				public void command() {
					show(new SampleDetails().createNew());
				}
			}.begin();
		}
	};

	public static final Screen newProject = new Screen(LocaleHandler.lc_entity
			.TokenSpace_New_Project()) {
		public void executeToken(final String args) {
			new VoidLoggedInOp() {
				public void command() {
					show(new ProjectDetails().createNew());
				}
			}.begin();
		}
	};
	
	public static final TokenHandler projectInvite = new IKey(
			LocaleHandler.lc_entity.TokenSpace_Project_Invite()) {
		public int get(final Object obj) {
			return ((Project) obj).getId();
		}
		public void execute(final int id) {
			new ServerOp<Project>() {
				public void begin() {
					MpDb.project_svc.details(id, this);
				}
				public void onSuccess(final Project result) {
					show(new ProjectInvite().newInvite(result));
				}
			}.begin();
		}
	};
	
	public static final TokenHandler projectViewInvites = new IKey(
			LocaleHandler.lc_entity.TokenSpace_Project_View_Invites()) {
		public int get(final Object obj) {
			return ((Project) obj).getId();
		}
		public void execute(final int id) {
			show(new InviteStatus().showById(id));
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
			new VoidLoggedInOp() {
				@Override
				public void command() {
					show(new Confirmation().fill(args));
				}
			}.execute();
		}
	};
	public static final Screen requestRoleChange = new Screen(
			"RequestRoleChange") {
		public void executeToken(final String args) {
			new VoidLoggedInOp() {
				@Override
				public void command() {
					show(new RequestRoleChange().createNew());
				}
			}.execute();
		}
	};
	public static final Screen reviewRoleChanges = new Screen(
			"ConfirmRoleChange") {
		public void executeToken(final String args) {
			new VoidLoggedInOp() {
				@Override
				public void command() {
					show(new ReviewRoleChanges());
				}
			}.execute();
		}
	};
	public static final Screen rebuildSearchIndex = new Screen(
			"RebuildSearchIndex") {
		public void executeToken(final String args) {
			new LoggedInServerOp<Void>() {
				@Override
				public void command() {
					new ServerOp<Void>() {
						public void begin() {
							MpDb.search_svc.rebuildSearchIndex(this);
						}

						public void onSuccess(Void result) {
							Window.alert("done regenerating indexes");
						}
					}.begin();
				}
			}.execute();
		}
	};
	private static final TokenHandler myInvites = new LKey(
			LocaleHandler.lc_entity.TokenSpace_My_Invites()) {

		public long get(Object obj) {
			return ((User) obj).getId();
		}
		
		public void execute(final long id) {
			new VoidLoggedInOp() {
				public void command() {
					show(new MyInvites().showById(id));
				}
			}.begin();
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
		register(iphoneApp);
		register(allPublicSamples);
		register(samplesForUser);
		register(projectDescription);
		register(projectSamples);
		register(myInvites);
		register(editProfile);
		register(imageBrowserDetails);
		register(chemicalAnalysisDetails);
		register(enterSample);
		register(newProject);
		register(projectInvite);
		register(projectViewInvites);
		register(bulkUpload);
		register(search);
		register(ImageListViewer);
		register(allProjects);
		register(newSubsample);
		register(newChemicalAnalysis);
		register(createImageMap);
		register(confirmation);
		register(rebuildSearchIndex);
		register(requestRoleChange);
		register(reviewRoleChanges);

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

	// private static void showIntroduction() {
	// // show(MetPetDBApplication.introduction);
	// show(RootPanel.get("screen-Introduction"));
	// }

	public static String detailsOfSample(final long sampleId) {
		return sampleDetails.makeToken(sampleId);
	}

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
	
	public static String sendNewInvite(final Project p) {
		return projectInvite.makeToken(p);
	}
	
	public static String viewInviteStatus(final Project p) {
		return projectViewInvites.makeToken(p);
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
	
	public static String descriptionOf(Project p) {
		return projectDescription.makeToken(p);
	}
	
	public static String viewMyInvites(User u) {
		return myInvites.makeToken(u);
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
		MetPetDBApplication.dispatchBeforeCurrentPageChanged(h,args);
	}
	
	public static void executeToken(final TokenHandler h, final String args){
		h.executeToken(args);
	}

	public void onHistoryChanged(final String historyToken) {
		currentToken = historyToken;
		dispatch(historyToken);
		MetPetDBApplication.dispatchCurrentPageChanged();
	}

	private TokenSpace() {}

	
}
