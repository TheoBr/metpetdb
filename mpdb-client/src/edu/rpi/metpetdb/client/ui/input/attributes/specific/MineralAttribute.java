package edu.rpi.metpetdb.client.ui.input.attributes.specific;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.MObject;
import edu.rpi.metpetdb.client.model.Mineral;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.SampleMineral;
import edu.rpi.metpetdb.client.model.validation.MineralConstraint;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.input.DetailsPanel;
import edu.rpi.metpetdb.client.ui.input.MultipleObjectDetailsPanel;
import edu.rpi.metpetdb.client.ui.input.WizardDialog;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TreeAttribute;

public class MineralAttribute extends GenericAttribute implements ClickListener {

	private final Button chooseMinerals;
	private MObject obj;
	private GenericAttribute ga;
	private final SimplePanel container;
	private TreeAttribute tree;
	private WizardDialog dialog;
	
	
	private DetailsPanel p_mineral;

	public MineralAttribute(final MineralConstraint mc) {
		this(mc, 0);
	}
	public MineralAttribute(final MineralConstraint mc, int maxMinerals) {
		super(mc);
		chooseMinerals = new Button("Choose Minerals...", this);
		container = new SimplePanel();
		tree = new TreeAttribute(mc, 4, maxMinerals);
	}
	
	public Widget[] createDisplayWidget(final MObject obj) {
		remakeContainer(obj);
		return new Widget[] {container};
	}
	
	private void remakeContainer(final MObject obj) {
		final Widget[] widgets = tree.createDisplayWidget(obj);
		container.clear();
		for(int i = 0;i<widgets.length;++i)
			container.add(widgets[i]);
	}

	public Widget[] createEditWidget(final MObject obj, final String id,
			final GenericAttribute ga) {
		final VerticalPanel vp = new VerticalPanel();
		chooseMinerals.setEnabled(true);
		this.obj = obj;
		vp.add(chooseMinerals);
		final ArrayList tempList = new ArrayList();
		if (get(obj) != null)
			tempList.addAll(get(obj));
		tree.setSelectedItems(tempList);
		remakeContainer(obj);
		vp.add(container);
		this.ga = ga;
		return new Widget[]{vp};
	}
	
	private WizardDialog makeWizardDialog() {
		final WizardDialog wd = new WizardDialog();

		final MultipleObjectDetailsPanel p_amounts = new MultipleObjectDetailsPanel(
				new GenericAttribute[]{new TextAttribute(
						MpDb.doc.SampleMineral_amount)});
		
		final GenericAttribute mineral_attributes[] = {tree};
		
		p_mineral = new DetailsPanel(mineral_attributes,
				null, false);
		
		p_mineral.edit(this.obj);
		
		final ServerOp mineral_amount = new ServerOp() {
			public void begin() {
				// Before we edit the minerals we have to convert them to
				// SampleMineral
				final Iterator itr = tree.getSelectedItems().iterator();
				final ArrayList newSelectedItems = new ArrayList();
				while (itr.hasNext()) {
					final Mineral mineral = (Mineral) itr.next();
					final Object object = containsObject(
							((Sample) MineralAttribute.this.obj).getMinerals(),
							mineral);
					if (object != null) {
						newSelectedItems.add(object);
					} else {
						final SampleMineral sampleMineral = new SampleMineral();
						sampleMineral.setMineral(mineral);
						newSelectedItems.add(sampleMineral);
					}
				}
				tree.setSelectedItems(newSelectedItems);
				p_amounts.edit(newSelectedItems);
			}
			public void onSuccess(final Object result) {

			}
		};
		wd.addStep(p_mineral, 0, "Select Minerals");
		if (ga.getConstraint().equals(MpDb.doc.Sample_minerals)) {
			wd.addTabChangeListener(mineral_amount);
			wd.addStep(p_amounts, 1, "Enter Amounts");
		}
		return wd;
	}
	
	protected Collection get(final GenericAttribute ga) {
		return tree.get(ga);
	}

	
	protected void set(final MObject obj, final Object v) {
		tree.set(obj, v);
	}
	
	protected Object get(final Widget editWidget) {
		return tree.get(editWidget);
	}
	
	protected Collection get(final MObject obj) {
		return tree.get(obj);
	}

	/**
	 * Special version of contains that uses equals instead of hashcode
	 * 
	 * @param c
	 * @param o
	 * @return
	 */
	private Object containsObject(final Collection c, final Object o) {
		if (c  == null || o == null)
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
			final ServerOp dialog_finish = new ServerOp() {
				public void begin() {
					MineralAttribute.this
							.remakeContainer(MineralAttribute.this.obj);
				}
				public void onSuccess(final Object result) {

				}
			};
			p_mineral.setBean(this.obj);
			dialog.clearDialogFinishListeners();
			dialog.addDialogFinishListener(dialog_finish);
			dialog.show();
		}
	}

}