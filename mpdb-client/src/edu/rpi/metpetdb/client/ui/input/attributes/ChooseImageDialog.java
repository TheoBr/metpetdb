package edu.rpi.metpetdb.client.ui.input.attributes;

import com.google.gwt.gen2.table.client.SelectionGrid.SelectionPolicy;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.commands.MCommand;
import edu.rpi.metpetdb.client.ui.dialogs.MDialogBox;
import edu.rpi.metpetdb.client.ui.image.browser.ImageBrowserImageList;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.image.AddImageWizard;

public class ChooseImageDialog extends MDialogBox implements ClickListener {

	private final MCommand<Image> continuation;
	private Button cancel;
	private Button ok;
	private Button newImage;
	private ImageBrowserImageList list;
	private Subsample s;

	public ChooseImageDialog(final MCommand<Image> r, final Subsample s) {
		final FlowPanel fp = new FlowPanel();
		continuation = r;

		list = new ImageBrowserImageList() {
			@Override
			public void update(PaginationParameters p,
					AsyncCallback<Results<Image>> ac) {
				MpDb.image_svc.allImages(s.getId(), p, ac);
			}
		};
		list.getDataTable().setSelectionPolicy(SelectionPolicy.RADIO);
		fp.add(list);

		cancel = new Button(LocaleHandler.lc_text.buttonCancel(), this);
		ok = new Button("Ok", this);
		newImage = new Button("New Image", this);

		this.s = s;

		fp.add(newImage);
		fp.add(ok);
		fp.add(cancel);
		this.setWidget(fp);
	}

	public void onClick(final Widget sender) {
		if (sender == cancel) {
			this.hide();
		} else if (sender == ok) {
			if (continuation != null && list.getSelectedValues().size() > 0)
				continuation.execute(list.getSelectedValues().get(0));
			this.hide();
		} else if (sender == newImage) {
			new AddImageWizard(new MCommand<Image>() {
				@Override
				public void execute(Image result) {
					s.addImage(result);
					if (continuation != null)
						continuation.execute(result);
					hide();
				}
			}).show();
		}
	}

}
