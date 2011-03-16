package edu.rpi.metpetrest.security;

import java.security.DigestException;
import java.security.MessageDigest;

public class SessionEncrypter {
	private static final byte SALT_SZ = 16;
	private static final byte DIGEST_SZ = Util.DIGEST_SZ;
	private static final byte[] key = {
			(byte) 0xa4, (byte) 0xae, 0x66, 0x6b, 0x0f, 0x47, (byte) 0x88, 0x1c
	};

	private static void xor(final byte enc[]) {
		enc[0] ^= enc[4 + 0];
		enc[1] ^= enc[4 + 1];
		enc[2] ^= enc[4 + 2];
		enc[3] ^= enc[4 + 3];
	}

	public static String crypt(final int userId) {
		if (userId < 1)
			throw new IllegalArgumentException("Invalid userid.");
		try {
			final byte[] enc = new byte[4 + SALT_SZ + DIGEST_SZ];
			enc[0] = (byte) ((userId >> 24) & 0xff);
			enc[1] = (byte) ((userId >> 16) & 0xff);
			enc[2] = (byte) ((userId >> 8) & 0xff);
			enc[3] = (byte) (userId & 0xff);

			final byte[] salt = new byte[SALT_SZ];
			Util.rng.nextBytes(salt);
			System.arraycopy(salt, 0, enc, 4, SALT_SZ);

			final MessageDigest md = Util.createMessageDigest();
			md.update(enc, 0, 4 + SALT_SZ);
			md.update(key);
			md.digest(enc, 4 + SALT_SZ, DIGEST_SZ);
			xor(enc);
			return Util.toHex(enc);
		} catch (DigestException de) {
			throw new IllegalStateException("Digest error: " + de.getMessage());
		}
	}

	public static Integer verify(final String str) {
		if (str == null)
			return null;
		final byte[] rawEncodedData = Util.fromHex(str);
		if (rawEncodedData == null)
			return null;
		final int saltSz = rawEncodedData.length - DIGEST_SZ - 4;
		if (saltSz < 4)
			return null;
		final MessageDigest md = Util.createMessageDigest();
		xor(rawEncodedData);
		md.update(rawEncodedData, 0, 4 + saltSz);
		md.update(key);
		final byte[] dig = md.digest();
		if (dig.length != DIGEST_SZ)
			return null;
		for (int a = DIGEST_SZ - 1, b = rawEncodedData.length - 1; a >= 0;)
			if (dig[a--] != rawEncodedData[b--])
				return null;
		return new Integer(((rawEncodedData[0] & 0xff) << 24)
				| ((rawEncodedData[1] & 0xff) << 16)
				| ((rawEncodedData[2] & 0xff) << 8)
				| (rawEncodedData[3] & 0xff));
	}
}
