package n42.common;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;

public class BytesUtilsTest {
	private static final Random RND = new Random();

	@Test
	public void testUtf() {
		for (String s: new String[]{"", "aaa", "abba", "привет"}) {
			String recoded = BytesUtils.utf2string(BytesUtils.string2utf(s));
			assertEquals(s, recoded);
		}
	}

	@Test
	public void testBytes2hex() {
		assertEquals("", BytesUtils.bytes2hex(bytes()));
		assertEquals("cafe0001", BytesUtils.bytes2hex(bytes(0xCA, 0xFE, 0, 1)));
		assertEquals("0102ff", BytesUtils.bytes2hex(bytes(0x01, 0x02, 0xFF)));
	}
	
	@Test(expected=ParseException.class)
	public void testHex2bytesCaseIsImportantAndShouldBeLowercase() throws ParseException {
		BytesUtils.hex2bytes("CAFE0001");
	}

	@Test
	public void testHex2bytes() throws ParseException {
		assertArrayEquals(bytes(), BytesUtils.hex2bytes(""));
		assertArrayEquals(bytes(0xCA, 0xFE, 0, 1), BytesUtils.hex2bytes("cafe0001"));
		assertArrayEquals(bytes(0x01, 0x02, 0xFF), BytesUtils.hex2bytes("0102ff"));
	}

	@Test
	public void testLong2hex() {
		assertEquals("0", BytesUtils.long2hex(0L));
		assertEquals("1", BytesUtils.long2hex(1L));
		assertEquals("1a", BytesUtils.long2hex(0x1aL));
		assertEquals("1a2b", BytesUtils.long2hex(0x1a2bL));
		assertEquals("1a2b3c", BytesUtils.long2hex(0x1a2b3cL));
		assertEquals("1a2b3c4d", BytesUtils.long2hex(0x1a2b3c4dL));
		assertEquals("1a2b3c4d5e", BytesUtils.long2hex(0x1a2b3c4d5eL));
		assertEquals("1a2b3c4d5e6f", BytesUtils.long2hex(0x1a2b3c4d5e6fL));
		assertEquals("1a2b3c4d5e6f70", BytesUtils.long2hex(0x1a2b3c4d5e6f70L));
		assertEquals("1a2b3c4d5e6f7081", BytesUtils.long2hex(0x1a2b3c4d5e6f7081L));
		assertEquals("ffffffffffffffff", BytesUtils.long2hex(0xffffffffffffffffL));
	}

	@Test
	public void testHex2long() throws ParseException {
		assertEquals(BytesUtils.hex2long("0"), 0L);
		assertEquals(BytesUtils.hex2long("1"), 0x1L);
		assertEquals(BytesUtils.hex2long("1a"), 0x1aL);
		assertEquals(BytesUtils.hex2long("1a2b"), 0x1a2bL);
		assertEquals(BytesUtils.hex2long("1a2b3c"), 0x1a2b3cL);
		assertEquals(BytesUtils.hex2long("1a2b3c4d"), 0x1a2b3c4dL);
		assertEquals(BytesUtils.hex2long("1a2b3c4d5e"), 0x1a2b3c4d5eL);
		assertEquals(BytesUtils.hex2long("1a2b3c4d5e6f"), 0x1a2b3c4d5e6fL);
		assertEquals(BytesUtils.hex2long("1a2b3c4d5e6f70"), 0x1a2b3c4d5e6f70L);
		assertEquals(BytesUtils.hex2long("1a2b3c4d5e6f7081"), 0x1a2b3c4d5e6f7081L);
	}

	@Test
	public void testInt2hex() {
		assertEquals("0", BytesUtils.int2hex(0));
		assertEquals("1", BytesUtils.int2hex(1));
		assertEquals("1a", BytesUtils.int2hex(0x1a));
		assertEquals("1a2b", BytesUtils.int2hex(0x1a2b));
		assertEquals("1a2b3c", BytesUtils.int2hex(0x1a2b3c));
		assertEquals("1a2b3c4d", BytesUtils.int2hex(0x1a2b3c4d));
		assertEquals("ffffffff", BytesUtils.int2hex(0xffffffff));
	}

