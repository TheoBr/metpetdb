package edu.rpi.metpetdb.client.ui.widgets.paging;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Widget that allows the user to choose a page size
 * @author anthony
 *
 */
public abstract class PageSizeChooser extends HTMLPanel {
	
	private ListBox pageSizeChoice;
	private Integer[] choices = {
			10, 25, 75
	};
	
	public PageSizeChooser() {
		super("Show <span id=\"perpage-choice\"></span> per page.");
		pageSizeChoice = new ListBox();
		setPageSizeChoices(choices);
		pageSizeChoice.addChangeListener(new ChangeListener() {
			public void onChange(Widget sender) {
				try {
					setPageSizeImpl(Integer.parseInt(pageSizeChoice
							.getItemText(pageSizeChoice.getSelectedIndex())));
				} catch (Exception e) {

				} finally {

				}
			}
		});
		addAndReplaceElement(pageSizeChoice, "perpage-choice");
		setStyleName("perpage");
	}
	
	public void setPageSizeChoices(Integer[] choices) {
		pageSizeChoice.clear();
		for (Integer choice : choices) {
			pageSizeChoice.addItem(choice.toString());
		}
		this.choices = choices;
	}
	
	public void setSelectedPageSize(int pageSize) {
		for(int i = 0;i<choices.length;++i) {
			if (pageSize == choices[i]) {
				pageSizeChoice.setSelectedIndex(i);
				break;
			}
		}
	}
	
	
	public abstract void setPageSizeImpl(int newSize);

}
