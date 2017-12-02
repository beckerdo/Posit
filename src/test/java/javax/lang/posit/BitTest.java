package javax.lang.posit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * General test of this class.
 *
 * @author <a href="mailto://dan@danbecker.info>Dan Becker</a>
 */
public class BitTest {
	public static final String[] BINARY_TEST_CASES = { 
			null, "", // 0
			"0", "1", // 1
			"00", "01", "10", "11", // 2
			"00000", "00001", "00010", "00011", "00100", "00101", "00110", "00111", // 5
			"01000", "01001", "01010", "01011", "01100", "01101", "01110", "01111", // 5
			"10000", "10001", "10010", "10011", "10100", "10101", "10110", "10111", // 5
			"11000", "11001", "11010", "11011", "11100", "11101", "11110", "11111", // 5
	};

	public static final String[] EXPECTED = { 
			null, "", // 0
			"0", "1", // 1
			"00", "11", "10", "01", // 2
			"00000", "11111", "11110", "11101", "11100", "11011", "11010", "11001", // 5
			"11000", "10111", "10110", "10101", "10100", "10011", "10010", "10001", // 5
			"10000", "01111", "01110", "01101", "01100", "01011", "01010", "01001", // 5
			"01000", "00111", "00110", "00101", "00100", "00011", "00010", "00001", // 5
	};

	@Test
	public void invertTest() {
		// Test that parsing and toString are commutative.
		for (int i = 0; i < BINARY_TEST_CASES.length; i++) {
			assertEquals("Invert test on 1", '0', Bit.invert('1'));
			assertEquals("Invert test on 0", '1', Bit.invert('0'));
			assertEquals("Invert test on alpha a", '0', Bit.invert('a'));
		}
	}

	@Test
	public void twosCompTest() {
		// Test that parsing and toString are commutative.
		for (int i = 0; i < BINARY_TEST_CASES.length; i++) {
			assertEquals("Twos complement test on " + BINARY_TEST_CASES[i], EXPECTED[i],
					Bit.twosComplement(BINARY_TEST_CASES[i]));
		}

	}

}
