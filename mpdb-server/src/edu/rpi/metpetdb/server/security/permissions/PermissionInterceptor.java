package edu.rpi.metpetdb.server.security.permissions;

import java.io.Serializable;
import java.security.Principal;
import java.util.Collection;

import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.interfaces.HasOwner;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.security.permissions.principals.OwnerPrincipal;

public class PermissionInterceptor extends EmptyInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PermissionInterceptor() {

	}

	/**
	 * Method called just before an object is initialized. This method is called
	 * by "load" actions and queries, but there does not seem to be a way to
	 * distinguish between these cases.
	 * 
	 * @param entity
	 * 		uninitialized instance of the class to be loaded
	 * @param id
	 * 		the identifier of the new instance.
	 * @param state
	 * 		array of property values.
	 * @param propertyNames
	 * 		array of property names.
	 * @param types
	 * 		array of property types.
	 * @return true if the state was modified in any way.
	 * @throws CallbackException
	 * 		if a problem occured.
	 */
	public boolean onLoad(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) throws CallbackException {

		checkPermissions(entity, state, propertyNames, false);
		return super.onLoad(entity, id, state, propertyNames, types);
	}

	private void checkPermissions(Object entity, Object[] state,
			String[] propertyNames, boolean saving) {
		if (entity instanceof HasOwner) {
			if (MpDbServlet.currentReq() != null) {
				final Collection<Principal> principals = MpDbServlet.currentReq().principals;
				if (principals == null) {
					// throw new CallbackException(
					// "Invalid Subject, Please Log back in");
					// TODO remove this
					return;
				}
				if (!principals.contains(
						new OwnerPrincipal(getOwnerId(propertyNames, state)))) {
					throw new CallbackException(
							"Cannot load objects you don't own, we don't like to share.");
				}
				if (saving) {
					if (isPublic(propertyNames, state)) {
						throw new CallbackException(
								"Public data cannot be modified.");
					}
				}
			}
		}
	}

	private boolean isPublic(String[] propertyNames, Object[] state) {
		for (int i = 0; i < propertyNames.length; ++i) {
			if (propertyNames[i].equals("publicData")) {
				return Boolean.parseBoolean(state[i].toString());
			}
		}
		return false;
	}

	private int getOwnerId(String[] propertyNames, Object[] state) {
		int ownerId = 0;
		for (int i = 0; i < propertyNames.length; ++i) {
			if (propertyNames[i].equals("owner")) {
				ownerId = ((User) state[i]).getId();
				break;
			}
		}
		return ownerId;
	}

	/**
	 * Method called when an object is detected to be dirty, during a flush.
	 * This method is called by "modify" actions.
	 * 
	 * @param entity
	 * 		object to be updated in the database.
	 * @param id
	 * 		the identifier of the instance.
	 * @param currentState
	 * 		array of property values.
	 * @param previousState
	 * 		cached array of property values.
	 * @param propertyNames
	 * 		array of property names.
	 * @param types
	 * 		array of property types.
	 * @return true if the currentState was modified in any way.
	 * @throws CallbackException
	 * 		if a problem occured.
	 */
	public boolean onFlushDirty(Object entity, Serializable id,
			Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) throws CallbackException {
		checkPermissions(entity, previousState, propertyNames, true);
		return super.onFlushDirty(entity, id, currentState, previousState,
				propertyNames, types);
	}

	/**
	 * Method called before an object is saved. This method is called by
	 * "create" actions.
	 * 
	 * @param entity
	 * 		object to be saved to the database.
	 * @param id
	 * 		the identifier of the instance.
	 * @param state
	 * 		array of property values.
	 * @param propertyNames
	 * 		array of property names.
	 * @param types
	 * 		array of property types.
	 * @return true if the user modified the state in any way.
	 * @throws CallbackException
	 * 		if a problem occured.
	 */
	public boolean onSave(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) throws CallbackException {
		checkPermissions(entity, state, propertyNames, true);
		return super.onSave(entity, id, state, propertyNames, types);
	}

	/**
	 * Method called before an object is delete. This method is called by
	 * "delete" actions.
	 * 
	 * @param entity
	 * 		object to be deleted from the database.
	 * @param id
	 * 		the identifier of the instance.
	 * @param state
	 * 		array of property values.
	 * @param propertyNames
	 * 		array of property names.
	 * @param types
	 * 		array of property types.
	 * @throws CallbackException
	 * 		if a problem occured.
	 */
	public void onDelete(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) throws CallbackException {

		super.onDelete(entity, id, state, propertyNames, types);
	}
}
