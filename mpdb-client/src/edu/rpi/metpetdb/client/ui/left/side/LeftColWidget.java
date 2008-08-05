package edu.rpi.metpetdb.client.ui.left.side;

import java.util.HashSet;
import java.util.List;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.ProjectDTO;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.model.SubsampleDTO;
import edu.rpi.metpetdb.client.ui.MetPetDBApplication;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.image.browser.LeftSideLayer;

public class LeftColWidget extends SimplePanel {

	private final FlowPanel item;

	private static LeftSideLayer l;
	private static MySubsamples ms;
	private static SubsampleDTO s;

	public LeftColWidget(final String headerText) {
		// super();

		item = new FlowPanel();

		this.setWidget(item);

		final Element header = DOM.createElement("h1");
		DOM.setElementAttribute(header, "class", "leftsideHeader");
		final Label headerLink = new Label(headerText);
		DOM.appendChild(header, headerLink.getElement());
		DOM.appendChild(item.getElement(), header);

	}

	public void add(final Widget w) {
		item.add(w);
	}

	public static void updateLeftSide(final String left) {
		MetPetDBApplication.clearLeftSide();
		if (left.equals(LocaleHandler.lc_entity.LeftSide_UserInfo())) {
			MetPetDBApplication.appendToLeft(new UserInfo(MpDb.currentUser()));
		} else if (left.equals(LocaleHandler.lc_entity.LeftSide_MySamples())) {
			MetPetDBApplication.appendToLeft(new MySamples());
		} else if (left.equals(LocaleHandler.lc_entity.LeftSide_MySearch())) {
			MetPetDBApplication.appendToLeft(new MySearch());
		} else if (left.equals(LocaleHandler.lc_entity.LeftSide_MyProjects())) {
			new ServerOp() {
				@Override
				public void begin() {
					MpDb.project_svc.all(MpDb.currentUser().getId(), this);
				}
				public void onSuccess(Object result) {
					MetPetDBApplication
							.appendToLeft(new MyProjects(
									new HashSet<ProjectDTO>(
											(List<ProjectDTO>) result)));
				}
			}.begin();
		}
	}

	public static void updateLeftSide(final String left, final SampleDTO sample) {
		MetPetDBApplication.clearLeftSide();
		if (left.equals(LocaleHandler.lc_entity.LeftSide_MySubsamples())) {
			MetPetDBApplication.appendToLeft(new MySubsamples(sample, History
					.getToken().split("-")[0]) {
				public void onLoadCompletion() {
				};
			});
		}
	}

	public static void updateLeftSide(final String left,
			final SampleDTO sample, final SubsampleDTO subsample) {
		MetPetDBApplication.clearLeftSide();
		if (left.equals(LocaleHandler.lc_entity.LeftSide_MySubsamples())) {
			MetPetDBApplication
					.appendToLeft(new MySubsamples(sample, subsample) {
						public void onLoadCompletion() {
						};
					});
		} else if (left
				.equals(LocaleHandler.lc_entity.LeftSide_LeftSideLayer())) {
			final MySubsamples ms = new MySubsamples(sample, subsample) {
				public void onLoadCompletion() {
					LeftColWidget.insertMySubsamplesLeftSide(this);
				}
			};
			MetPetDBApplication.appendToLeft(ms);
		}
	}

	public static void insertLayersLeftSide(final LeftSideLayer layer,
			final SubsampleDTO sub) {
		l = layer;
		s = sub;
		if (ms != null) {
			ms.insertLayers(l, s);
		}
	}

	public static void insertMySubsamplesLeftSide(final MySubsamples subsamples) {
		ms = subsamples;
		if (l != null) {
			ms.insertLayers(l, s);
		}
	}

	public static void clearLeft() {
		ms = null;
		l = null;
		s = null;
	}
}
