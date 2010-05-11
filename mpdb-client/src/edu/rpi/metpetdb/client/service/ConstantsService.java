package edu.rpi.metpetdb.client.service;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * Provides access to constant values.
 * <p>
 * Most data served back by this service does not change, or is not expected to
 * frequently change. The values are either 'hardcoded' into the backend
 * database or are somewhat mutable, but unlikely to change during a user's
 * execution of the application.
 * </p>
 */
public interface ConstantsService extends RemoteService {
}
