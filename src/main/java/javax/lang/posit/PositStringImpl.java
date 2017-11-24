package javax.lang.posit;

import java.math.BigInteger;

/**
 * Posit implementation based on String
 * <p>
 * String-based Posits are not compact. One character represents one binary
 * digit. However, String-based Posits can be arbitrary length and dynamic
 * range.
 * <p>
 * String may represent any Posit binary String, the bit size is determined by
 * the length of the String.
 *
 * @see Posit
 *
 * @author <a href="mailto://dan@danbecker.info>Dan Becker</a>
 */
public final class PositStringImpl extends Posit implements Comparable<Posit> {
	/** Serialization version */
	private static final long serialVersionUID = 1L;

	/** internal representation */
	private String internal;
	private byte maxExponentSize = 2;

	// Constructors
	/**
	 * @see Posit#()
	 */
	public PositStringImpl() {
		parse("");
	}

	/**
	 * @see Posit#(Object)
	 */
	public PositStringImpl(final String s) throws NumberFormatException {
		parse(s);
	}

	/**
	 * @see Posit#(Object,byte)
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
	 * "Suppose we view the bit string for a posit p as a signed integer, ranging
	 * from -2^(n-1) to 2^(n-1)-1. Let k be the integer represented by the regime
	 * bits, let e be the unsigned integer represented by the exponent bits, if any
	 * let f be the fraction bits, represented by 1.f1...fn, if any Then x=0, when
	 * p=0 x=±∞, when p=-2^(n-1) x=sign(p)*useed^k*2^e*f,all other p."
	 * <p>
	 * For example p="0 0001 101 11011101" with es=3<br/>
	 * x=1*256^(-3)*2^(5)*(1+221/256)=477/134217728 ~=3.55393*10^(-6)
	 * <p>
	 *
	 * @see Posit#doubleValue()
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
		final String spaced = PositDomain.toSpacedString(internal, maxExponentSize);
		final boolean positive = isPositive();
		double val = positive ? 1.0 : -1.0;
		final BigInteger useed = getUseed();
		final int k = getRegimeK();
		double useedK = 1.0;
		if (k >= 0) {
			useedK = useed.pow(k).doubleValue();
			val *= useedK; // sign*regime
		} else {
			useedK = useed.pow(Math.abs(k)).doubleValue();
			val /= useedK; // sign*regime
		}
		final String exponent = getExponent();
		if (null != exponent && exponent.length() > 0) {
			final double expVal = PositDomain.getExponentVal(exponent, positive);
			final double twoe = Math.pow(2.0, expVal);
			val *= twoe;// sign*regime*exp
		}
		final String fraction = getFraction();
		if (null != fraction && fraction.length() > 0) {
			double fracMultiplier = PositDomain.getFractionMultiplier(fraction);
			val *= fracMultiplier; // sign*regime*exp*frac
		}
		return val;
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
	 *            a string of the format ("0","1")*. If the string has whitespace,
	 *            it is trimmed. If the string starts with "0b" it is trimmed.
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
		final boolean positive = isPositive();
		final String regime = PositDomain.getRegime(internal);
		return PositDomain.getRegimeK(regime);
	}

	@Override
	/**
	 * @see Posit#getExponentFraction()
	 */
	public String getExponentFraction() {
		return PositDomain.getExponentFraction(internal);
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
		return PositDomain.getExponent(getExponentFraction(), getMaxExponentSize());
	}

	@Override
	/**
	 * @see Posit#getFraction()
	 */
	public String getFraction() {
		return PositDomain.getFraction(getExponentFraction(), getMaxExponentSize());
	}

    @Override
    /**
     * @see Posit#getFractionMultiplier()
     */
    public double getFractionMultiplier() {        
        int fs = getBitSize() - 3 - getMaxExponentSize();
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
		// System.out.println( "Posit \"" + internal + "\", exponent=\"" + getExponent()
		// + "\"");
		// final String exponent = getExponent();
		// if (null == exponent || exponent.length() < 1) {
		// return BIGINT_2;
		// }
		// if (isPositive()) {
		// return PositDomain.getUseed(Integer.parseInt(exponent, 2));
		// }
		// return PositDomain.getUseed(Integer.parseInt(Bit.twosComplement(exponent),
		// 2));
		return PositDomain.getUseed(getMaxExponentSize());
	}
}