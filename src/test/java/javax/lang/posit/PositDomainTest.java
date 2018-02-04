package javax.lang.posit;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.math.BigInteger;
import org.junit.Before;
import org.junit.Test;


/**
 * General test of this class.
 *
 * @author <a href="mailto://dan@danbecker.info">Dan Becker</a>
 */
public class PositDomainTest {
    // Seven bit ES2 have this range ............. 0.00000095367431640625 to 1048576.0
    // public static final double COMPARE_PRECISION = 0.0000000000000001;
    public static final double COMPARE_PRECISION = 0.000000000001;

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
        // System.out.println("i=" + i + ", posit=" + PositDomain.toDetailsString(instance, 2));
        final String returned = PositDomain.toDetailsString(instance, 2);
        assertNotNull(returned);
        assertTrue(returned.length() > 0);
        assertTrue(returned.contains("es2"));
        assertTrue(returned.contains("us16"));
        assertTrue(returned.contains("us^k="));
        assertTrue(returned.contains("e="));
        assertTrue(returned.contains("f="));
        assertTrue(returned.contains("val=-16.0"));
    }

    public static final BigInteger[] EXPECTED_USEED = new BigInteger[] { new BigInteger("2"), new BigInteger("4"),
            new BigInteger("16"), new BigInteger("256"), new BigInteger("65536"), new BigInteger("4294967296"),
            new BigInteger("18446744073709551616"), new BigInteger("340282366920938463463374607431768211456"),
            new BigInteger("115792089237316195423570985008687907853269984665640564039457584007913129639936") };

    @Test
    public void positUseed() {
        // Useed is 2 ** 2 ** es
        for (int es = 0; es < 9; es++) {
            // Puny long runs out at es=6, puny double rounds at es=6.
            // System.out.println( "Es is " + es + ", useed is " + Math.pow(2, Math.pow(2,
            // es)));
            // Useed is 2 ** 2 ** es
            assertEquals("Exponent useed test on es " + es, EXPECTED_USEED[es], PositImmutable.getUseed(es));
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
        // for (int i = 0; i < BINARY_TEST_CASES.length; i++) {
        //     assertArrayEquals("getComponents ES1 test on " + BINARY_TEST_CASES[i], EXPECTED_ES1_COMPONENTS[i],
        //             PositDomain.getComponents(BINARY_TEST_CASES[i], 1, false));
        // }
        // Test ES1 components with twos complement
        for (int i = 0; i < BINARY_TEST_CASES.length; i++) {
            assertArrayEquals("getComponents twos ES1 test on " + BINARY_TEST_CASES[i],
                    EXPECTED_ES1_COMPONENTS_TWOS[i],
                    PositDomain.getComponentsFlipNegative(BINARY_TEST_CASES[i], 1));
        }
        // Test ES2 components with NO twos complement
        // for (int i = 0; i < BINARY_TEST_CASES.length; i++) {
        //     assertArrayEquals("getComponents ES2 test on " + BINARY_TEST_CASES[i],
        //             EXPECTED_ES2_COMPONENTS[i],
        //             PositDomain.getComponents(BINARY_TEST_CASES[i], 2, false));
        // }
        // Test ES2 components with twos complement
        for (int i = 0; i < BINARY_TEST_CASES.length; i++) {
            assertArrayEquals("getComponents twos ES2 test on " + BINARY_TEST_CASES[i],
                    EXPECTED_ES2_COMPONENTS_TWOS[i],
                    PositDomain.getComponentsFlipNegative(BINARY_TEST_CASES[i], 2));
        }
    }

    public static final String[] EXPECTED_ES1_SPACED_STRING = { 
            "", // 0
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
            "0 000", "0 001", "0 01 0", "0 01 1", "0 10 0", "0 10 1", "0 110", "0 111", // 4
            "1 000", "1 001", "1 01 0", "1 01 1", "1 10 0", "1 10 1", "1 110", "1 111", // 4
            "0 0000", "0 0001", "0 001 0", "0 001 1", "0 01 00", "0 01 01", "0 01 10", "0 01 11", // 5
            "0 10 00", "0 10 01", "0 10 10", "0 10 11", "0 110 0", "0 110 1", "0 1110", "0 1111", // 5
            "1 0000", "1 0001", "1 001 0", "1 001 1", "1 01 00", "1 01 01", "1 01 10", "1 01 11", // 5
            "1 10 00", "1 10 01", "1 10 10", "1 10 11", "1 110 0", "1 110 1", "1 1110", "1 1111", // 5
    };
    public static final String[] EXPECTED_ES2_SPACED_STRING_TWOS = { 
            "", // 0
            "0", "1", // 1
            "0 0 e_ f_", "0 1 e_ f_", "1 0 e_ f_", "1 1 e_ f_", // 2
            "0 00 e_ f_", "0 01 e_ f_", "0 10 e_ f_", "0 11 e_ f_", "1 00 e_ f_", "1 11 e_ f_", "1 10 e_ f_", "1 01 e_ f_", // 3
            "0 000 e_ f_", "0 001 e_ f_", "0 01 e0 f_", "0 01 e1 f_", "0 10 e0 f_", "0 10 e1 f_", "0 110 e_ f_", "0 111 e_ f_", // 4
            "1 000 e_ f_", "1 111 e_ f_", "1 110 e_ f_", "1 10 e1 f_", "1 10 e0 f_", "1 01 e1 f_", "1 01 e0 f_", "1 001 e_ f_", // 4
            "0 0000 e_ f_", "0 0001 e_ f_", "0 001 e0 f_", "0 001 e1 f_", "0 01 e00 f_", "0 01 e01 f_", "0 01 e10 f_", "0 01 e11 f_", // 5
            "0 10 e00 f_", "0 10 e01 f_", "0 10 e10 f_", "0 10 e11 f_", "0 110 e0 f_", "0 110 e1 f_", "0 1110 e_ f_", "0 1111 e_ f_", // 5
            "1 0000 e_ f_", "1 1111 e_ f_", "1 1110 e_ f_", "1 110 e1 f_", "1 110 e0 f_", "1 10 e11 f_", "1 10 e10 f_", "1 10 e01 f_", // 5
            "1 10 e00 f_", "1 01 e11 f_", "1 01 e10 f_", "1 01 e01 f_", "1 01 e00 f_", "1 001 e1 f_", "1 001 e0 f_", "1 0001 e_ f_", // 5
    };

    @Test
    public void toSpacedString() {
        // Test ES1 spaced string with NO twos complement
        // toSpacedString supports twosComplement, component markers, placeHolders
        // for (int i = 0; i < BINARY_TEST_CASES.length; i++) {
            // System.out.println( "toSpacedString i=" + i + ", exp=\"" + EXPECTED_ES1_SPACED_STRING[i] + 
            //         "\", got=\"" + PositDomain.toSpacedString(BINARY_TEST_CASES[i], 1, false, true, false) + "\"");
        //     assertEquals("toSpacedString ES1 test on " + BINARY_TEST_CASES[i], EXPECTED_ES1_SPACED_STRING[i],
        //            PositDomain.toSpacedString(BINARY_TEST_CASES[i], 1, false, true, false));
        //}
        // Test ES1 spaced string with twos complement
        for (int i = 0; i < BINARY_TEST_CASES.length; i++) {
            // System.out.println( "toSpacedString i=" + i + ", exp=\"" + EXPECTED_ES1_SPACED_STRING_TWOS[i] + 
            //         "\", got=\"" + PositDomain.toSpacedString(BINARY_TEST_CASES[i], 1, true, true, false) + "\"");
            assertEquals("toSpacedString twos ES1 test on " + BINARY_TEST_CASES[i], EXPECTED_ES1_SPACED_STRING_TWOS[i],
                    PositDomain.toSpacedString(BINARY_TEST_CASES[i], 1, true, false));
        }
        // Test ES2 spaced string with NO twos complement. Use a default/compact method.
        // for (int i = 0; i < BINARY_TEST_CASES.length; i++) {
            // System.out.println( "toSpacedString i=" + i + ", exp=\"" + EXPECTED_ES2_SPACED_STRING[i] + 
            //         "\", got=\"" + PositDomain.toSpacedString(BINARY_TEST_CASES[i], 2, false) + "\"");
        //    assertEquals("toSpacedString ES2 test on " + BINARY_TEST_CASES[i], EXPECTED_ES2_SPACED_STRING[i],
        //            PositDomain.toSpacedString(BINARY_TEST_CASES[i], 2, false));
        //}
        // Test ES2 spaced string with twos complement. Use markers and placeholders.
        for (int i = 0; i < BINARY_TEST_CASES.length; i++) {
            // System.out.println( "toSpacedString i=" + i + ", exp=\"" + EXPECTED_ES2_SPACED_STRING_TWOS[i] + 
            //         "\", got=\"" + PositDomain.toSpacedString(BINARY_TEST_CASES[i], 2, true, true, true) + "\"");
            assertEquals("toSpacedString twos ES2 test on " + BINARY_TEST_CASES[i], EXPECTED_ES2_SPACED_STRING_TWOS[i],
                    PositDomain.toSpacedString(BINARY_TEST_CASES[i], 2, true, true));
        }
    }
    
    // es = 0, useed = 2^2^0 = 2
    public static final double[] EXPECTED_FOURBIT_ES0 = {
            // k            -2      -1        -1         0         0          0        2
            // u^k         1/4                             1                  2        4
            // "0 000", "0 001", "0 01 0", "0 01 1", "0 10 0", "0 10 1", "0 110", "0 111", // 4
            // "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111", // 4
            0.0, 1.0/4.0, 1.0/2.0, 2.0/3.0, 1.0, 3.0/2.0, 2.0, 4.0, 
            Double.POSITIVE_INFINITY, -4.0, -2.0, -3.0/2.0, -1.0, -2.0/3.0, -1.0/2.0, -1.0/4.0 };
    @Test
    public void fourBitES0() {
        int oosCount = 0;
        for (int i = 0; i < EXPECTED_FOURBIT_ES0.length; i++) {
            final String instance = String.format("%4s", Integer.toBinaryString(i)).replace(" ", "0");
            final Posit p = new PositStringImpl(instance, 0);
            final String outOfSpec = outOfSpec(p.doubleValue(), EXPECTED_FOURBIT_ES0[i], COMPARE_PRECISION );
            if (outOfSpec.length() > 0) {
                System.out.println("val=" + p.doubleValue() );
                System.out.println("i=" + i + ", posit=" + PositDomain.toDetailsString(instance, 0) + ",exp="
                        + EXPECTED_FOURBIT_ES0[i] + outOfSpec);
                oosCount++;
            }
            // System.out.println("i=" + i + ", posit=" + PositDomain.toDetailsString(instance, 0) + ",exp="
            //         + EXPECTED_FOURBIT_ES0[i] + outOfSpec);
            assertEquals(EXPECTED_FOURBIT_ES0[i], p.doubleValue(), PositDomainTest.COMPARE_PRECISION);
        }
        System.out.println("fourBitES0 out of spec count=" + oosCount + "/" + EXPECTED_FOURBIT_ES0.length + ".");
    }

    // es = 1, useed = 2^2^1 = 4
    public static final double[] EXPECTED_FOURBIT_ES1 = { 
            0.0, 1.0/16.0, 1.0/4.0, 1.0/2.0, 
            1.0, 2.0, 4.0, 16.0,
            Double.POSITIVE_INFINITY, -16.0, -4.0, -2.0, 
            -1.0, -1.0/2.0, -1.0/4.0, -1.0/16.0 };
    @Test
    public void fourBitES1() {
        int oosCount = 0;
        for (int i = 0; i < EXPECTED_FOURBIT_ES1.length; i++) {
            final String instance = String.format("%4s", Integer.toBinaryString(i)).replace(" ", "0");
            final Posit p = new PositStringImpl(instance, 1);
            final String outOfSpec = outOfSpec(p.doubleValue(), EXPECTED_FOURBIT_ES1[i], COMPARE_PRECISION);
            if (outOfSpec.length() > 0) {
                oosCount++;
                System.out.println("i=" + i + ", posit=" + PositDomain.toDetailsString(instance, 1) + ",exp="
                        + EXPECTED_FOURBIT_ES1[i] + outOfSpec);
            }
            assertEquals(EXPECTED_FOURBIT_ES1[i], p.doubleValue(), PositDomainTest.COMPARE_PRECISION);
        }
        System.out.println("fourBitES1 out of spec count=" + oosCount + "/" + EXPECTED_FOURBIT_ES1.length + ".");
    }

    // es = 2, useed = 2^2^2 = 256
    public static final double[] EXPECTED_FOURBIT_ES2 = { 
            0.0, 1.0 / 256.0, 1.0 / 16.0, 1.0 / 4.0, 1.0, 4.0, 16.0, 256.0,
            Double.POSITIVE_INFINITY, -256.0, -16.0, -4.0, -1.0, -1.0 / 4.0, -1.0 / 16.0, -1.0 / 256 };
    @Test
    public void fourBitES2() {
        int oosCount = 0;
        for (int i = 0; i < EXPECTED_FOURBIT_ES2.length; i++) {
            final String instance = String.format("%4s", Integer.toBinaryString(i)).replace(" ", "0");
            final Posit p = new PositStringImpl(instance, 2);
            final String outOfSpec = outOfSpec(p.doubleValue(), EXPECTED_FOURBIT_ES2[i], COMPARE_PRECISION);
            if (outOfSpec.length() > 0) {
                oosCount++;
                System.out.println("i=" + i + ", posit=" + PositDomain.toDetailsString(instance, 2) + ",exp="
                        + EXPECTED_FOURBIT_ES2[i] + outOfSpec);
            }
            assertEquals(EXPECTED_FOURBIT_ES2[i], p.doubleValue(), PositDomainTest.COMPARE_PRECISION);
        }
        System.out.println("fourBitES2 out of spec count=" + oosCount + "/" + EXPECTED_FOURBIT_ES2.length + ".");
    }

    // Check that posits invert and negate symmetrically.
    @Test
    public void fourBitSymmetry() {
        int oosCount = 0;
        final int FULL = EXPECTED_FOURBIT_ES2.length;
        final int HALF = FULL / 2;
        final int QUARTER = FULL / 4;
        for (int maxEs = 0; maxEs < 3; maxEs++) {
            System.out.println("fourBitSymmetry of maxEs=" + maxEs + ".");
            
            
            for (int i = 1; i < QUARTER; i++) {
                final String instance1 = String.format("%4s", Integer.toBinaryString(i)).replace(" ", "0");
                final String instance2 = String.format("%4s", Integer.toBinaryString(HALF - i)).replace(" ", "0");
                final String instance3 = String.format("%4s", Integer.toBinaryString(HALF + i)).replace(" ", "0");
                final String instance4 = String.format("%4s", Integer.toBinaryString(FULL - i)).replace(" ", "0");
            
                final Posit p1 = new PositStringImpl(instance1, maxEs);
                final Posit p2 = new PositStringImpl(instance2, maxEs);
                final Posit p3 = new PositStringImpl(instance3, maxEs);
                final Posit p4 = new PositStringImpl(instance4, maxEs);
            
                // 1/p1 to p2
                String outOfSpec = outOfSpec(1.0 / p1.doubleValue(), p2.doubleValue(), COMPARE_PRECISION);
                if (outOfSpec.length() > 0) {
                    oosCount++;
                    System.out.println("i=" + i + ", posit=" + p2 
                       + ", 1.0/p1=" + 1.0 / p1.doubleValue() + ", p2=" + p2.doubleValue()
                       + outOfSpec);
                }
                assertEquals(1.0 / p1.doubleValue(), p2.doubleValue(), PositDomainTest.COMPARE_PRECISION);
                
                // p1 to -p4
                outOfSpec = outOfSpec(p1.doubleValue(), -1.0 * p4.doubleValue(), COMPARE_PRECISION);
                if (outOfSpec.length() > 0) {
                    oosCount++;
                    System.out.println("i=" + i + ", posit=" + p1 
                       + ", p1=" + p1.doubleValue() + ", -p4=" + -1.0 * p4.doubleValue()
                       + outOfSpec);
                }
                assertEquals(p1.doubleValue(), -1.0 * p4.doubleValue(), PositDomainTest.COMPARE_PRECISION);

                // p2 to -p3
                outOfSpec = outOfSpec(p2.doubleValue(), -1.0 * p3.doubleValue(), COMPARE_PRECISION);
                if (outOfSpec.length() > 0) {
                    oosCount++;
                    System.out.println("i=" + i + ", posit=" + p2 
                       + ", p2=" + p2.doubleValue() + ", -p3=" + -1.0 * p3.doubleValue()
                       + outOfSpec);
                }
                assertEquals(p2.doubleValue(), -1.0 * p3.doubleValue(), PositDomainTest.COMPARE_PRECISION);

                // 1/p4 to p3
                outOfSpec = outOfSpec(1.0 / p4.doubleValue(), p3.doubleValue(), COMPARE_PRECISION);
                if (outOfSpec.length() > 0) {
                    oosCount++;
                    System.out.println("i=" + i + ", posit=" + p3 
                       + ", 1.0/p4=" + 1.0 / p4.doubleValue() + ", p3=" + p3.doubleValue()
                       + outOfSpec);
                }
            }
            System.out.println("fourBitSymmetry out of spec count=" + oosCount + "/" + EXPECTED_FOURBIT_ES2.length + ".");
        }        
    }

    // Check that expected values invert and negate symmetrically.
    @Test
    public void fourBitExpectedValueSymmetry() {
        int oosCount = 0;
        final int FULL = EXPECTED_FOURBIT_ES2.length;
        final int HALF = FULL / 2;
        final int QUARTER = FULL / 4;
        for (int maxEs = 0; maxEs < 3; maxEs++) {
            System.out.println("fourBitExpectedValueSymmetry of maxEs=" + maxEs + ".");
            
            double[] EXPECTED_VALUE = EXPECTED_FOURBIT_ES0;
            switch ( maxEs ) {
                case 0 : EXPECTED_VALUE = EXPECTED_FOURBIT_ES0; break;
                case 1 : EXPECTED_VALUE = EXPECTED_FOURBIT_ES1; break;
                case 2 : EXPECTED_VALUE = EXPECTED_FOURBIT_ES2; break;
                default: throw new IllegalArgumentException( "do not know how to validate four bit maxEs=" + maxEs);
            }
            for (int i = 1; i < QUARTER; i++) {
                final double d1 = EXPECTED_VALUE[ i ];
                final double d2 = EXPECTED_VALUE[ HALF - i ];
                final double d3 = EXPECTED_VALUE[ HALF + i ];
                final double d4 = EXPECTED_VALUE[ FULL - i ];
            
                // 1/d1 to d2
                String outOfSpec = outOfSpec(1.0 / d1, d2, COMPARE_PRECISION);
                if (outOfSpec.length() > 0) {
                    oosCount++;
                    System.out.println("i=" + i  + ", 1.0/d1=" + 1.0 / d1 + ", d2=" + d2
                       + outOfSpec);
                }
                assertEquals(1.0 / d1, d2, PositDomainTest.COMPARE_PRECISION);
                
                // d1 to -d4
                outOfSpec = outOfSpec(d1, -1.0 * d4, COMPARE_PRECISION);
                if (outOfSpec.length() > 0) {
                    oosCount++;
                    System.out.println("i=" + i  + ", d1=" + d1 + ", -p4=" + -1.0 * d4
                       + outOfSpec);
                }
                assertEquals(d1, -1.0 * d4, PositDomainTest.COMPARE_PRECISION);

                // p2 to -p3
                outOfSpec = outOfSpec(d2, -1.0 * d3, COMPARE_PRECISION);
                if (outOfSpec.length() > 0) {
                    oosCount++;
                    System.out.println("i=" + i + ", d2=" + d2 + ", -d3=" + -1.0 * d3
                       + outOfSpec);
                }
                assertEquals(d2, -1.0 * d3, PositDomainTest.COMPARE_PRECISION);

                // 1/p4 to p3
                outOfSpec = outOfSpec(1.0 / d4, d3, COMPARE_PRECISION);
                if (outOfSpec.length() > 0) {
                    oosCount++;
                    System.out.println("i=" + i + ", 1.0/d4=" + 1.0 / d4 + ", d3=" + d3
                       + outOfSpec);
                }
            }
            System.out.println("fourBitExpectedValueSymmetry out of spec count=" + oosCount + "/" + EXPECTED_FOURBIT_ES2.length + ".");
        }        
    }
    
    // es = 0, useed = 2^2^0 = 2
    public static final double[] EXPECTED_FIVEBIT_ES0 = {
            0.0, 1.0 / 8.0, 1.0 / 4.0, 1.0 / 3.0 , 1.0 / 2.0, 4.0 / 7.0, 4.0 / 6.0, 4.0 / 5.0,
            1.0, 5.0 / 4.0, 6.0 / 4.0, 7.0 / 4.0, 2.0, 3.0, 4.0, 8.0,
            Double.POSITIVE_INFINITY, -8.0, -4.0, -3.0, -2.0, -7.0/4.0, -6.0/4.0, -5.0/4.0,
            -1.0, -4.0 / 5.0, -4.0 / 6.0, -4.0 / 7.0, -1.0 / 2.0, -1.0 / 3.0, -1.0 / 4.0, -1.0 / 8.0 };    
    @Test
    public void fiveBitES0() {
        int oosCount = 0;
        for (int i = 0; i < EXPECTED_FIVEBIT_ES0.length; i++) {
            final String instance = String.format("%5s", Integer.toBinaryString(i)).replace(" ", "0");
            final Posit p = new PositStringImpl(instance, 0);
            final String outOfSpec = outOfSpec(p.doubleValue(), EXPECTED_FIVEBIT_ES0[i], COMPARE_PRECISION);
            if (outOfSpec.length() > 0) {
                System.out.println("val=" + p.doubleValue() );
                oosCount++;
                System.out.println("testcase=" + i + ", posit=" + PositDomain.toDetailsString(instance, 0) + ",exp="
                        + EXPECTED_FIVEBIT_ES0[i] + outOfSpec);
            }
            assertEquals(EXPECTED_FIVEBIT_ES0[i], p.doubleValue(), PositDomainTest.COMPARE_PRECISION);
        }
        System.out.println("fiveBitES0 out of spec count=" + oosCount + "/" + EXPECTED_FIVEBIT_ES0.length + ".");
    }
    
    // es = 0, useed = 2^2^0 = 2
    public static final double[] EXPECTED_FIVEBIT_ES0_GUSTAFSON = {
            0.0, 1.0 / 8.0, 1.0 / 4.0, 3.0 / 8.0 , 1.0 / 2.0, 5.0 / 8.0, 3.0 / 4.0, 7.0 / 8.0,
            1.0, 5.0 / 4.0, 6.0 / 4.0, 7.0 / 4.0, 2.0, 3.0, 4.0, 8.0,
            Double.POSITIVE_INFINITY, -8.0, -4.0, -3.0, -2.0, -7.0/4.0, -6.0/4.0, -5.0/4.0,
            -1.0, -7.0 / 8.0, -3.0 / 4.0, -5.0 / 8.0, -1.0 / 2.0, -3.0 / 8.0, -1.0 / 4.0, -1.0 / 8.0 };    
    @Test
    public void fiveBitES0Gustafson() {
        int oosCount = 0;
        for (int i = 0; i < EXPECTED_FIVEBIT_ES0.length; i++) {
            final String instance = String.format("%5s", Integer.toBinaryString(i)).replace(" ", "0");
            final Posit p = new PositStringImpl(instance, 0);
            final String outOfSpec = outOfSpec(p.doubleValueGustafson(), EXPECTED_FIVEBIT_ES0_GUSTAFSON[i], COMPARE_PRECISION);
            if (outOfSpec.length() > 0) {
                System.out.println("val=" + p.doubleValueGustafson() );
                oosCount++;
                System.out.println("testcase=" + i + ", posit=" + PositDomain.toDetailsString(instance, 0) + ",exp="
                        + EXPECTED_FIVEBIT_ES0_GUSTAFSON[i] + outOfSpec);
            }
            assertEquals(EXPECTED_FIVEBIT_ES0_GUSTAFSON[i], p.doubleValueGustafson(), PositDomainTest.COMPARE_PRECISION);
        }
        System.out.println("fiveBitES0Gustafson out of spec count=" + oosCount + "/" + EXPECTED_FIVEBIT_ES0.length + ".");
    }

    @Test
    public void fiveBitES0Difference() {
        int BITS = 5;
        int MAXES = 0;
        int LIMIT = Bit.pow( 2, BITS); 
        int oosCount = 0;
        for (int i = 0; i < LIMIT; i++) {
            final String instance = String.format("%5s", Integer.toBinaryString(i)).replace(" ", "0");
            final Posit p = new PositStringImpl(instance, MAXES);
            final String outOfSpec = outOfSpec(p.doubleValueGustafson(), p.doubleValue(), COMPARE_PRECISION);
            if (outOfSpec.length() > 0) {
                oosCount++;
                System.out.println("testcase=" + i + ", posit=" + PositDomain.toDetailsString(instance, 0) + ",gustafson="
                        + p.doubleValueGustafson() + outOfSpec);
            }
        }
        System.out.println("fiveBitES0Gustafson out of spec count=" + oosCount + "/" + LIMIT + ".");
    }
    
    // es = 1, useed = 2^2^1 = 4
    public static final double[] EXPECTED_FIVEBIT_ES1 = {
            0.0, 1.0 / 64.0, 1.0 / 16.0, 1.0 / 8.0, 1.0 / 4.0, 1.0 / 3.0, 1.0 / 2.0, 2.0 / 3.0,
            1.0, 3.0 / 2.0, 2.0, 3.0, 4.0, 8.0, 16.0, 64.0,
            Double.POSITIVE_INFINITY, -64.0, -16.0, -8.0, -4.0, -3.0, -2.0, -3.0 / 2.0,
            -1.0, -2.0 / 3.0, -1.0 / 2.0, -1.0 / 3.0, -1.0 / 4.0, -1.0 / 8.0, -1.0 / 16.0, -1.0 / 64.0 };
    @Test
    public void fiveBitES1() {
        int oosCount = 0;
        for (int i = 0; i < EXPECTED_FIVEBIT_ES1.length; i++) {
            final String instance = String.format("%5s", Integer.toBinaryString(i)).replace(" ", "0");
            final Posit p = new PositStringImpl(instance, 1);
            final String outOfSpec = outOfSpec(p.doubleValue(), EXPECTED_FIVEBIT_ES1[i], COMPARE_PRECISION);
            if (outOfSpec.length() > 0) {
                oosCount++;
                System.out.println("testcase=" + i + ", posit=" + PositDomain.toDetailsString(instance, 1) + ",exp="
                        + EXPECTED_FIVEBIT_ES1[i] + outOfSpec);
            }
            assertEquals(EXPECTED_FIVEBIT_ES1[i], p.doubleValue(), PositDomainTest.COMPARE_PRECISION);
        }
        System.out.println("fiveBitES1 out of spec count=" + oosCount + "/" + EXPECTED_FIVEBIT_ES1.length + ".");
    }

    // es = 2, useed = 2^2^2 = 16
    public static final double[] EXPECTED_FIVEBIT_ES2 = {
            0.0, // 0 "0 0000"
            1.0 / 4096.0, // 1 "0 0001"
            1.0 / 256.0, // 2 "0 001 e0"
            1.0 / 64.0, // 3 "0 001 e1"
            1.0 / 16.0, // 4 "0 01 e00"
            1.0 / 8.0, // 5 "0 01 e01"
            1.0 / 4.0, // 6 "0 01 e10"
            1.0 / 2.0, // 7 "0 01 e11"
            1.0,
            2.0,
            4.0,
            8.0,
            16.0,
            64.0,
            256.0,
            4096.0,
            Double.POSITIVE_INFINITY,
            -4096.0, -256.0, -64.0, -16.0, -8.0, -4.0, -2.0, -1.0,
            -1.0 / 2.0, -1.0 / 4.0, -1.0 / 8.0, -1.0 / 16.0, -1.0 / 64.0, -1.0 / 256.0, -1.0 / 4096.0 };
    
    @Test
    public void fiveBitES2() {
        int oosCount = 0;
        for (int i = 0; i < EXPECTED_FIVEBIT_ES2.length; i++) {
            final String instance = String.format("%5s", Integer.toBinaryString(i)).replace(" ", "0");
            final Posit p = new PositStringImpl(instance, 2);
            final String outOfSpec = outOfSpec(p.doubleValue(), EXPECTED_FIVEBIT_ES2[i], COMPARE_PRECISION);
            if (outOfSpec.length() > 0) {
                oosCount++;
                System.out.println("testcase=" + i + ", posit=" + PositDomain.toDetailsString(instance, 2) + ",exp="
                        + EXPECTED_FIVEBIT_ES2[i] + outOfSpec);
            }
            assertEquals(EXPECTED_FIVEBIT_ES2[i], p.doubleValue(), PositDomainTest.COMPARE_PRECISION);
        }
        System.out.println("fiveBitES2 out of spec count=" + oosCount + "/" + EXPECTED_FIVEBIT_ES2.length + ".");
    }

    // es = 0, useed = 2^2^0 = 2
    public static final double[] EXPECTED_SIXBIT_ES0 = {
            0.0, // "0 00000", 0
            1.0 / 16.0, // "0 00001", 1, k=-4, 1/2^4=0.0625
            1.0 / 8.0, // "0 0001 0", 2, k=-3, 1/2^3=0.125
            1.0 / 6.0, // "0 0001 1", 3, k=-3, 1/2^3=0.125, 1 = 1/2^3 + 1/2^2 * 1/2
            1.0 / 4.0, // "0 001 00", 4, k=-2, 1/2^2=0.25
            1.0 / 3.5, // "0 001 01", 5, k=-2, 1/2^2=0.25, 01 = 1/2^2 + 1/2^1 *1/4
            1.0 / 3.0, // "0 001 10", 6, k=-2, 1/2^2=0.25, 10 = 1/2^2 + 1/2^1 *2/4
            1.0 / 2.5, // "0 001 11", 7, k=-2, 1/2^2=0.25, 11 = 1/2^2 + 1/2^1 *3/4
            1.0 / 2.0, // "0 01 000", 8, k=-1, 1/2^1=0.5
            1.0 / 1.875, // "0 01 001", 9, k=-1, 1/2^1=0.5, 001 = 1/2^1 + 1/2^0 *1/8 
            1.0 / 1.75, // "0 01 010", 10, k=-1, 1/2^1=0.5, 010 = 1/2^1 + 1/2^0 *2/8
            1.0 / 1.625, // "0 01 011", 11, k=-1, 1/2^1=0.5, 011 = 1/2^1 + 1/2^0 *3/8
            1.0 / 1.5, // "0 01 100", 12, k=-1, 1/2^1=0.5, 100 = 1/2^1 + 1/2^0 *4/8
            1.0 / 1.375, // "0 01 101", 13, k=-1, 1/2^1=0.5, 101 = 1/2^1 + 1/2^0 *5/8
            1.0 / 1.25, // "0 01 110", 14, k=-1, 1/2^1=0.5, 110 = 1/2^1 + 1/2^0 *6/8
            1.0 / 1.125, // "0 01 111", 15, k=-1, 1/2^1=0.5, 111 = 1/2^1 + 1/2^0 *7/8
            1.0, // "0 10 000", 16, k=0, 1/2^0=1
            1.125, // "0 10 001", 17, k=0, 1/2^0=1, 001 = (2^1-2^0)/2 *1/8 = 2-1*1/8 = 1*1/8 
            1.25, // "0 10 010", 18, k=0, 1/2^0=1
            1.375, // "0 10 011", 19, k=0, 1/2^0=1
            1.5, // "0 10 100", 20, k=0, 1/2^0=1
            1.625, // "0 10 101", 21, k=0, 1/2^0=1
            1.75, // "0 10 110", 22, k=0, 1/2^0=1
            1.875, // "0 10 111", 23, k=0, 1/2^0=1
            2.0, // "0 110 00", 24, k=1, 2^1=2
            2.5, // "0 110 01", 25, k=1, 2^1=2, 01 = (2^2-2^1)/2 *1/4 = 4-2 *1/4, 2*1/4
            3.0, // "0 110 10", 26
            3.5, // "0 110 11", 27
            4.0, // "0 1110 0", 28, k-2, 2^2=4
            6.0, // "0 1110 1", 29
            8.0, // "0 11110", 30, k=3, 2^3=8
            16.0, // "0 11111", 31, k=4, 2^4=16
            Double.POSITIVE_INFINITY, // "1 00000", 32
            -16.0, // "1 00001", 33
            -8.0, // "1 0001 0", 34
            -6.0, // "1 0001 1", 35
            -4.0, // "1 001 00", 36
            -3.5, // "1 001 01", 37
            -3.0, // "1 001 10", 38
            -2.5, // "1 001 11", 39
            -2.0, // "1 01 0 00", 40
            -1.875, // "1 01 0 01", 41
            -1.75, // "1 01 0 10", 42
            -1.625, // "1 01 0 11", 43
            -1.5, // "1 01 1 00", 44
            -1.375, // "1 01 1 01", 45
            -1.25, // "1 01 1 10", 46
            -1.125, // "1 01 1 11", 47
            -1.0, // "1 10 0 00", 48
            -1.0 / 1.125, // "1 10 0 01", 49
            -1.0 / 1.25, // "1 10 0 10", 50
            -1.0 / 1.375, // "1 10 0 11", 51
            -1.0 / 1.5, // "1 10 1 00", 52
            -1.0 / 1.625, // "1 10 1 01", 53
            -1.0 / 1.75, // "1 10 1 10", 54
            -1.0 / 1.875, // "1 10 1 11", 55
            -1.0 / 2.0, // "1 110 00", 56
            -1.0 / 2.5, // "1 110 01", 57
            -1.0 / 3.0, // "1 110 10", 58
            -1.0 / 3.5, // "1 110 11", 59
            -1.0 / 4.0, // "1 1110 0", 60
            -1.0 / 6.0, // "1 1110 1", 61
            -1.0 / 8.0, // "1 11110", 62
            -1.0 / 16.0, // "1 11111", 63
    };

    @Test
    public void sixBitES0() {
        int oosCount = 0;
        for (int i = 0; i < EXPECTED_SIXBIT_ES0.length; i++) {
            final String instance = String.format("%6s", Integer.toBinaryString(i)).replace(" ", "0");
            final Posit p = new PositStringImpl(instance, 0);
            final String outOfSpec = outOfSpec(p.doubleValue(), EXPECTED_SIXBIT_ES0[i], COMPARE_PRECISION);
            if (outOfSpec.length() > 0) {
                oosCount++;
                System.out.println("sixBitES0 i=" + i + ", posit=" + PositDomain.toDetailsString(instance, 0) + ",exp="
                        + EXPECTED_SIXBIT_ES0[i] + ",1.0/exp=" + 1.0 / EXPECTED_SIXBIT_ES0[i] + outOfSpec);
            }
            assertEquals(EXPECTED_SIXBIT_ES0[i], p.doubleValue(), PositDomainTest.COMPARE_PRECISION);
        }
        System.out.println("sixBitES0 out of spec count=" + oosCount + "/" + EXPECTED_SIXBIT_ES0.length + ".");
    }

    // es = 1, useed = 2^2^1 = 4
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
            -1.0 / 4.0, // "1 110 00", 56
            -1.0 / 6.0, // "1 110 01", 57
            -1.0 / 8.0, // "1 110 10", 58
            -1.0 / 12.0, // "1 110 11", 59
            -1.0 / 16.0, // "1 1110 0", 60
            -1.0 / 32.0, // "1 1110 1", 61
            -1.0 / 64.0, // "1 11110", 62
            -1.0 / 256.0, // "1 11111", 63
    };

    @Test
    public void sixBitES1() {
        int oosCount = 0;
        for (int i = 0; i < EXPECTED_SIXBIT_ES1.length; i++) {
            final String instance = String.format("%6s", Integer.toBinaryString(i)).replace(" ", "0");
            final Posit p = new PositStringImpl(instance, 1);
            final String outOfSpec = outOfSpec(p.doubleValue(), EXPECTED_SIXBIT_ES1[i], COMPARE_PRECISION);
            if (outOfSpec.length() > 0) {
                oosCount++;
            }
            System.out.println("sixBitES1 i=" + i + ", posit=" + PositDomain.toDetailsString(instance, 1) + ",exp="
                    + EXPECTED_SIXBIT_ES1[i] + ",1.0/exp=" + 1.0 / EXPECTED_SIXBIT_ES1[i] + outOfSpec);
            assertEquals(EXPECTED_SIXBIT_ES1[i], p.doubleValue(), PositDomainTest.COMPARE_PRECISION);
        }
        System.out.println("sixBitES1 out of spec count=" + oosCount + "/" + EXPECTED_SIXBIT_ES1.length + ".");
    }

    @Test
    public void sixBitES1Difference() {
        int BITS = 6;
        int MAXES = 1;
        int LIMIT = Bit.pow( 2, BITS); 
        int oosCount = 0;
        for (int i = 0; i < LIMIT; i++) {
            final String instance = String.format("%5s", Integer.toBinaryString(i)).replace(" ", "0");
            final Posit p = new PositStringImpl(instance, MAXES);
            final String outOfSpec = outOfSpec(p.doubleValueGustafson(), p.doubleValue(), COMPARE_PRECISION);
            if (outOfSpec.length() > 0) {
                oosCount++;
                System.out.println("testcase=" + i + ", posit=" + PositDomain.toDetailsString(instance, 0) + ",gustafson="
                        + p.doubleValueGustafson() + outOfSpec);
            }
        }
        System.out.println("sixBitES1Gustafson out of spec count=" + oosCount + "/" + LIMIT + ".");
    }
    

    // es = 2, useed = 2^2^2 = 16
    public static final double[] EXPECTED_SIXBIT_ES2 = { 
            0.0, // "0 00000", 0
            1.0 / 65536.0, // "0 00001", 1
            1.0 / 4096.0, // "0 0001 0", 2
            1.0 / 1024.0, // "0 0001 1", 3
            1.0 / 256.0, // "0 001 00", 4
            1.0 / 128.0, // "0 001 01", 5
            1.0 / 64.0, // "0 001 10", 6
            1.0 / 32.0, // "0 001 11", 7
            1.0 / 16.0, // "0 01 00 0", 8
            1.0 / 12.0, // "0 01 00 1", 9
            1.0 / 8.0, // "0 01 01 0", 10
            1.0 / 6.0, // "0 01 01 1", 11
            1.0 / 4.0, // "0 01 10 0", 12
            1.0 / 3.0, // "0 01 10 1", 13
            1.0 / 2.0, // "0 01 11 0", 14
            1.0 / 1.5, // "0 01 11 1", 15
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
    public void sixBitES2() {
        int oosCount = 0;
        for (int i = 0; i < EXPECTED_SIXBIT_ES2.length; i++) {
            final String instance = String.format("%6s", Integer.toBinaryString(i)).replace(" ", "0");
            final Posit p = new PositStringImpl(instance, 2);
            final String outOfSpec = outOfSpec(p.doubleValue(), EXPECTED_SIXBIT_ES2[i], COMPARE_PRECISION);
            if (outOfSpec.length() > 0) {
                oosCount++;
                System.out.println("sixBitES2 i=" + i + ", posit=" + PositDomain.toDetailsString(instance, 2) + ",exp="
                        + EXPECTED_SIXBIT_ES2[i] + ",1.0/exp=" + 1.0 / EXPECTED_SIXBIT_ES2[i] + outOfSpec);
            }
            assertEquals(EXPECTED_SIXBIT_ES2[i], p.doubleValue(), PositDomainTest.COMPARE_PRECISION);
        }
        System.out.println("sixBitES2 out of spec count=" + oosCount + "/" + EXPECTED_SIXBIT_ES2.length + ".");
    }

    // es = 1, useed = 2^2^1 = 4
    public static final double[] EXPECTED_SEVENBIT_ES1 = {
            0.0, // 0b0000000,
            0.0009765625, // 0b0000001
            0.00390625, // 0b000001_0
            0.0078125, // 0b000001_1
            0.015625, // @test M7_1(0b00001_0_0) == M7_1(0.015625)
            0.0234375, // @test M7_1(0b00001_0_1) == M7_1(0.0234375)
            0.03125, // @test M7_1(0b00001_1_0) == M7_1(0.03125)
            0.046875, // @test M7_1(0b00001_1_1) == M7_1(0.046875)
            0.0625, // @test M7_1(0b0001_0_00) == M7_1(0.0625)
            0.078125, // @test M7_1(0b0001_0_01) == M7_1(0.078125)
            0.09375, // @test M7_1(0b0001_0_10) == M7_1(0.09375)
            0.109375, // @test M7_1(0b0001_0_11) == M7_1(0.109375)
            0.125, // @test M7_1(0b0001_1_00) == M7_1(0.125)
            0.15625, // @test M7_1(0b0001_1_01) == M7_1(0.15625)
            0.1875, // @test M7_1(0b0001_1_10) == M7_1(0.1875)
            0.21875, // @test M7_1(0b0001_1_11) == M7_1(0.21875)
            0.25, // @test M7_1(0b001_0_000) == M7_1(0.25)
            0.28125, // @test M7_1(0b001_0_001) == M7_1(0.28125)
            0.3125, // @test M7_1(0b001_0_010) == M7_1(0.3125)
            0.34375, // @test M7_1(0b001_0_011) == M7_1(0.34375)
            0.375, // @test M7_1(0b001_0_100) == M7_1(0.375)
            0.40625, // @test M7_1(0b001_0_101) == M7_1(0.40625)
            0.4375, // @test M7_1(0b001_0_110) == M7_1(0.4375)
            0.46875, // @test M7_1(0b001_0_111) == M7_1(0.46875)
            0.5, // @test M7_1(0b001_1_000) == M7_1(0.5)
            0.5625, // @test M7_1(0b001_1_001) == M7_1(0.5625)
            0.625, // @test M7_1(0b001_1_010) == M7_1(0.625)
            0.6875, // @test M7_1(0b001_1_011) == M7_1(0.6875)
            0.75, // @test M7_1(0b001_1_100) == M7_1(0.75)
            0.8125, // @test M7_1(0b001_1_101) == M7_1(0.8125)
            0.875, // @test M7_1(0b001_1_110) == M7_1(0.875)
            0.9375, // @test M7_1(0b001_1_111) == M7_1(0.9375)

            1.0, // @test M7_1(0b010_0_000) == M7_1(1.0)
            1.125, // @test M7_1(0b010_0_001) == M7_1(1.125)
            1.25, // @test M7_1(0b010_0_010) == M7_1(1.25)
            1.375, // @test M7_1(0b010_0_011) == M7_1(1.375)
            1.5, // @test M7_1(0b010_0_100) == M7_1(1.5)
            1.625, // @test M7_1(0b010_0_101) == M7_1(1.625)
            1.75, // @test M7_1(0b010_0_110) == M7_1(1.75)
            1.875, // @test M7_1(0b010_0_111) == M7_1(1.875)
            2.0, // @test M7_1(0b010_1_000) == M7_1(2.0)
            2.25, // @test M7_1(0b010_1_001) == M7_1(2.25)
            2.5, // @test M7_1(0b010_1_010) == M7_1(2.5)
            2.75, // @test M7_1(0b010_1_011) == M7_1(2.75)
            3.0, // @test M7_1(0b010_1_100) == M7_1(3.0)
            3.25, // @test M7_1(0b010_1_101) == M7_1(3.25)
            3.5, // @test M7_1(0b010_1_110) == M7_1(3.5)
            3.75, // @test M7_1(0b010_1_111) == M7_1(3.75)
            4.0, // @test M7_1(0b0110_0_00) == M7_1(4.0)
            5.0, // @test M7_1(0b0110_0_01) == M7_1(5.0)
            6.0, // @test M7_1(0b0110_0_10) == M7_1(6.0)
            7.0, // @test M7_1(0b0110_0_11) == M7_1(7.0)
            8.0, // @test M7_1(0b0110_1_00) == M7_1(8.0)
            10.0, // @test M7_1(0b0110_1_01) == M7_1(10.0)
            12.0, // @test M7_1(0b0110_1_10) == M7_1(12.0)
            14.0, // @test M7_1(0b0110_1_11) == M7_1(14.0)
            16.0, // @test M7_1(0b01110_0_0) == M7_1(16.0)
            24.0, // @test M7_1(0b01110_0_1) == M7_1(24.0)
            32.0, // @test M7_1(0b01110_1_0) == M7_1(32.0)
            48.0, // @test M7_1(0b01110_1_1) == M7_1(48.0)
            64.0, // @test M7_1(0b011110_0) == M7_1(64.0)
            128.0, // @test M7_1(0b011110_1) == M7_1(128.0)
            256.0, // @test M7_1(0b0111110) == M7_1(256.0)
            1024.0, // @test M7_1(0b0111111) == M7_1(1024.0)
    };

    @Test
    public void sevenBitES1() {
        int oosCount = 0;
        for (int i = 0; i < EXPECTED_SEVENBIT_ES1.length; i++) {
            final String instance = String.format("%7s", Integer.toBinaryString(i)).replace(" ", "0");
            final Posit p = new PositStringImpl(instance, 1);
            final String outOfSpec = outOfSpec(p.doubleValue(), EXPECTED_SEVENBIT_ES1[i], COMPARE_PRECISION);
            if (outOfSpec.length() > 0) {
                oosCount++;
                System.out.println("sevenBitES1 i=" + i + ", posit=" + PositDomain.toDetailsString(instance, 1) +
                        ",exp=" + EXPECTED_SEVENBIT_ES1[i] + ",1.0/exp=" + 1.0 / EXPECTED_SEVENBIT_ES1[i] + outOfSpec);
            }
            // assertEquals(EXPECTED_SEVENBIT_ES1[i], p.doubleValue(), PositDomainTest.COMPARE_PRECISION);
        }
        System.out.println("sevenBitES1 out of spec count=" + oosCount + "/" + EXPECTED_SEVENBIT_ES1.length + ".");
    }

    public static final double[] EXPECTED_SEVENBIT_ES2 = {
            0.0, // @test M7_2(0b0000000) == M7_2(0.0)
            0.00000095367431640625, // @test M7_2(0b0000001) == M7_2(0.00000095367431640625)
            0.0000152587890625, // @test M7_2(0b000001_0) == M7_2(0.0000152587890625)
            0.00006103515625, // @test M7_2(0b000001_1) == M7_2(0.00006103515625)
            0.000244140625, // @test M7_2(0b00001_00) == M7_2(0.000244140625)
            0.00048828125, // @test M7_2(0b00001_01) == M7_2(0.00048828125)
            0.0009765625, // @test M7_2(0b00001_10) == M7_2(0.0009765625)
            0.001953125, // @test M7_2(0b00001_11) == M7_2(0.001953125)
            0.00390625, // @test M7_2(0b0001_00_0) == M7_2(0.00390625)
            0.005859375, // @test M7_2(0b0001_00_1) == M7_2(0.005859375)
            0.0078125, // @test M7_2(0b0001_01_0) == M7_2(0.0078125)
            0.01171875, // @test M7_2(0b0001_01_1) == M7_2(0.01171875)
            0.015625, // @test M7_2(0b0001_10_0) == M7_2(0.015625)
            0.0234375, // @test M7_2(0b0001_10_1) == M7_2(0.0234375)
            0.03125, // @test M7_2(0b0001_11_0) == M7_2(0.03125)
            0.046875, // @test M7_2(0b0001_11_1) == M7_2(0.046875)
            0.0625, // @test M7_2(0b001_00_00) == M7_2(0.0625)
            0.078125, // @test M7_2(0b001_00_01) == M7_2(0.078125)
            0.09375, // @test M7_2(0b001_00_10) == M7_2(0.09375)
            0.109375, // @test M7_2(0b001_00_11) == M7_2(0.109375)
            0.125, // @test M7_2(0b001_01_00) == M7_2(0.125)
            0.15625, // @test M7_2(0b001_01_01) == M7_2(0.15625)
            0.1875, // @test M7_2(0b001_01_10) == M7_2(0.1875)
            0.21875, // @test M7_2(0b001_01_11) == M7_2(0.21875)
            0.25, // @test M7_2(0b001_10_00) == M7_2(0.25)
            0.3125, // @test M7_2(0b001_10_01) == M7_2(0.3125)
            0.375, // @test M7_2(0b001_10_10) == M7_2(0.375)
            0.4375, // @test M7_2(0b001_10_11) == M7_2(0.4375)
            0.5, // @test M7_2(0b001_11_00) == M7_2(0.5)
            0.625, // @test M7_2(0b001_11_01) == M7_2(0.625)
            0.75, // @test M7_2(0b001_11_10) == M7_2(0.75)
            0.875, // @test M7_2(0b001_11_11) == M7_2(0.875)
            1.0, // @test M7_2(0b010_00_00) == M7_2(1.0)
            1.25, // @test M7_2(0b010_00_01) == M7_2(1.25)
            1.5, // @test M7_2(0b010_00_10) == M7_2(1.5)
            1.75, // @test M7_2(0b010_00_11) == M7_2(1.75)
            2.0, // @test M7_2(0b010_01_00) == M7_2(2.0)
            2.5, // @test M7_2(0b010_01_01) == M7_2(2.5)
            3.0, // @test M7_2(0b010_01_10) == M7_2(3.0)
            3.5, // @test M7_2(0b010_01_11) == M7_2(3.5)
            4.0, // @test M7_2(0b010_10_00) == M7_2(4.0)
            5.0, // @test M7_2(0b010_10_01) == M7_2(5.0)
            6.0, // @test M7_2(0b010_10_10) == M7_2(6.0)
            7.0, // @test M7_2(0b010_10_11) == M7_2(7.0)
            8.0, // @test M7_2(0b010_11_00) == M7_2(8.0)
            10.0, // @test M7_2(0b010_11_01) == M7_2(10.0)
            12.0, // @test M7_2(0b010_11_10) == M7_2(12.0)
            14.0, // @test M7_2(0b010_11_11) == M7_2(14.0)
            16.0, // @test M7_2(0b0110_00_0) == M7_2(16.0)
            24.0, // @test M7_2(0b0110_00_1) == M7_2(24.0)
            32.0, // @test M7_2(0b0110_01_0) == M7_2(32.0)
            48.0, // @test M7_2(0b0110_01_1) == M7_2(48.0)
            64.0, // @test M7_2(0b0110_10_0) == M7_2(64.0)
            96.0, // @test M7_2(0b0110_10_1) == M7_2(96.0)
            128.0, // @test M7_2(0b0110_11_0) == M7_2(128.0)
            192.0, // @test M7_2(0b0110_11_1) == M7_2(192.0)
            256.0, // @test M7_2(0b01110_00) == M7_2(256.0)
            512.0, // @test M7_2(0b01110_01) == M7_2(512.0)
            1024.0, // @test M7_2(0b01110_10) == M7_2(1024.0)
            2048.0, // @test M7_2(0b01110_11) == M7_2(2048.0)
            4096.0, // @test M7_2(0b011110_0) == M7_2(4096.0)
            16384.0, // @test M7_2(0b011110_1) == M7_2(16384.0)
            65536.0, // @test M7_2(0b0111110) == M7_2(65536.0)
            1048576.0 // @test M7_2(0b0111111) == M7_2(1048576.0)
    };

    @Test
    public void sevenBitES2() {
        int oosCount = 0;
        for (int i = 0; i < EXPECTED_SEVENBIT_ES2.length; i++) {
            final String instance = String.format("%7s", Integer.toBinaryString(i)).replace(" ", "0");
            final Posit p = new PositStringImpl(instance, 2);
            final String outOfSpec = outOfSpec(p.doubleValue(), EXPECTED_SEVENBIT_ES2[i], COMPARE_PRECISION);
            if (outOfSpec.length() > 0) {
                oosCount++;
                System.out.println("sevenBitES2 i=" + i + ", posit=" + PositDomain.toDetailsString(instance, 1) +
                        ",exp=" + EXPECTED_SEVENBIT_ES2[i] + ",1.0/exp=" + 1.0 / EXPECTED_SEVENBIT_ES2[i] + outOfSpec);
            }
            // assertEquals(EXPECTED_SEVENBIT_ES2[i], p.doubleValue(), PositDomainTest.COMPARE_PRECISION);
        }
        System.out.println("sevenBitES2 out of spec count=" + oosCount + "/" + EXPECTED_SEVENBIT_ES2.length + ".");
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

    
    /**
     * A sixteenBit, es = 3 example.
     * <p>
     * Figure 5 example from "Beating Floating Point"
     */
    @Test
    public void sixteenBitES3PositPaperExample() {
        final int MAX_ES = 3;

        // sign + regime
        String instance = "0" + "0001" + "000" + "00000000";
        int i = Integer.parseInt(instance, 2); // 2048
        Posit p = new PositStringImpl(instance, MAX_ES);
        
        assertTrue(PositDomain.isPositive(instance));
        assertEquals("0001", PositDomain.getRegime(instance));
        assertEquals("000", PositDomain.getExponent(instance, MAX_ES));
        assertEquals("00000000", PositDomain.getFraction(instance, MAX_ES));
        System.out.println("i=" + i + ", p.double=" + p.doubleValue());
        System.out.println("i=" + i + ", posit=" + PositDomain.toDetailsString(instance, MAX_ES));
        // from PositDomain val=5.9604644775390625E-8
        // from Julia           5.960464477539063e-8, "0 0001 000 00000000"
        assertTrue(equals(p.doubleValue(), 0.000000059604644775390625, COMPARE_PRECISION));
        
        // sign + regime + exponent
        instance = "0" + "0001" + "101" + "00000000";
        i = Integer.parseInt(instance, 2); // 3328
        p = new PositStringImpl(instance, MAX_ES);
        
        assertTrue(PositDomain.isPositive(instance));
        assertEquals("0001", PositDomain.getRegime(instance));
        assertEquals("101", PositDomain.getExponent(instance, MAX_ES));
        assertEquals("00000000", PositDomain.getFraction(instance, MAX_ES));
        System.out.println("i=" + i + ", p.double=" + p.doubleValue());
        System.out.println("i=" + i + ", posit=" + PositDomain.toDetailsString(instance, MAX_ES));
        // from PositDomain val=1.9073486328125E-6
        // from Julia           1.9073486328125e-6, "0 0001 101 00000000"
        assertTrue(equals(p.doubleValue(), 0.0000019073486328125, COMPARE_PRECISION));

        // sign + regime + exponent + fraction
        instance  = "0" + "0001" + "101" + "11011101";
        i = Integer.parseInt(instance, 2); // 3549
        p = new PositStringImpl(instance, MAX_ES);

        assertTrue(PositDomain.isPositive(instance));
        assertEquals("0001", PositDomain.getRegime(instance));
        assertEquals("101", PositDomain.getExponent(instance, MAX_ES));
        assertEquals("11011101", PositDomain.getFraction(instance, MAX_ES));
        System.out.println("i=" + i + ", p.double=" + p.doubleValue());
        System.out.println("i=" + i + ", posit=" + PositDomain.toDetailsString(instance, MAX_ES));
        // from PositDomain val=3.355884879725086E-6
        // "0 0001 101 11011101" // k=-3, e=5, f=221/256, =256^-3 * 2^5 * 1.86
        // "0 000110111011101"
        // "0 111001000100010" // invert
        // "0 111001000100011" // twos
        // "0 1110 010 00100011" // k=2, e=2, f=35/256, =1.0 / 256^2 *2^2* * 1.13671875 
                
        // from paper           3.55393E−6
        // from Julia           3.553926944732666e-6, "0 0001 101 11011101"
        System.out.println(  "Delta of Julia/PositDomain=" + p.doubleValue()/0.000003553926944732666);
        assertTrue(equals(p.doubleValue(), 0.000003553926944732666, COMPARE_PRECISION));        
    }

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