	@Test
	public void testHex2int() throws ParseException {
		assertEquals(0, BytesUtils.hex2int("0"));
		assertEquals(1, BytesUtils.hex2int("1"));
		assertEquals(0x1a, BytesUtils.hex2int("1a"));
		assertEquals(0x1a2b, BytesUtils.hex2int("1a2b"));
		assertEquals(0x1a2b3c, BytesUtils.hex2int("1a2b3c"));
		assertEquals(0x1a2b3c4d, BytesUtils.hex2int("1a2b3c4d"));
		assertEquals(0xffffffff, BytesUtils.hex2int("ffffffff"));
	}

	@Test
	public void testInt2bytes() {
		assertArrayEquals(bytes(0x0, 0x0, 0x0, 0x0), BytesUtils.int2bytes(0));
		assertArrayEquals(bytes(0x0, 0x0, 0x0, 0x1), BytesUtils.int2bytes(1));
		assertArrayEquals(bytes(0x0, 0x0, 0x0, 0x1a), BytesUtils.int2bytes(0x1a));
		assertArrayEquals(bytes(0x0, 0x0, 0x1a, 0x2b), BytesUtils.int2bytes(0x1a2b));
		assertArrayEquals(bytes(0x0, 0x1a, 0x2b, 0x3c), BytesUtils.int2bytes(0x1a2b3c));
		assertArrayEquals(bytes(0x1a, 0x2b, 0x3c, 0x4d), BytesUtils.int2bytes(0x1a2b3c4d));
		assertArrayEquals(bytes(0xFF, 0xFA, 0xFB, 0xFC), BytesUtils.int2bytes(0xFFFAFBFC));
		assertArrayEquals(bytes(0xFF, 0xFF, 0xFF, 0xFF), BytesUtils.int2bytes(0xFFFFFFFF));
	}

	@Test
	public void testbytes2int() {
		assertEquals(0, BytesUtils.bytes2int(bytes(0x0, 0x0, 0x0, 0x0)));
		assertEquals(1, BytesUtils.bytes2int(bytes(0x0, 0x0, 0x0, 0x1)));
		assertEquals(0x1a, BytesUtils.bytes2int(bytes(0x0, 0x0, 0x0, 0x1a)));
		assertEquals(0x1a2b, BytesUtils.bytes2int(bytes(0x0, 0x0, 0x1a, 0x2b)));
		assertEquals(0x1a2b3c, BytesUtils.bytes2int(bytes(0x0, 0x1a, 0x2b, 0x3c)));
		assertEquals(0x1a2b3c4d, BytesUtils.bytes2int(bytes(0x1a, 0x2b, 0x3c, 0x4d)));
		assertEquals(0xFfFaFbFc, BytesUtils.bytes2int(bytes(0xFf, 0xFa, 0xFb, 0xFc)));
		assertEquals(0xFFFFFFFF, BytesUtils.bytes2int(bytes(0xFF, 0xFF, 0xFF, 0xFF)));
	}

	@Test
	public void testshort2bytes() {
		assertArrayEquals(bytes(0x0, 0x0), BytesUtils.short2bytes((short)0));
		assertArrayEquals(bytes(0x0, 0x1), BytesUtils.short2bytes((short)1));
		assertArrayEquals(bytes(0x0, 0x1a), BytesUtils.short2bytes((short)0x1a));
		assertArrayEquals(bytes(0x1a, 0x2b), BytesUtils.short2bytes((short)0x1a2b));
		assertArrayEquals(bytes(0xFF, 0xFA), BytesUtils.short2bytes((short)0xFFFA));
		assertArrayEquals(bytes(0xFF, 0xFF), BytesUtils.short2bytes((short)0xFFFFF));
	}

	@Test
	public void testbytes2short() {
		assertEquals(0, BytesUtils.bytes2short(bytes(0x0, 0x0)));
		assertEquals(1, BytesUtils.bytes2short(bytes(0x0, 0x1)));
		assertEquals((short)0x1a, BytesUtils.bytes2short(bytes(0x0, 0x1a)));
		assertEquals((short)0x1a2b, BytesUtils.bytes2short(bytes(0x1a, 0x2b)));
		assertEquals((short)0xFfFa, BytesUtils.bytes2short(bytes(0xFf, 0xFa)));
		assertEquals((short)0xFFFF, BytesUtils.bytes2short(bytes(0xFF, 0xFF)));
	}

