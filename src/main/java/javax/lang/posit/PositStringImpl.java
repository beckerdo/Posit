package javax.lang.posit;

import java.math.BigInteger;

/**
 * Posit implementation based on String
 * <p>
 * String-based Posits are not compact. One character represents one binary digit. However, String-based Posits can be
 * arbitrary length and dynamic range.
 * <p>
 * String may represent any Posit binary String, the bit size is determined by the length of the String.
 *
 * @see Posit
 * @author <a href="mailto://dan@danbecker.info">Dan Becker</a>
 */
public final class PositStringImpl extends Posit implements Comparable<Posit> {
    /** Serialization version */
    private static final long serialVersionUID = 1L;

    /** internal representation */
    private String internal;
    /** consider making a class/static or factory method. */
    private byte maxExponentSize = 2;

    // Constructors
    /**
     * @see Posit#Posit()
     */
    public PositStringImpl() {
        parse("");
    }

    /**
     * @see Posit#Posit(Object)
     */
    public PositStringImpl(final String s) throws NumberFormatException {
        parse(s);
    }

    /**
     * @see Posit#Posit(Object,int)
     */
    public PositStringImpl(final String s, int es) throws NumberFormatException {
        setMaxExponentSize((byte) es);
        parse(s);
    }

    // Number interface
    @Override
    /**
     * @see Posit#byteValue()
     */
    public byte byteValue() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    /**
     * @see Posit#shortValue()
     */
    public short shortValue() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    /**
     * @see Posit#intValue()
     */
    public int intValue() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    /**
     * @see Posit#longValue()
     */
    public long longValue() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    /**
     * @see Posit#floatValue()
     */
    public float floatValue() {
        // TODO Auto-generated method stub
        return 0.0f;
    }

    @Override
    /**
     * "Suppose we view the bit string for a posit p as a signed integer, ranging from -2^(n-1) to 2^(n-1)-1. Let k be
     * the integer represented by the regime bits, let e be the unsigned integer represented by the exponent bits, if
     * any let f be the fraction bits, represented by 1.f1...fn, if any Then x=0, when p=0 x=±∞, when p=-2^(n-1)
     * x=sign(p)*useed^k*2^e*f,all other p."
     * <p>
     * This is the Becker method of calculating fractions, which is the same as Gustafson from 1 to infinity (∞) to -1.
     * The method differs from Gustafson in that the Becker method reflects points on the top half of the
     * number circle to the bottom half. Thus, for 5 bit, es1, we see the following fractions around 1.0:
     * <table>
     * <thead><th>4</th><th>6</th><th>6</th><th>7</th><th>8</th><th>9</th><th>10</th><th>11</th><th>12</th></thead>
     * <tr><td>binary</td><td>0 01 0 0</td><td>0 01 0 1</td><td>0 01 1 0</td><td>0 01 1 1</td>
     * <td>0 10 0 0</td><td>0 10 0 1</td><td>0 10 1 0</td><td>0 10 1 1</td></tr>
     * <tr><td>Gustafson</td></tr><td>1.0/4.0</td><td>3.0/8.0</td><td>1.0/2.0</td><td>3.0/4.0</td>
     * <td>1.0</td><td>3.0/2.0<td><td>2.0</td><td>3.0</td><td>4.0</td></tr>
     * <tr><td>Becker</td><td>1.0/4.0</td><td>1.0/3.0</td><td>1.0/2.0</td><td>2.0/3.0</td>
     * <td>1.0</td><td>3.0/2.0<td><td>2.0</td><td>3.0</td><td>4.0</td></tr>
     * </table>
     * <p>
     * For example, Gustafson caclulates p="0 0001 101 11011101" with es=3<br/>
     * x=1*256^(-3)*2^(5)*(1+221/256)=477/134217728 ~=3.55393*10^(-6)
     * <p>
     * The calculation relies on certain reflections:
     * pos,<1,  unflipped regime begins with 0, result = 1.0 / calculation( twosComp( regime, exponent, fraction ) )
     * pos,>=1, unflipped regime begins with 1, result = 1.0 * calculation( regime, exponent, fraction )
     * neg,>=1, unflipped regime begins with 0, result = -1.0 * calculation( twosComp( regime, exponent, fraction ) 
     * neg,<1,  unflipped regime begins with 1, result = -1.0 / calculation( regime, exponent, fraction ) 
     *
     * @see Posit#doubleValueGustafson()
     * @return nearest double value (using reflection calculated fractions)
     */
    public double doubleValue() {
        // Temp implementation Should be compacted, more native.
        if (null == internal || internal.length() == 0) {
            return 0.0;
        }
        if (isZero()) {
            return 0.0;
        }
        if (isInfinite()) {
            return Double.POSITIVE_INFINITY;
        }
        final boolean positive = isPositive();
        double sign = positive ? 1.0 : -1.0;
        final BigInteger useed = getUseed(); // 2^2^maxEs
        // Use symmetry to adjust regime, exponent, fraction
        boolean twos = null != internal && internal.length() > 1 && '0' == internal.charAt(1);
        final String [] components = PositDomain.getComponentsFlipReflection(internal, getMaxExponentSize());
        String regime = components[PositEnum.REGIME.v()];
        String exponent = components[PositEnum.EXPONENT.v()];
        String fraction = components[PositEnum.FRACTION.v()];
        final int k = PositDomain.getRegimeK(regime); // run length exponent
        double useedK = useed.pow(Math.abs(k)).doubleValue(); // useed^k
        double twoe = 1.0;
        if (null != exponent && exponent.length() > 0) {
            final double expVal = PositDomain.getExponentVal(exponent, getMaxExponentSize());
            twoe = Math.pow(2.0, expVal);
        }
        double fracMultiplier = 1.0;
        if (null != fraction && fraction.length() > 0) {
            fracMultiplier = PositDomain.getFractionMultiplier(fraction);
        }
        // "calculation()" mentioned in comment
        double calculation = useedK * twoe * fracMultiplier;
        if ( positive ) {
            if ( !twos ) {
                return calculation;
            } else {
                return sign / calculation;
            }
        } else {
            if ( twos ) {
                return sign * calculation;
            } else {
                return sign / calculation;
            }            
        }
    }

