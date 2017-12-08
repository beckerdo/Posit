package javax.lang.posit;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import java.math.BigInteger;
import org.junit.Before;
import org.junit.Test;


/**
 * General test of this class.
 *
 * @author <a href="mailto://dan@danbecker.info>Dan Becker</a>
 */
public class PositDomainTest {
    public static final double COMPARE_PRECISION = 0.00000001;

    public static final String[] BINARY_TEST_CASES = {
            "", // 0
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

    @Before
    public void setup() {
    }

    public static void testNull(final Object o) {
        // Math interface
        assertEquals(Boolean.FALSE, PositDomain.isInfinite((String) o));
        assertEquals(Boolean.FALSE, PositDomain.isZero((String) o));
        assertEquals(Boolean.FALSE, PositDomain.isPositive((String) o));
        assertEquals(Boolean.TRUE, PositDomain.isExact((String) o));
        // Posit domain interface
        assertEquals("", PositDomain.getRegime((String) o));
        assertEquals(0, PositDomain.getRegimeK((String) o));
        // assertEquals(2, PositDomain.getUseed((String) o));
        // assertEquals("", PositDomain.getExponent((String) o));
    }

    @Test
    public void testNull() {
        testNull(null);
    }

    @Test
    public void testNullString() {
        testNull((String) null);
    }

    @Test
    public void testLength0() {
        testNull("");
    }

    public static final String[] EXPECTED_REGIME = { "", // 0
            "", "", // 1
            "0", "1", "0", "1", // 2
            "00", "01", "10", "11", "00", "11", "10", "01", // 3
            "000", "001", "01", "01", "10", "10", "110", "111", // 4
            "000", "111", "110", "10", "10", "01", "01", "001", // 4
            "0000", "0001", "001", "001", "01", "01", "01", "01", // 5
            "10", "10", "10", "10", "110", "110", "1110", "1111", // 5
            "0000", "1111", "1110", "110", "110", "10", "10", "10", // 5
            "10", "01", "01", "01", "01", "001", "001", "0001", // 5
    };
    public static final int[] EXPECTED_REGIME_K = { 0, // 0
            0, 0, // 1
            -1, 0, -1, 0, // 2
            -2, -1, 0, 1, -2, 1, 0, -1, // 3
            -3, -2, -1, -1, 0, 0, 1, 2, // 4
            -3, 2, 1, 0, 0, -1, -1, -2, // 4
            -4, -3, -2, -2, -1, -1, -1, -1, // 5
            0, 0, 0, 0, 1, 1, 2, 3, // 5
            -4, 3, 2, 1, 1, 0, 0, 0, // 5
            0, -1, -1, -1, -1, -2, -2, -3, // 5
    };
    public static final String[] EXPECTED_FRACTION_ES0 = { "", // 0
            "", "", // 1
            "", "", "", "", // 2
            "", "", "", "", "", "", "", "", // 3
            "", "", "0", "1", "0", "1", "", "", // 4
            "", "", "", "1", "0", "1", "0", "", // 4
            "", "", "0", "1", "00", "01", "10", "11", // 5
            "00", "01", "10", "11", "0", "1", "", "", // 5
            "", "", "", "1", "0", "11", "10", "01", // 5
            "00", "11", "10", "01", "00", "1", "0", "", // 5
    };
    public static final String[] EXPECTED_EXPONENT_ES1 = { "", // 0
            "", "", // 1
            "", "", "", "", // 2
            "", "", "", "", "", "", "", "", // 3
            "", "", "0", "1", "0", "1", "", "", // 4
            "", "", "", "1", "0", "1", "0", "", // 4
            "", "", "0", "1", "0", "0", "1", "1", // 5
            "0", "0", "1", "1", "0", "1", "", "", // 5
            "", "", "", "1", "0", "1", "1", "0", // 5
            "0", "1", "1", "0", "0", "1", "0", "", // 5
    };
    public static final String[] EXPECTED_FRACTION_ES1 = { "", // 0
            "", "", // 1
            "", "", "", "", // 2
            "", "", "", "", "", "", "", "", // 3
            "", "", "", "", "", "", "", "", // 4
            "", "", "", "", "", "", "", "", // 4
            "", "", "", "", "0", "1", "0", "1", // 5
            "0", "1", "0", "1", "", "", "", "", // 5
            "", "", "", "", "", "1", "0", "1", // 5
            "0", "1", "0", "1", "0", "", "", "", // 5
    };
    public static final String[] EXPECTED_EXPONENT_ES2 = { "", // 0
            "", "", // 1
            "", "", "", "", // 2
            "", "", "", "", "", "", "", "", // 3
            "", "", "0", "1", "0", "1", "", "", // 4
            "", "", "", "1", "0", "1", "0", "", // 4
            "", "", "0", "1", "00", "01", "10", "11", // 5
            "00", "01", "10", "11", "0", "1", "", "", // 5
            "", "", "", "1", "0", "11", "10", "01", // 5
            "00", "11", "10", "01", "00", "1", "0", "", // 5
    };
    public static final String[] EXPECTED_FRACTION_ES2 = { "", // 0
            "", "", // 1
            "", "", "", "", // 2
            "", "", "", "", "", "", "", "", // 3
            "", "", "", "", "", "", "", "", // 4
            "", "", "", "", "", "", "", "", // 4
            "", "", "", "", "", "", "", "", // 5
            "", "", "", "", "", "", "", "", // 5
            "", "", "", "", "", "", "", "", // 5
            "", "", "", "", "", "", "", "", // 5
    };

    @Test
    public void positComponents() {
        for (int i = 0; i < BINARY_TEST_CASES.length; i++) {
            // test domain info
            assertEquals("Regime test on " + BINARY_TEST_CASES[i], EXPECTED_REGIME[i],
                    PositDomain.getRegime(BINARY_TEST_CASES[i]));
            // System.out.println( "i=" + i + ", bits=" + expectedString + ", regime=" +
            // posit.getRegime() + ", k=" + posit.getRegimeK() );
            assertEquals("Regime K test on " + BINARY_TEST_CASES[i], EXPECTED_REGIME_K[i],
                    PositDomain.getRegimeK(EXPECTED_REGIME[i]));

            // Expected exponent, es=0
            assertEquals("Exponent test es=0 on " + BINARY_TEST_CASES[i], "",
                    PositDomain.getExponent(BINARY_TEST_CASES[i], 0));
            // Expected fraction, es=0
            assertEquals("Fraction test es=0 on " + BINARY_TEST_CASES[i], EXPECTED_FRACTION_ES0[i],
                    PositDomain.getFraction(BINARY_TEST_CASES[i], 0));
            // Expected exponent, es=1
            assertEquals("Exponent test es=1 on " + BINARY_TEST_CASES[i], EXPECTED_EXPONENT_ES1[i],
                    PositDomain.getExponent(BINARY_TEST_CASES[i], 1));
            // Expected fraction, es=1
            assertEquals("Fraction test es=1 on " + BINARY_TEST_CASES[i], EXPECTED_FRACTION_ES1[i],
                    PositDomain.getFraction(BINARY_TEST_CASES[i], 1));
            // Expected exponent, es=2
            assertEquals("Exponent test es=2 on " + BINARY_TEST_CASES[i], EXPECTED_EXPONENT_ES2[i],
                    PositDomain.getExponent(BINARY_TEST_CASES[i], 2));
            // Expected fraction, es=2
            assertEquals("Fraction test es=2 on " + BINARY_TEST_CASES[i], EXPECTED_FRACTION_ES2[i],
                    PositDomain.getFraction(BINARY_TEST_CASES[i], 2));
        }
    }

    @Test
    public void positDetailsString() {
        final String instance = "10100";
        final int i = Integer.parseInt(instance, 2);
        System.out.println("i=" + i + ", posit=" + PositDomain.toDetailsString(instance, 1));
    }

    public static final BigInteger[] EXPECTED_USEED = new BigInteger[] { new BigInteger("2"), new BigInteger("4"),
            new BigInteger("16"), new BigInteger("256"), new BigInteger("65536"), new BigInteger("4294967296"),
            new BigInteger("18446744073709551616"), new BigInteger("340282366920938463463374607431768211456"),
            new BigInteger("115792089237316195423570985008687907853269984665640564039457584007913129639936") };

    @Test
    public void positUseed() {
        assertEquals("Exponent useed test on es -1", BigInteger.ZERO, PositDomain.getUseed(-1));
        // Useed is 2 ** 2 ** es
        for (int es = 0; es < 9; es++) {
            // Puny long runs out at es=6, puny double rounds at es=6.
            // System.out.println( "Es is " + es + ", useed is " + Math.pow(2, Math.pow(2,
            // es)));
            // Useed is 2 ** 2 ** es
            assertEquals("Exponent useed test on es " + es, EXPECTED_USEED[es], PositDomain.getUseed(es));
        }
    }

    public static final boolean[] EXPECTED_ZERO = { false, // 0
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
    public static final boolean[] EXPECTED_INFINITE = { false, // 0
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

    @Test
    public void isZeroInfinity() {
        // Test that parsing and toString are commutative.
        for (int i = 0; i < BINARY_TEST_CASES.length; i++) {
            // System.out.println( "i=" + i + ", bits=" + BINARY_TEST_CASES[i] + ", isZero="
            // + posit.isZero() + ", isInfinity=" + posit.isInfinite());
            assertEquals("isZero test on " + BINARY_TEST_CASES[i], EXPECTED_ZERO[i],
                    PositDomain.isZero(BINARY_TEST_CASES[i]));
            assertEquals("isInfinte test on " + BINARY_TEST_CASES[i], EXPECTED_INFINITE[i],
                    PositDomain.isInfinite(BINARY_TEST_CASES[i]));
        }
    }

    public static final boolean[] EXPECTED_POSITIVE = { false, // 0
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
    public static final boolean[] EXPECTED_EXACT = { true, // 0
            true, true, // 1
            true, true, true, true, // 2
            true, false, true, false, true, false, true, false, // 3
            true, false, true, false, true, false, true, false, // 4
            true, false, true, false, true, false, true, false, // 4
            true, false, true, false, true, false, true, false, // 5
            true, false, true, false, true, false, true, false, // 5
            true, false, true, false, true, false, true, false, // 5
            true, false, true, false, true, false, true, false, // 5
    };

    @Test
    public void isPositveIsExact() {
        // Test that parsing and toString are commutative.
        for (int i = 0; i < BINARY_TEST_CASES.length; i++) {
            assertEquals("isPositive test on " + BINARY_TEST_CASES[i], EXPECTED_POSITIVE[i],
                    PositDomain.isPositive(BINARY_TEST_CASES[i]));
            assertEquals("isExact test on " + BINARY_TEST_CASES[i], EXPECTED_EXACT[i],
                    PositDomain.isExact(BINARY_TEST_CASES[i]));
        }
    }

    public static final String[][] EXPECTED_ES1_COMPONENTS = {
            { "", "", "", "" }, // 0
            { "0", "", "", "" }, { "1", "", "", "" }, // 1
            { "0", "0", "", "" }, { "0", "1", "", "" }, { "1", "0", "", "" }, { "1", "1", "", "" }, // 2
            { "0", "00", "", "" }, { "0", "01", "", "" }, { "0", "10", "", "" }, { "0", "11", "", "" },
            { "1", "00", "", "" }, { "1", "01", "", "" }, { "1", "10", "", "" }, { "1", "11", "", "" }, // 3
            { "0", "000", "", "" }, { "0", "001", "", "" }, { "0", "01", "0", "" }, { "0", "01", "1", "" },
            { "0", "10", "0", "" }, { "0", "10", "1", "" }, { "0", "110", "", "" }, { "0", "111", "", "" }, // 4
            { "1", "000", "", "" }, { "1", "001", "", "" }, { "1", "01", "0", "" }, { "1", "01", "1", "" },
            { "1", "10", "0", "" }, { "1", "10", "1", "" }, { "1", "110", "", "" }, { "1", "111", "", "" }, // 4
            { "0", "0000", "", "" }, { "0", "0001", "", "" }, { "0", "001", "0", "" }, { "0", "001", "1", "" },
            { "0", "01", "0", "0" }, { "0", "01", "0", "1" }, { "0", "01", "1", "0" }, { "0", "01", "1", "1" }, // 5
            { "0", "10", "0", "0" }, { "0", "10", "0", "1" }, { "0", "10", "1", "0" }, { "0", "10", "1", "1" },
            { "0", "110", "0", "" }, { "0", "110", "1", "" }, { "0", "1110", "", "" }, { "0", "1111", "", "" }, // 5
            { "1", "0000", "", "" }, { "1", "0001", "", "" }, { "1", "001", "0", "" }, { "1", "001", "1", "" },
            { "1", "01", "0", "0" }, { "1", "01", "0", "1" }, { "1", "01", "1", "0" }, { "1", "01", "1", "1" }, // 5
            { "1", "10", "0", "0" }, { "1", "10", "0", "1" }, { "1", "10", "1", "0" }, { "1", "10", "1", "1" },
            { "1", "110", "0", "" }, { "1", "110", "1", "" }, { "1", "1110", "", "" }, { "1", "1111", "", "" }, // 5
    };
    public static final String[][] EXPECTED_ES1_COMPONENTS_TWOS = {
            { "", "", "", "" }, // 0
            { "0", "", "", "" }, { "1", "", "", "" }, // 1
            { "0", "0", "", "" }, { "0", "1", "", "" }, { "1", "0", "", "" }, { "1", "1", "", "" }, // 2
            { "0", "00", "", "" }, { "0", "01", "", "" }, { "0", "10", "", "" }, { "0", "11", "", "" },
            { "1", "00", "", "" }, { "1", "11", "", "" }, { "1", "10", "", "" }, { "1", "01", "", "" }, // 3
            { "0", "000", "", "" }, { "0", "001", "", "" }, { "0", "01", "0", "" }, { "0", "01", "1", "" },
            { "0", "10", "0", "" }, { "0", "10", "1", "" }, { "0", "110", "", "" }, { "0", "111", "", "" }, // 4
            { "1", "000", "", "" }, { "1", "111", "", "" }, { "1", "110", "", "" }, { "1", "10", "1", "" },
            { "1", "10", "0", "" }, { "1", "01", "1", "" }, { "1", "01", "0", "" }, { "1", "001", "", "" }, // 4
            { "0", "0000", "", "" }, { "0", "0001", "", "" }, { "0", "001", "0", "" }, { "0", "001", "1", "" },
            { "0", "01", "0", "0" }, { "0", "01", "0", "1" }, { "0", "01", "1", "0" }, { "0", "01", "1", "1" }, // 5
            { "0", "10", "0", "0" }, { "0", "10", "0", "1" }, { "0", "10", "1", "0" }, { "0", "10", "1", "1" },
            { "0", "110", "0", "" }, { "0", "110", "1", "" }, { "0", "1110", "", "" }, { "0", "1111", "", "" }, // 5
            { "1", "0000", "", "" }, { "1", "1111", "", "" }, { "1", "1110", "", "" }, { "1", "110", "1", "" },
            { "1", "110", "0", "" }, { "1", "10", "1", "1" }, { "1", "10", "1", "0" }, { "1", "10", "0", "1" }, // 5
            { "1", "10", "0", "0" }, { "1", "01", "1", "1" }, { "1", "01", "1", "0" }, { "1", "01", "0", "1" },
            { "1", "01", "0", "0" }, { "1", "001", "1", "" }, { "1", "001", "0", "" }, { "1", "0001", "", "" }, // 5
    };
    public static final String[][] EXPECTED_ES2_COMPONENTS = {
            { "", "", "", "" }, // 0
            { "0", "", "", "" }, { "1", "", "", "" }, // 1
            { "0", "0", "", "" }, { "0", "1", "", "" }, { "1", "0", "", "" }, { "1", "1", "", "" }, // 2
            { "0", "00", "", "" }, { "0", "01", "", "" }, { "0", "10", "", "" }, { "0", "11", "", "" },
            { "1", "00", "", "" }, { "1", "01", "", "" }, { "1", "10", "", "" }, { "1", "11", "", "" }, // 3
            { "0", "000", "", "" }, { "0", "001", "", "" }, { "0", "01", "0", "" }, { "0", "01", "1", "" },
            { "0", "10", "0", "" }, { "0", "10", "1", "" }, { "0", "110", "", "" }, { "0", "111", "", "" }, // 4
            { "1", "000", "", "" }, { "1", "001", "", "" }, { "1", "01", "0", "" }, { "1", "01", "1", "" },
            { "1", "10", "0", "" }, { "1", "10", "1", "" }, { "1", "110", "", "" }, { "1", "111", "", "" }, // 4
            { "0", "0000", "", "" }, { "0", "0001", "", "" }, { "0", "001", "0", "" }, { "0", "001", "1", "" },
            { "0", "01", "00", "" }, { "0", "01", "01", "" }, { "0", "01", "10", "" }, { "0", "01", "11", "" }, // 5
            { "0", "10", "00", "" }, { "0", "10", "01", "" }, { "0", "10", "10", "" }, { "0", "10", "11", "" },
            { "0", "110", "0", "" }, { "0", "110", "1", "" }, { "0", "1110", "", "" }, { "0", "1111", "", "" }, // 5
            { "1", "0000", "", "" }, { "1", "0001", "", "" }, { "1", "001", "0", "" }, { "1", "001", "1", "" },
            { "1", "01", "00", "" }, { "1", "01", "01", "" }, { "1", "01", "10", "" }, { "1", "01", "11", "" }, // 5
            { "1", "10", "00", "" }, { "1", "10", "01", "" }, { "1", "10", "10", "" }, { "1", "10", "11", "" },
            { "1", "110", "0", "" }, { "1", "110", "1", "" }, { "1", "1110", "", "" }, { "1", "1111", "", "" }, // 5
    };
    public static final String[][] EXPECTED_ES2_COMPONENTS_TWOS = {
            { "", "", "", "" }, // 0
            { "0", "", "", "" }, { "1", "", "", "" }, // 1
            { "0", "0", "", "" }, { "0", "1", "", "" }, { "1", "0", "", "" }, { "1", "1", "", "" }, // 2
            { "0", "00", "", "" }, { "0", "01", "", "" }, { "0", "10", "", "" }, { "0", "11", "", "" },
            { "1", "00", "", "" }, { "1", "11", "", "" }, { "1", "10", "", "" }, { "1", "01", "", "" }, // 3
            { "0", "000", "", "" }, { "0", "001", "", "" }, { "0", "01", "0", "" }, { "0", "01", "1", "" },
            { "0", "10", "0", "" }, { "0", "10", "1", "" }, { "0", "110", "", "" }, { "0", "111", "", "" }, // 4
            { "1", "000", "", "" }, { "1", "111", "", "" }, { "1", "110", "", "" }, { "1", "10", "1", "" },
            { "1", "10", "0", "" }, { "1", "01", "1", "" }, { "1", "01", "0", "" }, { "1", "001", "", "" }, // 4
            { "0", "0000", "", "" }, { "0", "0001", "", "" }, { "0", "001", "0", "" }, { "0", "001", "1", "" },
            { "0", "01", "00", "" }, { "0", "01", "01", "" }, { "0", "01", "10", "" }, { "0", "01", "11", "" }, // 5
            { "0", "10", "00", "" }, { "0", "10", "01", "" }, { "0", "10", "10", "" }, { "0", "10", "11", "" },
            { "0", "110", "0", "" }, { "0", "110", "1", "" }, { "0", "1110", "", "" }, { "0", "1111", "", "" }, // 5
            { "1", "0000", "", "" }, { "1", "1111", "", "" }, { "1", "1110", "", "" }, { "1", "110", "1", "" },
            { "1", "110", "0", "" }, { "1", "10", "11", "" }, { "1", "10", "10", "" }, { "1", "10", "01", "" }, // 5
            { "1", "10", "00", "" }, { "1", "01", "11", "" }, { "1", "01", "10", "" }, { "1", "01", "01", "" },
            { "1", "01", "00", "" }, { "1", "001", "1", "" }, { "1", "001", "0", "" }, { "1", "0001", "", "" }, // 5
    };

    @Test
    public void getComponentsTest() {
        // Test ES1 components with NO twos complement
        for (int i = 0; i < BINARY_TEST_CASES.length; i++) {
            assertArrayEquals("getComponents ES1 test on " + BINARY_TEST_CASES[i], EXPECTED_ES1_COMPONENTS[i],
                    PositDomain.getComponents(BINARY_TEST_CASES[i], 1, false));
        }
        // Test ES1 components with twos complement
        for (int i = 0; i < BINARY_TEST_CASES.length; i++) {
            assertArrayEquals("getComponents twos ES1 test on " + BINARY_TEST_CASES[i],
                    EXPECTED_ES1_COMPONENTS_TWOS[i],
                    PositDomain.getComponents(BINARY_TEST_CASES[i], 1, true));
        }
        // Test ES2 components with NO twos complement
        for (int i = 0; i < BINARY_TEST_CASES.length; i++) {
            assertArrayEquals("getComponents ES2 test on " + BINARY_TEST_CASES[i],
                    EXPECTED_ES2_COMPONENTS[i],
                    PositDomain.getComponents(BINARY_TEST_CASES[i], 2, false));
        }
        // Test ES2 components with twos complement
        for (int i = 0; i < BINARY_TEST_CASES.length; i++) {
            assertArrayEquals("getComponents twos ES2 test on " + BINARY_TEST_CASES[i],
                    EXPECTED_ES2_COMPONENTS_TWOS[i],
                    PositDomain.getComponents(BINARY_TEST_CASES[i], 2, true));
        }
    }

    public static final String[] EXPECTED_ES1_SPACED_STRING = { "", // 0
            "0", "1", // 1
            "0 0", "0 1", "1 0", "1 1", // 2
            "0 00", "0 01", "0 10", "0 11", "1 00", "1 01", "1 10", "1 11", // 3
            "0 000", "0 001", "0 01 e0", "0 01 e1", "0 10 e0", "0 10 e1", "0 110", "0 111", // 4
            "1 000", "1 001", "1 01 e0", "1 01 e1", "1 10 e0", "1 10 e1", "1 110", "1 111", // 4
            "0 0000", "0 0001", "0 001 e0", "0 001 e1", "0 01 e0 f0", "0 01 e0 f1", "0 01 e1 f0", "0 01 e1 f1", // 5
            "0 10 e0 f0", "0 10 e0 f1", "0 10 e1 f0", "0 10 e1 f1", "0 110 e0", "0 110 e1", "0 1110", "0 1111", // 5
            "1 0000", "1 0001", "1 001 e0", "1 001 e1", "1 01 e0 f0", "1 01 e0 f1", "1 01 e1 f0", "1 01 e1 f1", // 5
            "1 10 e0 f0", "1 10 e0 f1", "1 10 e1 f0", "1 10 e1 f1", "1 110 e0", "1 110 e1", "1 1110", "1 1111", // 5
    };
    public static final String[] EXPECTED_ES1_SPACED_STRING_TWOS = { "", // 0
            "0", "1", // 1
            "0 0", "0 1", "1 0", "1 1", // 2
            "0 00", "0 01", "0 10", "0 11", "1 00", "1 11", "1 10", "1 01", // 3
            "0 000", "0 001", "0 01 e0", "0 01 e1", "0 10 e0", "0 10 e1", "0 110", "0 111", // 4
            "1 000", "1 111", "1 110", "1 10 e1", "1 10 e0", "1 01 e1", "1 01 e0", "1 001", // 4
            "0 0000", "0 0001", "0 001 e0", "0 001 e1", "0 01 e0 f0", "0 01 e0 f1", "0 01 e1 f0", "0 01 e1 f1", // 5
            "0 10 e0 f0", "0 10 e0 f1", "0 10 e1 f0", "0 10 e1 f1", "0 110 e0", "0 110 e1", "0 1110", "0 1111", // 5
            "1 0000", "1 1111", "1 1110", "1 110 e1", "1 110 e0", "1 10 e1 f1", "1 10 e1 f0", "1 10 e0 f1", // 5
            "1 10 e0 f0", "1 01 e1 f1", "1 01 e1 f0", "1 01 e0 f1", "1 01 e0 f0", "1 001 e1", "1 001 e0", "1 0001", // 5
    };
    public static final String[] EXPECTED_ES2_SPACED_STRING = { "", // 0
            "0", "1", // 1
            "0 0", "0 1", "1 0", "1 1", // 2
            "0 00", "0 01", "0 10", "0 11", "1 00", "1 01", "1 10", "1 11", // 3
            "0 000", "0 001", "0 01 e0", "0 01 e1", "0 10 e0", "0 10 e1", "0 110", "0 111", // 4
            "1 000", "1 001", "1 01 e0", "1 01 e1", "1 10 e0", "1 10 e1", "1 110", "1 111", // 4
            "0 0000", "0 0001", "0 001 e0", "0 001 e1", "0 01 e00", "0 01 e01", "0 01 e10", "0 01 e11", // 5
            "0 10 e00", "0 10 e01", "0 10 e10", "0 10 e11", "0 110 e0", "0 110 e1", "0 1110", "0 1111", // 5
            "1 0000", "1 0001", "1 001 e0", "1 001 e1", "1 01 e00", "1 01 e01", "1 01 e10", "1 01 e11", // 5
            "1 10 e00", "1 10 e01", "1 10 e10", "1 10 e11", "1 110 e0", "1 110 e1", "1 1110", "1 1111", // 5
    };
    public static final String[] EXPECTED_ES2_SPACED_STRING_TWOS = { "", // 0
            "0", "1", // 1
            "0 0", "0 1", "1 0", "1 1", // 2
            "0 00", "0 01", "0 10", "0 11", "1 00", "1 11", "1 10", "1 01", // 3
            "0 000", "0 001", "0 01 e0", "0 01 e1", "0 10 e0", "0 10 e1", "0 110", "0 111", // 4
            "1 000", "1 111", "1 110", "1 10 e1", "1 10 e0", "1 01 e1", "1 01 e0", "1 001", // 4
            "0 0000", "0 0001", "0 001 e0", "0 001 e1", "0 01 e00", "0 01 e01", "0 01 e10", "0 01 e11", // 5
            "0 10 e00", "0 10 e01", "0 10 e10", "0 10 e11", "0 110 e0", "0 110 e1", "0 1110", "0 1111", // 5
            "1 0000", "1 1111", "1 1110", "1 110 e1", "1 110 e0", "1 10 e11", "1 10 e10", "1 10 e01", // 5
            "1 10 e00", "1 01 e11", "1 01 e10", "1 01 e01", "1 01 e00", "1 001 e1", "1 001 e0", "1 0001", // 5
    };

    @Test
    public void toSpacedString() {
        // Test ES1 spaced string with NO twos complement
        for (int i = 0; i < BINARY_TEST_CASES.length; i++) {
            assertEquals("toSpacedString ES1 test on " + BINARY_TEST_CASES[i], EXPECTED_ES1_SPACED_STRING[i],
                    PositDomain.toSpacedString(BINARY_TEST_CASES[i], 1, false));
            assertEquals("toSpacedString ES1 without spaces on " + BINARY_TEST_CASES[i], BINARY_TEST_CASES[i],
                    PositDomain.toSpacedString(BINARY_TEST_CASES[i], 1, false).replaceAll("[\\sef]", ""));
        }
        // Test ES1 spaced string with twos complement
        for (int i = 0; i < BINARY_TEST_CASES.length; i++) {
            assertEquals("toSpacedString twos ES1 test on " + BINARY_TEST_CASES[i], EXPECTED_ES1_SPACED_STRING_TWOS[i],
                    PositDomain.toSpacedString(BINARY_TEST_CASES[i], 1, true));
        }
        // Test ES2 spaced string with NO twos complement
        for (int i = 0; i < BINARY_TEST_CASES.length; i++) {
            assertEquals("toSpacedString ES2 test on " + BINARY_TEST_CASES[i], EXPECTED_ES2_SPACED_STRING[i],
                    PositDomain.toSpacedString(BINARY_TEST_CASES[i], 2, false));
            assertEquals("toSpacedString ES2 without stpaces on " + BINARY_TEST_CASES[i], BINARY_TEST_CASES[i],
                    PositDomain.toSpacedString(BINARY_TEST_CASES[i], 2, false).replaceAll("[ef\\s]", ""));
        }
        // Test ES2 spaced string with twos complement
        for (int i = 0; i < BINARY_TEST_CASES.length; i++) {
            assertEquals("toSpacedString twos ES2 test on " + BINARY_TEST_CASES[i], EXPECTED_ES2_SPACED_STRING_TWOS[i],
                    PositDomain.toSpacedString(BINARY_TEST_CASES[i], 2, true));
        }
    }

    public static final double[] EXPECTED_FOURBIT_ES0 = {
            // "0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", // 4
            // "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111", // 4
            0.0, 0.25, 0.5, 0.75, 1.0, 1.5, 2.0, 4.0, Double.POSITIVE_INFINITY, -4.0, -2.0, -1.5, -1.0, -0.75, -0.5,
            -0.25, };
    public static final double[] EXPECTED_FOURBIT_ES1 = { 0.0, 0.0625, 0.25, 0.5, 1.0, 2.0, 4.0, 16.0,
            Double.POSITIVE_INFINITY, -16.0, -4.0, -2.0, -1.0, -0.5, -0.25, -0.0625, };
    public static final double[] EXPECTED_FOURBIT_ES2 = { 0.0, 0.00390625, 0.0625, 0.25, 1.0, 4.0, 16.0, 256.0,
            Double.POSITIVE_INFINITY, -256.0, -16.0, -4.0, -1.0, -0.25, -0.0625, -0.00390625, };

    @Test
    public void fourBitES0() {
        int oosCount = 0;
        for (int i = 0; i < EXPECTED_FOURBIT_ES0.length; i++) {
            final String instance = String.format("%4s", Integer.toBinaryString(i)).replace(" ", "0");
            final Posit p = new PositStringImpl(instance, 0);
            final String outOfSpec = outOfSpec(p.doubleValue(), EXPECTED_FOURBIT_ES0[i], COMPARE_PRECISION);
            if (outOfSpec.length() > 0) {
                oosCount++;
            }
            System.out.println("i=" + i + ", posit=" + PositDomain.toDetailsString(instance, 0) + ",exp="
                    + EXPECTED_SIXBIT_ES1[i] + outOfSpec);
            assertEquals(EXPECTED_FOURBIT_ES0[i], p.doubleValue(), PositDomainTest.COMPARE_PRECISION);
        }
        System.out.println("Out of spec count=" + oosCount + "/" + EXPECTED_FOURBIT_ES0.length + ".");
    }

    @Test
    public void fourBitES1() {
        int oosCount = 0;
        for (int i = 0; i < EXPECTED_FOURBIT_ES1.length; i++) {
            final String instance = String.format("%4s", Integer.toBinaryString(i)).replace(" ", "0");
            final Posit p = new PositStringImpl(instance, 1);
            final String outOfSpec = outOfSpec(p.doubleValue(), EXPECTED_FOURBIT_ES1[i], COMPARE_PRECISION);
            if (outOfSpec.length() > 0) {
                oosCount++;
            }
            System.out.println("i=" + i + ", posit=" + PositDomain.toDetailsString(instance, 1) + ",exp="
                    + EXPECTED_SIXBIT_ES1[i] + outOfSpec);
            // assertEquals(EXPECTED_FOURBIT_ES0[i], p.doubleValue(), PositDomainTest.COMPARE_PRECISION);
        }
        System.out.println("Out of spec count=" + oosCount + "/" + EXPECTED_FOURBIT_ES1.length + ".");
    }

    @Test
    public void fourBitES2() {
        int oosCount = 0;
        for (int i = 0; i < EXPECTED_FOURBIT_ES2.length; i++) {
            final String instance = String.format("%4s", Integer.toBinaryString(i)).replace(" ", "0");
            final Posit p = new PositStringImpl(instance, 2);
            final String outOfSpec = outOfSpec(p.doubleValue(), EXPECTED_FOURBIT_ES2[i], COMPARE_PRECISION);
            if (outOfSpec.length() > 0) {
                oosCount++;
            }
            System.out.println("i=" + i + ", posit=" + PositDomain.toDetailsString(instance, 2) + ",exp="
                    + EXPECTED_FOURBIT_ES2[i] + outOfSpec);
            // assertEquals(EXPECTED_FOURBIT_ES0[i], p.doubleValue(),PositDomainTest.COMPARE_PRECISION);
        }
        System.out.println("Out of spec count=" + oosCount + "/" + EXPECTED_FOURBIT_ES2.length + ".");
    }

    public static final double[] EXPECTED_FIVEBIT_ES1 = { 0.0, 1.0 / 64.0, 1.0 / 16.0, 1.0 / 8.0, 1.0 / 4.0,
            1.0 / 3.0, 1.0 / 2.0, 1.0 / 1.5, 1.0, 1.5, 2.0, 3.0, 4.0, 8.0, 16.0, 64.0, Double.POSITIVE_INFINITY,
            -64.0, -16.0, -8.0, -4.0, -3.0, -2.0, -1.5, -1.0, -1.0 / 1.5, -1.0 / 2.0, -1.0 / 3.0, -1.0 / 4.0,
            -1.0 / 8.0, -1.0 / 16.0, -1.0 / 64.0 };
    public static final double[] EXPECTED_FIVEBIT_ES2 = { 0.0, 1.0 / 4096.0, 1.0 / 256.0, 1.0 / 64.0, 1.0 / 16.0,
            1.0 / 8.0, 1.0 / 4.0, 1.0 / 2.0, 1.0, 2.0, 4.0, 8.0, 16.0, 64.0, 256.0, 4096.0, Double.POSITIVE_INFINITY,
            -4096.0, -256.0, -64.0, -16.0, -8.0, -4.0, -2.0, -1.0, -1.0 / 2.0, -1.0 / 4.0, -1.0 / 8.0, -1.0 / 16.0,
            -1.0 / 64.0, -1.0 / 256.0, -1.0 / 4096.0 };

    @Test
    public void fiveBitES1() {
        int oosCount = 0;
        // es62
        for (int i = 0; i < EXPECTED_FIVEBIT_ES1.length; i++) {
            final String instance = String.format("%5s", Integer.toBinaryString(i)).replace(" ", "0");
            final Posit p = new PositStringImpl(instance, 1);
            final String outOfSpec = outOfSpec(p.doubleValue(), EXPECTED_FIVEBIT_ES1[i], COMPARE_PRECISION);
            if (outOfSpec.length() > 0) {
                oosCount++;
            }
            System.out.println("testcase=" + i + ", posit=" + PositDomain.toDetailsString(instance, 1) + ",exp="
                    + EXPECTED_FIVEBIT_ES1[i] + outOfSpec);
            // assertEquals(EXPECTED_FIVEBIT_ES2[i], p.doubleValue(),
            // PositDomainTest.COMPARE_PRECISION);
        }
        System.out.println("Out of spec count=" + oosCount + "/" + EXPECTED_FIVEBIT_ES1.length + ".");
    }

    @Test
    public void fiveBitES2() {
        int oosCount = 0;
        // es62
        for (int i = 0; i < EXPECTED_FIVEBIT_ES2.length; i++) {
            final String instance = String.format("%5s", Integer.toBinaryString(i)).replace(" ", "0");
            final Posit p = new PositStringImpl(instance, 2);
            final String outOfSpec = outOfSpec(p.doubleValue(), EXPECTED_FIVEBIT_ES2[i], COMPARE_PRECISION);
            if (outOfSpec.length() > 0) {
                oosCount++;
            }
            System.out.println("testcase=" + i + ", posit=" + PositDomain.toDetailsString(instance, 2) + ",exp="
                    + EXPECTED_FIVEBIT_ES2[i] + outOfSpec);
            // assertEquals(EXPECTED_FIVEBIT_ES2[i], p.doubleValue(),
            // PositDomainTest.COMPARE_PRECISION);
        }
        System.out.println("Out of spec count=" + oosCount + "/" + EXPECTED_FIVEBIT_ES2.length + ".");
    }

    /** 0, 1, ±∞,-1 plus or minus two */
    public static final int[] TEST_CASES_6BIT = { 62, 63, 0, 1, 2, 14, 15, 16, 17, 18, 30, 31, 32, 33, 34, 46, 47, 48,
            49, 50 };
    public static final double[] EXPECTED_SIXBIT_ES0 = { -0.125, -0.0625, 0.0, 0.0625, 0.125, 0.875, 0.9375, 1.0, 1.125,
            1.25, 8.0, 16.0, Double.POSITIVE_INFINITY, -16.0, -8.0, -1.25, -1.125, -1.0, -0.9375, -0.875, };
    public static final double[] EXPECTED_SIXBIT_ES1 = {
            0.0, // "0 00000", 0
            1.0 / 256.0, // "0 00001", 1
            1.0 / 64.0, // "0 0001 0", 2
            1.0 / 32.0, // "0 0001 1", 3
            1.0 / 16.0, // "0 001 00", 4
            1.0 / 12.0, // "0 001 01", 5
            1.0 / 8.0, // "0 001 10", 6
            1.0 / 6.0, // "0 001 11", 7
            1.0 / 4.0, // "0 01 0 00", 8
            1.0 / 3.5, // "0 01 0 01", 9
            1.0 / 3.0, // "0 01 0 10", 10
            1.0 / 2.5, // "0 01 0 11", 11
            1.0 / 2.0, // "0 01 1 00", 12
            1.0 / 1.75, // "0 01 1 01", 13
            1.0 / 1.5, // "0 01 1 10", 14
            1.0 / 1.25, // "0 01 1 11", 15
            1.0, // "0 10 0 00", 16
            1.25, // "0 10 0 01", 17
            1.5, // "0 10 0 10", 18
            1.75, // "0 10 0 11", 19
            2.0, // "0 10 1 00", 20
            2.5, // "0 10 1 01", 21
            3.0, // "0 10 1 10", 22
            3.5, // "0 10 1 11", 23
            4.0, // "0 110 00", 24
            6.0, // "0 110 01", 25
            8.0, // "0 110 10", 26
            12.0, // "0 110 11", 27
            16.0, // "0 1110 0", 28
            32.0, // "0 1110 1", 29
            64.0, // "0 11110", 30
            256.0, // "0 11111", 31
            Double.POSITIVE_INFINITY, // "1 00000", 32
            -256.0, // "1 00001", 33
            -64.0, // "1 0001 0", 34
            -32.0, // "1 0001 1", 35
            -16.0, // "1 001 00", 36
            -12.0, // "1 001 01", 37
            -8.0, // "1 001 10", 38
            -6.0, // "1 001 11", 39
            -4.0, // "1 01 0 00", 40
            -3.5, // "1 01 0 01", 41
            -3.0, // "1 01 0 10", 42
            -2.5, // "1 01 0 11", 43
            -2.0, // "1 01 1 00", 44
            -1.75, // "1 01 1 01", 45
            -1.5, // "1 01 1 10", 46
            -1.25, // "1 01 1 11", 47
            -1.0, // "1 10 0 00", 48
            -1.0 / 1.25, // "1 10 0 01", 49
            -1.0 / 1.5, // "1 10 0 10", 50
            -1.0 / 1.75, // "1 10 0 11", 51
            -1.0 / 2.0, // "1 10 1 00", 52
            -1.0 / 2.5, // "1 10 1 01", 53
            -1.0 / 3.0, // "1 10 1 10", 54
            -1.0 / 3.5, // "1 10 1 11", 55
            -1.0 / 3.0, // "1 110 00", 56
            -1.0 / 4.0, // "1 110 01", 57
            -1.0 / 6.0, // "1 110 10", 58
            -1.0 / 8.0, // "1 110 11", 59
            -1.0 / 16.0, // "1 1110 0", 60
            -1.0 / 32.0, // "1 1110 1", 61
            -1.0 / 64.0, // "1 11110", 62
            -1.0 / 256.0, // "1 11111", 63
    };

    public static final double[] EXPECTED_SIXBIT_ES2 = { 0.0, // "0 00000", 0
            1.0 / 65536.0, // "0 00001", 1
            1.0 / 4096.0, // "0 0001 0", 2
            1.0 / 1024.0, // "0 0001 1", 3
            1.0 / 256.0, // "0 001 00", 4
            1.0 / 128.0, // "0 001 01", 5
            1.0 / 64.0, // "0 001 10", 6
            1.0 / 32.0, // "0 001 11", 7
            1.0 / 16.0, // "0 01 000", 8
            1.0 / 12.0, // "0 01 001", 9
            1.0 / 8.0, // "0 01 010", 10
            1.0 / 6.0, // "0 01 011", 11
            1.0 / 4.0, // "0 01 100", 12
            1.0 / 3.0, // "0 01 101", 13
            1.0 / 2.0, // "0 01 110", 14
            1.0 / 1.5, // "0 01 111", 15
            1.0, // "0 10 000", 16
            1.5, // "0 10 001", 17
            2.0, // "0 10 010", 18
            3.0, // "0 10 011", 19
            4.0, // "0 10 100", 20
            6.0, // "0 10 101", 21
            8.0, // "0 10 110", 22
            12.0, // "0 10 111", 23
            16.0, // "0 110 00", 24
            32.0, // "0 110 01", 25
            64.0, // "0 110 10", 26
            128.0, // "0 110 11", 27
            256.0, // "0 1110 0", 28
            1024.0, // "0 1110 1", 29
            4096.0, // "0 11110", 30
            65536.0, // "0 11111", 31
            Double.POSITIVE_INFINITY, // "1 00000", 32
            -65536.0, // "1 00001", 33
            -4096.0, // "1 0001 0", 34
            -1024.0, // "1 0001 1", 35
            -256.0, // "1 001 00", 36
            -128.0, // "1 001 01", 37
            -64.0, // "1 001 10", 38
            -32.0, // "1 001 11", 39
            -16.0, // "1 01 000", 40
            -12.0, // "1 01 001", 41
            -8.0, // "1 01 010", 42
            -6.0, // "1 01 011", 43
            -4.0, // "1 01 100", 44
            -3.0, // "1 01 101", 45
            -2.0, // "1 01 110", 46
            -1.5, // "1 01 111", 47
            -1.0, // "1 10 000", 48
            -1.0 / 1.5, // "1 10 001", 49
            -1.0 / 2.0, // "1 10 010", 50
            -1.0 / 3.0, // "1 10 011", 51
            -1.0 / 4.0, // "1 10 100", 52
            -1.0 / 6.0, // "1 10 101", 53
            -1.0 / 8.0, // "1 10 110", 54
            -1.0 / 12.0, // "1 10 111", 55
            -1.0 / 16.0, // "1 110 00", 56
            -1.0 / 32.0, // "1 110 01", 57
            -1.0 / 64.0, // "1 110 10", 58
            -1.0 / 128.0, // "1 110 11", 59
            -1.0 / 256.0, // "1 1110 0", 60
            -1.0 / 1024.0, // "1 1110 1", 61
            -1.0 / 4096.0, // "1 11110", 62
            -1.0 / 65536.0, // "1 11111", 63
    };

    @Test
    public void sixBitES1() {
        int oosCount = 0;
        // es61
        for (int i = 0; i < EXPECTED_SIXBIT_ES1.length; i++) {
            final String instance = String.format("%6s", Integer.toBinaryString(i)).replace(" ", "0");
            final Posit p = new PositStringImpl(instance, 1);
            final String outOfSpec = outOfSpec(p.doubleValue(), EXPECTED_SIXBIT_ES1[i], COMPARE_PRECISION);
            if (outOfSpec.length() > 0) {
                oosCount++;
            }
            System.out.println("i=" + i + ", posit=" + PositDomain.toDetailsString(instance, 1) + ",exp="
                    + EXPECTED_SIXBIT_ES1[i] + outOfSpec);
            // assertEquals(EXPECTED_SIXBIT_ES2[i], p.doubleValue(),
            // PositDomainTest.COMPARE_PRECISION);
        }
        System.out.println("Out of spec count=" + oosCount + "/" + EXPECTED_SIXBIT_ES1.length + ".");
    }

    @Test
    public void sixBitES2() {
        int oosCount = 0;
        // es62
        for (int i = 0; i < EXPECTED_SIXBIT_ES2.length; i++) {
            final String instance = String.format("%6s", Integer.toBinaryString(i)).replace(" ", "0");
            final Posit p = new PositStringImpl(instance, 2);
            final String outOfSpec = outOfSpec(p.doubleValue(), EXPECTED_SIXBIT_ES2[i], COMPARE_PRECISION);
            if (outOfSpec.length() > 0) {
                oosCount++;
            }
            System.out.println("i=" + i + ", posit=" + PositDomain.toDetailsString(instance, 2) + ",exp="
                    + EXPECTED_SIXBIT_ES2[i] + outOfSpec);
            // assertEquals(EXPECTED_SIXBIT_ES2[i], p.doubleValue(),
            // PositDomainTest.COMPARE_PRECISION);
        }
        System.out.println("Out of spec count=" + oosCount + "/" + EXPECTED_SIXBIT_ES2.length + ".");
    }

    /** Returns empty string for in spec, sentinal and percentage for out of spec compare. */
    public String outOfSpec(final double value, final double expected, final double precision) {
        if (!equals(value, expected, precision)) {
            final double absVal = Math.abs(value);
            final double absExp = Math.abs(expected);
            final double max = Math.max(absVal, absExp);
            final double min = Math.min(absVal, absExp);
            double ratio = Double.POSITIVE_INFINITY;
            String outOfSpec = "***";
            if (!equals(min, 0.0, precision)) {
                ratio = max / min;
                outOfSpec = String.format(" %s(%3.2f%s)", "***", ratio, "%");
            }
            return outOfSpec;
        }
        return "";
    }

    /**
     * Returns true if the difference between doubles is less than epsilon. Comparison with loose precision.
     */
    public static boolean equals(final double a, final double b, final double epsilon) {
        // Test for infinity, NAN, etc.
        if (a == b) {
            return true;
        }
        final double difference = a - b;
        if (Math.abs(difference) < epsilon) {
            return true;
        }
        return false;
    }

    /** 0, 1, ±∞,-1 plus or minus two */
    public static final int[] TEST_CASES_8BIT = { 254, 255, 0, 1, 2, 62, 63, 64, 65, 66, 126, 127, 128, 129, 130, 190,
            191, 192, 193, 194 };
    public static final double[] EXPECTED_EIGHTBIT_ES0 = { -0.03125, -0.015625, 0.0, 0.015625, 0.03125, 0.96875,
            0.984375, 1.0, 1.03125, 1.0625, 32.0, 64.0, Double.POSITIVE_INFINITY, -64.0, -32.0, -1.0625, -1.03125, -1.0,
            -0.984375, -0.96875, };
    public static final double[] EXPECTED_EIGHTBIT_ES1 = { -0.0009765625, -0.000244140625, 0.0, 0.000244140625,
            0.0009765625, 0.9375, 0.96875, 1.0, 1.0625, 1.125, 1024.0, 4096.0, Double.POSITIVE_INFINITY, -4096.0,
            -1024.0, -1.125, -1.0625, -1.0, -0.96875, -0.9375, };
    public static final double[] EXPECTED_EIGHTBIT_ES2 = { -9.5367431640625e-7, -5.960464477539063e-8, 0.0,
            5.960464477539063e-8, 9.5367431640625e-7, 0.875, 0.9375, 1.0, 1.125, 1.25, 1.048576e6, 1.6777216e7,
            Double.POSITIVE_INFINITY, -1.6777216e7, -1.048576e6, -1.25, -1.125, -1.0, -0.9375, -0.875, };

    // Scalar product a.b = sum a1b1+a2b2+...+anbn
    // [1,3,-5].[4,-2,-2] = 1.4 + 3.-2 + (-5.-1)=3
    //
    // a=(3,2e7,1,-1,8.0e7)
    // b=(4.0e7,1,-1,-1.6e7)
    //
    // Correct a.b=2
    // float,32 a.b=0
    // double,64 a.b=0

}