	@Test
	public void testLong2bytes() {
		assertArrayEquals(bytes(0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0), BytesUtils.long2bytes(0L));
		assertArrayEquals(bytes(0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1), BytesUtils.long2bytes(1L));
		assertArrayEquals(bytes(0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1a), BytesUtils.long2bytes(0x1aL));
		assertArrayEquals(bytes(0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1a, 0x2b), BytesUtils.long2bytes(0x1a2bL));
		assertArrayEquals(bytes(0x0, 0x0, 0x0, 0x0, 0x0, 0x1a, 0x2b, 0x3c), BytesUtils.long2bytes(0x1a2b3cL));
		assertArrayEquals(bytes(0x0, 0x0, 0x0, 0x0, 0x1a, 0x2b, 0x3c, 0x4d), BytesUtils.long2bytes(0x1a2b3c4dL));
		assertArrayEquals(bytes(0x0, 0x0, 0x0, 0x1a, 0x2b, 0x3c, 0x4d, 0x5e), BytesUtils.long2bytes(0x1a2b3c4d5eL));
		assertArrayEquals(bytes(0x0, 0x0, 0x1a, 0x2b, 0x3c, 0x4d, 0x5e, 0x6f), BytesUtils.long2bytes(0x1a2b3c4d5e6fL));
		assertArrayEquals(bytes(0x0, 0x1a, 0x2b, 0x3c, 0x4d, 0x5e, 0x6f, 0x7a), BytesUtils.long2bytes(0x1a2b3c4d5e6f7aL));
		assertArrayEquals(bytes(0x1a, 0x2b, 0x3c, 0x4d, 0x5e, 0x6f, 0x7a, 0x8b), BytesUtils.long2bytes(0x1a2b3c4d5e6f7a8bL));
		assertArrayEquals(bytes(0xFf, 0xFa, 0xFb, 0xFc, 0xFe, 0xFd, 0x01, 0x02), BytesUtils.long2bytes(0xFfFaFbFcFeFd0102L));
		assertArrayEquals(bytes(0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF), BytesUtils.long2bytes(0xFFFFFFFFFFFFFFFFL));
	}

	@Test
	public void testbytes2long() {
		assertEquals(0x0L, BytesUtils.bytes2long(bytes(0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0)));
		assertEquals(0x1L, BytesUtils.bytes2long(bytes(0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1)));
		assertEquals(0x1aL, BytesUtils.bytes2long(bytes(0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1a)));
		assertEquals(0x1a2bL, BytesUtils.bytes2long(bytes(0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1a, 0x2b)));
		assertEquals(0x1a2b3cL, BytesUtils.bytes2long(bytes(0x0, 0x0, 0x0, 0x0, 0x0, 0x1a, 0x2b, 0x3c)));
		assertEquals(0x1a2b3c4dL, BytesUtils.bytes2long(bytes(0x0, 0x0, 0x0, 0x0, 0x1a, 0x2b, 0x3c, 0x4d)));
		assertEquals(0x1a2b3c4d5eL, BytesUtils.bytes2long(bytes(0x0, 0x0, 0x0, 0x1a, 0x2b, 0x3c, 0x4d, 0x5e)));
		assertEquals(0x1a2b3c4d5e6fL, BytesUtils.bytes2long(bytes(0x0, 0x0, 0x1a, 0x2b, 0x3c, 0x4d, 0x5e, 0x6f)));
		assertEquals(0x1a2b3c4d5e6f7aL, BytesUtils.bytes2long(bytes(0x0, 0x1a, 0x2b, 0x3c, 0x4d, 0x5e, 0x6f, 0x7a)));
		assertEquals(0x1a2b3c4d5e6f7a8bL, BytesUtils.bytes2long(bytes(0x1a, 0x2b, 0x3c, 0x4d, 0x5e, 0x6f, 0x7a, 0x8b)));
		assertEquals(0xFfFaFbFcFdFe0102L, BytesUtils.bytes2long(bytes(0xFf, 0xFa, 0xFb, 0xFc, 0xFd, 0xFe, 0x01, 0x02)));
		assertEquals(0xFFFFFFFFFFFFFFFFL, BytesUtils.bytes2long(bytes(0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF)));
	}

