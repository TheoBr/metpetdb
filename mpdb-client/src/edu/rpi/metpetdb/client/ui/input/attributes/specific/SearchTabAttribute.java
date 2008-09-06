package edu.rpi.metpetdb.client.ui.input.attributes.specific;

import java.util.ArrayList;
import java.util.Iterator;

import org.postgis.LinearRing;
import org.postgis.Point;
import org.postgis.Polygon;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.RockTypeDTO;
import edu.rpi.metpetdb.client.model.SearchSampleDTO;
import edu.rpi.metpetdb.client.model.validation.primitive.StringConstraint;
import edu.rpi.metpetdb.client.ui.input.ObjectEditorPanel;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;

public class SearchTabAttribute extends GenericAttribute {
	private GenericAttribute[][] atts;
	private ArrayList<ObjectEditorPanel<SearchSampleDTO>> searchSamples = new ArrayList();

	public SearchTabAttribute(final StringConstraint sc,
			final GenericAttribute[][] atts) {
		super(sc);
		this.atts = atts;
	}

	public Widget[] createDisplayWidget(final MObjectDTO obj) {

		return new Widget[] {};
	}

	public Widget[] createEditWidget(final MObjectDTO obj, final String id) {
		final TabPanel tabs = new TabPanel();
		for (int i = 0; i < atts.length; i++) {
			final ObjectEditorPanel<SearchSampleDTO> p_searchSample = new ObjectEditorPanel<SearchSampleDTO>(
					atts[i], LocaleHandler.lc_text.addSample(),
					LocaleHandler.lc_text.addSampleDescription()) {
				protected void loadBean(final AsyncCallback<SearchSampleDTO> ac) {
				}
				protected void saveBean(final AsyncCallback<SearchSampleDTO> ac) {
					ac.onSuccess((SearchSampleDTO) this.getBean());
				}
				protected void deleteBean(final AsyncCallback<Object> ac) {
				}
				protected void onSaveCompletion(final MObjectDTO result) {
					this.edit(result);
					clearConstraints();
					addConstraints((SearchSampleDTO) result);
				}
			};
			p_searchSample.getSaveButton().setText("Set");
			if (i == 0)
				tabs.add(p_searchSample, "Rock Type");
			if (i == 1)
				tabs.add(p_searchSample, "Region");
			if (i == 2)
				tabs.add(p_searchSample, "Minerals");
			if (i == 3)
				tabs.add(p_searchSample, "Chemistry");
			if (i == 4)
				tabs.add(p_searchSample, "Other");
			p_searchSample.edit(obj);
			searchSamples.add(p_searchSample);
		}
		tabs.selectTab(0);
		tabs.getTabBar().addStyleName("subpages");
		final HorizontalPanel hp = new HorizontalPanel();
		final Widget display = SearchConstraintDisplay();

		hp.add(display);
		hp.add(tabs);
		hp.setCellVerticalAlignment(display, HasVerticalAlignment.ALIGN_TOP);
		hp.setCellVerticalAlignment(tabs, HasVerticalAlignment.ALIGN_TOP);
		hp.setCellHorizontalAlignment(tabs, HasHorizontalAlignment.ALIGN_RIGHT);
		return new Widget[] {
			hp
		};
	}

	protected void set(final MObjectDTO obj, final Object o) {
		mSet(obj, o);
	}

	protected Object get(Widget editWidget) throws ValidationException {
		for (int i = 0; i < atts.length; i++) {
			searchSamples.get(i).getBean();
		}

		return null;
	}

	private VerticalPanel vp;
	private final static Label noConstraints = new Label(
			"Set your search criteria by selecting from the categories on the right.");

