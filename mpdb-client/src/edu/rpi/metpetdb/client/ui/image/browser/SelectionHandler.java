package edu.rpi.metpetdb.client.ui.image.browser;

import java.util.ArrayList;
import java.util.List;

public class SelectionHandler {
	private final List<ImageOnGridContainer> selectedImages;
	private final List<List<ImageOnGridContainer>> groups;
	private final ImageBrowserDetails imageBrowser;
	
	public SelectionHandler(final ImageBrowserDetails imageBrowser){
		selectedImages = new ArrayList<ImageOnGridContainer>();
		groups = new ArrayList<List<ImageOnGridContainer>>();
		this.imageBrowser = imageBrowser;
	}
	
	public List<ImageOnGridContainer> getSelectedImages() {
		return selectedImages;
	}
	
	public void addNewImage(final ImageOnGridContainer newImage) {
		final List<ImageOnGridContainer> newGroup = new ArrayList<ImageOnGridContainer>();
		newGroup.add(newImage);
		groups.add(newGroup);
	}
	
	public void removeImage(final ImageOnGridContainer iogToRemove) {
		selectedImages.remove(iogToRemove);
		List<ImageOnGridContainer> group = getGroupByImage(iogToRemove);
		if (group != null) {
			group.remove(iogToRemove);
			if (group.isEmpty()) {
				groups.remove(group);
			}
		}
		updateGroupOptions();
	}
	
	public void selectImage(final ImageOnGridContainer iogToSelect) {
		List<ImageOnGridContainer> groupToSelect = getGroupByImage(iogToSelect);
		if (groupToSelect != null && !iogToSelect.isLocked()) {
			for (ImageOnGridContainer iog : groupToSelect) {
				if (!selectedImages.contains(iog))
					selectedImages.add(iog);
			}
			addGroupSelectCSS(groupToSelect);
		}
		updateGroupOptions();
	}
	
	public void unselectImage(final ImageOnGridContainer iogToUnselect) {
		List<ImageOnGridContainer> groupToUnselect = getGroupByImage(iogToUnselect);
		if (groupToUnselect != null) {
			selectedImages.removeAll(groupToUnselect);
			removeGroupSelectCSS(groupToUnselect);
		}
		updateGroupOptions();
	}
	
	public void groupSelectedImages() {
		final List<ImageOnGridContainer> newGroup = new ArrayList<ImageOnGridContainer>();
		newGroup.addAll(selectedImages);
		for (ImageOnGridContainer iog : selectedImages) {
			List<ImageOnGridContainer> groupToRemove = getGroupByImage(iog);
			if (groupToRemove != null) {
				groups.remove(groupToRemove);
			}
		}
		groups.add(newGroup);
		updateGroupOptions();
	}
	
	public void ungroupSelectedImages() {
		for (ImageOnGridContainer iog : selectedImages) {
			List<ImageOnGridContainer> groupToRemove = getGroupByImage(iog);
			if (groupToRemove != null) {
				groups.remove(groupToRemove);
			}
		}
		for (ImageOnGridContainer iog : selectedImages) {
			addNewImage(iog);
		}
		updateGroupOptions();
	}
	
	public List<ImageOnGridContainer> getGroupByImage(final ImageOnGridContainer iogToFind) {
		for (List<ImageOnGridContainer> group : groups) {
			for (ImageOnGridContainer iog : group) {
				if (iog == iogToFind) {
					return group;
				}
			}
		}
		return null;
	}
	
	public void unselectAll() {
		for (ImageOnGridContainer iog : selectedImages) {
			iog.getImageContainer().removeStyleDependentName("selected");
		}
		selectedImages.clear();	
		updateGroupOptions();
	}
	
	public void removeGroupSelectCSS(final List<ImageOnGridContainer> group){
		for (ImageOnGridContainer iog : group) {
			iog.getImageContainer().removeStyleDependentName("selected");
		}
	}
	
	public void addGroupSelectCSS(final List<ImageOnGridContainer> group){
		for (ImageOnGridContainer iog : group) {
			iog.getImageContainer().addStyleDependentName("selected");
		}
	}
	
	public boolean isImageSelected(final ImageOnGridContainer iog) {
		return iog.getImageContainer().getStyleName().contains("selected");
	}
	
	public void updateGroupOptions() {
		if (selectedImages.size() < 2) {
			imageBrowser.setGroupEnabled(false);
			imageBrowser.setUngroupEnabled(false);
		} else {
			List<ImageOnGridContainer> group = getGroupByImage(selectedImages.get(0));
			if (group.containsAll(selectedImages)) {
				imageBrowser.setGroupEnabled(false);
				imageBrowser.setUngroupEnabled(true);
			} else {
				imageBrowser.setGroupEnabled(true);
				imageBrowser.setUngroupEnabled(false);
			}
		}
	}
	
	public boolean rectangleIntersection(final ImageOnGridContainer iog, 
			final int top, final int bottom, final int left, final int right) {
		if (iog.getImageContainer().getWidget(1).getAbsoluteTop()-imageBrowser.getGrid().getAbsoluteTop() > bottom) return false;
		if (iog.getImageContainer().getWidget(1).getAbsoluteTop()-imageBrowser.getGrid().getAbsoluteTop() + iog.getImageContainer().getWidget(1).getOffsetHeight() < top) return false;
		if (iog.getImageContainer().getWidget(1).getAbsoluteLeft()-imageBrowser.getGrid().getAbsoluteLeft() > right) return false;
		if (iog.getImageContainer().getWidget(1).getAbsoluteLeft()-imageBrowser.getGrid().getAbsoluteLeft() + iog.getImageContainer().getWidget(1).getOffsetWidth() < left) return false;
		return true;
	}
	
	public void selectRectangle(final Point p1, final Point p2) {
		// Determine top, left, bottom, and right
		final int top;
		final int bottom;
		final int left;
		final int right;

		if (p1.x > p2.x) {
			right = (int) p1.x;
			left = (int) p2.x;
		} else {
			right = (int) p2.x;
			left = (int) p1.x;
		}
		if (p1.y > p2.y) {
			top = (int) p2.y;
			bottom = (int) p1.y;
		} else {
			top = (int) p1.y;
			bottom = (int) p2.y;
		}

		// select the groups that are within are rectangle
		for (List<ImageOnGridContainer> group : groups) {
			for (ImageOnGridContainer iog : group) {
				if (rectangleIntersection(iog,top,bottom,left,right)) {
					selectImage(iog);
					break;
				}
			}
		}
	}
}