	@Test
	public void testdate2bytes() {
		assertArrayEquals(bytes(0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0), BytesUtils.date2bytes(new Date(0L)));
		assertArrayEquals(bytes(0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1), BytesUtils.date2bytes(new Date(1L)));
		assertArrayEquals(bytes(0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1a), BytesUtils.date2bytes(new Date(0x1aL)));
		assertArrayEquals(bytes(0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1a, 0x2b), BytesUtils.date2bytes(new Date(0x1a2bL)));
		assertArrayEquals(bytes(0x0, 0x0, 0x0, 0x0, 0x0, 0x1a, 0x2b, 0x3c), BytesUtils.date2bytes(new Date(0x1a2b3cL)));
		assertArrayEquals(bytes(0x0, 0x0, 0x0, 0x0, 0x1a, 0x2b, 0x3c, 0x4d), BytesUtils.date2bytes(new Date(0x1a2b3c4dL)));
		assertArrayEquals(bytes(0x0, 0x0, 0x0, 0x1a, 0x2b, 0x3c, 0x4d, 0x5e), BytesUtils.date2bytes(new Date(0x1a2b3c4d5eL)));
		assertArrayEquals(bytes(0x0, 0x0, 0x1a, 0x2b, 0x3c, 0x4d, 0x5e, 0x6f), BytesUtils.date2bytes(new Date(0x1a2b3c4d5e6fL)));
		assertArrayEquals(bytes(0x0, 0x1a, 0x2b, 0x3c, 0x4d, 0x5e, 0x6f, 0x7a), BytesUtils.date2bytes(new Date(0x1a2b3c4d5e6f7aL)));
		assertArrayEquals(bytes(0x1a, 0x2b, 0x3c, 0x4d, 0x5e, 0x6f, 0x7a, 0x8b), BytesUtils.date2bytes(new Date(0x1a2b3c4d5e6f7a8bL)));
		assertArrayEquals(bytes(0xFf, 0xFa, 0xFb, 0xFc, 0xFe, 0xFd, 0x01, 0x02), BytesUtils.date2bytes(new Date(0xFfFaFbFcFeFd0102L)));
		assertArrayEquals(bytes(0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF), BytesUtils.date2bytes(new Date(0xFFFFFFFFFFFFFFFFL)));
	}

	@Test
	public void testbytes2date() {
		assertEquals(new Date(0x0L), BytesUtils.bytes2date(bytes(0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0)));
		assertEquals(new Date(0x1L), BytesUtils.bytes2date(bytes(0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1)));
		assertEquals(new Date(0x1aL), BytesUtils.bytes2date(bytes(0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1a)));
		assertEquals(new Date(0x1a2bL), BytesUtils.bytes2date(bytes(0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1a, 0x2b)));
		assertEquals(new Date(0x1a2b3cL), BytesUtils.bytes2date(bytes(0x0, 0x0, 0x0, 0x0, 0x0, 0x1a, 0x2b, 0x3c)));
		assertEquals(new Date(0x1a2b3c4dL), BytesUtils.bytes2date(bytes(0x0, 0x0, 0x0, 0x0, 0x1a, 0x2b, 0x3c, 0x4d)));
		assertEquals(new Date(0x1a2b3c4d5eL), BytesUtils.bytes2date(bytes(0x0, 0x0, 0x0, 0x1a, 0x2b, 0x3c, 0x4d, 0x5e)));
		assertEquals(new Date(0x1a2b3c4d5e6fL), BytesUtils.bytes2date(bytes(0x0, 0x0, 0x1a, 0x2b, 0x3c, 0x4d, 0x5e, 0x6f)));
		assertEquals(new Date(0x1a2b3c4d5e6f7aL), BytesUtils.bytes2date(bytes(0x0, 0x1a, 0x2b, 0x3c, 0x4d, 0x5e, 0x6f, 0x7a)));
		assertEquals(new Date(0x1a2b3c4d5e6f7a8bL), BytesUtils.bytes2date(bytes(0x1a, 0x2b, 0x3c, 0x4d, 0x5e, 0x6f, 0x7a, 0x8b)));
		assertEquals(new Date(0xFfFaFbFcFdFe0102L), BytesUtils.bytes2date(bytes(0xFf, 0xFa, 0xFb, 0xFc, 0xFd, 0xFe, 0x01, 0x02)));
		assertEquals(new Date(0xFFFFFFFFFFFFFFFFL), BytesUtils.bytes2date(bytes(0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF)));
	}

