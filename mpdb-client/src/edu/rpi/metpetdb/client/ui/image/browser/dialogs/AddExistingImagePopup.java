package edu.rpi.metpetdb.client.ui.image.browser.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.gen2.table.event.client.PageLoadEvent;
import com.google.gwt.gen2.table.event.client.PageLoadHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.image.browser.ImageBrowserImageList;
import edu.rpi.metpetdb.client.ui.image.browser.ImageOnGridContainer;

public class AddExistingImagePopup extends PopupPanel {

	public AddExistingImagePopup(final Widget sender,
			final ServerOp<Collection<Image>> r, final Subsample subsample,
			final Map<Image, ImageOnGridContainer> imagesOnGrid) {
		super(true);
		final VerticalPanel vp = new VerticalPanel();
		this.setPopupPosition(sender.getAbsoluteLeft(),
				sender.getAbsoluteTop() + 20);
		final ImageBrowserImageList list = new ImageBrowserImageList() {
			@Override
			public void update(final PaginationParameters p,
					final AsyncCallback<Results<Image>> ac) {
				MpDb.image_svc.allImages(subsample.getId(), p, ac);
			}

			@Override
			public void getAllIds(AsyncCallback<Map<Object, Boolean>> ac) {
				MpDb.image_svc.allImageIds(subsample.getId(), ac);
			}

		};
		list.addPageLoadHandler(new PageLoadHandler() {
			public void onPageLoad(PageLoadEvent event) {
				// Color images already on the image map gray
				for (int i = 0; i < list.getRowCount(); ++i) {
					final Image image = list.getRowValue(i);
					if (imagesOnGrid.containsKey(image)) {
						list.getRowFormatter().setVisible(i, false);
					}
				}
			}
		});
		vp.add(new Button("Add Selected", new ClickListener() {
			public void onClick(final Widget sender) {
				new ServerOp<List<Image>>() {
					public void begin() {
						List<Long> ids = new ArrayList<Long>();
						for (Object id : list.getSelectedValues()) {
							ids.add((Long) id);
						}
						MpDb.image_svc.details(ids, this);
					}

					public void onSuccess(List<Image> result) {
						r.onSuccess(result);
						((PopupPanel) sender.getParent().getParent()).hide();
					}

					public void onFailure(Throwable e) {
						((PopupPanel) sender.getParent().getParent()).hide();
						super.onFailure(e);
					}
				}.begin();
			}
		}));
		final ScrollPanel scrollPanel = new ScrollPanel(list);
		scrollPanel.setStyleName("mpdb-SubsampleScrollPanel");
		vp.add(scrollPanel);
		vp.add(new Button("Hide", new ClickListener() {
			public void onClick(final Widget sender) {
				((PopupPanel) sender.getParent().getParent()).hide();
			}
		}));
		this.setStyleName("mpdb-AddExistingSubsamplePopup");
		DOM.setStyleAttribute(this.getElement(), "zIndex", "1000");
		setWidget(vp);
	}
}
