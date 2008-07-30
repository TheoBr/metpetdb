package edu.rpi.metpetdb.client.ui.left.side;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.ui.Label;

import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.model.SubsampleDTO;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.image.browser.LeftSideLayer;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.MUnorderedList;
import edu.rpi.metpetdb.client.ui.widgets.MUnorderedList.ListItem;

public class MySubsamples extends LeftColWidget implements UsesLeftColumn {
	private MUnorderedList pList;

	public MySubsamples(final List<SubsampleDTO> subsamples) {
		super("Sample " + subsamples.get(0).getSample().getName());
		this.setStyleName("lcol-MyProjects");
		// MetPetDBApplication.registerPageWatcher(this);
		pList = new MUnorderedList();
		final MUnorderedList details = new MUnorderedList();
		final MLink addSubSample = new MLink("Add Subsample", TokenSpace
				.createNewSubsample(subsamples.get(0).getSample()));
		details.add(new MLink("Details", TokenSpace.detailsOf(subsamples.get(0)
				.getSample())));
		pList.add(addSubSample);
		if (subsamples != null)
			pList.add(addSubSamples(subsamples));
		final Label subsamplesLabel = new Label(subsamples.get(0).getSample()
				.getName()
				+ "'s Subsamples");
		details.setStyleName("lcol-sectionList");
		pList.setStyleName("lcol-sectionList");
		subsamplesLabel.getElement().setClassName("h1");
		subsamplesLabel.setStyleName("leftsideHeader");

		this.add(details);
		this.add(subsamplesLabel);
		this.add(pList);
	}

	public void setCur(final SubsampleDTO cur) {
		removeCur();
		Iterator<ListItem> itr = pList.getItems().iterator();
		while (itr.hasNext()) {
			MLink item = (MLink) itr.next().getWidget();
			if (item.getText().equals(cur.getName()))
				item.addStyleName("cur");
		}
	}

	private void removeCur() {
		Iterator<ListItem> itr = pList.getItems().iterator();
		while (itr.hasNext()) {
			itr.next().getWidget().removeStyleName("cur");
		}
	}

	public void insertLayers(final LeftSideLayer layers,
			final SubsampleDTO subsample) {

		HashSet<ListItem> temp = (HashSet<ListItem>) pList.getItems();
		MUnorderedList tempList = (MUnorderedList) temp.iterator().next()
				.getWidget();
		for (int i = 0; i < tempList.getWidgetCount(); i++) {
			final MLink tempLink = (MLink) tempList.getWidget(i);
			if (tempLink.getText().equals(subsample.getName())) {
				tempList.add(layers, i + 1);
				break;
			}
		}
	}

	public void removeLayers(final LeftSideLayer layers) {
		HashSet<ListItem> temp = (HashSet<ListItem>) pList.getItems();
		MUnorderedList tempList = (MUnorderedList) temp.iterator().next()
				.getWidget();
		tempList.remove(layers);
	}

	public MySubsamples(final SampleDTO sample) {
		super(sample.getName());
		this.setStyleName("lcol-MyProjects");
		final MLink details = new MLink("Details", TokenSpace.detailsOf(sample));
		this.add(details);
	}

	public static MUnorderedList addSubSamples(List<SubsampleDTO> subsamples) {
		final MUnorderedList list = new MUnorderedList();

		Iterator it = subsamples.iterator();
		while (it.hasNext()) {
			final SubsampleDTO subsample = (SubsampleDTO) it.next();
			list.add(showSubsampleDetails(subsample));

		}

		return list;
	}

	public static MLink showSubsampleDetails(final SubsampleDTO subsample) {
		final MLink focusProject = new MLink(subsample.getName(), TokenSpace
				.detailsOf(subsample));
		return focusProject;
	}

	public void onPageChanged() {
		// MetPetDBApplication.removePageWatcher(this);
		// MetPetDBApplication.removeFromLeft(this);
	}

}