	@Test
	public void testconcat() {
		assertArrayEquals(bytes(), BytesUtils.concat(bytes(), bytes()));
		assertArrayEquals(bytes(1), BytesUtils.concat(bytes(1), bytes()));
		assertArrayEquals(bytes(1), BytesUtils.concat(bytes(), bytes(1)));
		assertArrayEquals(bytes(1, 2), BytesUtils.concat(bytes(1), bytes(2)));
		assertArrayEquals(bytes(1, 2, 3, 4), BytesUtils.concat(bytes(1, 2), bytes(3, 4)));
		assertArrayEquals(bytes(1, 2, 3, 4), BytesUtils.concat(bytes(1, 2), null, bytes(3, 4), null));
	}

	@Test
	public void testconcatByte() {
		assertArrayEquals(bytes(0), BytesUtils.concat(bytes(), (byte) 0));
		assertArrayEquals(bytes(0, 1), BytesUtils.concat(bytes(0), (byte) 1));
		assertArrayEquals(bytes(0, 1, 2), BytesUtils.concat(bytes(0, 1), (byte) 2));
	}

	@Test
	public void testIncrement() {
		assertTrue(BytesUtils.increment(bytes()));
		
		byte [] arr = bytes(0);
		assertFalse(BytesUtils.increment(arr));
		assertArrayEquals(bytes(1), arr);
		
		arr = bytes(0, 1);
		assertFalse(BytesUtils.increment(arr));
		assertArrayEquals(bytes(0, 2), arr);
		
		arr = bytes(0, 0xFF);
		assertFalse(BytesUtils.increment(arr));
		assertArrayEquals(bytes(1, 0), arr);

		arr = bytes(0xFF, 0xFF);
		assertTrue(BytesUtils.increment(arr));
		assertArrayEquals(bytes(0, 0), arr);
	}
	
	@Test
	public void testIncrementString() {
		assertEquals("", BytesUtils.incrementString(""));
		assertEquals("2", BytesUtils.incrementString("1"));
		assertEquals(string(1, 0), BytesUtils.incrementString(string(0, 65535)));

	}

	@Test
	public void testbool2bytes() {
		assertArrayEquals(bytes(0), BytesUtils.bool2bytes(false));
		assertArrayEquals(bytes(1), BytesUtils.bool2bytes(true));
	}

	@Test
	public void testbytes2bool() {
		assertEquals(false, BytesUtils.bytes2bool(bytes(0)));
		assertEquals(true, BytesUtils.bytes2bool(bytes(1)));
	}
	
	@Test
	public void testBlob2BytesAndBytes2Blob() {
		testBlob2BytesAndBytes2Blob(bytes());
		testBlob2BytesAndBytes2Blob(bytes(1));
		testBlob2BytesAndBytes2Blob(bytes(1,2));
		testBlob2BytesAndBytes2Blob(bytes(1,2,3));
		testBlob2BytesAndBytes2Blob(bytes(1,2,3,4));
	}
	
	@Test
	public void testRead() throws IOException {
		testRead("".getBytes(), 0);
		testRead("Hello world".getBytes(), 10);
		testRead(randomBytes(4096), 0);
		testRead(randomBytes(8192), 0);
		testRead(randomBytes(8192), 5000);
		testRead(randomBytes(6127670), -1);
	}
	
	@Test
	public void testBase64urlsafeEncode() {
		assertEquals("", BytesUtils.bytes2base64str(bytes(), true));
		assertEquals("AQ", BytesUtils.bytes2base64str(bytes(1), true));
		assertEquals("AQI", BytesUtils.bytes2base64str(bytes(1,2), true));
		assertEquals("AQID", BytesUtils.bytes2base64str(bytes(1,2,3), true));
	}
	
