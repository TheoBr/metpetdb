package edu.rpi.metpetdb.client.service;

/**
 * Misc. constants required by both client and server implementations.
 * <p>
 * These constants are used by both the client-side UI code and the server side
 * service implementations. Exposing them here in a client-side class allows
 * both to access the same constants.
 * </p>
 */
public class MpDbConstants {
	/**
	 * Name of the browser cookie used to contain the user id.
	 * <p>
	 * This cookie contains the user id and some random encrypted data to verify
	 * the cookie has not been modified, falsified or tampered with.
	 * </p>
	 */
	public static final String USERID_COOKIE = "identity";

	/**
	 * PostGIS SRID constant for the WGS 84 projection.
	 * <p>
	 * This is the projection that we store all of our geometry objects in. It
	 * is also the projection used by Google Maps, and many other popular
	 * mapping applications that support the entire Earth.
	 * </p>
	 */
	public static final int WGS84 = 4326;

	private MpDbConstants() {
	}
}
