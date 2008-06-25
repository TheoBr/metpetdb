package edu.rpi.metpetdb.client.ui.left.side;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.ui.Label;

import edu.rpi.metpetdb.client.model.SubsampleDTO;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.MUnorderedList;

public class MySubsamples extends LeftColWidget implements UsesLeftColumn {

	public MySubsamples(final List<SubsampleDTO> subsamples) {
		super(subsamples.get(0).getSample().getName());
		this.setStyleName("lcol-MyProjects");
		// MetPetDBApplication.registerPageWatcher(this);
		final MUnorderedList pList = new MUnorderedList();
		final MLink addSubSample = new MLink("Add Subsample",
				TokenSpace.enterSubsample);
		final MLink details = new MLink("Details", TokenSpace
				.detailsOf(subsamples.get(0).getSample()));
		// pList.add(addSubSample);
		if (subsamples != null)
			pList.add(addSubSamples(subsamples));
		final Label subsamplesLabel = new Label(subsamples.get(0).getSample()
				.getName()
				+ "'s Subsamples");
		details.setStyleName("lcol-sectionList");
		pList.setStyleName("lcol-sectionList");
		subsamplesLabel.setStyleName("lcol-MyProjects");

		this.add(details);
		this.add(subsamplesLabel);
		this.add(pList);

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
		// End
		// of
		// ClickListener

		// myProjects.add(focusProject);
		return focusProject;
	}

	public void onPageChanged() {
		// MetPetDBApplication.removePageWatcher(this);
		// MetPetDBApplication.removeFromLeft(this);
	}

}
