package edu.rpi.metpetdb.client.ui.user;

import java.util.Collection;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.Role;
import edu.rpi.metpetdb.client.model.RoleChange;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.commands.MCommand;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.input.ObjectEditorPanel;
import edu.rpi.metpetdb.client.ui.input.OnEnterPanel;
import edu.rpi.metpetdb.client.ui.input.attributes.AsyncListboxAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;

/**
 * Interface for a user to request a role change
 * 
 * @author anthony
 * 
 */
public class RequestRoleChange extends FlowPanel {
	private static MCommand<Object> onRoleSelected = new MCommand<Object>() {
		@Override
		public void execute(final Object object) {
			if (object instanceof Role) {
				new ServerOp<Collection<User>>() {

					@Override
					public void begin() {
						MpDb.user_svc.getEligableSponsors((Role) object, this);
					}

					public void onSuccess(Collection<User> result) {
						sponsors.setValues(result);
					}
					
				}.begin();
			}
		}
	};
	private static AsyncListboxAttribute<Role> roles = new AsyncListboxAttribute<Role>(
			MpDb.doc.RoleChange_role, onRoleSelected);
	private static AsyncListboxAttribute<User> sponsors = new AsyncListboxAttribute<User>(
			MpDb.doc.RoleChange_sponsor);
	private static GenericAttribute[] attributes = {
			new TextAttribute(MpDb.doc.RoleChange_user).setReadOnly(true),
			roles, sponsors,
			new TextAttribute(MpDb.doc.RoleChange_requestReason),
	};

	private final ObjectEditorPanel<RoleChange> p_roleChange;
	private int projectId;

	public RequestRoleChange() {
		p_roleChange = new ObjectEditorPanel<RoleChange>(attributes,
				LocaleHandler.lc_text.addProject(), LocaleHandler.lc_text
						.addProjectDescription()) {
			protected void loadBean(final AsyncCallback<RoleChange> ac) {
				MpDb.user_svc.getRoleChange(getBean() != null
						&& !getBean().mIsNew() ? getBean().getId() : projectId,
						ac);
			}
			protected void saveBean(final AsyncCallback<RoleChange> ac) {
				MpDb.user_svc.saveRoleChange(getBean(), ac);
			}
			protected void deleteBean(final AsyncCallback<Object> ac) {
				// role changes cannot be deleted
			}
			protected boolean canEdit() {
				// TODO temporary while testing permissions
				// return MpDb.isCurrentUser(((Project) getBean()).getOwner());
				return true;
			}
		};
		add(new OnEnterPanel.ObjectEditor(p_roleChange));
	}

	public RequestRoleChange showById(final int id) {
		projectId = id;
		p_roleChange.load();
		return this;
	}

	public RequestRoleChange createNew() {
		final RoleChange p = new RoleChange();
		p.setUser(MpDb.currentUser());
		// fetch updated roles
		new ServerOp<Collection<Role>>() {
			@Override
			public void begin() {
				MpDb.user_svc.getEligableRoles(MpDb.currentUser().getRank(), this);
			}

			public void onSuccess(Collection<Role> result) {
				roles.setValues(result);
				p_roleChange.edit(p);
			}
		}.begin();
		return this;
	}
}
