package javax.lang.posit;

import java.math.BigInteger;


/**
 * Posit data and method
 * <p>
 * Encodes information from Posit papers. Items in this class should be usable by all Posit classes: Posit,
 * PositStringImpl, etc.
 * <p>
 * More information from John Gustafson: <a href="http://www.johngustafson.net/pdfs/BeatingFloatingPoint.pdf">Beating
 * Floating Point</a>
 * <p>
 * "Suppose we view the bit string for a posit p as a signed integer, ranging from -2^(n-1) to 2^(n-1)-1. Let k be the
 * integer represented by the regime bits, let e be the unsigned integer represented by the exponent bits, if any let f
 * be the fraction bits, represented by 1.f1...fn, if any Then x=0, when p=0 x=±∞, when p=-2^(n-1)
 * x=sign(p)*useed^k*2^e*f,all other p."
 * <p>
 * For example p="0 0001 101 11011101" with es=3<br/>
 * x=1*256^(-3)*2^(5)*(1+221/256)=477/134217728 ~=3.55393*10^(-6)
 *
 * @see Posit
 * @author <a href="mailto://dan@danbecker.info>Dan Becker</a>
 */
public final class PositDomain {
    /** Lots of exponents evaluate to this. */
    public static final BigInteger BIGINT_2 = new BigInteger("2");