    /**
     * 
     * @see Posit#doubleValueGustafson()
     * @return nearest double value (using Gustafson calculated fractions)
     */
    public double doubleValueGustafson() {
        // Temp implementation Should be compacted, more native.
        if (null == internal || internal.length() == 0) {
            return 0.0;
        }
        if (isZero()) {
            return 0.0;
        }
        if (isInfinite()) {
            return Double.POSITIVE_INFINITY;
        }
        final boolean positive = isPositive();
        double sign = positive ? 1.0 : -1.0;
        final BigInteger useed = getUseed(); // 2^2^maxEs
        final String [] components = PositDomain.getComponentsFlipNegative(internal, getMaxExponentSize());
        String regime = components[PositEnum.REGIME.v()];
        final int k = PositDomain.getRegimeK(regime); // run length exponent
        double useedK = 1.0;
        if (k >= 0) {
            useedK = useed.pow(k).doubleValue();
        } else {
            useedK = 1.0 / useed.pow(Math.abs(k)).doubleValue();
        }
        double twoe = 1.0;
        String exponent = components[PositEnum.EXPONENT.v()];
        if (null != exponent && exponent.length() > 0) {
            final double expVal = PositDomain.getExponentVal(exponent, getMaxExponentSize());
            twoe = Math.pow(2.0, expVal);
        }
        double fracMultiplier = 1.0;
        String fraction = components[PositEnum.FRACTION.v()];
        if (null != fraction && fraction.length() > 0) {
            fracMultiplier = PositDomain.getFractionMultiplier(fraction);
        }
        return sign * useedK * twoe * fracMultiplier;
    }

    @Override
    /**
     * @see Posit#stringValue()
     */
    public String stringValue() {
        return internal;
    }

