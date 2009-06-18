package edu.rpi.metpetdb.client.ui.input.attributes.specific.sample;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
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
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.commands.VoidMCommand;
import edu.rpi.metpetdb.client.ui.input.DetailsPanel;
import edu.rpi.metpetdb.client.ui.input.DetailsPanelEntry;
import edu.rpi.metpetdb.client.ui.input.MultipleObjectDetailsPanel;
import edu.rpi.metpetdb.client.ui.input.WizardDialog;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TreeAttribute;

public class MineralAttribute extends GenericAttribute implements ClickListener {

	private final Button chooseMinerals;
	private MObject obj;
	private final SimplePanel container;
	private TreeAttribute<Mineral> tree;
	private WizardDialog dialog;
	private boolean choseMinerals = false;

	private DetailsPanel p_mineral;

	final MultipleObjectDetailsPanel<SampleMineral> p_amounts = new MultipleObjectDetailsPanel<SampleMineral>(
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
		container = new SimplePanel();
		tree = new TreeAttribute<Mineral>(mc, 4, maxMinerals);
	}

	public Widget[] createDisplayWidget(final MObject obj) {
		remakeContainer(obj);
		return new Widget[] {
			container
		};
	}

	private void remakeContainer(final MObject obj) {
		this.obj = obj;
		addItemsToTree();
		final Widget[] widgets = tree.createDisplayWidget(obj);
		container.clear();
		for (int i = 0; i < widgets.length; ++i)
			container.add(widgets[i]);
	}

	public Widget[] createEditWidget(final MObject obj, final String id,
			final GenericAttribute ga) {
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
		tree.setSelectedItems(minerals);
	}
	private WizardDialog makeWizardDialog() {
		final WizardDialog wd = new WizardDialog();

		final GenericAttribute mineral_attributes[] = {
			tree
		};

		p_mineral = new DetailsPanel(mineral_attributes, null, false) {
			@Override
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
				// Before we edit the minerals we have to convert them to
				// SampleMineral
				final Iterator<Mineral> itr = tree.getSelectedItems()
						.iterator();
				final ArrayList<SampleMineral> newSelectedItems = new ArrayList<SampleMineral>();

				while (itr.hasNext()) {
					final Mineral mineral = itr.next();
					final SampleMineral sampleMineral = new SampleMineral();
					sampleMineral.setMineral(mineral);
					newSelectedItems.add(sampleMineral);
				}
				p_amounts.edit(merge(tree.getSelectedItems(), ((Sample)obj).getMinerals()));
				choseMinerals = true;
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

	protected Collection get(final GenericAttribute ga) {
		return tree.get(ga);
	}

	protected void set(final MObject obj, final Object v) {
		tree.set(obj, v);
	}

	public Object get(final Widget editWidget) {
		if (this.getConstraint().equals(MpDb.doc.Sample_minerals)) {
			// return the sample minerals
			if (choseMinerals)
				return p_amounts.getBeans();
			else
				return ((Sample)obj).getMinerals();
		} else {
			// return the minerals in the tree
			return tree.get(editWidget);
		}
	}

	protected Collection get(final MObject obj) {
		if (this.getConstraint().equals(MpDb.doc.Sample_minerals)) {
			// return the sample minerals
			return p_amounts.getBeans();
		} else {
			// return the minerals in the tree
			return tree.get(obj);
		}
	}

	/**
	 * Special version of contains that uses equals instead of hashcode
	 * 
	 * @param c
	 * @param o
	 * @return
	 */
	private Object containsObject(final Collection c, final Object o) {
		if (c == null || o == null)
			return null;
		final Iterator itr = c.iterator();
		while (itr.hasNext()) {
			final Object object = itr.next();
			if (o.equals(object)) {
				return object;
			}
		}
		return null;
	}

	public void onClick(final Widget sender) {
		if (sender == chooseMinerals) {
			if (dialog == null || !(p_mineral.getBean().equals(this.obj)))
				dialog = makeWizardDialog();
			final Command dialog_finish = new Command() {
				public void execute() {
					if (getConstraint().equals(MpDb.doc.Sample_minerals)){
						
						//hack to grab values from the form
						HashMap<GenericAttribute, DetailsPanelEntry> entries = dialog.getPanelEntries(1);
						Iterator entryItr = entries.entrySet().iterator();
						String htmlString = "";
						while (entryItr.hasNext()){
							Map.Entry<GenericAttribute, DetailsPanelEntry> pairs = (Map.Entry)entryItr.next();
							htmlString = pairs.getValue().getAttr().getMyPanel().toString();
						}

						Map<String, String> enteredValues = new HashMap();;
						
						Set<SampleMineral> amounts = p_amounts.getBeans();
						
						//set the sample amounts to the floats entered in the dialog
						//extra label at the beginning
						int index = htmlString.indexOf("/LABEL") + 6;
						htmlString = htmlString.substring(index);
						for(SampleMineral sm : amounts){
 							//get Mineral
							index = htmlString.indexOf("LABEL>") + 6;
							htmlString = htmlString.substring(index);
							int end = htmlString.indexOf("<");
							int endSpace = htmlString.indexOf(" ");
							String mineralString = htmlString.substring(0,Math.min(end, endSpace));
							
							//get value
							index = htmlString.indexOf("value=") + 6;
							htmlString = htmlString.substring(index);
							end = htmlString.indexOf(">");
							String floatString = htmlString.substring(0,end);
							
							enteredValues.put(mineralString, floatString);
						}
						
						//set each entered amount to its SampleMineral
						Iterator itr = amounts.iterator();
						while (itr.hasNext()){
							SampleMineral thisMineral = (SampleMineral) itr.next();
							thisMineral.setAmount(enteredValues.get(thisMineral.getMineral().toString()));
						}
						
						tree.set(obj, get(obj));
					}
					else
						//this is a hack to make it set the value the the user selected
						tree.set(obj, get(new Label()));
					MineralAttribute.this
							.remakeContainer(MineralAttribute.this.obj);
				}
			};
			dialog.clearDialogFinishListeners();
			dialog.addDialogFinishListener(dialog_finish);
			dialog.show();
		}
	}

}
