package edu.rpi.metpetdb.client.ui.input.attributes.specific;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.MineralDTO;
import edu.rpi.metpetdb.client.model.SampleMineralDTO;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.ValueInCollectionConstraint;
import edu.rpi.metpetdb.client.ui.input.DetailsPanel;
import edu.rpi.metpetdb.client.ui.input.WizardDialog;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TreeAttribute;
public class SearchMineralsAttribute extends GenericAttribute {

		private MObjectDTO obj;
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

		public Widget[] createDisplayWidget(final MObjectDTO obj) {
			return new Widget[] {
				
			};
		}

		public Widget[] createEditWidget(final MObjectDTO obj, final String id,
				final GenericAttribute ga) {
			return			tree.createEditWidget(obj, id, ga);
		}

		protected Collection get(final GenericAttribute ga) {
			return tree.get(ga);
		}

		protected void set(final MObjectDTO obj, final Object v) {
			tree.set(obj, v);
		}

		protected Object get(final Widget editWidget) {
			final Iterator itr = tree.getSelectedItems().iterator();
			final Set<Object> newSelectedItems = new HashSet();
			while (itr.hasNext()) {
				final MineralDTO mineral = (MineralDTO) itr.next();
				final SampleMineralDTO sampleMineral = new SampleMineralDTO();
				sampleMineral.setMineral(mineral);
				newSelectedItems.add(sampleMineral);
			}
			return newSelectedItems;	
		}

		protected Collection get(final MObjectDTO obj) {
			final Iterator itr = tree.getSelectedItems().iterator();
			final ArrayList newSelectedItems = new ArrayList();
			while (itr.hasNext()) {
				final MineralDTO mineral = (MineralDTO) itr.next();
				final SampleMineralDTO sampleMineral = new SampleMineralDTO();
				sampleMineral.setMineral(mineral);
				newSelectedItems.add(sampleMineral);
			}
			return newSelectedItems;		
		}
}
