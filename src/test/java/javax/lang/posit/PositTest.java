package javax.lang.posit;

import static javax.lang.posit.PositDomainTest.BINARY_TEST_CASES;
import static javax.lang.posit.PositDomainTest.EXPECTED_EXACT;
import static javax.lang.posit.PositDomainTest.EXPECTED_INFINITE;
import static javax.lang.posit.PositDomainTest.EXPECTED_POSITIVE;
import static javax.lang.posit.PositDomainTest.EXPECTED_REGIME;
import static javax.lang.posit.PositDomainTest.EXPECTED_REGIME_K;
import static javax.lang.posit.PositDomainTest.EXPECTED_ZERO;
import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.BitSet;

import org.junit.Test;

/**
 * General test of this class.
 * <p>
 * Tests in this class should work with all implementations of Posits. More
 * specific tests may be in specific test implementations.
 *
 * @author <a href="mailto://dan@danbecker.info>Dan Becker</a>
 */
public class PositTest {
	public static void testNull(final Posit p) {
		// Number interface
		assertEquals(0, p.byteValue());
		assertEquals(0, p.shortValue());
		assertEquals(0, p.intValue());
		assertEquals(0, p.longValue());
		assertEquals(0, p.floatValue(), PositDomainTest.COMPARE_PRECISION);
		assertEquals(0, p.doubleValue(), PositDomainTest.COMPARE_PRECISION);
		assertEquals("", p.stringValue());
		// Conversion interface
		p.parse(null);
		assertEquals("", p.toString());
		p.parse("");
		assertEquals("", p.toString());
		// Math interface
		assertEquals(Boolean.FALSE, p.isInfinite());
		assertEquals(Boolean.FALSE, p.isZero());
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
		assertEquals(new BigInteger("16"), p.getUseed());
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

	@Test
	public void testConstructor() {
		final Posit p = new PositStringImpl("0000", (byte) 0);
		assertEquals(0, p.getMaxExponentSize());
	}

	@Test
	public void parseStringBinary() {
		// Test that parsing and toString are commutative.
		for (int i = 0; i < BINARY_TEST_CASES.length; i++) {
			final Posit posit = new PositStringImpl(BINARY_TEST_CASES[i]);
			assertEquals(BINARY_TEST_CASES[i].length(), posit.getBitSize());
			assertEquals(BINARY_TEST_CASES[i], posit.toString());

			// test domain info
			// System.out.println( "Working test case " + i + " \"" + posit + "\"");
			assertEquals("Positive test on " + posit, EXPECTED_POSITIVE[i], posit.isPositive());
			assertEquals("Regime test on " + posit, EXPECTED_REGIME[i], posit.getRegime());
			// System.out.println( "i=" + i + ", bits=" + expectedString + ", regime=" +
			// posit.getRegime() + ", k=" + posit.getRegimeK() );
			assertEquals("Regime K test element " + i + ", posit=" + posit, EXPECTED_REGIME_K[i], posit.getRegimeK());

            // Useed is 2 ^ 2 ^ es
			assertEquals("Useed test element " + i + ", posit=" + posit,
					PositDomainTest.EXPECTED_USEED[posit.getExponent().length()], posit.getUseed()); 
		}
	}

	@Test
	public void isZeroInfinity() {
		// Test that parsing and toString are commutative.
		for (int i = 0; i < BINARY_TEST_CASES.length; i++) {
			final Posit posit = new PositStringImpl(BINARY_TEST_CASES[i]);
			// System.out.println( "i=" + i + ", bits=" + BINARY_TEST_CASES[i] + ", isZero="
			// + posit.isZero() + ", isInfinity=" + posit.isInfinite());
			assertEquals(EXPECTED_ZERO[i], posit.isZero());
			assertEquals(EXPECTED_INFINITE[i], posit.isInfinite());
		}
	}

	@Test
	public void isPositveIsExact() {
		// Test that parsing and toString are commutative.
		for (int i = 0; i < BINARY_TEST_CASES.length; i++) {
			final Posit posit = new PositStringImpl(BINARY_TEST_CASES[i]);
			assertEquals("isPositive test on " + BINARY_TEST_CASES[i], EXPECTED_POSITIVE[i], posit.isPositive());
			assertEquals("isExact test on " + BINARY_TEST_CASES[i], EXPECTED_EXACT[i], posit.isExact());
		}
	}

	@Test
	public void doubleValue() {
		final String TEST_CASE = "0000110111011101";
		final double EXPECTED = Double.parseDouble("3.55393E-6");
		final Posit posit = new PositStringImpl(TEST_CASE);
		posit.setMaxExponentSize((byte) 3);
		final double returned = posit.doubleValue();
		System.out.println("Posit=\"" + posit + "\", double=" + returned);

		assertEquals("Posit double value testCase=" + TEST_CASE + ", doubleVal=" + returned, EXPECTED, returned,
				PositDomainTest.COMPARE_PRECISION);
	}

	/**
	 * Spits out information about java.util.BitSet BitSet is so weird, not
	 * reporting its set size, but rather size based on the machine implementation
	 * or length based on which bits are set. Here is an example of BitSet size of
	 * 4. "BitSet weirdness: bitSet={1}, bitSet.length=2, bitSet.size=64,
	 * bitSetSize=4"
	 * <p>
	 * This test is a legacy artifact from when the first implementation was based
	 * on BitSet.
	 */
	@Test
	public void bitSetInfo() {
		// final int ADDRESS_BITS_PER_WORD = 6;
		// final int BITS_PER_WORD = 1 << ADDRESS_BITS_PER_WORD;
		// final int BIT_INDEX_MASK = BITS_PER_WORD - 1;
		// System.out.println( "BitSet info: ADDRESS_BITS_PER_WORD=6,BITS_PER_WORD="+
		// BITS_PER_WORD + ",BIT_INDEX_MASK=0b" +
		// Integer.toBinaryString(BIT_INDEX_MASK));

		final int SIZE = 4;
		BitSet bitSet = new BitSet(SIZE);
		bitSet.set(1);
		// System.out.println( "BitSet weirdness: bitSet=" + bitSet + ", bitSet.length="
		// + bitSet.length() + ", bitSet.size=" + bitSet.size() + ", bitSetSize=" + SIZE
		// );

		final boolean VERBOSE_OUTPUT = false;
		if (VERBOSE_OUTPUT) {
			for (int length = 0; length < 32; length++) {
				bitSet = new BitSet(length);
				for (int odds = 0; odds < length; odds++) {
					if (odds % 2 == 1) {
						bitSet.set(odds);
					}
				}
				System.out.print("Bit(" + length + "):");
				final byte[] byteArray = bitSet.toByteArray();
				for (int i = 0; i < byteArray.length; i++) {
					if (i != 0) {
						System.out.print(" Bit(" + length + "):");
					}
					final String byteString = Integer.toBinaryString(byteArray[i]);
					System.out.print("0b" + byteString);
				}
				System.out.println();
				System.out.print("Bit(" + length + "):0b");
				for (int i = 0; i < bitSet.size(); i++) { // use size, not length.
					System.out.print(bitSet.get(i) ? "1" : "0");
				}
				System.out.println();
			}
		}
	}
}