	@Test
	public void testbytesFromBase64urlsafe() {
		assertArrayEquals(bytes(), BytesUtils.base64str2bytes(""));
		assertArrayEquals(bytes(1), BytesUtils.base64str2bytes("AQ"));
		assertArrayEquals(bytes(1,2), BytesUtils.base64str2bytes("AQI"));
		assertArrayEquals(bytes(1,2,3), BytesUtils.base64str2bytes("AQID"));
		assertArrayEquals(bytes(1,2,3), BytesUtils.base64str2bytes("AQID=="));
	}
	@Ignore
	@Test
	public void toStringFromBase64String() {
		assertEquals("", BytesUtils.base64str2str(""));
		assertEquals("N", BytesUtils.base64str2str("Tw"));
		assertEquals("N4", BytesUtils.base64str2str("T0k"));
		assertEquals("N42", BytesUtils.base64str2str("T0lR"));
		assertEquals("N42 Inc.", BytesUtils.base64str2str("T0lRIEluYy4"));
	}
	@Ignore
	@Test
	public void toStringFromBase64Bytes() {
		assertEquals("", BytesUtils.base64bytes2str(bytes()));
		assertEquals("N", BytesUtils.base64bytes2str("Tw".getBytes(Charsets.UTF8)));
		assertEquals("N4", BytesUtils.base64bytes2str("T0k".getBytes(Charsets.UTF8)));
		assertEquals("N42", BytesUtils.base64bytes2str("T0lR".getBytes(Charsets.UTF8)));
		assertEquals("N42 Inc.", BytesUtils.base64bytes2str("T0lRIEluYy4".getBytes(Charsets.UTF8)));
	}

	@Test
	public void testReadAllBytes() throws IOException {
		assertArrayEquals(bytes(), IOUtils.toByteArray(new ByteArrayInputStream(bytes())));
		assertArrayEquals(bytes(1), IOUtils.toByteArray(new ByteArrayInputStream(bytes(1))));
		assertArrayEquals(bytes(1,2,3), IOUtils.toByteArray(new ByteArrayInputStream(bytes(1,2,3))));
		assertArrayEquals(bytes(1,2,3,4), IOUtils.toByteArray(new ByteArrayInputStream(bytes(1,2,3,4))));
	}
	
	@Test
	public void testSlice() throws IOException {
		assertArrayEquals(bytes(), BytesUtils.slice(bytes(), null, null));
		assertArrayEquals(bytes(1), BytesUtils.slice(bytes(1), 0, 1));
		assertArrayEquals(bytes(1), BytesUtils.slice(bytes(1), null, 1));
		assertArrayEquals(bytes(1,2), BytesUtils.slice(bytes(1,2), 0, 2));
		assertArrayEquals(bytes(1,2), BytesUtils.slice(bytes(1,2), null, 2));
		assertArrayEquals(bytes(2), BytesUtils.slice(bytes(1,2), 1, null));
		assertArrayEquals(bytes(2,3), BytesUtils.slice(bytes(1,2,3), 1, 3));
		assertArrayEquals(bytes(3,4), BytesUtils.slice(bytes(1,2,3,4,5), 2, 4));
		assertArrayEquals(bytes(4,5), BytesUtils.slice(bytes(1,2,3,4,5), -2, null));
		assertArrayEquals(bytes(), BytesUtils.slice(bytes(1,2,3,4,5), 5, 1));
	}

	@Test(expected=ArrayIndexOutOfBoundsException.class)
	public void testSliceWrongFromIndex() throws IOException {
		BytesUtils.slice(bytes(1,2), 3, null);
	}
	
	@Test(expected=ArrayIndexOutOfBoundsException.class)
	public void testSliceWrongToIndex() throws IOException {
		BytesUtils.slice(bytes(1,2), null, 3);
	}
	
	@Test(expected=ArrayIndexOutOfBoundsException.class)
	public void testSliceWrongNegativeFromIndex() throws IOException {
		BytesUtils.slice(bytes(1,2), -5, null);
	}
	
	@Test(expected=ArrayIndexOutOfBoundsException.class)
	public void testSliceWrongNegativeToIndex() throws IOException {
		BytesUtils.slice(bytes(1,2), null, -5);
	}
	
