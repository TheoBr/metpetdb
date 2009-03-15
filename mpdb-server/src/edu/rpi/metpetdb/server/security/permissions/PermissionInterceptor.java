package edu.rpi.metpetdb.server.security.permissions;

import java.io.Serializable;
import java.security.Principal;
import java.util.Collection;
import java.util.HashSet;

import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Session;
import org.hibernate.type.Type;

import edu.rpi.metpetdb.client.error.security.AccountNotEnabledException;
import edu.rpi.metpetdb.client.error.security.CannotLoadPendingRoleException;
import edu.rpi.metpetdb.client.error.security.CannotLoadPrivateDataException;
import edu.rpi.metpetdb.client.error.security.CannotLoadPublicDataException;
import edu.rpi.metpetdb.client.error.security.CannotSaveDataException;
import edu.rpi.metpetdb.client.error.security.NotOwnerException;
import edu.rpi.metpetdb.client.error.security.UnableToModifyPublicDataException;
import edu.rpi.metpetdb.client.model.PendingRole;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.interfaces.HasOwner;
import edu.rpi.metpetdb.client.model.interfaces.HasSample;
import edu.rpi.metpetdb.client.model.interfaces.HasSubsample;
import edu.rpi.metpetdb.client.model.interfaces.PublicData;
import edu.rpi.metpetdb.server.DataStore;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.security.Action;
import edu.rpi.metpetdb.server.security.permissions.principals.AdminPrincipal;
import edu.rpi.metpetdb.server.security.permissions.principals.OwnerPrincipal;