    // Math interface
    /** Checks if a string of binary 0 and 1 characters represents infinity. */
    public static boolean isInfinite(String instance) {
        // '1' followed by zero or more '0' ("1+0*")
        if (null == instance || instance.length() < 1) {
            return false;
        }
        if ('1' != instance.charAt(0)) {
            return false;
        }
        for (int i = 1; i < instance.length(); i++) {
            if ('0' != instance.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    /** Checks if a string of binary 0 and 1 characters represents infinity. */
    public static boolean isZero(String instance) {
        // One or more '0' ("0+")
        if (null == instance || instance.length() < 1) {
            return false;
        }
        for (int i = 0; i < instance.length(); i++) {
            if ('0' != instance.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    /** Checks if a string of binary 0 and 1 characters is positive. */
    public static boolean isPositive(String instance) {
        // One or more '0' ("0+")
        if (null == instance || instance.length() < 1) {
            return false;
        }
        return '0' == instance.charAt(0);

    }

    /**
     * Checks if a string of binary 0 and 1 characters is exact.
     * <p>
     * A posit will be exact if the fraction is 0.
     * <p>
     * Posits of length 0, 1, 2, (0,1,∞,-1) are all exact.
     */
    public static boolean isExact(String instance) {
        if (null == instance || instance.length() < 3) {
            return true;
        }
        return '0' == instance.charAt(instance.length() - 1);

    }

    /**
     * Returns an array consisting of sign, regime, exponent, and fraction. Components are not interpreted, simply
     * grouped. You can use the PositEnum values to get the fields in this tuple.
     *
     * @param instance
     * @param maxExponent
     * @param flipNegative
     *            performs twos complement if negative
     * @return
     */
    public static String[] getComponents(String instance, int maxExponent, boolean flipNegative) {
        if (null == instance || instance.length() < 1) {
            return new String[]{"","","",""};
        }
        if (instance.length() == 1) {
            return new String[]{instance,"","",""};
        }
        final String[] components = new String[]{instance.substring(0, 1),"","",""};

        String remaining = instance.substring(1);
        if (flipNegative && !isPositive(instance)) {
            remaining = Bit.twosComplement(remaining);
        }

        // Regime is second char until terminated by end of string or opposite char.
        final char first = remaining.charAt(0);
        int rs = 0;
        for (int i = 0; i < remaining.length(); i++) {
            final char current = remaining.charAt(i);
            rs++;
            if (first != current) {
                break;
            }
        }
        components[PositEnum.REGIME.v()] = remaining.substring(0, rs);

        // 0123456789
        // 1001eeeeff
        final int esMax = remaining.length() - rs;
        final int es = Math.min(maxExponent, esMax);
        components[PositEnum.EXPONENT.v()] = remaining.substring(rs, rs + es);

        final int fs = remaining.length() - es - rs;
        if (fs > 0) {
            components[PositEnum.FRACTION.v()] = remaining.substring(rs + es);
        }
        return components;
    }

    /**
     * Returns the regime portion of a string of binary 0 and 1 characters. The String will be twos complemented for
     * negative instances.
     */
    public static String getRegime(String instance) {
        // If the bit size is less than 2, returns empty String.
        // First char of Posit is sign bit.
        // Regime is second char until terminated by end of string or opposite char.
        if (null == instance || instance.length() < 2) {
            return "";
        }
        final boolean positive = isPositive(instance);
        if (!positive) {
            instance = Bit.twosComplement(instance);
        }
        final char first = instance.charAt(1);
        final StringBuilder sb = new StringBuilder(first);
        for (int i = 1; i < instance.length(); i++) {
            final char current = instance.charAt(i);
            sb.append(current);
            if (first != current) {
                break;
            }
        }
        return sb.toString();
    }

    /** Returns the regime value of a string of binary 0 and 1 characters. */
    public static int getRegimeK(String regime) {
        // Returns the regime value K.
        // Let m be the number of identical bits starting the regime;
        // if the bits are 0, then k = −m;
        // if the bits are 1, then k = m − 1.
        // Examples (regime = K):
        // 0000=-4, 0001=-3,001x=-2,01xx=-1,10xx=0,110x=1,1110=2,1111=3
        if (null == regime || regime.length() < 1) {
            return 0;
        }
        final char first = regime.charAt(0);
        int k = first == '0' ? -1 : 0;
        for (int i = 1; i < regime.length(); i++) {
            final char current = regime.charAt(i);
            if (current != first) {
                break;
            }
            if ('0' == first) {
                k--;
            } else if ('1' == first) {
                k++;
            }
        }
        return k;
    }

    /**
     * Return the exponent part of a given string of 0 and 1 characters. The String will be twos complemented for
     * negative instances.
     */
    public static String getExponent(String instance, int maxExponent) {
        // Returns the exponent bits of this Posit as a String of "0" and "1".
        // If the regime fills the bit size, the exponent may be empty string.
        if (null == instance || instance.length() < 2) {
            return "";
        }
        final boolean positive = isPositive(instance);
        String remaining = instance.substring(1);
        if (!positive) {
            remaining = Bit.twosComplement(remaining);
        }

        // Regime is second char until terminated by end of string or opposite char.
        final char first = remaining.charAt(0);
        int rs = 0;
        for (int i = 0; i < remaining.length(); i++) {
            final char current = remaining.charAt(i);
            rs++;
            if (first != current) {
                break;
            }
        }

        // 0123456789
        // 1001eeeeff
        final int esMax = remaining.length() - rs;
        final int es = Math.min(maxExponent, esMax);
        return remaining.substring(rs, rs + es);
    }

    /** Return the value of an exponent string of 0 and 1 characters. */
    public static double getExponentVal(String exponent) {
        // Returns the exponent bits of this Posit as a String of "0" and "1".
        // If the regime fills the bit size, the exponent may be empty string.
        if (null == exponent || exponent.length() < 1) {
            return 0.0;
        }
        return Integer.parseUnsignedInt(exponent, 2);
    }

    /**
     * Return the fraction part of a given string of 0 and 1 characters. The String will be twos complemented for
     * negative instances.
     */
    public static String getFraction(String instance, int maxExponent) {
        // Returns the exponent bits of this Posit as a String of "0" and "1".
        // If the regime fills the bit size, the exponent may be empty string.
        if (null == instance || instance.length() < 2) {
            return "";
        }

        final boolean positive = isPositive(instance);
        String remaining = instance.substring(1);
        if (!positive) {
            remaining = Bit.twosComplement(remaining);
        }

        // Regime is second char until terminated by end of string or opposite char.
        final char first = remaining.charAt(0);
        int rs = 0;
        for (int i = 0; i < remaining.length(); i++) {
            final char current = remaining.charAt(i);
            rs++;
            if (first != current) {
                break;
            }
        }

        // 0123456789
        // 1001eeeeff
        final int esMax = remaining.length() - rs;
        final int es = Math.min(maxExponent, esMax);

        final int fs = remaining.length() - es - rs;
        if (fs > 0) {
            return remaining.substring(rs + es);
        }
        return "";
    }

    /** Return the value of a given fraction of 0 and 1 characters. */
    public static long getFractionVal(String fraction, boolean positive) {
        // Returns the exponent bits of this Posit as a String of "0" and "1".
        // If the regime fills the bit size, the exponent may be empty string.
        if (null == fraction || fraction.length() < 1) {
            return 0;
        }
        if (positive) {
            return Long.parseUnsignedLong(fraction, 2);
        } else {
            return Long.parseUnsignedLong(Bit.twosComplement(fraction), 2);
        }
    }

    /** Return the fraction multiplier of 0 and 1 characters. */
    public static double getFractionMultiplier(String fraction) {
        if (null == fraction || fraction.length() < 1) {
            return 1.0;
        }
        final double fnumerator = Long.parseUnsignedLong(fraction, 2);
        final double fdenominator = powerN(2, fraction.length());
        final double fmultiplier = 1.0 + fnumerator / fdenominator;
        return fmultiplier;
    }

    /** Return base^exp. Only 0..MAX_LONG exps are supported. */
    public static long powerN(long base, long exp) {
        if (exp < 1) {
            return 1; // no fractions here
        }
        long result = base;

        while (exp > 1) {
            result *= base;
            exp--;
        }
        return result;
    }

    // Puny long runs out at es=6, puny double rounds at es=6.
    // public static final BigInteger [] LOOKUP_2_2_N = new BigInteger [] { };
    public static final BigInteger[] LOOKUP_2_2_N = new BigInteger[]{BIGINT_2,new BigInteger("4"),
            new BigInteger("16"),new BigInteger("256"),new BigInteger("65536"),new BigInteger("4294967296"),
            new BigInteger("18446744073709551616"),new BigInteger("340282366920938463463374607431768211456"),
            new BigInteger("115792089237316195423570985008687907853269984665640564039457584007913129639936")};

    /** Get the useed of a given exponent size (2^2^exponentSize). */
    public static BigInteger getUseed(int es) {
        if (es < 0) {
            return BigInteger.ZERO;
        }
        if (es < LOOKUP_2_2_N.length) {
            return LOOKUP_2_2_N[es];
        }
        BigInteger previous = BigInteger.valueOf(2);
        for (int i = 0; i < es; i++) {
            previous = previous.pow(2);
        }
        // System.out.println( "Es is " + es + ", returning " + previous);
        return previous;
    }

    /** Return string with spaces between the sign,regime,exponent, and fraction. */
    public static String toSpacedString(String instance, int maxExponent, boolean flipNegative) {
        if (null == instance || instance.length() < 1) {
            return "";
        }
        if (instance.length() == 1) {
            return instance;
        }
        final String[] spacers = {""," "," e"," f"};
        final String[] components = getComponents(instance, maxExponent, flipNegative);
        final StringBuilder sb = new StringBuilder();
        // for SIGN, REGIME, EXPONENT, FRACTION
        for (final PositEnum component : PositEnum.values()) {
            final int position = component.ordinal();
            if (null == components[position] || components[position].length() < 1) {
                return sb.toString();
            }
            sb.append(spacers[position]);
            sb.append(components[position]);
        }
        return sb.toString();
    }

    /** Returns a very detailed view of the number. Exercises most APIs. */
    public static String toDetailsString(String instance, int maxExponent) {
        final BigInteger useed = getUseed(maxExponent);
        if (null == instance) {
            return "null es" + maxExponent + "useed=" + useed.toString();
        }
        if (instance.length() < 1) {
            return "\"\" es" + maxExponent + "useed=" + useed.toString();
        }
        if (instance.length() == 1) {
            return "\"" + instance + "\" es" + maxExponent + "useed=" + useed.toString();
        }
        final String spacedString = toSpacedString(instance, maxExponent, true);
        final StringBuilder sb = new StringBuilder();
        sb.append("\"" + spacedString + "\" es" + maxExponent + " useed" + useed.toString());
        if (isZero(instance)) {
            sb.append(", val=0.0");
            return sb.toString();
        }
        if (isInfinite(instance)) {
            sb.append(", val=" + Double.POSITIVE_INFINITY);
            return sb.toString();
        }

        final boolean positive = isPositive(instance);
        final String[] components = getComponents(instance, maxExponent, true);
        double val = positive ? 1.0 : -1.0;
        final String regime = components[PositEnum.REGIME.v()];
        if (null != regime && regime.length() > 0) {
            final int k = getRegimeK(regime);
            double useedK = 1.0;
            if (k >= 0) {
                useedK = useed.pow(k).doubleValue();
                val *= useedK;
            } else {
                useedK = useed.pow(Math.abs(k)).doubleValue();
                val /= useedK;
                useedK = 1.0 / useedK;
            }
            sb.append(", r=\"" + regime + "\" k=" + k + " useed^k=" + useedK + " val=" + val);
        } else {
            sb.append(", r=\"\"");
        }
        final String exponent = components[PositEnum.EXPONENT.v()];
        if (null != exponent && exponent.length() > 0) {
            final double expVal = PositDomain.getExponentVal(exponent);
            final double twoe = Math.pow(2.0, expVal);
            val *= twoe;// sign*regime*exp
            sb.append(", e=\"" + exponent + "\" e=" + expVal + " 2^e=" + twoe + " val=" + val);
        } else {
            sb.append(", e=\"\"");
        }
        final String fraction = components[PositEnum.FRACTION.v()];
        if (null != fraction && fraction.length() > 0) {
            final double fracMultiplier = PositDomain.getFractionMultiplier(fraction);
            val *= fracMultiplier; // sign*regime*exp*frac
            sb.append(", f=\"" + fraction + "\" fm " + fracMultiplier + " val=" + val);
        } else {
            sb.append(", f=\"\"");
        }
        if (0.0 != val) {
            sb.append(",1/val=" + 1.0 / val);
        }
        return sb.toString();
    }
}
