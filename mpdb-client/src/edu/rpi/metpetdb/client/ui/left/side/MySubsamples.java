package edu.rpi.metpetdb.client.ui.left.side;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.ui.MetPetDBApplication;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.image.browser.LeftSideLayer;
import edu.rpi.metpetdb.client.ui.widgets.MHtmlList;
import edu.rpi.metpetdb.client.ui.widgets.MLink;

public abstract class MySubsamples extends LeftColWidget implements
		UsesLeftColumn {
	private MHtmlList pList;
	private MHtmlList details;
	private Subsample current;
	private MLink detailsLink;
	private MLink addSubsampleLink;

	public MySubsamples(final Sample sample, final String token) {
		super("Sample " + sample.getName());
		this.setStyleName("lcol-MyProjects");
		MetPetDBApplication.registerPageWatcher(this);
		details = new MHtmlList();
		detailsLink = new MLink("Details", TokenSpace.detailsOf(sample));
		addSubsampleLink = new MLink("Add Subsample", TokenSpace
				.createNewSubsample(sample));
		details.add(detailsLink);
		details.add(addSubsampleLink);
		details.setStyleName("lcol-sectionList");
		add(details);
		new ServerOp() {
			@Override
			public void begin() {
				MpDb.subsample_svc.all(sample.getId(), this);
			}
			public void onSuccess(Object result) {
				if (token.equals(LocaleHandler.lc_entity
						.TokenSpace_Sample_Details()))
					detailsLink.addStyleName("cur");
				else if (token.equals(LocaleHandler.lc_entity
						.TokenSpace_Enter_Subsample()))
					addSubsampleLink.addStyleName("cur");
				if (((List<Subsample>) result).size() > 0) {
					createSubsamplesWidget((List<Subsample>) result);
				}
				onLoadCompletion();
			}
		}.begin();
	}

	public MySubsamples(final Sample sample, final Subsample subsample) {
		this(sample, "");
		current = subsample;
	}

	private void createSubsamplesWidget(final List<Subsample> subsamples) {
		pList = addSubSamples(subsamples);
		final Label subsamplesLabel = new Label(subsamples.get(0)
				.getSampleName()
				+ "'s Subsamples");
		subsamplesLabel.getElement().setClassName("h1");
		subsamplesLabel.setStyleName("leftsideHeader");
		pList.setStyleName("lcol-sectionList");
		this.add(subsamplesLabel);
		this.add(pList);
		if (current != null)
			setCur(current);
	}

	public void setCur(final Subsample cur) {
		removeCur();
		for (int i = 0; i < pList.getWidgetCount(); i++) {
			Widget w = pList.getWidget(i);
			if (w instanceof MLink) {
				if (((MLink) w).getText().equals(cur.getName())) {
					w.addStyleName("cur");
				}
			}
		}
	}

	private void removeCur() {
		detailsLink.removeStyleName("cur");
		addSubsampleLink.removeStyleName("cur");
	}

	public void insertLayers(final LeftSideLayer layers,
			final Subsample subsample) {
		for (int i = 0; i < pList.getWidgetCount(); i++) {
			Widget w = pList.getWidget(i);
			if (w instanceof MLink) {
				if (((MLink) w).getText().equals(subsample.getName())) {
					pList.add(layers, i + 1);
					return;
				}
			}
		}
	}

	public void removeLayers(final LeftSideLayer layers) {
		pList.remove(layers);
	}

	public static MHtmlList addSubSamples(List<Subsample> subsamples) {
		final MHtmlList list = new MHtmlList();

		Iterator it = subsamples.iterator();
		while (it.hasNext()) {
			final Subsample subsample = (Subsample) it.next();
			list.add(showSubsampleDetails(subsample));
		}
		return list;
	}

	public static MLink showSubsampleDetails(final Subsample subsample) {
		final MLink focusProject = new MLink(subsample.getName(), TokenSpace
				.detailsOf(subsample));
		return focusProject;
	}

	public void onPageChanged() {
		LeftColWidget.clearLeft();
	}

	public abstract void onLoadCompletion();

}
