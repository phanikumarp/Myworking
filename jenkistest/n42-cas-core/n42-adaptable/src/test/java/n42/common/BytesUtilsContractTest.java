package n42.common;

import java.text.ParseException;

import org.junit.Test;

public class BytesUtilsContractTest {

	@Test(expected = IllegalArgumentException.class)
	public void testString2utfShouldFailDueToNullParameter() {
		BytesUtils.string2utf(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUtf2stringShouldFailDueToNullParameter() {
		BytesUtils.utf2string(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBytes2hexShouldFailDueToNullParameter() {
		BytesUtils.bytes2hex(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testHex2bytesShouldFailDueToNullParameter() throws ParseException {
		BytesUtils.hex2bytes(null);
	}

	@Test(expected = ParseException.class)
	public void testHex2bytesShouldFailDueToBadStringLength() throws ParseException {
		BytesUtils.hex2bytes("a");
	}

	@Test(expected = ParseException.class)
	public void testHex2bytesShouldFailDueToBadCharacters() throws ParseException {
		BytesUtils.hex2bytes("a*");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testHex2longShouldFailDueToNullParameter() throws ParseException {
		BytesUtils.hex2long(null);
	}

	@Test(expected = ParseException.class)
	public void testHex2longShouldFailDueToBadCharactersInString() throws ParseException {
		BytesUtils.hex2long("a*");

	}

	@Test(expected = ParseException.class)
	public void testHex2intShouldFailDueToStringTooLong() throws ParseException {
		BytesUtils.hex2int("010203040506070809");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testHex2intShouldFailDueToNullParameter() throws ParseException {
		BytesUtils.hex2int(null);
	}

}
