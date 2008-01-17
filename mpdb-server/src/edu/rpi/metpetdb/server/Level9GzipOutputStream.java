package edu.rpi.metpetdb.server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

class Level9GzipOutputStream extends DeflaterOutputStream {
	private final static int GZIP_MAGIC = 0x8b1f;
	private final CRC32 crc = new CRC32();

	Level9GzipOutputStream(final OutputStream out) throws IOException {
		super(out, new Deflater(9, true), 512);
		writeHeader();
		crc.reset();
	}

	public void write(final byte[] b, final int o, final int n)
			throws IOException {
		super.write(b, o, n);
		crc.update(b, o, n);
	}

	public void finish() throws IOException {
		if (!def.finished()) {
			def.finish();
			while (!def.finished())
				deflate();
			writeTrailer();
		}
	}

	public void close() throws IOException {
		finish();
		out.close();
	}

	private void writeHeader() throws IOException {
		writeShort(GZIP_MAGIC); // Magic number
		out.write(Deflater.DEFLATED); // Compression method (CM)
		out.write(0); // Flags (FLG)
		writeInt(0); // Modification time (MTIME)
		out.write(0); // Extra flags (XFL)
		out.write(0); // Operating system (OS)
	}

	private void writeTrailer() throws IOException {
		writeInt((int) crc.getValue()); // CRC-32 of uncompressed data
		writeInt(def.getTotalIn()); // Number of uncompressed bytes
	}

	private void writeInt(final int i) throws IOException {
		writeShort(i & 0xffff);
		writeShort((i >> 16) & 0xffff);
	}

	private void writeShort(final int s) throws IOException {
		out.write(s & 0xff);
		out.write((s >> 8) & 0xff);
	}
}
