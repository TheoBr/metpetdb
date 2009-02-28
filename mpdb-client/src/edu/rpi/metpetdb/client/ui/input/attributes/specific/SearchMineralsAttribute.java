package edu.rpi.metpetdb.client.ui.input.attributes.specific;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.Mineral;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.ValueInCollectionConstraint;
import edu.rpi.metpetdb.client.model.validation.interfaces.HasValues;
import edu.rpi.metpetdb.client.ui.input.DetailsPanel;
import edu.rpi.metpetdb.client.ui.input.WizardDialog;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TreeAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.search.SearchGenericAttribute;

public class SearchMineralsAttribute extends SearchGenericAttribute implements ClickListener{

	private MObject obj;
	private GenericAttribute ga;
	private TreeAttribute tree;
	private WizardDialog dialog;

	private DetailsPanel p_mineral;

	public SearchMineralsAttribute(final ObjectConstraint mc) {
		this(mc, 0);
	}
	public SearchMineralsAttribute(final ValueInCollectionConstraint mc) {
		this((PropertyConstraint) mc, 0);
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
		Widget [] ws = tree.createEditWidget(obj, id, ga);
		ArrayList<Tree> trees = tree.getTree();
		for (Tree t: trees){
			for(Widget w: t){
				((CheckBox)w).addClickListener(this);
			}
		}
		return ws;
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
			newSelectedItems.add(mineral);
		}
		return newSelectedItems;
	}

	protected Collection get(final MObject obj) {
		final Iterator itr = tree.getSelectedItems().iterator();
		final ArrayList newSelectedItems = new ArrayList();
		while (itr.hasNext()) {
			final Mineral mineral = (Mineral) itr.next();
			newSelectedItems.add(mineral);
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
	
	private Mineral getMineralWithId(final short id){
		Collection<?> minerals = ((HasValues)this.getConstraint()).getValues();
		Iterator<?> itr= minerals.iterator();
		Mineral m;
		Mineral result;
		while (itr.hasNext()){
			m = (Mineral) itr.next();
			if (m.getId()== id){
				return m;
			}
			m = getMineralWithId(id,m);
			if(m != null){
				return m;
			}
		}
		return null;
	}
	
	private Mineral getMineralWithId(final short id, final Mineral values){
		Collection<Mineral> minerals = values.getChildren();
		Iterator<Mineral> itr= minerals.iterator();
		Mineral m;
		Mineral result;
		while (itr.hasNext()){
			m = (Mineral) itr.next();
			if (m.getId()== id){
				return m;
			}
			m = getMineralWithId(id,m);
			if(m != null){
				return m;
			}
		}
		return null;
	}
	
	public void onClear(){
		for(CheckBox cb : (ArrayList<CheckBox>) tree.getSelectedWidgets()) {
			cb.setChecked(false);
			tree.getSelectedItems().remove(0);
		}
		
	}

	public void onClick(Widget sender) {
		SearchMineralsAttribute.this.getSearchInterface().createCritera();
	}
	
	public ArrayList<Widget> getCriteria(){
		final ArrayList<Widget> criteria = new ArrayList<Widget>();
		final ArrayList<Object> values = tree.getSelectedItems();
		final ArrayList<Widget> widgets = tree.getSelectedWidgets();
		for (int i = 0; i < values.size(); i++){
			int count = 0;
			int total = ((Mineral)values.get(i)).getChildren().size();
			String crit = "";
			Iterator<Mineral> itr = ((Mineral)values.get(i)).getChildren().iterator();
			while(itr.hasNext()){
				final Mineral m = itr.next();
				if (values.contains(m)){
					count++;
				}
			}
			if (count==total && ((Mineral)values.get(i)).getParentId() != null &&
					values.contains(getMineralWithId(((Mineral)values.get(i)).getParentId()))){
	//			criteria.add(createCritRow("Don't Show: "+ values.get(i).toString()));
			} else if (count == total){
				criteria.add(createCritRow("All " + values.get(i).toString()));
			} else if (total > 6 && total-count <= 3){
				crit = "All " + values.get(i).toString() + " except "; 
				Iterator<Mineral> itr2 = ((Mineral)values.get(i)).getChildren().iterator();
				while(itr2.hasNext()){
					final Mineral m = itr2.next();
					if (!values.contains(m)){
						crit+= m.toString() + ", ";
					}
				}
				crit = crit.substring(0,crit.length()-2);
				criteria.add(createCritRow(crit));
			} else if (count > 1){
				crit = values.get(i).toString() + ": "; 
				Iterator<Mineral> itr2 = ((Mineral)values.get(i)).getChildren().iterator();
				while(itr2.hasNext()){
					final Mineral m = itr2.next();
					if (values.contains(m)){
						crit+= m.toString() + ", ";
					}
				}
				crit = crit.substring(0,crit.length()-2);
				criteria.add(createCritRow(crit));			
			}
		}
		return criteria;
	}
}
