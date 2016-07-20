package n42.common;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.beans.Transient;

import org.junit.Test;

public class TransientDiscoveryTest {
	
	@Test
	public void testTransientField() {
		assertTrue(TransientDiscovery.isTransient(Klass.class, "transientField"));
	}

	@Test
	public void testTransientViaGetter() {
		assertTrue(TransientDiscovery.isTransient(Klass.class, "transientViaGetter"));
	}

	@Test
	public void testTransientTransientFieldAndGetter() {
		assertTrue(TransientDiscovery.isTransient(Klass.class, "transientFieldAndGetter"));
	}
	
	@Test
	public void testTransientNonTransient() {
		assertFalse(TransientDiscovery.isTransient(Klass.class, "nonTransient"));
	}
	
	@Test
	public void testCannotDiscover() {
		assertFalse(TransientDiscovery.isTransient(Klass.class, "discover"));
	}
	
	@Test
	public void testMissingField() {
		assertFalse(TransientDiscovery.isTransient(Klass.class, "missing"));
	}
	
	@SuppressWarnings("unused")
	private static class Klass {
		private transient Object transientField;
		private Object transientViaGetter;
		private transient Object transientFieldAndGetter;
		private Object nonTransient;
		private transient Object cannotDiscover;

		public Object getTransientField() {
			return transientField;
		}
		
		@Transient
		public Object getTransientViaGetter() {
			return transientViaGetter;
		}

		@Transient
		public Object getTransientFieldAndGetter() {
			return transientFieldAndGetter;
		}

		public Object getNonTransient() {
			return nonTransient;
		}

		public Object getDiscover() {
			return cannotDiscover;
		}

	}
}