public class PermissionInterceptor extends EmptyInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PermissionInterceptor() {

	}

	/**
	 * Checks if the current user is the owner of the entity, if they are they
	 * are safe, if not an exception is thrown
	 * 
	 * @param entity
	 *            entity to check the owner of
	 * @param state
	 * @param propertyNames
	 * @param principals
	 */
	private void checkOwner(Object entity, Object[] state,
			String[] propertyNames, Collection<Principal> principals) {
		if (entity instanceof HasOwner) {
			if (!principals.contains(new OwnerPrincipal(getOwnerId(
					propertyNames, state)))) {
				// only let the owner load it
				throw new CallbackException(new NotOwnerException());
			}
		}
	}

	/**
	 * Checks if entity is a member of a sample or a subsample, if it is it
	 * verifies whether the user can actually load the object based on whether
	 * they can load the subsample or sample
	 * 
	 * @param entity
	 * @param state
	 * @param propertyNames
	 */
	private void checkOwningObject(Object entity, Object[] state,
			String[] propertyNames) {
		if (entity instanceof HasSample || entity instanceof HasSubsample) {
			// means we are trying to load something that is a
			// child of a sample/subsample
			// and it does not have a public/private modifier
			// this means we have to load the parent object to
			// see if it is allowed to be loaded
			// it is private and they are the owner
			// now we have to check if the object they
			// are saving is part of another
			if (entity instanceof HasSample) {
				// see if we can load the sample id
				long sampleId = getSampleId(propertyNames, state);
				// we catch the exception in order
				// to reach the finally clause and close
				// the session
				final Session s = DataStore.open();
				try {
					s.getNamedQuery("Sample.byId").setParameter("id", sampleId)
							.uniqueResult();
				} catch (CallbackException ce) {
					throw ce;
				} finally {
					s.close();
				}
			}
			if (entity instanceof HasSubsample) {
				// see if we can load the subsample id
				long subsampleId = getSubsampleId(propertyNames, state);
				// we catch the exception in order
				// to reach the finally clause and close
				// the session
				final Session s = DataStore.open();
				try {
					s.getNamedQuery("Subsample.byId").setParameter("id",
							subsampleId).uniqueResult();
				} catch (CallbackException ce) {
					throw ce;
				} finally {
					s.close();
				}
			}
		}
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
		if (MpDbServlet.currentReq() != null) {
			Collection<Principal> principals = MpDbServlet.currentReq().principals;
			if (principals == null)
				principals = new HashSet<Principal>();
			if (!principals.contains(new AdminPrincipal())) {
				final int usersRank;
				if (MpDbServlet.currentReq().user != null) {
					usersRank = MpDbServlet.currentReq().user.getRank();
				} else
					usersRank = -1;
				boolean isPublic = false;
				if (entity instanceof PublicData) {
					isPublic = isPublic(propertyNames, state);
				}
				if (isPublic) {
					if (!RoleDefinitions.roleDefinitions.get(usersRank)
							.contains(Privilages.LOAD_PUBLIC_DATA)) {
						throw new CallbackException(
								new CannotLoadPublicDataException());
					}
				} else {
					// the object is either not public or not an instance of
					// public/private, so check for sure
					if (entity instanceof PublicData) {
						// see if they are allowed to load private data
						if (RoleDefinitions.roleDefinitions.get(usersRank)
								.contains(Privilages.LOAD_PRIVATE_DATA)) {
							// check if they have to be the owner to load the
							// private data
							if (!RoleDefinitions.roleDefinitions
									.get(usersRank)
									.contains(
											Privilages.LOAD_OTHERS_PRIVATE_DATA)) {
								checkOwner(entity, state, propertyNames,
										principals);
								checkOwningObject(entity, state, propertyNames);
							}
						} else {
							// user cannot load private data
							throw new CallbackException(
									new CannotLoadPrivateDataException());
						}
					} else {
						// this is the case in which the object has no
						// public/private modifier
						// this includes objects like Images, RockType, Region,
						// Mineral, etc.
						checkOwningObject(entity, state, propertyNames);
						checkOwner(entity, state, propertyNames, principals);
						if (entity instanceof PendingRole) {
							// since pending roles don't really have owners we
							// put them in their
							// own category and make it so only the sponsor and
							// the user can load them
							if (!(principals
									.contains(new OwnerPrincipal(getIdOfUser(
											"sponsor", propertyNames, state))) || principals
									.contains(new OwnerPrincipal(getIdOfUser(
											"user", propertyNames, state))))) {
								throw new CallbackException(
										new CannotLoadPendingRoleException());
							}
						} else {
							// this is the case when we are loading stuff like
							// RockType, Mineral, Oxide, Element
							// basic 3d party support objects that are not
							// associated with anyone, so we always allow
							// them to be loaded
						}
					}
				}
			}
		}
		return super.onLoad(entity, id, state, propertyNames, types);
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

	private int getIdOfUser(String propertyName, String[] propertyNames,
			Object[] state) {
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
				if (state != null && state[i] != null) {
					final Object value = state[i];
					if (value instanceof Sample && value != null) {
						propertyValue = (int) ((Sample) value).getId();
					} else if (value instanceof Subsample && value != null) {
						propertyValue = (int) ((Subsample) value).getId();
					} else {
						try {
							propertyValue = Integer.parseInt(value.toString());
						} catch (Exception e) {

						}
					}
					break;
				}
			}
		}
		return propertyValue;
	}

	private int getSampleId(String[] propertyNames, Object[] state) {
		return getProperty("sample", propertyNames, state);
	}

	private int getSubsampleId(String[] propertyNames, Object[] state) {
		return getProperty("subsample", propertyNames, state);
	}

	private void checkSavePermissions(Object entity, Object[] state,
			String[] propertyNames, Type[] types, boolean creating) {
		if (MpDbServlet.currentReq() != null) {
			Collection<Principal> principals = MpDbServlet.currentReq().principals;
			if (principals == null)
				principals = new HashSet<Principal>();
			if (!principals.contains(new AdminPrincipal())) {
				final int usersRank;
				boolean enabled = false;
				if (MpDbServlet.currentReq().user != null) {
					usersRank = MpDbServlet.currentReq().user.getRank();
					enabled = MpDbServlet.currentReq().user.getEnabled();
				} else
					usersRank = -1;
				boolean isPublic = false;
				if (entity instanceof PublicData) {
					isPublic = isPublic(propertyNames, state);
				}
				// user is trying to save something, if they are creating an
				// account let it go
				if (enabled) {
					if (isPublic && !creating) {
						// public data cannot be saved (except when creating it
						// initially)
						throw new CallbackException(
								new UnableToModifyPublicDataException());
					} else {
						if (RoleDefinitions.roleDefinitions.get(usersRank)
								.contains(Privilages.SAVE_PRIVATE_DATA)) {
							checkOwner(entity, state, propertyNames, principals);
							checkOwningObject(entity, state, propertyNames);
						} else {
							// let email password save the user instance
							if (Action.EMAIL_PASSWORD.equals(MpDbServlet
									.currentReq().action)
									&& entity instanceof User)
								return;
							else if (entity instanceof User && creating)
								// let them register
								return;
							else
								throw new CallbackException(
										new CannotSaveDataException());
						}
					}
				} else {
					// check if the user is saving themselves before failing
					if (entity instanceof User) {
						if (Action.EMAIL_PASSWORD.equals(MpDbServlet
								.currentReq().action))
							return;
						else if (MpDbServlet.currentReq().user != null
								&& ((User) entity).getId() == MpDbServlet
										.currentReq().user.getId())
							return;
						else
							if (!creating) //let accounts be created
								throw new CallbackException(new NotOwnerException());
					} else {
						throw new CallbackException(
								new AccountNotEnabledException());
					}
				}
			}
		}
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
		// we only check permissions if the object is different from it's
		// previous state
		boolean equals = true;
		for (int i = 0; i < previousState.length; ++i)
			if (previousState[i] == null && currentState[i] != null) {
				equals = false;
				break;
			} else if (previousState[i] != null
					&& !previousState[i].equals(currentState[i])) {
				equals = false;
				break;
			}
		if (!equals)
			checkSavePermissions(entity, previousState, propertyNames, types,
					false);
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
		checkSavePermissions(entity, state, propertyNames, types, true);
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
		checkSavePermissions(entity, state, propertyNames, types, false);
		super.onDelete(entity, id, state, propertyNames, types);
	}
}
