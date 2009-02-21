package edu.rpi.metpetdb.server.security.permissions;

import java.io.Serializable;
import java.security.Principal;
import java.util.Collection;

import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import edu.rpi.metpetdb.client.error.security.AccountNotEnabledException;
import edu.rpi.metpetdb.client.error.security.CannotLoadPendingRoleException;
import edu.rpi.metpetdb.client.error.security.CannotLoadPrivateDataException;
import edu.rpi.metpetdb.client.error.security.CannotLoadPublicDataException;
import edu.rpi.metpetdb.client.error.security.CannotSaveDataException;
import edu.rpi.metpetdb.client.error.security.NotOwnerException;
import edu.rpi.metpetdb.client.error.security.UnableToModifyPublicDataException;
import edu.rpi.metpetdb.client.model.PendingRole;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.interfaces.HasOwner;
import edu.rpi.metpetdb.client.model.interfaces.HasSample;
import edu.rpi.metpetdb.client.model.interfaces.HasSubsample;
import edu.rpi.metpetdb.client.model.interfaces.PublicData;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.security.Action;
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
	 *            uninitialized instance of the class to be loaded
	 * @param id
	 *            the identifier of the new instance.
	 * @param state
	 *            array of property values.
	 * @param propertyNames
	 *            array of property names.
	 * @param types
	 *            array of property types.
	 * @return true if the state was modified in any way.
	 * @throws CallbackException
	 *             if a problem occured.
	 */
	public boolean onLoad(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) throws CallbackException {

		checkPermissions(id, entity, state, propertyNames, false, false);
		return super.onLoad(entity, id, state, propertyNames, types);
	}

	private void checkPermissions(Serializable id, Object entity, Object[] state,
			String[] propertyNames, boolean saving, boolean creating) {
		if (MpDbServlet.currentReq() != null) {
			final Collection<Principal> principals = MpDbServlet.currentReq().principals;
			final int usersRank;
			boolean enabled = false;
			if (MpDbServlet.currentReq().user != null && MpDbServlet.currentReq().user.getRole() != null) {
				usersRank = MpDbServlet.currentReq().user.getRole()
					.getRank();
				enabled = MpDbServlet.currentReq().user.getEnabled();
			}
			else
				usersRank = -1;
			boolean isPublic = false;
			if (entity instanceof PublicData) {
				if (isPublic(propertyNames, state)) {
					isPublic = true;
				}
			}
			if (!saving) {
				if (isPublic) {
					if (RoleDefinitions.roleDefinitions.get(usersRank).contains(Privilages.LOAD_PUBLIC_DATA)) {
						return;
					} else {
						throw new CallbackException(new CannotLoadPublicDataException());
					}
				} else {
					//the object is either not public or not an instance of public/private, so check for sure
					if (entity instanceof PublicData) {
						//see if they are allowed to load private data
						if (RoleDefinitions.roleDefinitions.get(usersRank).contains(Privilages.LOAD_PRIVATE_DATA)) {
							//check if they have to be the owner to load the private data
							if (RoleDefinitions.roleDefinitions.get(usersRank).contains(Privilages.LOAD_OTHERS_PRIVATE_DATA)) {
								//they can load any ones private data so let it fly
								return; 
							} else {
								//check they are the owner before trying to load it
								if (entity instanceof HasOwner) {
									if (principals.contains(new OwnerPrincipal(getOwnerId(
											propertyNames, state)))) {
										//it is private and they are the owner, so let them load it
										return;
									} else {
										throw new CallbackException(new NotOwnerException());
									}
								} else {
									//hmm, object is private data but without an owner
									//this is the case of loading private image maps
									throw new CallbackException("Attempted to load an object that is considered private data, but the object has no owner.");
								}
							}
						} else {
							//user cannot load private data
							throw new CallbackException(new CannotLoadPrivateDataException());
						}
					} else {
						//this is the case in which the object has no public/private modifier
						//this includes objects like Images, RockType, Region, Mineral, etc.
						if (entity instanceof HasSample || entity instanceof HasSubsample) {
							//means we are trying to load something that is a child of a sample/subsample
							//and it does not have a public/private modifier
							//this means we have to load the parent object to see if it is allowed to be loaded
							if (entity instanceof HasSample) {
								//
								int sampleId = getSampleId(propertyNames, state);
								
							} else {
								//has subsample
							}
						} else if (entity instanceof HasOwner) {
							//object has an owner so check if it can be loaded by the current user
							//TODO comments fall under this category when they get implemented
							if (principals.contains(new OwnerPrincipal(getOwnerId(
									propertyNames, state)))) {
								//they are the owner so let them load it
								return;
							} else {
								throw new CallbackException(new NotOwnerException());
							}
						} else if (entity instanceof User) {
							//allows allow loading of users
							return;
						}else if (entity instanceof PendingRole) {
							//since pending roles don't really have owners we put them in their
							//own category and make it so only the sponsor and the user can load them
							if (principals.contains(new OwnerPrincipal(getIdOfUser("sponsor", propertyNames, state)))
									|| principals.contains(new OwnerPrincipal(getIdOfUser("user", propertyNames, state)))) {
								return;
							} else {
								throw new CallbackException(new CannotLoadPendingRoleException());
							}
						} else {
							//this is the case when we are loading stuff like RockType, Mineral, Oxide, Element
							//basic 3d party support objects that are not associated with anyone, so we always allow
							//them to be loaded
							return;
						}
					}
				}
			} else {
				//user is trying to save something, if they are creating an account let it go
				if (enabled || (creating && entity instanceof User)) {
					if (isPublic && !creating) {
						//public data cannot be saved (except when creating it initially)
						throw new CallbackException(new UnableToModifyPublicDataException());
					} else {
						if (RoleDefinitions.roleDefinitions.get(usersRank).contains(Privilages.SAVE_PRIVATE_DATA)) {
							//TODO check saving of other user's private data
							return; //let it fly
						} else {
							//let email password save the user instance
							if (Action.EMAIL_PASSWORD.equals(MpDbServlet.currentReq().action) && entity instanceof User)
								return;
							else if (entity instanceof User && creating)
								//let them register
								return;
							else
								throw new CallbackException(new CannotSaveDataException());
						}
					}
				} else {
					throw new CallbackException(new AccountNotEnabledException());
				}
			}
		}
	}
	private boolean isPublic(String[] propertyNames, Object[] state) {
		if (state == null || propertyNames == null)
			return false;
		for (int i = 0; i < propertyNames.length; ++i) {
			if (propertyNames[i].equals("publicData")) {
				if (state[i] != null)
					return Boolean.parseBoolean(state[i].toString());
				else
					return false;
			}
		}
		return false;
	}
	
	private int getIdOfUser(String propertyName, String[] propertyNames, Object[] state) {
		int ownerId = 0;
		for (int i = 0; i < propertyNames.length; ++i) {
			if (propertyNames[i].equals(propertyName)) {
				if (state != null && state[i] != null)
					ownerId = ((User) state[i]).getId();
				break;
			}
		}
		return ownerId;
	}

	private int getOwnerId(String[] propertyNames, Object[] state) {
		return getIdOfUser("owner", propertyNames, state);
	}

	private int getProperty(String property, String[] propertyNames,
			Object[] state) {
		int propertyValue = -1;
		for (int i = 0; i < propertyNames.length; ++i) {
			if (propertyNames[i].equals(property)) {
				if (state != null && state[i] != null)
					propertyValue = Integer.parseInt(state[i].toString());
				break;
			}
		}
		return propertyValue;
	}

	private int getVersion(String[] propertyNames, Object[] state) {
		return getProperty("version", propertyNames, state);
	}

	private int getSampleId(String[] propertyNames, Object[] state) {
		return getProperty("sample_id", propertyNames, state);
	}

	private int getSubsampleId(String[] propertyNames, Object[] state) {
		return getProperty("subsample_id", propertyNames, state);
	}

	/**
	 * Method called when an object is detected to be dirty, during a flush.
	 * This method is called by "modify" actions.
	 * 
	 * @param entity
	 *            object to be updated in the database.
	 * @param id
	 *            the identifier of the instance.
	 * @param currentState
	 *            array of property values.
	 * @param previousState
	 *            cached array of property values.
	 * @param propertyNames
	 *            array of property names.
	 * @param types
	 *            array of property types.
	 * @return true if the currentState was modified in any way.
	 * @throws CallbackException
	 *             if a problem occured.
	 */
	public boolean onFlushDirty(Object entity, Serializable id,
			Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) throws CallbackException {
		checkPermissions(id, entity, previousState, propertyNames, true, false);
		return super.onFlushDirty(entity, id, currentState, previousState,
				propertyNames, types);
	}

	/**
	 * Method called before an object is saved. This method is called by
	 * "create" actions.
	 * 
	 * @param entity
	 *            object to be saved to the database.
	 * @param id
	 *            the identifier of the instance.
	 * @param state
	 *            array of property values.
	 * @param propertyNames
	 *            array of property names.
	 * @param types
	 *            array of property types.
	 * @return true if the user modified the state in any way.
	 * @throws CallbackException
	 *             if a problem occured.
	 */
	public boolean onSave(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) throws CallbackException {
		checkPermissions(id, entity, state, propertyNames, true, true);
		return super.onSave(entity, id, state, propertyNames, types);
	}

	/**
	 * Method called before an object is delete. This method is called by
	 * "delete" actions.
	 * 
	 * @param entity
	 *            object to be deleted from the database.
	 * @param id
	 *            the identifier of the instance.
	 * @param state
	 *            array of property values.
	 * @param propertyNames
	 *            array of property names.
	 * @param types
	 *            array of property types.
	 * @throws CallbackException
	 *             if a problem occured.
	 */
	public void onDelete(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) throws CallbackException {
		checkPermissions(id, entity, state, propertyNames, true, false);
		super.onDelete(entity, id, state, propertyNames, types);
	}
}
