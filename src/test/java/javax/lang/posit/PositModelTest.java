package javax.lang.posit;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * General test of this class.
 *
 * @author <a href="mailto://dan@danbecker.info>Dan Becker</a>
 */
public class PositModelTest {
	@Before
	public void setup() {
	}

	public static void testNull(final Posit p) {
		assertEquals("", p.toString());
		p.parse("");
		assertEquals("", p.toString());
		// Math interface
		assertEquals(Boolean.FALSE, p.isInfinite());
		assertEquals(Boolean.FALSE, p.isZero());
		assertEquals(Boolean.FALSE, p.isNaN());
		// Object interface
		final Posit p2 = new PositStringImpl(null);
		assertEquals(0, p.compareTo(p2));
		assertEquals(0, Posit.compare(p, p2));
		assertEquals(0, p.hashCode());
		assertEquals(Boolean.TRUE, p.equals(p2));
		assertEquals("", p.toString());
		// Posit domain interface
		assertEquals(String.class, p.getImplementation());
		assertEquals(0, p.getBitSize());
		assertEquals(Boolean.FALSE, p.isPositive());
		assertEquals("", p.getRegime());
		assertEquals(0, p.getRegimeK());
		assertEquals(2, p.getUseed());
		assertEquals("", p.getExponent());
	}

	@Test
	public void testNull() {
		final Posit p = new PositStringImpl(null);
		testNull(p);
	}

	@Test
	public void testNullString() {
		final Posit p = new PositStringImpl((String) null);
		testNull(p);
	}

	@Test
	public void testLength0() {
		final Posit p = new PositStringImpl("");
		testNull(p);
	}

	public static final String[] BINARY_TEST_CASES = { "", // 0
			"0", "1", // 1
			"00", "01", "10", "11", // 2
			"000", "001", "010", "011", "100", "101", "110", "111", // 3
			"0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", // 4
			"1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111", // 4
			"00000", "00001", "00010", "00011", "00100", "00101", "00110", "00111", // 5
			"01000", "01001", "01010", "01011", "01100", "01101", "01110", "01111", // 5
			"10000", "10001", "10010", "10011", "10100", "10101", "10110", "10111", // 5
			"11000", "11001", "11010", "11011", "11100", "11101", "11110", "11111", // 5
	};

	// @Test
	// public void parseString() {
	// String [] TEST_CASES = { "0", "∞", "NaN", // specials
	// "0.00390625", "0.0625", "0.25", "1", "4", "16", "256", // positives
	// "-0.00390625", "-0.0625", "-0.25", "-1", "-4", "-16", "-256", // negatives
	// };
	//
	// // Test that parsing and toString are commutative.
	// for ( String testString : TEST_CASES ) {
	// Posit posit = new Posit( testString );
	// assertEquals( testString, posit.toString() );
	// }
	// }

