package edu.rpi.metpetdb.client.ui.input;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.UserDTO;
import edu.rpi.metpetdb.client.ui.FormOp;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.user.UsesCurrentUser;

public abstract class ObjectEditorPanel extends DetailsPanel
		implements
			ClickListener,
			UsesCurrentUser {
	private final Button edit;
	private final Button delete;
	 final Button cancel;
	 final Button save;

	protected ObjectEditorPanel(final GenericAttribute[] atts,
			final String header, final String description) {
		edit = new Button(LocaleHandler.lc_text.buttonEdit(), this);
		cancel = new Button(LocaleHandler.lc_text.buttonCancel(), this);
		save = new Submit(LocaleHandler.lc_text.buttonSave(), this);
		delete = new Button(LocaleHandler.lc_text.buttonDelete(), this);

		edit.setVisible(false);
		cancel.setVisible(false);
		save.setVisible(false);
		delete.setVisible(false);

		init(atts, new Button[]{edit, cancel, save, delete},true,true);
	}

	protected ObjectEditorPanel(final GenericAttribute[] atts) {
		this(atts, "", "");
	}

	public void onClick(final Widget sender) {
		if (save == sender)
			doSave();
		else if (edit == sender)
			edit(getBean());
		else if (cancel == sender)
			doCancel();
		else if (delete == sender)
			doDelete();
	}

	private void removePrimaryStyle() {
		cancel.removeStyleName("btnPrimary");
		edit.removeStyleName("btnPrimary");
		save.removeStyleName("btnPrimary");
		delete.removeStyleName("btnPrimary");
	}

	private void addSecondaryStyle() {
		cancel.removeStyleName("btnSecondary");
		edit.removeStyleName("btnSecondary");
		save.removeStyleName("btnSecondary");
		delete.removeStyleName("btnSecondary");
	}

	private void setActiveButton(final Button b) {
		removePrimaryStyle();
		addSecondaryStyle();
		b.addStyleName("btnPrimary");
		b.removeStyleName("btnSecondary");
	}

	public void onCurrentUserChanged(final UserDTO whoIsIt)
			throws LoginRequiredException {
		if (edit.isVisible() || (!cancel.isVisible() && !save.isVisible())) {
			// We are in 'show' mode. The user might be able to do an
			// edit now, or might not. So update the edit button.
			//
			final boolean ce = getBean() != null && canEdit();
			edit.setVisible(ce);
			edit.setEnabled(ce);
			setActiveButton(edit);
			return;
		}

		if (!canEdit()) {
			// The user cannot be editing the object. If cancel is
			// visible we might be able to drop back to show mode;
			// otherwise we have to abort this page view.
			//
			if (cancel.isVisible())
				doCancel();
			else if (save.isVisible())
				throw new LoginRequiredException();
		}
	}
	
	void doDelete() {
		setEnabled(false);
		delete();
	}

	void doCancel() {
		setEnabled(false);
		load();
	}

	void doSave() {
		new FormOp(this) {
			protected void onSubmit() {
				saveBean(this);
			}
			public void onSuccess(final Object result) {
				onSaveCompletion((MObjectDTO) result);
			}
		}.begin();
	}
	
	public void delete() {
		new ServerOp() {
			public void begin() {
				deleteBean(this);
			}
			public void onSuccess(final Object result) {
				onDeleteCompletion((MObjectDTO) result);
			}
		}.begin();
	}

	public void load() {
		new ServerOp() {
			public void begin() {
				loadBean(this);
			}
			public void onSuccess(final Object result) {
				onLoadCompletion((MObjectDTO) result);
			}
		}.begin();
	}

	public void show(final MObjectDTO obj) {
		super.show(obj);
		final boolean ed = canEdit();
		final boolean del = canDelete();
		edit.setVisible(ed);
		edit.setEnabled(ed);
		delete.setVisible(del);
		delete.setEnabled(del);
		cancel.setVisible(false);
		save.setVisible(false);
		setActiveButton(edit);
	}

	public void edit(final MObjectDTO obj) {
		final boolean nn = !obj.mIsNew();
		super.edit(obj);
		edit.setVisible(false);
		cancel.setVisible(nn);
		cancel.setEnabled(nn);
		save.setVisible(true);
		save.setEnabled(true);
		setActiveButton(save);
	}
	
	protected abstract void deleteBean(final AsyncCallback ac);

	protected abstract void loadBean(final AsyncCallback ac);

	protected boolean canEdit() {
		return false;
	}
	protected boolean canDelete() {
		return canEdit();
	}
	protected void saveBean(final AsyncCallback ac) {
		throw new UnsupportedOperationException();
	}
	protected void onSaveCompletion(final MObjectDTO result) {
		ObjectEditorPanel.this.show(result);
	}
	protected void onLoadCompletion(final MObjectDTO result) {
		ObjectEditorPanel.this.show(result);
	}
	protected void onDeleteCompletion(final Object result) {
		Window.alert("deleted");
	}
}
