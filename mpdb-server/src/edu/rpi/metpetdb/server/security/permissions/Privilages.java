package edu.rpi.metpetdb.server.security.permissions;

public enum Privilages {
	
	/** save own data, like samples, subsample */	
	SAVE_PRIVATE_DATA,
	/** load private data, whether it be theres or others */
	LOAD_PRIVATE_DATA,
	/**  load others private data */
	LOAD_OTHERS_PRIVATE_DATA,
	/** loading of public data */
	LOAD_PUBLIC_DATA,
	/** save own public data, like samples, subsamples */
	SAVE_OWN_PUBLIC_DATA,

}
