package edu.rpi.metpetdb.client.ui.input.attributes.specific.sample;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.Mineral;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.SampleMineral;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.ValueInCollectionConstraint;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.commands.VoidMCommand;
import edu.rpi.metpetdb.client.ui.input.DetailsPanel;
import edu.rpi.metpetdb.client.ui.input.MultipleObjectDetailsPanel;
import edu.rpi.metpetdb.client.ui.input.WizardDialog;
import edu.rpi.metpetdb.client.ui.input.attributes.FlyOutAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MHtmlList;
import edu.rpi.metpetdb.client.ui.widgets.MHtmlList.ListItem;

/**
 * 
 * @author millib2, ?
 * 
 * TODO: comments!!!
 *
 */
public class MineralAttribute extends GenericAttribute<MObject> implements ClickHandler {

	private final Button chooseMinerals;
	private MObject obj;
	private final HorizontalPanel container;
	
	private FlyOutAttribute<Mineral> flyOutTree;
	
	private WizardDialog dialog;
	private boolean mineralsChosen = false;
	private DetailsPanel<MObject> p_mineral;

	private final MultipleObjectDetailsPanel<SampleMineral> p_amounts = 
		new MultipleObjectDetailsPanel<SampleMineral>(
			new GenericAttribute[] {
				new TextAttribute(MpDb.doc.SampleMineral_Sample_minerals_amount)
			});

	public MineralAttribute(final ObjectConstraint<Mineral> mc) {
		this(mc, 0);
	}
	
	public MineralAttribute(final ValueInCollectionConstraint<Mineral> mc,
			int maxMinerals) {
		this((PropertyConstraint) mc, maxMinerals);
	} 
	
	public MineralAttribute(final PropertyConstraint mc, int maxMinerals) {
		super(mc);
		chooseMinerals = new Button("Choose Minerals...", this);
		container = new HorizontalPanel();
		flyOutTree = new FlyOutAttribute<Mineral>(mc, maxMinerals, false);
	}

	public Widget[] createDisplayWidget(final MObject obj) {
		
		final DisclosurePanel expand = new DisclosurePanel();
		List<String> vals = new ArrayList<String>();		
		this.obj = obj;
		
		addItemsToTree();
		
		final Widget[] widgets = flyOutTree.createDisplayWidget(obj);
		// MHtmlList tempList = (MHtmlList) ((SimplePanel) widgets[0]).getWidget();
		MHtmlList tempList = (MHtmlList) ((HorizontalPanel)widgets[0]).getWidget(0);
		
		Collection<ListItem> s = tempList.getItems();
		if (s != null) {
			final Iterator<ListItem> itr = s.iterator();
			while (itr.hasNext()) {
				vals.add(((Label)((MHtmlList.ListItem) itr.next()).getWidget()).getText());
			}
		}
		Collections.sort(vals);
		
		MHtmlList list = new MHtmlList();
		for (int i = 1; i < vals.size(); i++) {
			list.add(new Label(vals.get(i)));
		}

		if (vals.size() > 0) {
			final Label closedText = new Label(vals.get(0));
			expand.setHeader(closedText);
		}
		expand.add(list);
		expand.setAnimationEnabled(true);
		return new Widget[] {
			expand
		};
	}

	private void remakeContainer(final MObject obj) {
		
		this.obj = obj;
		
		addItemsToTree();
		
		container.clear();
		
		final Widget[] widgets = flyOutTree.createDisplayWidget(obj);
		
		for (int i = 0; i < widgets.length; ++i) {
			container.add(widgets[i]);
			System.out.println(widgets[i].toString());
		}
	}

	public Widget[] createEditWidget(final MObject obj, final String id,
			final GenericAttribute<MObject> ga) {
		
		final VerticalPanel vp = new VerticalPanel();
		chooseMinerals.setEnabled(true);
		this.obj = obj;
		vp.add(chooseMinerals);
		
		remakeContainer(obj);
		vp.add(container);
		
		return new Widget[] {
			vp
		};
	}