    // Conversion
    /**
     * Sets internal representation to the given String
     *
     * @param s
     *            a string of the format ("0","1")*. If the string has whitespace, it is trimmed. If the string starts
     *            with "0b" it is trimmed.
     * @throws NumberFormatException
     *             if the string does not contain a parsable binary number.
     */
    @Override
    public void parse(final String s) throws NumberFormatException {
        if (null == s) {
            internal = "";
            return;
        }
        String local = s.trim();
        if (local.startsWith("0b")) {
            local = local.substring(2);
        }
        for (int i = 0; i < local.length(); i++) {
            if ('0' != local.charAt(i) && '1' != local.charAt(i)) {
                throw new NumberFormatException("illegal character in \"" + local + "\"");
            }
        }
        internal = local;
    }

    // Math interface
    @Override
    /**
     * @see Posit#isInfinite()
     */
    public boolean isInfinite() {
        return PositDomain.isInfinite(internal);
    }

    @Override
    /**
     * @see Posit#isZero()
     */
    public boolean isZero() {
        return PositDomain.isZero(internal);
    }

    // Comparable interface
    /**
     * @see Posit#compareTo
     */
    public int compareTo(final PositStringImpl anotherPosit) {
        return PositStringImpl.compare(this, anotherPosit);
    }

    // Object methods
    /**
     * @see Posit#compare
     */
    public static int compare(final PositStringImpl p1, final PositStringImpl p2) {
        return p1.compareTo(p2);
    }

    /**
     * @see Posit#hashCode
     */
    @Override
    public int hashCode() {
        return internal.hashCode();
    }

    /**
     * @see Posit#equals
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof PositStringImpl) {
            final PositStringImpl other = (PositStringImpl) obj;
            return internal.equals(other.internal);
        }
        return false;
    }

    /**
     * @see Posit#toString
     */
    @Override
    public String toString() {
        return stringValue();
    }

    // Posit domain interface
    @Override
    /**
     * @see Posit#getImplementation
     */
    public Class<?> getImplementation() {
        return String.class;
    }

    @Override
    /**
     * @see Posit#getBitSize()
     */
    public int getBitSize() {
        return internal.length();
    }

    @Override
    /**
     * @see Posit#isPositive()
     */
    public boolean isPositive() {
        return isPositive(internal);
    }

    @Override
    /**
     * @see Posit#isExact()
     */
    public boolean isExact() {
        return PositDomain.isExact(internal);
    }

    /** Checks if a string of binary 0 and 1 characters is positive. */
    public static boolean isPositive(String instance) {
        // One or more '0' ("0+")
        if (instance.length() < 1) {
            return false;
        }
        return '0' == instance.charAt(0);

    }

    @Override
    /**
     * @see Posit#getRegime()
     */
    public String getRegime() {
        return PositDomain.getRegime(internal);
    }

    @Override
    /**
     * @see Posit#getRegimeK()
     */
    public int getRegimeK() {
        final String regime = PositDomain.getRegime(internal);
        return PositDomain.getRegimeK(regime);
    }

    @Override
    /**
     * @see Posit#getMaxExponentSize()
     */
    public byte getMaxExponentSize() {
        return maxExponentSize;
    }

    @Override
    /**
     * @see Posit#setMaxExponentSize()
     */
    public void setMaxExponentSize(byte maxExponentSize) {
        this.maxExponentSize = maxExponentSize;
    }

    @Override
    /**
     * @see Posit#getExponent()
     */
    public String getExponent() {
        return PositDomain.getExponent(internal, getMaxExponentSize());
    }

    @Override
    /**
     * @see Posit#getFraction()
     */
    public String getFraction() {
        return PositDomain.getFraction(internal, getMaxExponentSize());
    }

    @Override
    /**
     * @see Posit#getFractionMultiplier()
     */
    public double getFractionMultiplier() {
        final int fs = getBitSize() - 3 - getMaxExponentSize();
        if (fs < 1) {
            return 0;
        }
        return fs;
    }

    @Override
    /**
     * @see Posit#getUseed()
     */
    public BigInteger getUseed() {
        return PositEnv.getUseed(getMaxExponentSize());
    }
}
