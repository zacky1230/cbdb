package com.chineseall.util;

import org.apache.commons.codec.Charsets;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * IOUtils
 *
 * @author L.cm
 */
public class IOUtils extends org.springframework.util.StreamUtils {

	/**
	 * closeQuietly
	 *
	 * @param closeable 自动关闭
	 */
	public static void closeQuietly(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (IOException ioe) {
			// ignore
		}
	}

	/**
	 * InputStream to String utf-8
	 *
	 * @param input the <code>InputStream</code> to read from
	 * @return the requested String
	 * @throws NullPointerException if the input is null
	 */
	public static String toString(InputStream input) {
		return toString(input, Charsets.UTF_8);
	}

	/**
	 * InputStream to String
	 *
	 * @param input   the <code>InputStream</code> to read from
	 * @param charset the <code>Charset</code>
	 * @return the requested String
	 * @throws NullPointerException if the input is null
	 */
	public static String toString(InputStream input, Charset charset) {
		try {
			return IOUtils.copyToString(input, charset);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		} finally {
			IOUtils.closeQuietly(input);
		}
	}

	public static byte[] toByteArray(InputStream input) {
		try {
			return IOUtils.copyToByteArray(input);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		} finally {
			IOUtils.closeQuietly(input);
		}
	}

	/**
	 * Writes chars from a <code>String</code> to bytes on an
	 * <code>OutputStream</code> using the specified character encoding.
	 * <p>
	 * This method uses {@link String#getBytes(String)}.
	 *
	 * @param data     the <code>String</code> to write, null ignored
	 * @param output   the <code>OutputStream</code> to write to
	 * @param encoding the encoding to use, null means platform default
	 * @throws NullPointerException if output is null
	 * @throws IOException          if an I/O error occurs
	 */
	public static void write(final String data, final OutputStream output, final Charset encoding) throws IOException {
		if (data != null) {
			output.write(data.getBytes(encoding));
		}
	}
}
