package edu.rpi.metpetdb.server.dao.permissions;

import java.io.Serializable;
import java.security.AccessController;

import javax.security.auth.Subject;

import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

public class PermissionInterceptor extends EmptyInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PermissionInterceptor() {
		Subject subject = Subject.getSubject(AccessController.getContext());
		
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

		String className = entity.getClass().getName();
		SecurityManager sm = System.getSecurityManager();
		if (sm != null) {
			try {
				//sm.checkPermission();
			} catch (SecurityException e) {
				throw new CallbackException(e.getMessage(), e);
			}
		}
		return super.onLoad(entity, id, state, propertyNames, types);
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