	public Widget SearchConstraintDisplay() {
		vp = new VerticalPanel();
		vp.setStyleName("criteria");
		final FlexTable ft = new FlexTable();
		final Label header = new Label("Search Criteria");
		final Hyperlink save = new Hyperlink();
		save.setText("save");
		save.addStyleName("beta");
		final Hyperlink clear = new Hyperlink();
		clear.setText("clear");
		clear.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				clearConstraints();
			}
		});
		final HorizontalPanel actionHolder = new HorizontalPanel();
		actionHolder.add(save);
		actionHolder.add(new Label("|"));
		actionHolder.add(clear);
		ft.setWidget(0, 0, header);
		ft.setWidget(0, 1, actionHolder);
		ft.getFlexCellFormatter().setAlignment(0, 1,
				HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		ft.setStyleName("titlebar");
		vp.add(ft);
		vp.add(noConstraints);
		return (vp);
	}

	public void addConstraint(final Widget constraint) {
		if (noConstraints.getParent() == vp) {
			noConstraints.removeFromParent();
		}
		final FlexTable row = new FlexTable();
		Button remove = new Button();
		remove.setStyleName("remove");
		remove.setPixelSize(14, 15);
		remove.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				row.removeFromParent();
			}
		});
		row.setWidget(0, 0, constraint);
		row.setWidget(0, 1, remove);
		row.getFlexCellFormatter().setAlignment(0, 1,
				HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		row.setWidth("100%");
		vp.add(row);
	}

	public void addConstraints(SearchSampleDTO ss) {
		if (ss.getOwner() != null) {
			final FlowPanel ownerContainer = new FlowPanel();
			final Label ownerLbl = new Label("Owner:");
			ownerLbl.addStyleName("inline");
			final Label ownerConstraint = new Label(ss.getOwner());
			ownerConstraint.addStyleName("inline");
			ownerContainer.add(ownerLbl);
			ownerContainer.add(ownerConstraint);
			addConstraint(ownerContainer);
		}
		if (ss.getSesarNumber() != null) {
			final FlowPanel sesarContainer = new FlowPanel();
			final Label sesarLbl = new Label("Sesar Number:");
			sesarLbl.addStyleName("inline");
			final Label sesarConstraint = new Label(ss.getSesarNumber());
			sesarConstraint.addStyleName("inline");
			sesarContainer.add(sesarLbl);
			sesarContainer.add(sesarConstraint);
			addConstraint(sesarContainer);
		}
		if (ss.getAlias() != null) {
			final FlowPanel aliasContainer = new FlowPanel();
			final Label aliasLbl = new Label("Alias:");
			aliasLbl.addStyleName("inline");
			final Label aliasConstraint = new Label(ss.getAlias());
			aliasConstraint.addStyleName("inline");
			aliasContainer.add(aliasLbl);
			aliasContainer.add(aliasConstraint);
			addConstraint(aliasContainer);
		}
		if (ss.getCollectionDateRange() != null) {

		}

		if (ss.getBoundingBox() != null) {
			final Polygon box = (Polygon) ss.getBoundingBox();
			final LinearRing lr = (LinearRing) box.getRing(0);

			final Point SW = lr.getPoint(0);
			final Point NE = lr.getPoint(2);
			final FlowPanel Container = new FlowPanel();
			final Label LocLbl = new Label("Location:");
			LocLbl.addStyleName("inline");
			final Label SConstraint = new Label(" S: " + String.valueOf(SW.x));
			final Label WConstraint = new Label(" W: " + String.valueOf(SW.y));
			final Label NConstraint = new Label(" N: " + String.valueOf(NE.x));
			final Label EConstraint = new Label(" E: " + String.valueOf(NE.y));
			SConstraint.addStyleName("inline");
			WConstraint.addStyleName("inline");
			NConstraint.addStyleName("inline");
			EConstraint.addStyleName("inline");
			Container.add(LocLbl);
			Container.add(SConstraint);
			Container.add(WConstraint);
			Container.add(NConstraint);
			Container.add(EConstraint);
			addConstraint(Container);
		}

		if (ss.getElements() != null) {

		}
		if (ss.getOxides() != null) {

		}
		if (ss.getMinerals() != null) {
			// Iterator<SampleMineralDTO> itr = ss.getMinerals().iterator();
			// while (itr.hasNext()) {
			// final FlowPanel mineralContainer = new FlowPanel();
			// final SampleMineralDTO mineral = itr.next();
			// final Label mineralLbl = new Label("Mineral:");
			// mineralLbl.addStyleName("inline");
			// final Label rockConstraint = new Label(mineral.getName());
			// rockConstraint.addStyleName("inline");
			// mineralContainer.add(mineralLbl);
			// mineralContainer.add(rockConstraint);
			// addConstraint(mineralContainer);
			// }
		}
		if (ss.getPossibleRockTypes() != null) {
			Iterator<RockTypeDTO> itr = ss.getPossibleRockTypes().iterator();
			while (itr.hasNext()) {
				final FlowPanel rockContainer = new FlowPanel();
				final String rockType = itr.next().getRockType();
				final Label rockLbl = new Label("Rock Type:");
				rockLbl.addStyleName("inline");
				final Label rockConstraint = new Label(rockType);
				rockConstraint.addStyleName("inline");
				rockContainer.add(rockLbl);
				rockContainer.add(rockConstraint);
				addConstraint(rockContainer);
			}
		}
	}

	public void clearConstraints() {
		while (vp.getWidgetCount() > 1)
			vp.remove(vp.getWidgetCount() - 1);
		vp.add(noConstraints);
	}
}
