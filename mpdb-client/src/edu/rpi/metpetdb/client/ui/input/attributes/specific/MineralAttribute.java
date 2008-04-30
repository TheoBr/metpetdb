package edu.rpi.metpetdb.client.ui.input.attributes.specific;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.MineralDTO;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.model.SampleMineralDTO;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.ValueInCollectionConstraint;
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
	private SampleDTO obj;
	private GenericAttribute ga;
	private final SimplePanel container;
	private TreeAttribute<MineralDTO> tree;
	private WizardDialog dialog;

	private DetailsPanel<SampleDTO> p_mineral;

	public MineralAttribute(final ObjectConstraint mc) {
		this(mc, 0);
	}
	public MineralAttribute(final ValueInCollectionConstraint mc,
			int maxMinerals) {
		this((PropertyConstraint) mc, maxMinerals);
	}
	public MineralAttribute(final PropertyConstraint mc, int maxMinerals) {
		super(mc);
		chooseMinerals = new Button("Choose Minerals...", this);
		container = new SimplePanel();
		tree = new TreeAttribute<MineralDTO>(mc, 4, maxMinerals);
	}

	public Widget[] createDisplayWidget(final MObjectDTO obj) {
		remakeContainer(obj);
		return new Widget[] {
			container
		};
	}

	private void remakeContainer(final MObjectDTO obj) {
		final Widget[] widgets = tree.createDisplayWidget(obj,
				((SampleDTO) obj).getMinerals());
		container.clear();
		for (int i = 0; i < widgets.length; ++i)
			container.add(widgets[i]);
	}

	@Override
	public Widget[] createEditWidget(final MObjectDTO obj, final String id,
			final GenericAttribute ga) {
		final VerticalPanel vp = new VerticalPanel();
		chooseMinerals.setEnabled(true);
		this.obj = (SampleDTO) obj;
		vp.add(chooseMinerals);
		final ArrayList<MineralDTO> tempList = new ArrayList<MineralDTO>();
		if (get(obj) != null)
			tempList.addAll(get(obj));
		tree.setSelectedItems(tempList);
		remakeContainer(obj);
		vp.add(container);
		this.ga = ga;
		return new Widget[] {
			vp
		};
	}

	private WizardDialog makeWizardDialog() {
		final WizardDialog wd = new WizardDialog();

		final MultipleObjectDetailsPanel<SampleMineralDTO> p_amounts = new MultipleObjectDetailsPanel<SampleMineralDTO>(
				new GenericAttribute[] {
					new TextAttribute(
							MpDb.doc.SampleMineral_Sample_minerals_amount)
				});

		final GenericAttribute mineral_attributes[] = {
			tree
		};

		p_mineral = new DetailsPanel<SampleDTO>(mineral_attributes, null, false);

		p_mineral.edit(this.obj);

		final ServerOp mineral_amount = new ServerOp() {
			public void begin() {
				// Before we edit the minerals we have to convert them to
				// SampleMineral
				final Iterator<MineralDTO> itr = (Iterator<MineralDTO>) tree
						.getSelectedItems().iterator();
				final ArrayList<SampleMineralDTO> newSelectedItems = new ArrayList<SampleMineralDTO>();
				while (itr.hasNext()) {
					final MineralDTO mineral = (MineralDTO) itr.next();
					final SampleMineralDTO object = containsObject(
							MineralAttribute.this.obj.getMinerals(), mineral);
					if (object != null) {
						newSelectedItems.add(object);
					} else {
						final SampleMineralDTO sampleMineral = new SampleMineralDTO();
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

	protected void set(final MObjectDTO obj, final Object v) {
		tree.set(obj, v);
	}

	protected Object get(final Widget editWidget) {
		return tree.get(editWidget);
	}

	protected Collection<MineralDTO> get(final MObjectDTO obj) {
		return tree.get(obj);
	}

	/**
	 * Special version of contains that uses equals instead of hashcode
	 * 
	 * @param c
	 * @param o
	 * @return
	 */
	private SampleMineralDTO containsObject(
			final Collection<SampleMineralDTO> c, final MineralDTO o) {
		if (c == null || o == null)
			return null;
		final Iterator<SampleMineralDTO> itr = c.iterator();
		while (itr.hasNext()) {
			final SampleMineralDTO object = itr.next();
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
