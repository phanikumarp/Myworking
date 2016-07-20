package n42.common;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Pattern;

import javax.sql.rowset.serial.SerialBlob;

import n42.services.utils.ExceptionUtils;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public abstract class BytesUtils {

	private static final Logger LOG = LoggerFactory.getLogger(BytesUtils.class);
	private static final int HASHPRIME=31;
	private static final int DEFAULT_BUFSIZE=4096;
	private static final String BYTES_CANNOT_BE_NULL = "bytes should not be null";
	private static final Pattern LOOKS_LIKE_HUMAN_READABLE=Pattern.compile("(\\w|\\s)*");
	
	private BytesUtils() {
	}

	private static final String HEX_TRANSLATION_TABLE = "0123456789abcdef";

	/**
	 * Convert string to utf8 byte sequence
	 * 
	 * @return string converted to utf8 bytes (never null!)
	 * @throws NullPointerException
	 *             if s is null
	 */
	public static byte[] string2utf(String s) {
		Assert.notNull(s, "s should not be null");

		return s.getBytes(Charsets.UTF8);
	}

	/**
	 * Convert utf8 bytes sequence into a string
	 * 
	 * @return string converted from utfb bytes (never null!)
	 * @throws NullPointerException
	 *             if utf8 is null
	 */
	public static String utf2string(byte[] utf8) {
		Assert.notNull(utf8, "utf8 should not be null");

		return new String(utf8, 0, utf8.length, Charsets.UTF8);
	}

	/**
	 * Convert byte array to simple hex string, example:
	 * [0xCA, 0xFE, 0x00, 0x01] will be converted to "cafe0001"
	 * 
	 * @return hex string representation of the byte array (never null!).
	 * @throws NullPointerException
	 *             if bytes is null
	 */
	public static String bytes2hex(byte[] bytes) {
		Assert.notNull(bytes, BYTES_CANNOT_BE_NULL);

		StringBuilder result = new StringBuilder(bytes.length * 2);

		for (byte b: bytes) {
			int t1 = b & 15;
			int t2 = (b >>> 4) & 15;
			result.append(HEX_TRANSLATION_TABLE.charAt(t2));
			result.append(HEX_TRANSLATION_TABLE.charAt(t1));
		}

		return result.toString();
	}
	
	public static String byte2hex(byte b) {
		int t1 = b & 15;
		int t2 = (b >>> 4) & 15;

		StringBuilder result = new StringBuilder(2);
		result.append(HEX_TRANSLATION_TABLE.charAt(t2));
		result.append(HEX_TRANSLATION_TABLE.charAt(t1));

		return result.toString();
	}

	public static String bytesToHumanReadableString(byte[] bytes) {
		if (null == bytes) {
			return "null";
		}
		try {
			String utf = utf2string(bytes);
			if (LOOKS_LIKE_HUMAN_READABLE.matcher(utf).lookingAt()) {
				return utf;
			}
		} catch (Exception e) {
			// Ignore
		}
		return bytes2hex(bytes);
	}

	/**
	 * Convert hex string to byte array, for example, "cafe0001" will be
	 * converted to [0xCA, 0xFE, 0x00, 0x01]
	 * 
	 * @return bytes sequence converted from hex string
	 * @throws NullPointerException
	 *             if hex is null
	 * @throws ParseException
	 *             if hex contains bad characters or hex.length is not even
	 */
	public static byte[] hex2bytes(String hex) throws ParseException {
		Assert.notNull(hex, "hex should not be null");
		if (hex.length() % 2 != 0) {
			throw new ParseException("Wrong length(" + hex.length() + "): '" + hex + "'", 0);
		}
		int length = hex.length() / 2;
		byte[] result = new byte[length];

		for (int i = 0; i < length; i++) {
			int k = 2 * i;
			char c1 = hex.charAt(k);
			char c2 = hex.charAt(k + 1);

			int i1 = HEX_TRANSLATION_TABLE.indexOf(c1);
			if (i1 < 0) {
				throw new ParseException(hex, k);
			}
			int i2 = HEX_TRANSLATION_TABLE.indexOf(c2);
			if (i2 < 0) {
				throw new ParseException(hex, k);
			}

			int b = (i1 << 4) | i2;
			result[i] = (byte) b;
		}

		return result;
	}

	/**
	 * Convert long value to hex string
	 */
	public static String long2hex(long v) {
		return Long.toHexString(v);
	}

	/**
	 * Convert hex string to long
	 * 
	 * @throws NullPointerException
	 *             if hex is null
	 * @throws ParseException
	 *             if hex string contains bad values
	 */
	public static long hex2long(String hex) throws ParseException {
		Assert.notNull(hex, "hex should not be null");
		try {
			if (hex.length() > 16) {
				throw new ParseException(hex, 16);
			} else if (hex.length() > 15) {
				// If hex number is a big negative number, Long.parse will fail
				long p1 = Long.parseLong(hex.substring(0, 8), 16);
				long p2 = Long.parseLong(hex.substring(8), 16);
				return (p1 << 32) | p2;
			} else {
				return Long.parseLong(hex, 16);
			}
		} catch (NumberFormatException e) {
			LOG.debug(e.getMessage(), e);
			throw new ParseException(hex, 0);
		}
	}

	/**
	 * Convert integer value to hex string
	 */
	public static String int2hex(int v) {
		return Integer.toHexString(v);
	}

	/**
	 * Convert hex string to integer
	 * 
	 * @throws NullPointerException
	 *             if hex is null
	 * @throws ParseException
	 *             if hex string contains bad values
	 */
	public static int hex2int(String hex) throws ParseException {
		Assert.notNull(hex, "hex should not be null");
		try {
			if (hex.length() > 8) {
				throw new ParseException(hex, 8);
			} else if (hex.length() > 7) {
				// If hex number is a big negative number, Integer.parse will fail
				return (int) Long.parseLong(hex, 16);
			} else {
				return Integer.parseInt(hex, 16);
			}
		} catch (NumberFormatException e) {
			LOG.debug(e.getMessage(), e);
			throw new ParseException(hex, 0);
		}
	}

	/**
	 * Convert integer into 4 bytes in network order
	 */
	public static byte[] int2bytes(int i) {
		byte[] record = new byte[4];
		ByteBuffer b = ByteBuffer.wrap(record);
		b.putInt(i);
		return record;
	}

	/**
	 * Convert integer into 4 bytes in network order
	 */
	public static byte[] short2bytes(short s) {
		byte[] record = new byte[2];
		ByteBuffer b = ByteBuffer.wrap(record);
		b.putShort(s);
		return record;
	}

	/**
	 * Convert 4 bytes in network order to integer
	 * 
	 * @throws IllegalArgumentException
	 *             when fourBytes is null
	 * @throws IllegalArgumentException
	 *             when fourBytes.length != 4
	 */
	public static int bytes2int(byte[] fourBytes) {
		Assert.notNull(fourBytes, "fourBytes should not be null");
		if (4 != fourBytes.length) {
			throw new IllegalArgumentException("fourBytes.length=" + fourBytes.length + ", should be 4");
		}

		ByteBuffer b = ByteBuffer.wrap(fourBytes);
		return b.getInt();
	}

	/**
	 * Convert 2 bytes in network order to integer
	 * 
	 * @throws IllegalArgumentException
	 *             when twoBytes is null
	 * @throws IllegalArgumentException
	 *             when twoBytes.length != 2
	 */
	public static short bytes2short(byte[] twoBytes) {
		Assert.notNull(twoBytes, "twoBytes should not be null");
		if (2 != twoBytes.length) {
			throw new IllegalArgumentException("twoBytes.length=" + twoBytes.length + ", should be 2");
		}

		ByteBuffer b = ByteBuffer.wrap(twoBytes);
		return b.getShort();
	}

	/**
	 * Convert long into 8 bytes in network order
	 */
	public static byte[] long2bytes(long l) {
		byte[] record = new byte[8];
		ByteBuffer b = ByteBuffer.wrap(record);
		b.putLong(l);
		return record;
	}

	/**
	 * Convert 8 bytes in network order to long
	 * 
	 * @throws IllegalArgumentException
	 *             when eightBytes is null
	 * @throws IllegalArgumentException
	 *             when eightBytes.length != 8
	 */
	public static long bytes2long(byte[] eightBytes) {
		Assert.notNull(eightBytes, "eightBytes should not be null");
		if (8 != eightBytes.length) {
			throw new IllegalArgumentException("eightBytes.length=" + eightBytes.length + ", should be 8");
		}

		ByteBuffer b = ByteBuffer.wrap(eightBytes);
		return b.getLong();
	}

	public static byte[] date2bytes(Date date) {
		Assert.notNull(date, "date should not be null");

		return long2bytes(date.getTime());
	}

	public static Date bytes2date(byte[] bytes) {
		Assert.notNull(bytes, BYTES_CANNOT_BE_NULL);

		return new Date(bytes2long(bytes));
	}

	/**
	 * Concatenate several arrays into one ig array. Will skip null arrays.
	 * 
	 * @throws IllegalArgumentException
	 *             if arrays is null;
	 */
	public static byte[] concat(byte[]... arrays) {
		Assert.notNull(arrays, "arrays should not be null");

		int totalLength = 0;
		for (byte[] b: arrays) {
			if (null == b) {
				continue;
			}
			totalLength += b.length;
		}

		byte[] result = new byte[totalLength];
		int offset = 0;
		for (byte[] b: arrays) {
			if (null == b) {
				continue;
			}
			System.arraycopy(b, 0, result, offset, b.length);
			offset += b.length;
		}
		return result;
	}

	public static byte[] concat(byte[] bytes, byte b) {
		Assert.notNull(bytes, BYTES_CANNOT_BE_NULL);

		byte[] result = new byte[bytes.length + 1];
		System.arraycopy(bytes, 0, result, 0, bytes.length);
		result[bytes.length] = b;
		return result;
	}

	/**
	 * Increment arbitrary-precision fixed-point number represented as
	 * byte array by one.
	 * Note that if you try to increment something like 0xFFFF you'll get
	 * 0x0000 and true (carry flag) as a result
	 * 
	 * @param number
	 *            arbitrary-precision fixed-point number
	 * @return carry flag
	 * @throws IllegalArgumentException
	 *             when number is null
	 */
	public static boolean increment(byte[] number) {
		Assert.notNull(number, "number should not be null");

		boolean cf = true;
		
		for (int i=number.length-1; i>=0; i--) {
			int v = number[i] & 0xFF;
			v += 1;
			if (v >= 256) {
				v &= 0xFF;
				number [i] = (byte)v;
			} else {
				number [i] = (byte)v;
				cf = false;
				break;
			}
		}
		
		return cf;
	}
	
	/**
	 * Increment lexicographical string as if it was an array of bytes
	 * 
	 * @param number
	 *            arbitrary-precision fixed-point number
	 * @return result.length == number.length
	 * @throws IllegalArgumentException
	 *             when number is null
	 */
	public static String incrementString(String number) {
		Assert.notNull(number, "number should not be null");

		if (0 == number.length()) {
			return "";
		}
		char[] result = new char[number.length()];
		number.getChars(0, number.length(), result, 0);

		int i = result.length - 1;
		int add = 1;
		do {
			int v = result[i] & 0xFFFF;
			v += add;
			if (v >= 65536) {
				v -= 65536;
				add = 1; // Carry over
			} else {
				add = 0;
			}
			result[i] = (char) v;
			i--;
		} while (add > 0 && i >= 0);

		return new String(result);
	}

	public static byte[] bool2bytes(boolean b) {
		return b ? new byte[]{(byte) 1} : new byte[]{(byte) 0};
	}

	public static boolean bytes2bool(byte[] arr) {
		Assert.notNull(arr, "arr should not be null");
		Assert.isTrue(arr.length == 1, "arr.length should not be 1");

		return arr[0] != 0;
	}

	public static Blob bytes2blob(byte[] bytes) {
		Assert.notNull(bytes, BYTES_CANNOT_BE_NULL);

		try {
			return new SerialBlob(bytes);
		} catch (SQLException e) {
			LOG.error(ExceptionUtils.errorLogEntry(e));
			return null;
		}
	}

	public static byte[] blob2bytes(Blob blob) {
		Assert.notNull(blob, "blob should not be null");

		try {
			long length = blob.length();
			if (length > Integer.MAX_VALUE) {
				throw new IllegalArgumentException("blob length too big: " + blob.length());
			}

			int len = (int) length;
			if (0 == len) {
				return new byte[0];
			}
			return blob.getBytes(1, len);
		} catch (SQLException e) {
			LOG.error(ExceptionUtils.errorLogEntry(e));
			return new byte[0];
		}
	}

	public static byte[] read(InputStream in, int maxlength) throws IOException {
		Assert.notNull(in, "in should not be null");

		int buflen = maxlength / 10;
		if (buflen < DEFAULT_BUFSIZE) {
			buflen = DEFAULT_BUFSIZE;
		}
		byte[] buf = new byte[buflen];
		byte[] result = new byte[0];

		int read;
		while ((read = in.read(buf)) >= 0) {
			byte[] realloc = new byte[result.length + read];
			System.arraycopy(result, 0, realloc, 0, result.length);
			System.arraycopy(buf, 0, realloc, result.length, read);
			result = realloc;
		}

		return result;
	}

	public static String bytes2base64str(byte[] bytes, boolean urlSafe) {
		Assert.notNull(bytes, BYTES_CANNOT_BE_NULL);
		return urlSafe ? 
				Base64.encodeBase64URLSafeString(bytes) : 
				Base64.encodeBase64String(bytes);
	}

	public static byte[] base64str2bytes(String base64String) {
		Assert.notNull(base64String, "base64String should not be null");
		return Base64.decodeBase64(base64String);
	}

	public static String base64str2str(String base64String) {
		Assert.notNull(base64String, "base64String should not be null");
		return utf2string(Base64.decodeBase64(base64String));
	}

	public static String base64bytes2str(byte[] bytes64) {
		Assert.notNull(bytes64, "bytes64 should not be null");
		return utf2string(Base64.decodeBase64(bytes64));
	}


	/**
	 * Python-like slices support for byte[] arrays
	 * 
	 * @param bytes
	 * @param fromIndex
	 *            if null, 0 is assumed. If negative,
	 *            bytes.length+fromIndex is assumed
	 * 
	 * @param toIndex
	 *            if null, bytes.length is assumed. If negative,
	 *            bytes.length+toIndex is assumed
	 * @return empty array if resulting slice toIndex is less than fromIndex or
	 *         sliced arrat otherwise
	 * @throws ArrayIndexOutOfBoundsException
	 *             if any index is out of bytes bounds
	 */
	public static byte[] slice(byte[] bytes, Integer fromIndex, Integer toIndex) {
		Assert.notNull(bytes, BYTES_CANNOT_BE_NULL);

		int from = null != fromIndex ? fromIndex : 0;
		int to = null != toIndex ? toIndex : bytes.length;

		if (from < 0) {
			from = bytes.length + from;
		}

		if (to < 0) {
			to = bytes.length + to;
		}

		if (from < 0 || from > bytes.length) {
			throw new ArrayIndexOutOfBoundsException(fromIndex + " is out of bounds");
		}

		if (to < 0 || to > bytes.length) {
			throw new ArrayIndexOutOfBoundsException(toIndex + " is out of bounds");
		}

		if (from >= to) {
			return new byte[0];
		}

		byte[] result = new byte[to - from];
		System.arraycopy(bytes, from, result, 0, to - from);
		return result;
	}

	public static byte[] md5(byte[] data) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
			return md5.digest(data);
		} catch (NoSuchAlgorithmException e) {
			ExceptionUtils.propagate(e);
			return new byte[0];
		}
	}

	/**
	 * Generate long hash for an array of bytes.
	 */
	public static long longHash(byte ... bytes) {
		long result = 1;
		for (byte b : bytes) {
			result = HASHPRIME * result + b;
		}
		return result;
	}
	
	/**
	 * Generate long hash for 2 arrays of bytes
	 */
	public static long longHash(byte [] arr1, byte [] arr2) {
		return HASHPRIME*longHash(arr1) + longHash(arr2);
	}
	
	/**
	 * Generate long hash for 3 arrays of bytes
	 */
	public static long longHash(byte [] arr1, byte [] arr2, byte [] arr3) {
		return HASHPRIME*longHash(arr1, arr2) + longHash(arr3);
	}

	/**
	 * Generate long hash for 4 arrays of bytes
	 */
	public static long longHash(byte [] arr1, byte [] arr2, byte [] arr3, byte [] arr4) {
		return HASHPRIME*longHash(arr1, arr2, arr3) + longHash(arr4);
	}
	
	/**
	 * Generate long hash for 5 arrays of bytes
	 */
	public static long longHash(byte [] arr1, byte [] arr2, byte [] arr3, byte [] arr4, byte [] arr5) {
		return HASHPRIME*longHash(arr1, arr2, arr3, arr4) + longHash(arr5);
	}

	/**
	 * Marshalls several byte[] arrays into one single array. Remembers 
	 * the array lengths so original arrays could be restored back.
	 * @author borisman
	 *
	 */
	public static final class ChunkedArray {
		private final byte [] record;
		private final int [] lengths;

		private ChunkedArray(int chunksNum, byte [] ... chunks) {
			Assert.notNull(chunks, "chunks cannot be null");
			Assert.isTrue(chunksNum < 256, "Up to 256 chunks supported");
			Assert.isTrue(chunksNum == chunks.length);

			lengths = new int[chunks.length];
			int totalLen = 0;
			for (int i=0; i<chunks.length; i++) {
				int len = chunks[i].length;
				lengths[i] = len;
				totalLen += len;
			}

			record = new byte [totalLen + lengths.length*4 + 1];
			ByteBuffer b = ByteBuffer.wrap(record);
			for (byte [] chunk : chunks) {
				b.put(chunk);
			}
			for (int len : lengths) {
				b.putInt(len);
			}
			b.put((byte)(lengths.length));
		}
		
		private ChunkedArray(byte [] record) throws ParseException {
			Assert.notNull(record, "record cannot be null");
			
			this.record = Arrays.copyOf(record, record.length);

			if (record.length == 0) {
				throw new ParseException(bytes2hex(record), 0);
			}

			int lengthsNum = 0xFF & record[record.length-1];
			int lengthsLen = lengthsNum * 4;
			if (lengthsLen+1>record.length) {
				throw new ParseException(bytes2hex(record), record.length);
			}

			ByteBuffer b = ByteBuffer.wrap(record);
			b.position(record.length-lengthsLen-1);

			lengths = new int[lengthsNum];
			int totalLength = lengthsLen + 1;
			for (int i=0; i<lengthsNum; i++) {
				int len = b.getInt();
				lengths[i] = len;
				totalLength += len;
			}
			if (totalLength != record.length) {
				throw new ParseException(bytes2hex(record), 0);
			}
		}

		public static ChunkedArray create(byte [] ... chunks) {
			return new ChunkedArray(chunks.length, chunks);
		}

		public static ChunkedArray parse(byte [] record) throws ParseException {
			return new ChunkedArray(record);
		}

		public int size() {
			return lengths.length;
		}

		public byte [] getChunk(int chunk) {
			Assert.isTrue(0 <= chunk && chunk < lengths.length, "index out of range");

			int offset = 0;
			for (int i=0; i<chunk; i++) {
				offset += lengths[i];
			}

			byte [] result = new byte[lengths[chunk]];
			System.arraycopy(record, offset, result, 0, result.length);
			return result;
		}

		public byte [] marshall() {
			return Arrays.copyOf(record, record.length);
		}
	}
}
