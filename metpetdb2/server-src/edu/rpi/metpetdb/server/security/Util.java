package edu.rpi.metpetdb.server.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Util {
	static final SecureRandom rng;
	static final byte DIGEST_SZ = 20;

	static {
		try {
			rng = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException nsae) {
			final LinkageError le = new LinkageError("Missing PRNG.");
			le.initCause(nsae);
			throw le;
		}
	}

	static MessageDigest createMessageDigest() {
		try {
			return MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException nsae) {
			final LinkageError le = new LinkageError("Missing digest alg.");
			le.initCause(nsae);
			throw le;
		}
	}

	public static String toHex(final byte[] raw) {
		final StringBuffer out = new StringBuffer(raw.length * 2);
		for (int k = 0; k < raw.length; k++) {
			final int b = raw[k];
			final int b1 = (b >> 4) & 0xf;
			final int b2 = b & 0xf;
			out.append(b1 < 10 ? (char) ('0' + b1) : (char) ('a' + b1 - 10));
			out.append(b2 < 10 ? (char) ('0' + b2) : (char) ('a' + b2 - 10));
		}
		return out.toString();
	}

	public static byte[] fromHex(final String inStr) {
		final int len = inStr.length();
		if ((len & 1) != 0)
			return null;
		final byte[] out = new byte[len / 2];
		for (int j = 0, k = 0; k < len / 2; k++) {
			final char c1 = inStr.charAt(j++);
			final char c2 = inStr.charAt(j++);
			int b;

			if ('0' <= c1 && c1 <= '9')
				b = c1 - '0';
			else if ('a' <= c1 && c1 <= 'f')
				b = c1 - 'a' + 10;
			else
				return null;
			b <<= 4;

			if ('0' <= c2 && c2 <= '9')
				b |= c2 - '0';
			else if ('a' <= c2 && c2 <= 'f')
				b |= c2 - 'a' + 10;
			else
				return null;
			out[k] = (byte) b;
		}
		return out;
	}
}
