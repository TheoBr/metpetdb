package edu.rpi.metpetdb.client.ui.left.side;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.widgets.MHtmlList;
import edu.rpi.metpetdb.client.ui.widgets.MLink;

public class MySamples extends LeftColWidget implements UsesLeftColumn {

	public MySamples() {
		super("My Samples");

		this.setStyleName("lcol-MySamples");

		final MHtmlList list = new MHtmlList();
		final MLink addSamples = new MLink("Add Sample", TokenSpace.enterSample);
		final MLink allOfMySamples = new MLink("All", TokenSpace.samplesForUser);
		final MLink recentSamples = new MLink("Recent Samples",
				new ClickListener() {
					public void onClick(final Widget sender) {

					}
				});
		final MLink favoriteSamples = new MLink("Favorite Samples",
				new ClickListener() {
					public void onClick(final Widget sender) {

					}
				});

		recentSamples.addStyleName("beta");
		favoriteSamples.addStyleName("beta");
		list.add(addSamples);
		list.add(allOfMySamples);
		list.add(recentSamples);
		list.add(favoriteSamples);
		list.setStyleName("lcol-sectionList");
		this.add(list);
	}

	public void onPageChanged() {
		// TODO Auto-generated method stub

	}

}
