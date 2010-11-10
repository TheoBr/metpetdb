package edu.rpi.metpetdb.client.ui.input.attributes.specific.sample;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
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
import edu.rpi.metpetdb.client.ui.input.CurrentError;
import edu.rpi.metpetdb.client.ui.input.MultipleObjectDetailsPanel;
import edu.rpi.metpetdb.client.ui.input.attributes.FlyOutAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MHtmlList;
import edu.rpi.metpetdb.client.ui.widgets.MHtmlList.ListItem;

/**
 * Responsible for the display and editing/selection of lists of minerals, possibly
 * including quantities of each. Uses the FlyOutAttribute for selection and displays 
 * data as a list of names and quantity values.
 * 
 * @author millib2, wongp3
 */
public class MineralAttribute extends GenericAttribute<MObject> {

	private MObject obj;
	
	/**
	 * Allows for selection of Minerals from a tree structure.
	 */
	private FlyOutAttribute<Mineral> flyOutTree;
	
	/**
	 * Provides a panel for entering amounts for selected minerals.
	 */
	private MultipleObjectDetailsPanel<SampleMineral> amountsPanel;

	/**
	 * Constructor
	 * 
	 * @param mc
	 */
	public MineralAttribute(final ObjectConstraint<Mineral> mc) {
		this(mc, 0);
	}
	
	/**
	 * Constructor
	 * 
	 * @param mc
	 * @param maxMinerals
	 */
	public MineralAttribute(final ValueInCollectionConstraint<Mineral> mc,
			int maxMinerals) {
		this((PropertyConstraint) mc, maxMinerals);
	} 
	
	/**
	 * Constructor
	 * 
	 * @param mc
	 * @param maxMinerals
	 */
	public MineralAttribute(final PropertyConstraint mc, int maxMinerals) {
		super(mc);
		flyOutTree = new FlyOutAttribute<Mineral>(mc, maxMinerals, false);
		
		amountsPanel = new MultipleObjectDetailsPanel<SampleMineral>(
					new GenericAttribute[] {
						new TextAttribute(MpDb.doc.SampleMineral_Sample_minerals_amount)
					});
	}

	/**
	 * Creates the widget for the display of a set of minerals and quantities. 
	 * Minerals are displayed as a table of height ten, with as many columns 
	 * as necessary. If the MObject passed is a sample its mineral data is 
	 * displayed, otherwise the minerals set in the fly out tree are shown.
	 * 
	 * @param obj MObject with data to be displayed, usually a Sample 
	 */
	public Widget[] createDisplayWidget(final MObject obj) {
		
		this.obj = obj;
		
		//return new Widget[] { new Label("Widget!!") };
		
		FlexTable mineral_table = new FlexTable();
		List<String> vals = new ArrayList<String>();
		
		// Add sample
		if (this.obj instanceof Sample) {
			for (SampleMineral sm : ((Sample)this.obj).getMinerals()) {
				String text = sm.getMineral().getName();
				if (sm.getAmount() != null && !sm.getAmount().equals("")) {
					text += " (" + sm.getAmount() + ")";
				}
				vals.add(text);
			}
		}
		// Add chemical analysis
		else {
			final Widget[] widgets = flyOutTree.createDisplayWidget(obj);
			MHtmlList tempList = (MHtmlList) ((HorizontalPanel)widgets[0]).getWidget(0);
			
			Collection<ListItem> s = tempList.getItems();
			if (s != null) {
				final Iterator<ListItem> itr = s.iterator();
				while (itr.hasNext()) {
					String rock = ((Label)((MHtmlList.ListItem) itr.next()).getWidget()).getText();
					vals.add(rock);
				}
			}
		}
		
		Collections.sort(vals);
		
		mineral_table.setStyleName("mineral_table");
		mineral_table.setWidth("100%");
		
		int row = 0, col = 0;
		for(int i = 0; i < vals.size(); i++){
			if(i % 10 == 0){
				col++;
				row = 0;
			}
			mineral_table.setText(row, col, vals.get(i));
			row++;
		}
		
		return new Widget[] {
			mineral_table
		};
	}

