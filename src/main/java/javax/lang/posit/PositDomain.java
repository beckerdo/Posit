package javax.lang.posit;

import java.math.BigInteger;


/**
 * Encodes forumulas and information from Posit papers. Items in this class should be usable by all Posit implementation classes: Posit,
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
 * For example p="0 0001 101 11011101" with es=3<p>
 * x=1*256^(-3)*2^(5)*(1+221/256)=477/134217728 ~=3.55393*10^(-6)
 *
 * @see Posit
 * @author <a href="mailto://dan@danbecker.info">Dan Becker</a>
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
     * Returns an array consisting of sign, regime, exponent, and fraction components.
     * <p>
     * If the sign is negative, regime/exponent/fraction is twos complemented.
     * <p>
     * Components are not interpreted, simply grouped.
     * You can use the PositEnum values to get the fields in this tuple.
     *
     * @param instance
     * @param maxExponent
     * @return an array of sign, regime, exponent, and fraction components.
     */
    public static String[] getComponentsFlipNegative(String instance, int maxExponent) {
        if (null == instance || instance.length() < 1) {
            return new String[]{"","","",""};
        }
        if (instance.length() == 1) {
            return new String[]{instance,"","",""};
        }
        final String[] components = new String[]{instance.substring(0, 1),"","",""};

        String remaining = instance.substring(1);
        if (!isPositive(instance)) {
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
     * Returns an array consisting of sign, regime, exponent, and fraction components.
     * <p>
     * Will twos complement the regime/exponent/fraction based on the quadrant:
     * <ul>
     * <li>pos,&lt;1,  unflipped regime begins with 0, result = 1.0 / calculation( twosComp( regime, exponent, fraction ) )
     * <li>pos,&gt;=1, unflipped regime begins with 1, result = 1.0 * calculation( regime, exponent, fraction )
     * <li>neg,&gt;=1, unflipped regime begins with 0, result = -1.0 * calculation( twosComp( regime, exponent, fraction ) 
     * <li>neg,&lt;1,  unflipped regime begins with 1, result = -1.0 / calculation( regime, exponent, fraction )
     * </ul> 
     * <p>
     * Components are not interpreted, simply grouped.
     * You can use the PositEnum values to get the fields in this tuple.
     *
     * @param instance
     * @param maxExponent
     * @return an array of sign, regime, exponent, and fraction components.
     */
    // Like getComponents, but used for reflected regime, exponent, fraction.
    // For example 5 bit, es0, "0_10_01"=1.25, "0_01_11" == "0" + twosComp( "01_11" ) = 1/1.25;
    // Please twosComp/reflect remaining before sending.
    public static String[] getComponentsFlipReflection(String instance, int maxExponent) {
        if (null == instance || instance.length() < 1) {
            return new String[]{"","","",""};
        }
        if (instance.length() == 1) {
            return new String[]{instance,"","",""};
        }
        final String[] components = new String[]{instance.substring(0, 1),"","",""};

        String remaining = instance.substring(1);
        if (null == remaining || remaining.length() < 2) {
            return components;
        }
        if ( '0' == remaining.charAt(0) ) {
            remaining = Bit.twosComplement( remaining );
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
     * Returns the regime component of a string of binary 0 and 1 characters.
     * <p>
     * The String will be twos complemented for negative instances.
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

    /** Returns the regime value K of a string of binary 0 and 1 characters. 
     *  <p>
     *  Let m be the number (run length) of identical bits starting the regime:
     *  <ul>
     *  <li>if the bits are 0s, then k = −m;
     *  <li>if the bits are 1s, then k = m − 1.
     *  </ul>
     *  <p>
     *  Examples (regime=K):
     *  <code>
     *  0000=-4, 0001=-3,001x=-2,01xx=-1,10xx=0,110x=1,1110=2,1111=3
     *  </code>
    */
    public static int getRegimeK(String regime) {
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
     * Return the exponent component of a given string of 0 and 1 characters.
     * <p>
     * The String will be twos complemented for negative instances.
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
    public static double getExponentVal(String exponent, int maxExponent) {
        // If the regime fills the bit size, the exponent may be empty string.
        if (null == exponent || exponent.length() < 1) {
            return 0.0;
        }
        int unsignedVal = Integer.parseUnsignedInt(exponent, 2);
        if ( exponent.length() < maxExponent ) {
            // rotate left to make bigger, left adjust a truncated exponent.
            // System.out.println( "Exponent original val=" + unsignedVal + ", adjusted val=" + (unsignedVal << maxExponent - exponent.length()));
            unsignedVal  = unsignedVal << maxExponent - exponent.length();
        }
        return unsignedVal;
    }

    /**
     * Return the fraction component of a given string of 0 and 1 characters.
     * <p>
     * The String will be twos complemented for negative instances.
     */
    public static String getFraction(String instance, int maxExponent) {
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
        final double fdenominator = Bit.pow(2, fraction.length());
        final double fmultiplier = 1.0 + fnumerator / fdenominator;
        // final double fmultiplier = fnumerator / fdenominator;
        return fmultiplier;
    }

    // 
    /**
     * Return string with spaces between the sign,regime,exponent, and fraction.
     * @param instance is the String to render
     * @param maxExponent the maximum exponent
     * @param markers will place names before components
     * @param placeHolders will place placeholders in empty components
     * @return
     */
    public static String toSpacedString(String instance, int maxExponent, 
            boolean markers, boolean placeHolders) {
        if (null == instance || instance.length() < 1) {
            return "";
        }
        if (instance.length() == 1) {
            return instance;
        }
        final String[] spacers = {""," "," "," "};
        final String[] MARKERS = {"","","e","f"};
        final String PLACEHOLDER = "_";
        final String[] components = getComponentsFlipNegative(instance, maxExponent);
        final StringBuilder sb = new StringBuilder();
        // for SIGN, REGIME, EXPONENT, FRACTION
        for (final PositEnum component : PositEnum.values()) {
            final int position = component.ordinal();
            boolean empty = (null == components[position] || components[position].length() < 1);
            
            if ( empty ) {
                if ( placeHolders) {
                    sb.append(spacers[position]);
                    if ( markers ) {
                        sb.append(MARKERS[position]);                
                    }                
                    sb.append(PLACEHOLDER);
                }                
            } else {
                sb.append(spacers[position]);
                if ( markers ) {
                    sb.append(MARKERS[position]);                
                }                
                sb.append(components[position]);                
            }
        }
        return sb.toString();
    }

    /**
     * Simple version of toSpaceString with most common output.
     * @param instance
     * @param maxExponent
     * @return
     */
    public static String toSpacedString(String instance, int maxExponent ) {
        return toSpacedString(instance, maxExponent, 
                false, false); // !twos, markers, placeHolders
    }

    /** Returns a very detailed view of the number. Exercises most APIs. */
    public static String toDetailsString(String instance, int maxExponent) {
        final BigInteger useed = PositEnv.getUseed(maxExponent);
        if (null == instance) {
            return "null es" + maxExponent + "us" + useed.toString();
        }
        if (instance.length() < 1) {
            return "\"\" es" + maxExponent + "us" + useed.toString();
        }
        if (instance.length() == 1) {
            return "\"" + instance + "\" es" + maxExponent + "us" + useed.toString();
        }
        final String spacedString = toSpacedString(instance, maxExponent);
        final StringBuilder sb = new StringBuilder();
        sb.append("\"" + spacedString + "\" es" + maxExponent + " us" + useed.toString());
        if (isZero(instance)) {
            sb.append(", val=0.0");
            return sb.toString();
        }
        if (isInfinite(instance)) {
            sb.append(", val=" + Double.POSITIVE_INFINITY);
            return sb.toString();
        }

        final String[] components = getComponentsFlipNegative(instance, maxExponent);
        final String regime = components[PositEnum.REGIME.v()];
        if (null != regime && regime.length() > 0) {
            final int k = getRegimeK(regime);
            double useedK = 1.0;
            if (k >= 0) {
                useedK = useed.pow(k).doubleValue();
            } else {
                useedK = useed.pow(Math.abs(k)).doubleValue();
                useedK = 1.0 / useedK;
            }
            sb.append(", r=\"" + regime + "\" k=" + k + " us^k=" + useedK );
        } else {
            sb.append(", r=\"\"");
        }
        final String exponent = components[PositEnum.EXPONENT.v()];
        if (null != exponent && exponent.length() > 0) {
            final double expVal = PositDomain.getExponentVal(exponent, maxExponent);
            final double twoe = Math.pow(2.0, expVal);
            sb.append(", e=\"" + exponent + "\" 2^e=" + twoe);
        } else {
            sb.append(", e=\"\"");
        }
        final String fraction = components[PositEnum.FRACTION.v()];
        if (null != fraction && fraction.length() > 0) {
            final double fracMultiplier = PositDomain.getFractionMultiplier(fraction);
            sb.append(", f=\"" + fraction + "\" fm " + fracMultiplier );
        } else {
            sb.append(", f=\"\"");
        }
        Posit p = new PositStringImpl( instance, maxExponent);
        sb.append(", val="+ p.doubleValue() + ",1/val=" + 1.0 / p.doubleValue());
        return sb.toString();
    }
}
