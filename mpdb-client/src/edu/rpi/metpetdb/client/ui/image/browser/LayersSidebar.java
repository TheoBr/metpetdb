package edu.rpi.metpetdb.client.ui.image.browser;

import java.util.HashSet;
import java.util.Iterator;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.ui.sidebar.Sidebar;
import edu.rpi.metpetdb.client.ui.sidebar.UsesSidebar;
import edu.rpi.metpetdb.client.ui.widgets.MHtmlList;
import edu.rpi.metpetdb.client.ui.widgets.MText;

public class LayersSidebar extends Sidebar implements UsesSidebar {

	private final MHtmlList ul;
	private final LayerDragMouseListener layerDragger;
	private final HashSet<LayerItem> layerItems;

	public LayersSidebar(final String subsampleName) {
		add(new MText(subsampleName + "'s Layers", "h1"));
		layerItems = new HashSet<LayerItem>();
		ul = new MHtmlList();
		this.add(ul);
		// MetPetDBApplication.registerPageWatcher(this);
		layerDragger = new LayerDragMouseListener(ul, layerItems);
		ul.addMouseListener(layerDragger);
	}

	public void registerImage(final ImageOnGridContainer iog) {
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

	public int insertLayer(final ImageOnGridContainer iog) {
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

	public void removeImage(final ImageOnGridContainer iog) {
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

	private LayerItem findItemWithIog(final ImageOnGridContainer iog) {
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
		private ImageOnGridContainer iog;
		private int index;

		public LayerItem(final ImageOnGridContainer imageOnGrid, final int i) {
			iog = imageOnGrid;
			index = i;
		}

		public ImageOnGridContainer getImageOnGrid() {
			return iog;
		}
		public void setImageOnGrid(final ImageOnGridContainer imageOnGrid) {
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