	/**
	 * Creates the widget that allows for the editing of a list of minerals, possibly 
	 * including quantities. 
	 * 
	 * @param obj MObject with data to be displayed, usually a Sample
	 * @param id
	 * @param ga
	 */
	public Widget[] createEditWidget(final MObject obj, final String id,
			final GenericAttribute<MObject> ga) {
		
		this.obj = obj;
		
		final VerticalPanel vp = new VerticalPanel(); 
		
		// Clicking the "Choose Minerals..." button reveals the mineral chooser widget
		final DisclosurePanel mineralDPanel = new DisclosurePanel();
		mineralDPanel.setHeader(new Button("Choose Minerals..."));		
		final Widget[] ws = flyOutTree.createEditWidget(obj, "minerals", flyOutTree);
		for (Widget w : ws) {
			mineralDPanel.add(w);
		}

		// If we want to be able to enter mineral amounts, add the panel to do so
		if (this.getConstraint().equals(MpDb.doc.Sample_minerals)) {

			// Fixes a bug where minerals carry over from previous uses
			amountsPanel = null;
			amountsPanel = new MultipleObjectDetailsPanel<SampleMineral>(
					new GenericAttribute[] {
						new TextAttribute(MpDb.doc.SampleMineral_Sample_minerals_amount)
					});
			
			//final DisclosurePanel amountsDPanel = new DisclosurePanel();
			//amountsDPanel.setHeader(new Button("Choose Amounts..."));
			
			final VerticalPanel amountsContentPanel = new VerticalPanel();
			setAmountsPanel(amountsContentPanel);
			
			// Whenever the user selects or deselects an item on the mineral chooser,
			// the amounts panel needs to be updated
			flyOutTree.addClickListener(new ClickListener() {
				public void onClick(Widget sender) {					
					setAmountsPanel(amountsContentPanel);
				}
			});
			
			//amountsDPanel.add(amountsContentPanel);

			//vp.add(amountsDPanel);
			vp.add(amountsContentPanel);
		}

		vp.add(mineralDPanel);
		
		return new Widget[] {
			vp
		};
	}
	
	/**
	 * Updates the amounts panel to display the minerals currently selected
	 * in the fly out tree.
	 * 
	 * @param amountsContentPanel 
	 */
	private void setAmountsPanel(VerticalPanel amountsContentPanel) {
		
		// Give the amountsPanel the updated list of minerals
		amountsPanel.edit(merge(flyOutTree.getSelectedItems(), 
				((Sample)obj).getMinerals()));
		
		// Clear and repopulate amountsContentPanel
		amountsContentPanel.clear();
		if (amountsPanel.getBeans().size() > 0) {
			amountsContentPanel.add(amountsPanel);
		}
		else {
			final Label noMineralsLabel = new Label("No minerals selected."); 
			amountsContentPanel.add(noMineralsLabel);
		}	
	}
	
	/**
	 * Builds a list of SampleMinerals from selectedItems. SampleMinerals
	 * already contained in originalItems are just added to the list, while
	 * new objects are instantiated for those that are not. 
	 * 
	 * @param selectedItems List of minerals (no amount data) usually grabbed
	 * 		  from the fly out tree.
	 * @param originalItems List of Sample Minerals usually taken from the Sample (obj).
	 * @returns A list of SampleMinerals equivalent to the given list of Minerals, 
	 * selectedItems.
	 */
	private ArrayList<SampleMineral> merge(
			final ArrayList<Mineral> selectedItems,
			final Set<SampleMineral> originalItems) {
		
		final ArrayList<SampleMineral> merged = new ArrayList<SampleMineral>();
		
		// Copy over all existing SampleMinerals
		if (originalItems != null) {
			for(SampleMineral so : originalItems) {
				if (selectedItems.contains(so))
					merged.add(so);
			}
		}
		
		// Create a new SampleMineral for each new object
		for(Mineral m : selectedItems) {
			if (!merged.contains(m)) {
				merged.add(new SampleMineral(m));
			}
		}
		
		return merged;
	}
	
	/**
	 * We want to intercept the call to commitEdit so that our amounts can
	 * be validated.
	 * 
	 * @param obj
	 * @param wditWidget
	 * @param err
	 * @param r 
	 */
	@Override
	public void commitEdit(final MObject obj, final Widget[] editWidget,
			final CurrentError err, final Command r) {
		
		amountsPanel.validateEdit(null);
		super.commitEdit(obj, editWidget, err, r);		
	}

	/**
	 * Updates the data object of the fly out tree.
	 */
	protected void set(final MObject obj, final Object v) {
		flyOutTree.set(obj, v);
	}
	
	/**
	 * Retrieves the fly out tree's list of selected minerals.
	 * 
	 * @param ga
	 * @return Collection of minerals.
	 */
	protected Collection<Mineral> get(final GenericAttribute<MObject> ga) {
		return flyOutTree.get(ga);
	}

	/**
	 * If we can enter amounts get the collection of SampleMinerals
	 * from the amounts panel, otherwise get the list of Minerals
	 * from the fly out tree.
	 * 
	 * @param editWidget
	 * @return Either a Set of SampleMinerals a list of Minerals.
	 */
	public Object get(final Widget editWidget) {
		if (this.getConstraint().equals(MpDb.doc.Sample_minerals)) {
			// Return selected sample minerals (minerals + amounts)
			return amountsPanel.getBeans();
		} else {
			// Return the minerals in the tree (no amounts)
			return flyOutTree.get(editWidget);
		}
	}

	/**
	 * If we can enter amounts get the collection of SampleMinerals
	 * from the amounts panel, otherwise get the list of Minerals
	 * from the fly out tree.
	 * 
	 * @param obj
	 * @return Either a Set of SampleMinerals a list of Minerals.
	 */
	protected Collection<?> get(final MObject obj) {
		if (this.getConstraint().equals(MpDb.doc.Sample_minerals)) {
			// Return selected sample minerals (minerals + amounts)
			return amountsPanel.getBeans();
		} else {
			// Return the minerals in the tree (no amounts)
			return flyOutTree.get(obj);
		}
	}

}