	@Test
	public void parseStringBinary() {
		final boolean[] EXPECTED_POSITIVE = { false, // 0
				true, false, // 1
				true, true, false, false, // 2
				true, true, true, true, false, false, false, false, // 3
				true, true, true, true, true, true, true, true, // 4
				false, false, false, false, false, false, false, false, // 4
				true, true, true, true, true, true, true, true, // 5
				true, true, true, true, true, true, true, true, // 5
				false, false, false, false, false, false, false, false, // 5
				false, false, false, false, false, false, false, false, // 5
		};
		final String[] EXPECTED_REGIME = { "", // 0
				"", "", // 1
				"0", "1", "0", "1", // 2
				"00", "01", "10", "11", "00", "01", "10", "11", // 3
				"000", "001", "01", "01", "10", "10", "110", "111", // 4
				"000", "001", "01", "01", "10", "10", "110", "111", // 4
				"0000", "0001", "001", "001", "01", "01", "01", "01", // 5
				"10", "10", "10", "10", "110", "110", "1110", "1111", // 5
				"0000", "0001", "001", "001", "01", "01", "01", "01", // 5
				"10", "10", "10", "10", "110", "110", "1110", "1111", // 5
		};
		final int[] EXPECTED_REGIME_K = { 0, // 0
				-1, 0, // 1
				-1, 0, -1, 0, // 2
				-2, -1, 0, 1, -2, -1, 0, 1, // 3
				-3, -2, -1, -1, 0, 0, 1, 2, // 4
				-3, -2, -1, -1, 0, 0, 1, 2, // 4
				-4, -3, -2, -2, -1, -1, -1, -1, // 5
				0, 0, 0, 0, 1, 1, 2, 3, // 5
				-4, -3, -2, -2, -1, -1, -1, -1, // 5
				0, 0, 0, 0, 1, 1, 2, 3, // 5
		};
		final String[] EXPECTED_EXPONENT = { "", // 0
				"0", "1", // 1
				"00", "01", "10", "11", // 2
				"000", "001", "010", "011", "100", "101", "110", "111", // 3
				"0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", // 4
				"1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111", // 4
				"00000", "00001", "00010", "00011", "00100", "00101", "00110", "00111", // 5
				"01000", "01001", "01010", "01011", "01100", "01101", "01110", "01111", // 5
				"10000", "10001", "10010", "10011", "10100", "10101", "10110", "10111", // 5
				"11000", "11001", "11010", "11011", "11100", "11101", "11110", "11111", // 5
		};
		final String[] EXPECTED_FRACTION = { "", // 0
				"0", "1", // 1
				"00", "01", "10", "11", // 2
				"000", "001", "010", "011", "100", "101", "110", "111", // 3
				"0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", // 4
				"1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111", // 4
				"00000", "00001", "00010", "00011", "00100", "00101", "00110", "00111", // 5
				"01000", "01001", "01010", "01011", "01100", "01101", "01110", "01111", // 5
				"10000", "10001", "10010", "10011", "10100", "10101", "10110", "10111", // 5
				"11000", "11001", "11010", "11011", "11100", "11101", "11110", "11111", // 5
		};

		// Test that parsing and toString are commutative.
		for (int i = 0; i < BINARY_TEST_CASES.length; i++) {
			final String expectedString = BINARY_TEST_CASES[i];
			final Posit posit = new PositStringImpl(expectedString);
			assertEquals(expectedString.length(), posit.getBitSize());
			assertEquals(expectedString, posit.toString());

			// test domain info
			// System.out.println( "Working test case " + i + " \"" + posit + "\"");
			assertEquals("Positive test on " + posit, EXPECTED_POSITIVE[i], posit.isPositive());
			assertEquals("Regime test on " + posit, EXPECTED_REGIME[i], posit.getRegime());
			// System.out.println( "i=" + i + ", bits=" + expectedString + ", regime=" +
			// posit.getRegime() + ", k=" + posit.getRegimeK() );
			assertEquals("Regime K test on " + posit, EXPECTED_REGIME_K[i], posit.getRegimeK());
			assertEquals("Regime useed test on " + posit, (long) Math.pow(2, Math.pow(2, EXPECTED_REGIME[i].length())),
					posit.getUseed()); // Useed is 2 ** 2 ** es
		}

	}

	@Test
	public void isZeroInfinity() {
		final boolean[] EXPECTED_IS_ZERO = { false, // 0
				true, false, // 1
				true, false, false, false, // 2
				true, false, false, false, false, false, false, false, // 3
				true, false, false, false, false, false, false, false, // 4
				false, false, false, false, false, false, false, false, // 4
				true, false, false, false, false, false, false, false, // 5
				false, false, false, false, false, false, false, false, // 5
				false, false, false, false, false, false, false, false, // 5
				false, false, false, false, false, false, false, false, // 5
		};
		final boolean[] EXPECTED_IS_INFINITE = { false, // 0
				false, true, // 1
				false, false, true, false, // 2
				false, false, false, false, true, false, false, false, // 3
				false, false, false, false, false, false, false, false, // 4
				true, false, false, false, false, false, false, false, // 4
				false, false, false, false, false, false, false, false, // 5
				false, false, false, false, false, false, false, false, // 5
				true, false, false, false, false, false, false, false, // 5
				false, false, false, false, false, false, false, false, // 5
		};

		// Test that parsing and toString are commutative.
		for (int i = 0; i < BINARY_TEST_CASES.length; i++) {
			final Posit posit = new PositStringImpl(BINARY_TEST_CASES[i]);
			// System.out.println( "i=" + i + ", bits=" + BINARY_TEST_CASES[i] + ", isZero="
			// + posit.isZero() + ", isInfinity=" + posit.isInfinite());
			assertEquals(EXPECTED_IS_ZERO[i], posit.isZero());
			assertEquals(EXPECTED_IS_INFINITE[i], posit.isInfinite());
		}

	}
}