	private void addItemsToTree() {
		final ArrayList<Mineral> minerals = new ArrayList<Mineral>();
		if (get(obj) != null) {
			for (Object o : get(obj)) {
				if (o instanceof SampleMineral)
					minerals.add(((SampleMineral) o).getMineral());
				else if (o instanceof Mineral)
					minerals.add((Mineral) o);
			}
		}
		
		// tree.setSelectedItems(minerals);
		flyOutTree.setSelectedItems(minerals);
	}
	
	private WizardDialog makeWizardDialog() {
		
		final WizardDialog wd = new WizardDialog();

		final GenericAttribute<?> mineral_attributes[] = {
			flyOutTree
		};

		p_mineral = new DetailsPanel<MObject>(mineral_attributes, null, false) {
			public boolean validateEdit(final VoidMCommand r) {
				if (r != null)
					r.execute();
				// don't worry about validating this, they are just selecting
				// minerals
				return true;
			}
		};
		
		wd.turnOffValidation();

		p_mineral.edit(this.obj);
		
		final Command mineral_amount = new Command() {
			public void execute() {
				p_amounts.edit(merge(flyOutTree.getSelectedItems(), 
						((Sample)obj).getMinerals()));
				mineralsChosen = true;
			}
		};
		
		wd.addStep(p_mineral, 0, "Select Minerals");
		if (this.getConstraint().equals(MpDb.doc.Sample_minerals)) {
			wd.addTabChangeListener(mineral_amount);
			wd.addStep(p_amounts, 1, "Enter Amounts");
		}
		
		return wd;
	}

	private ArrayList<SampleMineral> merge(
			final ArrayList<Mineral> selectedItems,
			final Set<SampleMineral> originalItems) {
		final ArrayList<SampleMineral> merged = new ArrayList<SampleMineral>();
		if (originalItems != null) {
			for(SampleMineral so : originalItems) {
				if (selectedItems.contains(so))
					merged.add(so);
			}
		}
		for(Mineral m : selectedItems) {
			if (!merged.contains(m)) {
				final SampleMineral sm = new SampleMineral();
				sm.setMineral(m);
				merged.add(sm);
			}
		}
		return merged;
	}
	
	protected Collection<Mineral> get(final GenericAttribute<MObject> ga) {
		return flyOutTree.get(ga);
	}

	protected void set(final MObject obj, final Object v) {
		flyOutTree.set(obj, v);
	}

	public Object get(final Widget editWidget) {
		if (this.getConstraint().equals(MpDb.doc.Sample_minerals)) {
			// return the sample minerals
			if (mineralsChosen)
				return p_amounts.getBeans();
			else
				return ((Sample)obj).getMinerals();
		} else {
			// return the minerals in the tree
			return flyOutTree.get(editWidget);
		}
	}

	protected Collection<?> get(final MObject obj) {
		if (this.getConstraint().equals(MpDb.doc.Sample_minerals)) {
			// return the sample minerals
			return p_amounts.getBeans();
		} else {
			// return the minerals in the tree
			return flyOutTree.get(obj);
		}
	}
	
	/**
	 * Called when the user clicks the "Choose minerals..." button.
	 * 
	 * @param event
	 * 		The GWT ClickEvent object associated with the user's click action. 
	 */
	public void onClick(ClickEvent event) {
		// Create and show the select minerals dialog
		if (event.getSource() == chooseMinerals) {
			
			if (dialog == null || !(p_mineral.getBean().equals(this.obj)))
				dialog = makeWizardDialog();
			
			// Command to be executed when the user is finished with the 
			// select minerals dialog
			final Command dialog_finish = new Command() {
				public void execute() {
					if (getConstraint().equals(MpDb.doc.Sample_minerals)){
						p_amounts.validateEdit(null);
						flyOutTree.set(obj, get(obj));
					}
					else {
						//this is a hack to make it set the value the the user selected
						flyOutTree.set(obj, get(new Label()));
					}
					MineralAttribute.this.remakeContainer(MineralAttribute.this.obj);
				}
			};
			
			dialog.clearDialogFinishListeners();
			dialog.addDialogFinishListener(dialog_finish);
			dialog.show();
		}
	}
}