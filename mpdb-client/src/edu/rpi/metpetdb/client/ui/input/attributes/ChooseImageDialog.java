package edu.rpi.metpetdb.client.ui.input.attributes;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.dialogs.MDialogBox;
import edu.rpi.metpetdb.client.ui.image.browser.ImageList;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.AddImageWizard;

public class ChooseImageDialog extends MDialogBox implements ClickListener {

	private final ServerOp continuation;
	private Button cancel;
	private Button ok;
	private Button newImage;
	private ImageList list;
	private Subsample s;

	public ChooseImageDialog(final ServerOp r, final Subsample s) {
		final FlowPanel fp = new FlowPanel();
		continuation = r;

		list = new ImageList(s.getId(), true, true);

		fp.add(list);

		cancel = new Button("Cancel", this);
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
			if (continuation != null && list.getSelectedImages().size() > 0)
				continuation.onSuccess(list.getSelectedImages().toArray()[0]);
			this.hide();
		} else if (sender == newImage) {
			new ServerOp() {
				public void begin() {
					new AddImageWizard(this).show();
				}
				public void onSuccess(final Object result) {
					if (result != null) {
						s.addImage((Image) result);
						if (continuation != null)
							continuation.onSuccess(result);
						ChooseImageDialog.this.hide();
					}
				}
			}.begin();
		}
	}

}
