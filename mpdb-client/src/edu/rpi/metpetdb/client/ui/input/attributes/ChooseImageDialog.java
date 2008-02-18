package edu.rpi.metpetdb.client.ui.input.attributes;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.ImageDTO;
import edu.rpi.metpetdb.client.model.SubsampleDTO;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.dialogs.MDialogBox;
import edu.rpi.metpetdb.client.ui.image.browser.ImageList;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.AddImageWizard;


public class ChooseImageDialog extends MDialogBox implements ClickListener {
	
	private final ServerOp continuation;
	private Button cancel;
	private Button ok;
	private Button newImage;
	private ImageList list;
	private SubsampleDTO s;
	
	
	public ChooseImageDialog(final ServerOp r, final SubsampleDTO s) {
		final FlowPanel fp = new FlowPanel();
		continuation = r;
		
		list = new ImageList(s.getId(), true,true);
		
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
						s.addImage((ImageDTO) result);
						if (continuation != null)
							continuation.onSuccess(result);
						ChooseImageDialog.this.hide();
					}
				}
			}.begin();
		}
	}

}
