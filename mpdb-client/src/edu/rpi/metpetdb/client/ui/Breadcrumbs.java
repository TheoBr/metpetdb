package edu.rpi.metpetdb.client.ui;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.XMLParser;

import edu.rpi.metpetdb.client.model.ChemicalAnalysisDTO;
import edu.rpi.metpetdb.client.model.GridDTO;
import edu.rpi.metpetdb.client.model.ProjectDTO;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.model.SubsampleDTO;
import edu.rpi.metpetdb.client.model.UserDTO;
import edu.rpi.metpetdb.client.ui.left.side.LeftColWidget;
import edu.rpi.metpetdb.client.ui.widgets.MLink;

public class Breadcrumbs extends HorizontalPanel {
	private static final Label lbl = new Label("You are here:");
	private static final String separator = ">";
	private static final String tokenSep = "-";
	private bcNode root;
	private String id;
	private static bcNode current;

	public Breadcrumbs() {
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET,
				URL.encode("siteMap.xml"));
		try {
			requestBuilder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
				}

				public void onResponseReceived(Request request,
						Response response) {
					createTree(response.getText());
				}
			});
		} catch (RequestException ex) {
		}
	}

	public Breadcrumbs(final String HistoryToken) {
		this();
		update(HistoryToken);
	}

	/*
	 * We need to get rid of the comments in the beginning of the xml file
	 * because XMLParser doesn't know how to do it. Only place comments can be
	 * handled is before the <?xml version=....> line.
	 */
	private String stripBeginningComments(String xmlText) {
		xmlText = xmlText.substring(xmlText.indexOf("<?xml version="));
		return xmlText;
	}

	/*
	 * Takes the xml file and creates a tree structure from it
	 */
	private void createTree(String xmlText) {
		xmlText = stripBeginningComments(xmlText);
		Document siteMapDOM = XMLParser.parse(xmlText);
		Element siteMapElement = siteMapDOM.getDocumentElement();
		XMLParser.removeWhitespace(siteMapElement);

		// get root node
		final Node RootXML = siteMapElement.getChildNodes().item(0);
		root = new bcNode();

		createTreeRecursive(RootXML, root);

	}

	/*
	 * Recursive call to parse the xml file and create a tree from it If the
	 * screen tag is present it assumes the value is false If it's true just
	 * don't put it in the xml file, the constructor for the node sets the
	 * screen variable to true on default.
	 */
	private void createTreeRecursive(final Node RootXML, final bcNode Root) {
		for (int i = 0; i < RootXML.getChildNodes().getLength(); i++) {
			final Node tempXML = RootXML.getChildNodes().item(i);
			if (tempXML.getNodeName().equals("name")) {
				Root.setName(tempXML.getFirstChild().getNodeValue());
			} else if (tempXML.getNodeName().equals("token")) {
				Root.setToken(tempXML.getFirstChild().getNodeValue());
			} else if (tempXML.getNodeName().equals("screen")) {
				Root.setScreen(false);
			} else if (tempXML.getNodeName().equals("leftside")) {
				Root.setLeftSide(tempXML.getFirstChild().getNodeValue());
			} else if (tempXML.getNodeName().equals("page")) {
				final bcNode temp = new bcNode(Root);
				Root.addChild(temp);
				createTreeRecursive(RootXML.getChildNodes().item(i), temp);
			}
		}
	}

	/*
	 * Need to find the target page in our tree so we know where to start
	 * creating our breadcrumb links
	 */
	private bcNode findNode(final String target) {
		if (root.getToken().equals(target)) {
			onFindSuccess(root);
			return null;
		}
		return findNodeRecursive(root, target);
	}

	private bcNode findNodeRecursive(final bcNode root, final String target) {
		for (bcNode child : root.getChildren()) {
			if (child.getToken().equals(target)) {
				onFindSuccess(child);
			}
			if (child.getChildren() != null) {
				findNodeRecursive(child, target);
			}
		}
		return root;
	}

	/*
	 * If we found the page in our tree then start moving up the tree
	 */
	private void onFindSuccess(final bcNode currentPage) {
		add(lbl);
		bcNode Node = currentPage;
		current = currentPage;
		LeftColWidget.updateLeftSide(Node.getLeftSide());
		onFindSuccessRecursive(Node);
	}
	private void onFindSuccessRecursive(final bcNode Node) {
		if (Node != null) {
			final MLink bcItem = new MLink();
			/*
			 * Check if it needs a custom name and target history
			 */
			if (!Node.isScreen()) {
				bcItem.setTargetHistoryToken(Node.getToken() + tokenSep
						+ getId());
				bcItem.setText(Node.getName());
				insertBcItem(bcItem, Node);
				getAliasById(id, Node.getName(), Node);
			} else {
				bcItem.setTargetHistoryToken(Node.getToken());
				bcItem.setText(Node.getName());
				insertBcItem(bcItem, Node);
				onFindSuccessRecursive(Node.getParent());
			}
		}

	}

	private void insertBcItem(final MLink bc, final bcNode node) {
		insert(bc, 1);
		if (node.getParent() != null) {
			insert(new Label(separator), 1);
		} else
			setVisible(true);
	}

	/*
	 * if it has a "-" in the history token then break it up
	 */
	private String parseToken(final String token) {
		final String[] split = token.split(tokenSep);
		if (split.length > 1)
			id = split[1];
		return split[0];
	}

	private String getId() {
		return id;
	}

	/*
	 * Annoying function needed to replace page names with their specific alias.
	 * If adding a new page that needs a specific name then you'll need to add
	 * special code here to get it.
	 * 
	 * If the name of a page depends on a page further down the hierarchy tree
	 * then you also need to change the id variable for the next page (i.e.
	 * Chemical Analysis, we're only given the id for the chemical analysis so
	 * once we load the specific name for it, we need to replace id with it's
	 * subsample's id, and then once we have the subsample's name we replace id
	 * with it's sample id).
	 * 
	 * OnSuccess make sure to also call
	 * Breadcrumbs.this.onFindSuccessRecursive(node.getParent()); to continue
	 * the traversing of the tree back to "Home".
	 */
	private void getAliasById(final String Id, final String name,
			final bcNode Node) {
		if (name.equals("Specific Sample")) {
			new ServerOp<SampleDTO>() {
				public void begin() {
					MpDb.sample_svc.details(Long.parseLong(Id), this);
				}

				public void onSuccess(SampleDTO result) {
					for (Widget w : Breadcrumbs.this.getChildren()) {
						if (w instanceof MLink) {
							if (((MLink) w).getText().equals(name)) {
								((MLink) w).setText(result.getAlias());
								if (MetPetDBApplication.getLeftCount() == 0)
									LeftColWidget.updateLeftSide(Node
											.getLeftSide(), result);
								Breadcrumbs.this.onFindSuccessRecursive(Node
										.getParent());
								return;
							}
						}
					}
				}
			}.begin();
		} else if (name.equals("Map")) {
			new ServerOp<GridDTO>() {
				public void begin() {
					MpDb.imageBrowser_svc.details(Long.parseLong(Id), this);
				}
				public void onSuccess(GridDTO result) {
					for (Widget w : Breadcrumbs.this.getChildren()) {
						if (w instanceof MLink) {
							if (((MLink) w).getText().equals(name)) {
								if (MetPetDBApplication.getLeftCount() == 0)
									LeftColWidget.updateLeftSide(Node
											.getLeftSide(), result
											.getSubsample().getSample(), result
											.getSubsample());
								Breadcrumbs.this.id = String.valueOf(result
										.getSubsample().getId());
								Breadcrumbs.this.onFindSuccessRecursive(Node
										.getParent());
								return;
							}
						}
					}
				}
			}.begin();
		} else if (name.equals("Specific Subsample")) {
			new ServerOp<SubsampleDTO>() {
				public void begin() {
					MpDb.subsample_svc.details(Long.parseLong(Id), this);
				}

				public void onSuccess(SubsampleDTO result) {
					for (Widget w : Breadcrumbs.this.getChildren()) {
						if (w instanceof MLink) {
							if (((MLink) w).getText().equals(name)) {
								((MLink) w).setText(result.getName());
								if (MetPetDBApplication.getLeftCount() == 0)
									LeftColWidget.updateLeftSide(Node
											.getLeftSide(), result.getSample(),
											result);
								Breadcrumbs.this.id = String.valueOf(result
										.getSample().getId());
								Breadcrumbs.this.onFindSuccessRecursive(Node
										.getParent());
								return;
							}
						}
					}
				}
			}.begin();
		} else if (name.equals("Specific Analysis")) {
			new ServerOp<ChemicalAnalysisDTO>() {
				public void begin() {
					MpDb.chemicalAnalysis_svc.details(Long.parseLong(Id), this);
				}

				public void onSuccess(ChemicalAnalysisDTO result) {
					for (Widget w : Breadcrumbs.this.getChildren()) {
						if (w instanceof MLink) {
							if (((MLink) w).getText().equals(name)) {
								((MLink) w).setText(result.getSpotId());
								Breadcrumbs.this.id = String.valueOf(result
										.getSubsample().getId());
								Breadcrumbs.this.onFindSuccessRecursive(Node
										.getParent());
								return;
							}
						}
					}
				}
			}.begin();
		} else if (name.equals("Specific User")) {
			new ServerOp<UserDTO>() {
				public void begin() {
					MpDb.user_svc.details(Id, this);
				}
				public void onSuccess(UserDTO result) {
					for (int i = 0; i < Breadcrumbs.this.getWidgetCount(); i++) {
						final Widget w = Breadcrumbs.this.getWidget(i);
						if (w instanceof MLink) {
							if (((MLink) w).getText().equals(name)) {
								((MLink) w).setText(result.getUsername());
								Breadcrumbs.this.onFindSuccessRecursive(Node
										.getParent());
								break;
							}
						}
					}
				}
			}.begin();
		} else if (name.equals("Create Map")) {
			new ServerOp<SubsampleDTO>() {
				public void begin() {
					MpDb.subsample_svc.details(Long.parseLong(Id), this);
				}

				public void onSuccess(SubsampleDTO result) {
					LeftColWidget.updateLeftSide(Node.getLeftSide(), result
							.getSample(), result);
					Breadcrumbs.this.onFindSuccessRecursive(Node.getParent());
				}
			}.begin();
		} else if (name.equals("Specific Project")) {
			new ServerOp<ProjectDTO>() {
				public void begin() {
					MpDb.project_svc.details(Integer.parseInt(Id), this);
				}

				public void onSuccess(ProjectDTO result) {
					for (int i = 0; i < Breadcrumbs.this.getWidgetCount(); i++) {
						final Widget w = Breadcrumbs.this.getWidget(i);
						if (w instanceof MLink) {
							if (((MLink) w).getText().equals(name)) {
								((MLink) w).setText(result.getName());
								Breadcrumbs.this.onFindSuccessRecursive(Node
										.getParent());
								break;
							}
						}
					}
				}
			}.begin();
		} else {
			Breadcrumbs.this.onFindSuccessRecursive(Node.getParent());
			return;
		}
	}

	/*
	 * Called to update where we are in the site. Hide the breadcrumbs bar until
	 * it's finished loading
	 */
	public void update(final String historyToken) {
		clear();
		setVisible(false);
		findNode(parseToken(historyToken));
	}

	public static bcNode getCurrentNode() {
		return current;
	}

}
