package edu.rpi.metpetdb.client.locale;

import com.google.gwt.core.client.GWT;

/**
 * Client side async service proxies and global constants.
 * <p>
 * <i><b>Only available on the client side.</b></i>
 * </p>
 * <p>
 * The server side GWT runtime is unable to create service proxies, and is
 * equally unable to load locale files.
 * </p>
 */
public class LocaleHandler {

	public static final LocaleText lc_text;

	public static final LocaleEntity lc_entity;

	static {
		lc_text = (LocaleText) GWT.create(LocaleText.class);
		lc_entity = (LocaleEntity) GWT.create(LocaleEntity.class);
	}

	private LocaleHandler() {
	}
}