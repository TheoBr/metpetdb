package edu.rpi.metpetdb.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Compresses static content, caches it in memory, and serves it clients.
 * <p>
 * A large part of our application is based on very static, and very large
 * content. In particular images, CSS, and especially the browser-specific
 * generated JavaScript/HTML files produced by the GWT compiler. In this latter
 * case we can easily reduce the file from 200+ KiB to around 61 KiB by
 * compressing it with the GZIP compression algorithm/format at compression
 * level 9. Since the data compression for a file that large does take a little
 * bit of time we cache it in memory once it has been created.
 * </p>
 * <p>
 * This servlet also sets very aggressive caching rules for the large generated
 * HTML pages. These end in <code>.cache.html</code> and are accessed by an
 * MD-5 checksum of their content, so the browser really can safely cache the
 * file for extremely long periods of time and never even consult the server
 * about the validity of that object. For the <code>.nocache.html</code> files
 * that provides the table of current MD-5 checksum names we do the exact
 * opposite and act like the content is always expired; this way the browser is
 * encouraged to redownload and recheck that table on every application startup.
 * </p>
 */
public class StaticCompresser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final long I_SHORT_AGE = 24L * 60 * 60 * 1000;
	private static final long I_LONG_AGE = 365L * 24 * 60 * 60 * 1000;
	private static final String IF_NONE_MATCH = "If-None-Match";
	private static final String VARY = "Vary";
	private static final String DATE = "Date";
	private static final String ETAG = "ETag";
	private static final String PRAGMA = "Pragma";
	private static final String EXPIRES = "Expires";
	private static final String CACHE_CONTROL = "Cache-Control";
	private static final String ACCEPT_ENCODING = "Accept-Encoding";
	private static final String CONTENT_ENCODING = "Content-Encoding";
	private static final String NOT_MODIFIED = "Not Modified";
	private static final String NOT_FOUND = "Not Found";
	private static final String CONTENT_ENCODING_GZIP = "gzip";
	private static final String NO_CACHE = "no-cache";
	private static final String NO_CACHE_NO_STORE = "no-cache, no-store";
	private static final String S_SHORT_AGE;
	private static final String S_LONG_AGE;

	private static String maxage(final long v) {
		return "max-age=" + (v / 1000);
	}
	static {
		S_SHORT_AGE = "public, must-revalidate, " + maxage(I_SHORT_AGE);
		S_LONG_AGE = "public, " + maxage(I_LONG_AGE);
	}

	private final Map files = new HashMap();
	private final MessageDigest etagComputer;
	private long currentCacheSize;

	{
		try {
			etagComputer = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException nsae) {
			final LinkageError le = new LinkageError("Missing digest alg.");
			le.initCause(nsae);
			throw le;
		}
	}

	public void destroy() {
		synchronized (this) {
			log("Destroyed file cache: " + files.size() + " files; "
					+ (currentCacheSize / 1024) + " KiB");
			files.clear();
		}
		super.destroy();
	}

	protected void doGet(final HttpServletRequest req,
			final HttpServletResponse rsp) throws ServletException, IOException {
		final FileData file = lookup(req.getServletPath().substring(1));
		if (file == null) {
			rsp.sendError(HttpServletResponse.SC_NOT_FOUND, NOT_FOUND);
			return;
		}

		final long now = System.currentTimeMillis();
		rsp.setDateHeader(DATE, now);
		rsp.setHeader(ETAG, file.etag);
		switch (file.cacheMode) {
			case FileData.NEVER :
				rsp.setDateHeader(EXPIRES, 0);
				rsp.setHeader(CACHE_CONTROL, NO_CACHE_NO_STORE);
				rsp.setHeader(PRAGMA, NO_CACHE);
				break;
			case FileData.SHORT_TERM :
				rsp.setDateHeader(EXPIRES, now + I_SHORT_AGE);
				rsp.setHeader(CACHE_CONTROL, S_SHORT_AGE);
				rsp.setHeader(VARY, ACCEPT_ENCODING);
				break;
			case FileData.LONG_TERM :
				rsp.setDateHeader(EXPIRES, now + I_LONG_AGE);
				rsp.setHeader(CACHE_CONTROL, S_LONG_AGE);
				rsp.setHeader(VARY, ACCEPT_ENCODING);
				break;
		}

		if (file.notModified(req)) {
			rsp.sendError(HttpServletResponse.SC_NOT_MODIFIED, NOT_MODIFIED);
			return;
		}

		final byte[] content;
		if (acceptsGzipEncoding(req) && file.gzipContent != null) {
			content = file.gzipContent;
			rsp.setHeader(CONTENT_ENCODING, CONTENT_ENCODING_GZIP);
		} else
			content = file.rawContent;

		rsp.setContentLength(content.length);
		rsp.setContentType(file.contentType);
		rsp.setStatus(HttpServletResponse.SC_OK);
		rsp.getOutputStream().write(content);
	}

	private static boolean acceptsGzipEncoding(final HttpServletRequest r) {
		final String e = r.getHeader(ACCEPT_ENCODING);
		return e != null && e.indexOf(CONTENT_ENCODING_GZIP) != -1;
	}

	private synchronized FileData lookup(final String name) throws IOException {
		FileData file = (FileData) files.get(name);
		if (file != null)
			return file;

		final InputStream i = getServletContext().getResourceAsStream(name);
		if (i == null)
			return null;

		file = new FileData();
		file.contentType = contentType(name);
		file.cacheMode = cacheMode(name);
		try {
			file.read(i);
		} finally {
			i.close();
		}
		etagComputer.reset();
		etagComputer.update(file.rawContent);
		file.etag = toHex(etagComputer.digest());
		file.compress();

		currentCacheSize += file.rawContent.length;
		if (file.gzipContent != null)
			currentCacheSize += file.gzipContent.length;
		files.put(name, file);

		final StringBuffer msg = new StringBuffer();
		msg.append("Load ");
		msg.append(name);
		msg.append(':');
		if (file.gzipContent != null) {
			final int saved = file.rawContent.length - file.gzipContent.length;
			msg.append(" gzip saved ");
			msg.append(saved);
			msg.append(" bytes (");
			msg.append(saved * 100 / file.rawContent.length);
			msg.append("%)");
		} else {
			msg.append(" not compressable");
		}
		log(msg.toString());
		log("Cache size: " + files.size() + " files; "
				+ (currentCacheSize / 1024) + " KiB");

		return file;
	}

	private static int cacheMode(final String name) {
		if (name.endsWith(".nocache.html"))
			return FileData.NEVER;
		else if (name.endsWith(".cache.html") || name.endsWith(".cache.xml"))
			return FileData.LONG_TERM;
		else
			return FileData.SHORT_TERM;
	}

	private static String contentType(final String name) {
		if (name.endsWith(".xml"))
			return "application/xml";

		else if (name.endsWith(".css"))
			return "text/css";
		else if (name.endsWith(".html"))
			return "text/html";

		else if (name.endsWith(".gif"))
			return "image/gif";
		else if (name.endsWith(".jpg"))
			return "image/jpeg";
		else if (name.endsWith(".png"))
			return "image/png";

		else
			return "application/octet-stream";
	}

	private static String toHex(final byte[] raw) {
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

	/** The cached file content, and its important metadata. */
	private static final class FileData {
		static final int NEVER = 0;
		static final int SHORT_TERM = 1;
		static final int LONG_TERM = 2;
		byte[] rawContent;
		byte[] gzipContent;
		String etag;
		int cacheMode;
		String contentType;

		boolean notModified(final HttpServletRequest req) {
			return etag.equals(req.getHeader(IF_NONE_MATCH));
		}

		void read(final InputStream i) throws IOException {
			final ByteArrayOutputStream byteBuf = new ByteArrayOutputStream();
			final byte[] raw = new byte[1024];
			int n;
			while ((n = i.read(raw)) > 0)
				byteBuf.write(raw, 0, n);
			rawContent = byteBuf.toByteArray();
		}

		void compress() throws IOException {
			final Level9GzipOutputStream gz;
			final ByteArrayOutputStream byteBuf = new ByteArrayOutputStream();
			gz = new Level9GzipOutputStream(byteBuf);
			gz.write(rawContent);
			gz.close();
			gzipContent = byteBuf.toByteArray();
			if (gzipContent.length >= rawContent.length)
				gzipContent = null;
			else if (rawContent.length - gzipContent.length <= 100)
				gzipContent = null;
		}
	}
}
