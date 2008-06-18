package edu.rpi.metpetdb.client.ui;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.HistoryListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.NoSuchObjectException;
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
import edu.rpi.metpetdb.client.ui.image.browser.ImageBrowserDetails;
import edu.rpi.metpetdb.client.ui.image.browser.ImageListViewer;
import edu.rpi.metpetdb.client.ui.objects.details.ChemicalAnalysisDetails;
import edu.rpi.metpetdb.client.ui.objects.details.ProjectDetails;
import edu.rpi.metpetdb.client.ui.objects.details.SampleDetails;
import edu.rpi.metpetdb.client.ui.objects.details.SubsampleDetails;
import edu.rpi.metpetdb.client.ui.objects.list.SampleListEx;
import edu.rpi.metpetdb.client.ui.objects.list.UserSamplesList;
import edu.rpi.metpetdb.client.ui.search.Search;
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
	public static final TokenSpace INSTANCE = new TokenSpace();
	private static final Map<String, TokenHandler> handlers = new HashMap<String, TokenHandler>();
	private static final TokenHandler sampleDetails = new LKey("SampleDetails") {
		public long get(final Object obj) {
			return ((SampleDTO) obj).getId();
		}

		public void execute(final long id) {
			show(new SampleDetails().showById(id));
		}
	};
	private static final TokenHandler userDetails = new SKey("UserDetails") {
		public String get(final Object obj) {
			return ((UserDTO) obj).getUsername();
		}

		public void execute(final String username) {
			show(new UserDetails(username));
		}
	};
	private static final TokenHandler projectDetails = new IKey("Project") {
		public int get(final Object obj) {
			return ((ProjectDTO) obj).getId();
		}

		public void execute(final int id) {
			show(new ProjectDetails().showById(id));
		}
	};
	private static final TokenHandler subsampleDetails = new LKey(
			"SubsampleDetails") {
		public long get(final Object obj) {
			return ((SubsampleDTO) obj).getId();
		}

		public void execute(final long id) {
			show(new SubsampleDetails().showById(id));
		}
	};
	private static final TokenHandler subsampleEdit = new LKey("SubsampleEdit") {
		public long get(final Object obj) {
			return ((SubsampleDTO) obj).getId();
		}

		public void execute(final long id) {
			show(new SubsampleDetails().edit(id));
		}
	};
	private static final TokenHandler imageBrowserDetails = new LKey(
			"ImageBrowserDetails") {
		public long get(final Object obj) {
			return ((GridDTO) obj).getId();
		}

		public void execute(final long id) {
			show(new ImageBrowserDetails().showById(id));
		}
	};
	private static final TokenHandler chemicalAnalysisDetails = new LKey(
			"ChemicalAnalysis") {
		public long get(final Object obj) {
			return ((ChemicalAnalysisDTO) obj).getId();
		}

		public void execute(final long id) {
			show(new ChemicalAnalysisDetails().showById(id));
		}
	};
	private static final TokenHandler ImageListViewer = new LKey(
			"ImageListViewer") {
		public long get(final Object obj) {
			return ((SubsampleDTO) obj).getId();
		}

		public void execute(final long id) {
			show(new ImageListViewer(id));
		}
	};
	public static final Screen register = new Screen("Register") {
		public void executeToken(final String args) {
			show(new UserRegistrationPanel());
		}
	};
	public static final Screen introduction = new Screen("Introduction") {
		public void executeToken(final String args) {
			show(new HTML(
					"<h1>Welcome to MetPetDB</h1> <img src=\"images/slices.jpg\" alt=\"\" class=\"r\"><p><strong>MetPetDB</strong> is a database for metamorphic petrology that is being designed and built by a global community of metamorphic petrologists in collaboration with computer scientists at Rensselaer Polytechnic Institute as part of the National Cyberinfrastructure Initiative and supported by the National Science Foundation.</p><p>This project will support the development, implementation and population of MetPetDB with the purpose of:</p><ol><li><span>archiving published data,</span></li><li><span>storing new data for ready access to researchers and students,</span></li><li><span>facilitating the gathering of information for researchers beginning new projects,</span></li><li><span>providing a search mechanism for data relating to anywhere on the globe,</span></li><li><span>providing a platform for collaborative studies among researchers, and</span></li><li><span>serving as a portal for students beginning their studies of metamorphic geology.</span></li></ol><p>Read more about the MetPetDB project <a href=\"#\" title=\"More about MetPetDB\">here</a>.</p><h2>Other Databases</h2><table id=\"dblist\" cellspacing=\"0\"><tbody><tr><td><a href=\"http://www.earthchem.org\" title=\"EarthChem\"><img src=\"images/earthchem-logo.jpg\" alt=\"EarthChem\"></a>Advanced Data Management in Solid Earth Geochemistry</td><td><a href=\"http://www.petdb.org\" title=\"PETDB\"><img src=\"images/petdb-logo.jpg\" alt=\"PETDB\"></a>Petrological Database of the Ocean Floor</td><td><a href=\"http://navdat.kgs.ku.edu\" title=\"NAVDAT\"><img src=\"images/navdat-logo.jpg\" alt=\"NAVDAT\"></a>The Western North American Volcanic and Intrusive Rock Database</td><td><a href=\"http://georoc.mpch-mainz.gwdg.de/georoc/Start.asp\" title=\"GEOROC\"><img src=\"images/georoc-logo.jpg\" alt=\"GEOROC\"></a>Geochemistry of Rocks of the Ocean and Continents</td><td><a href=\"http://www.geosamples.org\" title=\"SESAR\"><img src=\"images/sesar-logo.jpg\" alt=\"SESAR\"></a>System for Earth Sample Registration</td></tr></tbody></table>"));
		}
	};
	public static final Screen bulkUpload = new Screen("BulkUpload") {
		public void executeToken(final String args) {
			show(new BulkUploadPanel());
		}
	};
	public static final Screen search = new Screen("Search") {
		public void executeToken(final String args) {
			show(new Search().createNew());
		}
	};
	public static final Screen editProfile = new Screen("EditProfile") {
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

	public static final Screen allSamples = new Screen("AllSamples") {
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

	public static final Screen allPublicSamples = new Screen("AllPublicSamples") {
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

	private static final TokenHandler projectSamples = new LKey(
			"ProjectSamples") {
		public long get(final Object obj) {
			return ((ProjectDTO) obj).getId();
		}

		public void execute(final long id) {
			// SampleList list = new SampleList(new DataProvider() {
			// public void update(final PaginationParameters p,
			// final AsyncCallback ac) {
			// MpDb.project_svc.samplesFromProject(p, id, ac);
			// }
			// }, "Samples for Project");
			// show(list);
		}
	};

	// public static final Screen samplesForUser = new Screen("SamplesForUser")
	// {
	// public void executeToken(final String args) {
	// new LoggedInServerOp() {
	// public void command() {
	// show(new SampleListEx() {
	// public void update(final PaginationParameters p,
	// final AsyncCallback<Results<SampleDTO>> ac) {
	// long id = (long) (MpDb.currentUser().getId());
	// MpDb.sample_svc.allSamplesForUser(p, id, ac);
	// }
	// });
	// }
	// }.begin();
	//
	// }
	// };

	public static final Screen samplesForUser = new Screen("SamplesForUser") {
		public void executeToken(final String args) {
			new LoggedInServerOp() {
				public void command() {
					show(new UserSamplesList().display());
				}
			}.begin();

		}
	};

	public static final Screen enterSample = new Screen("EnterSample") {
		public void executeToken(final String args) {
			new LoggedInServerOp() {
				public void command() {
					show(new SampleDetails().createNew());
				}
			}.begin();
		}
	};

	public static final Screen newProject = new Screen("NewProject") {
		public void executeToken(final String args) {
			new LoggedInServerOp() {
				public void command() {
					show(new ProjectDetails().createNew());
				}
			}.begin();
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
		register(chemicalAnalysisDetails);
		register(enterSample);
		register(newProject);
		register(bulkUpload);
		register(search);
		register(ImageListViewer);

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
