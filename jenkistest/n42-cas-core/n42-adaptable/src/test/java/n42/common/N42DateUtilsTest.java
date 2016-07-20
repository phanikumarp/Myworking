package n42.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

public class N42DateUtilsTest {

	@Test
	public void testBefore() {
		assertFalse(N42DateUtils.before(null, null));
		assertFalse(N42DateUtils.before(null, new Date()));
		assertFalse(N42DateUtils.before(new Date(0l), new Date(0l)));
		assertFalse(N42DateUtils.before(new Date(), new Date(0l)));
		assertTrue(N42DateUtils.before(new Date(), null));
		assertTrue(N42DateUtils.before(new Date(0l), new Date()));
	}

	@Test
	public void testCopyOf() {
		Date date = new Date();
		Date other = new Date(0l);

		assertNotSame(date, N42DateUtils.copyOf(date));
		assertEquals(date, N42DateUtils.copyOf(date));

		assertNotSame(date, N42DateUtils.copyOf(date, other));
		assertEquals(date, N42DateUtils.copyOf(date, other));

		assertSame(other, N42DateUtils.copyOf(null, other));
		assertEquals(other, N42DateUtils.copyOf(null, other));
	}
}