	@Test
	public void testMD5() {
		byte [] digest = BytesUtils.md5(new byte[0]);
		assertEquals(16, digest.length);
		
		assertArrayEquals(BytesUtils.md5(new byte[0]), BytesUtils.md5(new byte[0]));
		assertArrayEquals(BytesUtils.md5(BytesUtils.string2utf("aaa")), BytesUtils.md5(BytesUtils.string2utf("aaa")));
		assertArrayEquals(BytesUtils.md5(BytesUtils.string2utf("aaabbbccccdddd")), BytesUtils.md5(BytesUtils.string2utf("aaabbbccccdddd")));
	}
	
	@Test
	public void testLongHash() {
		assertEquals(BytesUtils.longHash(bytes()), BytesUtils.longHash(bytes()));
		assertEquals(BytesUtils.longHash(bytes(1)), BytesUtils.longHash(bytes(1)));
		assertEquals(BytesUtils.longHash(bytes(1,2)), BytesUtils.longHash(bytes(1,2)));
		assertEquals(BytesUtils.longHash(bytes(1,2,3)), BytesUtils.longHash(bytes(1,2,3)));
		assertEquals(BytesUtils.longHash(bytes(1,2,3,4)), BytesUtils.longHash(bytes(1,2,3,4)));
		assertEquals(BytesUtils.longHash(bytes(1,2,3,4,5)), BytesUtils.longHash(bytes(1,2,3,4,5)));
		assertEquals(BytesUtils.longHash(bytes(1,2,3,4,5,6)), BytesUtils.longHash(bytes(1,2,3,4,5,6)));
		assertEquals(BytesUtils.longHash(bytes(1,2,3,4,5,6,7)), BytesUtils.longHash(bytes(1,2,3,4,5,6,7)));
		assertEquals(BytesUtils.longHash(bytes(1,2,3,4,5,6,7,8)), BytesUtils.longHash(bytes(1,2,3,4,5,6,7,8)));
		
		assertDifferentHashes(bytes(1), bytes(0));
		assertDifferentHashes(bytes(1,2), bytes(1,0));
		assertDifferentHashes(bytes(1,2,3), bytes(1,2,0));
		assertDifferentHashes(bytes(1,2,3,4), bytes(1,2,3,0));
		assertDifferentHashes(bytes(1,2,3,4,5), bytes(1,2,3,4,0));
		assertDifferentHashes(bytes(1,2,3,4,5,6), bytes(1,2,3,4,5,0));
		assertDifferentHashes(bytes(1,2,3,4,5,6,7), bytes(1,2,3,4,5,6,0));
		assertDifferentHashes(bytes(1,2,3,4,5,6,7,8), bytes(1,2,3,4,5,6,7,0));
	}
	
	private void assertDifferentHashes(byte[] arr1, byte[] arr2) {
		long hash1 = BytesUtils.longHash(arr1);
		long hash2 = BytesUtils.longHash(arr2);
		
		if (hash1 == hash2) {
			fail(hash1+" is hash for both "+BytesUtils.bytesToHumanReadableString(arr1)+" and "+BytesUtils.bytesToHumanReadableString(arr2));
		}
	}

	private void testRead(byte[] bytes, int maxlen) throws IOException {
		InputStream wrapper = new ByteArrayInputStream(bytes);
		byte [] read = BytesUtils.read(wrapper, maxlen);
		assertArrayEquals(bytes, read);
	}

	private void testBlob2BytesAndBytes2Blob(byte[] bytes) {
		assertArrayEquals(bytes, BytesUtils.blob2bytes(BytesUtils.bytes2blob(bytes)));
	}
	
	private static String string(int... chars) {
		char [] ch = new char[chars.length];
		
		for (int i=0; i<chars.length; i++) {
			ch[i] = (char) chars[i];
		}
		
		return new String(ch);
	}

	private static byte[] bytes(int... vals) {
		byte[] result = new byte[vals.length];
		int i = 0;
		for (int v: vals) {
			if (v < 0 || v > 255) {
				throw new IllegalArgumentException("byte value " + v + " not in byte range");
			}
			v = v & 0xFF;
			result[i] = (byte) v;
			i++;
		}
		return result;
	}
	
	private static byte [] randomBytes(int length) {
		byte [] result = new byte[length];
		RND.nextBytes(result);
		return result;
	}
}
