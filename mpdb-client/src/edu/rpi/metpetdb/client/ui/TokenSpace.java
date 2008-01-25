package edu.rpi.metpetdb.client.ui;

import java.util.HashMap;
import java.util.Map;

import org.gwtwidgets.client.ui.pagination.DataProvider;
import org.gwtwidgets.client.ui.pagination.PaginationParameters;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.HistoryListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.model.Grid;
import edu.rpi.metpetdb.client.model.MineralAnalysis;
import edu.rpi.metpetdb.client.model.Project;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.ui.TokenHandler.IKey;
import edu.rpi.metpetdb.client.ui.TokenHandler.LKey;
import edu.rpi.metpetdb.client.ui.TokenHandler.SKey;
import edu.rpi.metpetdb.client.ui.TokenHandler.Screen;
import edu.rpi.metpetdb.client.ui.bulk.upload.BulkUploadPanel;
import edu.rpi.metpetdb.client.ui.image.browser.ImageBrowserDetails;
import edu.rpi.metpetdb.client.ui.objects.details.MineralAnalysisDetails;
import edu.rpi.metpetdb.client.ui.objects.details.ProjectDetails;
import edu.rpi.metpetdb.client.ui.objects.details.SampleDetails;
import edu.rpi.metpetdb.client.ui.objects.details.SubsampleDetails;
import edu.rpi.metpetdb.client.ui.objects.details.UserDetails;
import edu.rpi.metpetdb.client.ui.objects.list.SampleList;
import edu.rpi.metpetdb.client.ui.objects.list.UserSampleList;
import edu.rpi.metpetdb.client.ui.user.EditUserProfile;
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
	public static final TokenSpace INSTANCE = new TokenSpace();
	private static final Map handlers = new HashMap();
	private static final TokenHandler sampleDetails = new LKey("SampleDetails") {
		public long get(final Object obj) {
			return ((Sample) obj).getId();
		}
		public void execute(final long id) {
			show(new SampleDetails().showById(id));
		}
	};
	private static final TokenHandler userDetails = new SKey("UserDetails") {
		public String get(final Object obj) {
			return ((User) obj).getUsername();
		}
		public void execute(final String username) {
			show(new UserDetails(username));
		}
	};
	private static final TokenHandler projectDetails = new IKey("Project") {
		public int get(final Object obj) {
			return ((Project) obj).getId();
		}
		public void execute(final int id) {
			show(new ProjectDetails().showById(id));
		}
	};
	private static final TokenHandler subsampleDetails = new LKey(
			"SubsampleDetails") {
		public long get(final Object obj) {
			return ((Subsample) obj).getId();
		}
		public void execute(final long id) {
			show(new SubsampleDetails().showById(id));
		}
	};
	private static final TokenHandler subsampleEdit = new LKey("SubsampleEdit") {
		public long get(final Object obj) {
			return ((Subsample) obj).getId();
		}
		public void execute(final long id) {
			show(new SubsampleDetails().edit(id));
		}
	};
	private static final TokenHandler imageBrowserDetails = new LKey(
			"ImageBrowserDetails") {
		public long get(final Object obj) {
			return ((Grid) obj).getId();
		}
		public void execute(final long id) {
			show(new ImageBrowserDetails().showById(id));
		}
	};
	private static final TokenHandler mineralAnalysisDetails = new LKey(
			"MineralAnalysis") {
		public long get(final Object obj) {
			return ((MineralAnalysis) obj).getId();
		}
		public void execute(final long id) {
			show(new MineralAnalysisDetails().showById(id));
		}
	};
	public static final Screen register = new Screen("Register") {
		public void executeToken(final String args) {
			show(new UserRegistrationPanel());
		}
	};
	public static final Screen introduction = new Screen("Introduction") {
		public void executeToken(final String args) {
			showIntroduction();
		}
	};
	public static final Screen bulkUpload = new Screen("BulkUpload") {
		public void executeToken(final String args) {
			show(new BulkUploadPanel());
		}
	};
	public static final Screen editProfile = new Screen("EditProfile") {
		public void executeToken(final String args) {
			new ServerOp() {
				public void begin() {
					MpDb.user_svc.beginEditMyProfile(this);
				}
				public void onSuccess(final Object result) {
					show(new EditUserProfile(((User) result)));
				}
			}.begin();
		}
	};

	public static final Screen allSamples = new Screen("AllSamples") {
		public void executeToken(final String args) {
			SampleList list = new SampleList(new DataProvider() {
				public void update(final PaginationParameters p,
						final AsyncCallback ac) {
					MpDb.sample_svc.all(p, ac);
				}
			},"All Samples");
			show(list);
		}
	};
	
	public static final Screen allPublicSamples = new Screen("AllPublicSamples") {
		public void executeToken(final String args) {
			SampleList list = new SampleList(new DataProvider() {
				public void update(final PaginationParameters p,
						final AsyncCallback ac) {
					MpDb.sample_svc.allPublicSamples(p, ac);
				}
			}, "All Public Samples");
			show(list);
		}
	};

	private static final TokenHandler projectSamples = new LKey("ProjectSamples") {
		public long get(final Object obj) {
			return ((Project) obj).getId();
		}
		public void execute(final long id) {
			SampleList list = new SampleList(new DataProvider() {
				public void update(final PaginationParameters p,
						final AsyncCallback ac) {
					MpDb.project_svc.samplesFromProject(p, id, ac);
					}
			},"Samples for Project");
			show(list);
		}
	};

	public static final Screen samplesForUser = new Screen("SamplesForUser") {
		public void executeToken(final String args) {
			if (this.getName().equals(History.getToken()))
				new ServerOp() {
					public void begin() {
						if (MpDb.isLoggedIn()) {
							final UserSampleList list = new UserSampleList(
									new DataProvider() {
										public void update(
												final PaginationParameters p,
												final AsyncCallback ac) {
											long id = (long) (MpDb
													.currentUser().getId());
											MpDb.sample_svc.allSamplesForUser(
													p, id, ac);
										}
									});
							show(list);
						} else
							onFailure(new LoginRequiredException());
					}
					public void onSuccess(Object result) {
					}
					public void cancel() {
						// Go back to revert the history token
						History.back();
					}
				}.begin();
			else
				History.newItem(this.getName());
		}
	};

	public static final Screen enterSample = new Screen("EnterSample") {
		public void executeToken(final String args) {
			if (this.getName().equals(History.getToken()))
				new ServerOp() {
					public void begin() {
						if (MpDb.isLoggedIn())
							show(new SampleDetails().createNew());
						else
							onFailure(new LoginRequiredException());
					}
					public void onSuccess(Object result) {
					}
					public void cancel() {
						// Revert the history token
						History.back();
					}
				}.begin();
			else
				History.newItem(this.getName());
		}
	};

	public static final Screen newProject = new Screen("NewProject") {
		public void executeToken(final String args) {
			if (this.getName().equals(History.getToken()))
				new ServerOp() {
					public void begin() {
						if (MpDb.isLoggedIn())
							show(new ProjectDetails().createNew());
						else
							onFailure(new LoginRequiredException());
					}
					public void onSuccess(Object result) {
					}
					public void cancel() {
						// Go back to revert the history token
						History.back();
					}
				}.begin();
			else
				History.newItem(this.getName());
		}
	};

	public static final Screen enterSubsample = new Screen("EnterSubsample") {
		public void executeToken(final String args) {
			// TODO we need some way to get the sample though...thats the only
			// problem
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
		register(mineralAnalysisDetails);
		register(enterSample);
		register(newProject);
		register(bulkUpload);

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
	}
	private static void showIntroduction() {
		show(MetPetDBApplication.introduction);
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
	public static String detailsOf(final MineralAnalysis ma) {
		return mineralAnalysisDetails.makeToken(ma);
	}
	public static String edit(final Subsample s) {
		return subsampleEdit.makeToken(s);
	}
	public static String listOf(final Project p) {
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
		dispatch(historyToken);
		MetPetDBApplication.dispatchCurrentPageChanged();
	}

	private TokenSpace() {
	}
}
