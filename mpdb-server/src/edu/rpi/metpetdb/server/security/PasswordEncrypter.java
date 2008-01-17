package edu.rpi.metpetdb.server.security;

import java.io.UnsupportedEncodingException;
import java.security.DigestException;
import java.security.MessageDigest;

/** Provides password encoding and testing. */
public class PasswordEncrypter {
	private static final byte SALT_SZ = 4;
	private static final byte DIGEST_SZ = Util.DIGEST_SZ;

	private static byte[] toBytes(final String str) {
		try {
			return str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException uee) {
			throw new LinkageError("No UTF-8 support.");
		}
	}

	/**
	 * Generate a new random password.
	 * 
	 * @return the new password string, in plaintext format, so the caller can
	 *         make it available to the user through some means.
	 */
	public static String randomPassword() {
		final int len = 8;
		final String c = "abcdefghijkmnopqrstuvwxyz23456789";
		final StringBuffer p = new StringBuffer(len);
		for (int i = 0; i < len; ++i)
			p.append(c.charAt(Util.rng.nextInt(c.length())));
		return p.toString();
	}

	/**
	 * Encode a plaintext password using a secure digest.
	 * 
	 * @param plaintext
	 *            the password. Must not be null, but may be of any length.
	 * @return a fixed length byte array containing the secure digest unique to
	 *         this password. It is impossible to recover <code>plaintext</code>
	 *         from the returned value.
	 */
	public static byte[] crypt(final String plaintext) {
		try {
			final byte[] enc = new byte[1 + SALT_SZ + DIGEST_SZ];
			final byte[] salt = new byte[SALT_SZ];
			enc[0] = SALT_SZ;
			Util.rng.nextBytes(salt);
			System.arraycopy(salt, 0, enc, 1, SALT_SZ);
			final MessageDigest md = Util.createMessageDigest();
			md.update(salt);
			md.update(toBytes(plaintext));
			md.digest(enc, 1 + SALT_SZ, DIGEST_SZ);
			return enc;
		} catch (DigestException de) {
			throw new IllegalStateException("Digest error: " + de.getMessage());
		}
	}

	/**
	 * Test a given plaintext password against an existing encoded version.
	 * 
	 * @param encoded
	 *            a prior return value from {@link #crypt(String)}.
	 * @param plaintext
	 *            the plaintext password to test. Must be the same as the prior
	 *            argument given to {@link #crypt(String)}.
	 * @return true if plaintext matches the previously supplied plaintext;
	 *         false otherwise.
	 */
	public static boolean verify(final byte[] encoded, final String plaintext) {
		if (encoded == null || plaintext == null)
			return false;
		if (encoded.length < (1 + 1 + DIGEST_SZ))
			return false;
		final MessageDigest md = Util.createMessageDigest();
		md.update(encoded, 1, encoded[0]);
		md.update(toBytes(plaintext));
		final byte[] dig = md.digest();
		if (dig.length != DIGEST_SZ)
			return false;
		for (int a = DIGEST_SZ - 1, b = encoded.length - 1; a >= 0; a--, b--)
			if (dig[a] != encoded[b])
				return false;
		return true;
	}

	/**
	 * calculates the checksum of a file
	 * 
	 * @param file
	 * @return
	 */
	public static String calculateChecksum(final byte[] file) {
		try {
			final byte[] enc = new byte[1 + SALT_SZ + DIGEST_SZ];
			final byte[] salt = new byte[SALT_SZ];
			enc[0] = SALT_SZ;
			Util.rng.nextBytes(salt);
			System.arraycopy(salt, 0, enc, 1, SALT_SZ);
			final MessageDigest md = Util.createMessageDigest();
			md.update(salt);
			md.update(file);
			md.digest(enc, 1 + SALT_SZ, DIGEST_SZ);
			return Util.toHex(enc);
		} catch (DigestException de) {
			throw new IllegalStateException("Digest error: " + de.getMessage());
		}
	}
}
