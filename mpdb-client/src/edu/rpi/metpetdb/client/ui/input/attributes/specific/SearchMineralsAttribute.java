package edu.rpi.metpetdb.client.ui.input.attributes.specific;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.Mineral;
import edu.rpi.metpetdb.client.model.SampleMineral;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.ValueInCollectionConstraint;
import edu.rpi.metpetdb.client.ui.input.DetailsPanel;
import edu.rpi.metpetdb.client.ui.input.WizardDialog;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TreeAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.search.SearchGenericAttribute;

public class SearchMineralsAttribute extends SearchGenericAttribute {

	private MObject obj;
	private GenericAttribute ga;
	private TreeAttribute tree;
	private WizardDialog dialog;

	private DetailsPanel p_mineral;

	public SearchMineralsAttribute(final ObjectConstraint mc) {
		this(mc, 0);
	}
	public SearchMineralsAttribute(final ValueInCollectionConstraint mc,
			int maxMinerals) {
		this((PropertyConstraint) mc, maxMinerals);
	}
	public SearchMineralsAttribute(final PropertyConstraint mc, int maxMinerals) {
		super(mc);
		tree = new TreeAttribute(mc, 4, maxMinerals);
	}

	public Widget[] createDisplayWidget(final MObject obj) {
		return new Widget[] {

		};
	}

	public Widget[] createEditWidget(final MObject obj, final String id,
			final GenericAttribute ga) {
		return tree.createEditWidget(obj, id, ga);
	}

	protected Collection get(final GenericAttribute ga) {
		return tree.get(ga);
	}

	protected void set(final MObject obj, final Object v) {
		tree.set(obj, v);
	}

	protected Object get(final Widget editWidget) {
		final Iterator itr = tree.getSelectedItems().iterator();
		final Set<Object> newSelectedItems = new HashSet();
		while (itr.hasNext()) {
			final Mineral mineral = (Mineral) itr.next();
			final SampleMineral sampleMineral = new SampleMineral();
			sampleMineral.setMineral(mineral);
			newSelectedItems.add(sampleMineral);
		}
		return newSelectedItems;
	}

	protected Collection get(final MObject obj) {
		final Iterator itr = tree.getSelectedItems().iterator();
		final ArrayList newSelectedItems = new ArrayList();
		while (itr.hasNext()) {
			final Mineral mineral = (Mineral) itr.next();
			final SampleMineral sampleMineral = new SampleMineral();
			sampleMineral.setMineral(mineral);
			newSelectedItems.add(sampleMineral);
		}
		return newSelectedItems;
	}
	
	public void onRemoveCriteria(final Object obj){
		if (tree.getSelectedItems().contains(obj)){
			int index = tree.getSelectedItems().indexOf(obj);
			tree.getSelectedItems().remove(index);
			((CheckBox) tree.getSelectedWidgets().get(index)).setChecked(false);
			tree.getSelectedWidgets().remove(index);
		}
	}
	
	public ArrayList<Pair> getCriteria(){
		final ArrayList<Pair> criteria = new ArrayList<Pair>();
//		final ArrayList<Object> widgets = tree.getSelectedWidgets();
		final ArrayList<Object> values = tree.getSelectedItems();
		for (int i = 0; i < values.size(); i++){
			criteria.add(new Pair(createCritRow("Mineral:", values.get(i).toString()), values.get(i)));
		}
		return criteria;
	}
}
