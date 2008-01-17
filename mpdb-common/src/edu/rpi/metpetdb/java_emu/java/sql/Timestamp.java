package java.sql;

import java.util.Date;

/* Emulation of java.sql.Timestamp for browser. */
public class Timestamp extends Date {
	public Timestamp() {
	}
	public Timestamp(final long v) {
		super(v);
	}
}
