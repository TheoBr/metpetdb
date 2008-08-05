package edu.rpi.metpetdb.client.ui.image.browser;

import java.util.HashSet;
import java.util.Iterator;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.ui.left.side.LeftColWidget;
import edu.rpi.metpetdb.client.ui.left.side.UsesLeftColumn;
import edu.rpi.metpetdb.client.ui.widgets.MUnorderedList;

public class LeftSideLayer extends LeftColWidget implements UsesLeftColumn {

	private final MUnorderedList ul;
	private final LayerDragMouseListener layerDragger;
	private final HashSet<LayerItem> layerItems;

	public LeftSideLayer(final String subsampleName) {
		super(subsampleName + "'s Layers");
		layerItems = new HashSet<LayerItem>();
		ul = new MUnorderedList();
		this.add(ul);
		// MetPetDBApplication.registerPageWatcher(this);
		layerDragger = new LayerDragMouseListener(ul, layerItems);
		ul.addMouseListener(layerDragger);
	}

	public void registerImage(final ImageOnGrid iog) {
		final SimplePanel container = new SimplePanel();
		final String checkBoxText = iog.getIog().getImage().getFilename()
				.equals("") ? iog.getIog().getImage().getChecksum() : iog
				.getIog().getImage().getFilename();
		final CheckBox checkBox = new CheckBox(checkBoxText);
		checkBox.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				if (!checkBox.isChecked()) {
					iog.getImageContainer()
							.setStyleName("imageContainerHidden");
				} else {
					if (iog.isMenuHidden())
						iog.getImageContainer().setStyleName(
								"imageContainerNoMenu");
					else
						iog.getImageContainer().setStyleName("imageContainer");
				}
			}
		});
		checkBox.setChecked(true);
		container.setWidget(checkBox);

		ul.add(container, insertLayer(iog));
	}

	public int insertLayer(final ImageOnGrid iog) {
		final Iterator<LayerItem> itr = layerItems.iterator();
		int newIndex = layerItems.size();
		while (itr.hasNext()) {
			final LayerItem layerItem = itr.next();
			if (layerItem.getImageOnGrid().getIog().getZorder() > iog.getIog()
					.getZorder()) {
				if (layerItem.getIndex() < newIndex) {
					// newIndex = layerItem.getIndex();
				}
				// layerItem.decrementIndex();
			} else {
				--newIndex;
				layerItem.incrementIndex();
			}
		}
		final LayerItem newItem = new LayerItem(iog, newIndex);
		layerItems.add(newItem);
		return newIndex;
	}

	public void removeImage(final ImageOnGrid iog) {
		final LayerItem removeItem = findItemWithIog(iog);
		if (removeItem != null) {
			final Iterator<LayerItem> itr = layerItems.iterator();
			final int removeItemIndex = removeItem.getIndex();
			while (itr.hasNext()) {
				final LayerItem layerItem = (LayerItem) itr.next();
				if (layerItem.getIndex() > removeItemIndex) {
					layerItem.decrementIndex();
				}
			}
			layerItems.remove(removeItem);
			ul.remove(removeItem.getIndex());
		}
	}

	private LayerItem findItemWithIog(final ImageOnGrid iog) {
		final Iterator<LayerItem> itr = layerItems.iterator();
		while (itr.hasNext()) {
			final LayerItem layerItem = (LayerItem) itr.next();
			if (layerItem.getImageOnGrid().equals(iog)) {
				return layerItem;
			}
		}
		return null;
	}

	public void onPageChanged() {
		// MetPetDBApplication.removeFromLeft(this);
	}

	public class LayerItem {
		private ImageOnGrid iog;
		private int index;

		public LayerItem(final ImageOnGrid imageOnGrid, final int i) {
			iog = imageOnGrid;
			index = i;
		}

		public ImageOnGrid getImageOnGrid() {
			return iog;
		}
		public void setImageOnGrid(final ImageOnGrid imageOnGrid) {
			iog = imageOnGrid;
		}

		public int getIndex() {
			return index;
		}
		public void setIndex(final int i) {
			index = i;
		}

		public void incrementIndex() {
			++index;
		}
		public void decrementIndex() {
			--index;
		}
	}

}
