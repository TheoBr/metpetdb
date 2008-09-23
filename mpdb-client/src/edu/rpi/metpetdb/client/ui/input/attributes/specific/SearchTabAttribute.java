package edu.rpi.metpetdb.client.ui.input.attributes.specific;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

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
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.RockType;
import edu.rpi.metpetdb.client.model.SampleMineral;
import edu.rpi.metpetdb.client.model.SearchElement;
import edu.rpi.metpetdb.client.model.SearchOxide;
import edu.rpi.metpetdb.client.model.SearchSample;
import edu.rpi.metpetdb.client.model.properties.Property;
import edu.rpi.metpetdb.client.model.validation.primitive.StringConstraint;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.input.ObjectEditorPanel;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MTabPanel;
import edu.rpi.metpetdb.client.ui.widgets.MTwoColPanel;

public class SearchTabAttribute extends GenericAttribute {
	private GenericAttribute[][] atts;
	private ArrayList<ObjectEditorPanel<SearchSample>> searchSamples = new ArrayList();

	public SearchTabAttribute(final StringConstraint sc,
			final GenericAttribute[][] atts) {
		super(sc);
		this.atts = atts;
	}

	public Widget[] createDisplayWidget(final MObject obj) {

		return new Widget[] {};
	}

	public Widget[] createEditWidget(final SearchSample obj, final String id) {
		final MTwoColPanel panel = new MTwoColPanel();
		final MTabPanel tabs = new MTabPanel();
		for (int i = 0; i < atts.length; i++) {
			final ObjectEditorPanel<SearchSample> p_searchSample = new ObjectEditorPanel<SearchSample>(
					atts[i], LocaleHandler.lc_text.addSample(),
					LocaleHandler.lc_text.addSampleDescription()) {
				protected void loadBean(final AsyncCallback<SearchSample> ac) {
				}
				protected void saveBean(final AsyncCallback<SearchSample> ac) {
					ac.onSuccess((SearchSample) this.getBean());
				}
				protected void deleteBean(final AsyncCallback<Object> ac) {
				}
				protected void onSaveCompletion(final SearchSample result) {
					this.edit(result);
					clearConstraints();
					addConstraints((SearchSample) result);
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
		final Widget display = SearchConstraintDisplay();

		panel.getLeftCol().add(display);
		panel.getRightCol().add(tabs);
		panel.setLeftColWidth("30%");
		panel.setRightColWidth("70%");
		return new Widget[] {
			panel
		};
	}

	protected void set(final MObject obj, final Object o) {
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
				removeFromBean(((CritContainer) constraint).getConstraint(),
						((CritContainer) constraint).getProperty());
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

	public void addConstraints(SearchSample ss) {
		if (ss.getOwner() != null) {
			addConstraint(createCritRow("Owner:", ss.getOwner(), ss.getOwner(),
					(Property) MpDb.oc.SearchSample_owner.property));
		}
		if (ss.getSesarNumber() != null) {
			addConstraint(createCritRow("Sesar Number:", ss.getSesarNumber(),
					ss.getSesarNumber(),
					(Property) MpDb.oc.SearchSample_sesarNumber.property));
		}
		if (ss.getAlias() != null) {
			addConstraint(createCritRow("Alias:", ss.getAlias(), ss.getAlias(),
					(Property) MpDb.oc.SearchSample_alias.property));
		}
		if (ss.getCollectionDateRange() != null) {
			final int StartMonth = ss.getCollectionDateRange().getStartAsDate()
					.getMonth();
			final int StartDay = ss.getCollectionDateRange().getStartAsDate()
					.getDay();
			final int StartYear = ss.getCollectionDateRange().getStartAsDate()
					.getYear();
			final String StartDate = String.valueOf(StartMonth) + "/"
					+ String.valueOf(StartDay) + "/"
					+ String.valueOf(StartYear);
			final int EndMonth = ss.getCollectionDateRange().getEndAsDate()
					.getMonth();
			final int EndDay = ss.getCollectionDateRange().getEndAsDate()
					.getDay();
			final int EndYear = ss.getCollectionDateRange().getEndAsDate()
					.getYear();
			final String EndDate = String.valueOf(EndMonth) + "/"
					+ String.valueOf(EndDay) + "/" + String.valueOf(EndYear);
			String range = StartDate + " - " + EndDate;
			addConstraint(createCritRow(
					"Date Range:",
					range,
					ss.getCollectionDateRange(),
					(Property) MpDb.oc.SearchSample_collectionDateRange.property));
		}

		if (ss.getBoundingBox() != null) {
			final Polygon box = (Polygon) ss.getBoundingBox();
			final LinearRing lr = (LinearRing) box.getRing(0);
			final Point SW = lr.getPoint(0);
			final Point NE = lr.getPoint(2);
			addConstraint(createCritRow("Location:", "S: "
					+ String.valueOf(SW.x) + " W: " + String.valueOf(SW.y)
					+ " N: " + String.valueOf(NE.x) + " E: "
					+ String.valueOf(NE.y), ss.getBoundingBox(),
					MpDb.oc.SearchSample_boundingBox.property));
		}

		if (ss.getElements() != null) {
			Iterator<SearchElement> itr = ss.getElements().iterator();
			while (itr.hasNext()) {
				final SearchElement sElement = itr.next();
				addConstraint(createCritRow("Element:", sElement.getName(),
						sElement, MpDb.doc.SearchSample_elements.property));
			}
		}
		if (ss.getOxides() != null) {
			Iterator<SearchOxide> itr = ss.getOxides().iterator();
			while (itr.hasNext()) {
				final SearchOxide sOxide = itr.next();
				addConstraint(createCritRow("Oxide:", sOxide.getName(), sOxide,
						MpDb.doc.SearchSample_oxides.property));
			}
		}
		if (ss.getMinerals() != null) {
			Iterator<SampleMineral> itr = ss.getMinerals().iterator();
			while (itr.hasNext()) {
				final SampleMineral sm = itr.next();
				addConstraint(createCritRow("Mineral:", sm.getName(), sm,
						MpDb.doc.SearchSample_minerals.property));
			}
		}
		if (ss.getPossibleRockTypes() != null) {
			Iterator<RockType> itr = ss.getPossibleRockTypes().iterator();
			while (itr.hasNext()) {
				final RockType rt = itr.next();
				addConstraint(createCritRow("Rock Type:", rt.getRockType(), rt,
						MpDb.doc.SearchSample_possibleRockTypes.property));
			}
		}
	}

	public Widget createCritRow(final String label, final String value,
			final Object obj, final Property p) {
		final CritContainer container = new CritContainer(obj, p);
		final Label critLabel = new Label(label);
		critLabel.addStyleName("critlabel");
		critLabel.addStyleName("inline");
		final Label critConstraint = new Label(value);
		critConstraint.addStyleName("inline");
		container.add(critLabel);
		container.add(critConstraint);
		return container;

	}

	public void clearConstraints() {
		while (vp.getWidgetCount() > 1)
			vp.remove(vp.getWidgetCount() - 1);
		vp.add(noConstraints);
	}

	private void removeFromBean(final Object constraint, final Property p) {
		SearchSample ss = (SearchSample) searchSamples.get(0).getBean();
		Object value = ss.mGet(p);
		if (value instanceof Set) {
			((Set) value).remove(constraint);
		} else
			value = null;
		ss.mSet(p, value);
		for (int i = 0; i < searchSamples.size(); i++) {
			searchSamples.get(i).edit(searchSamples.get(i).getBean());
		}
	}

	private class CritContainer extends FlowPanel {
		final Object constraint;
		final Property p;

		public CritContainer(final Object constraint, final Property p) {
			this.constraint = constraint;
			this.p = p;
		}
		public Object getConstraint() {
			return constraint;
		}
		public Property getProperty() {
			return p;
		}
	};
}
