package javax.lang.posit;

import java.math.BigInteger;

/**
 * Posit implementation based on String
 * <p>
 * String-based Posits are not compact. One character represents one binary
 * digit. However, String-based Posits can be arbitrary length and dynamic
 * range.
 * <p>
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
	 * @see Posit#(String)
	 */
	public PositStringImpl(final String s) throws NumberFormatException {
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
	 * @see Posit#doubleValue()
	 */
	public double doubleValue() {
		// Temp implementation
		if ( isZero() ) {
			return 0.0;
		}
		if ( isInfinite() ) {
			return Double.POSITIVE_INFINITY;
		}
		BigInteger useed = getUseed();
		// System.out.println( "Posit \"" + this + "\", useed=" + useed);
		int k = getRegimeK();
	    // System.out.println( "Posit \"" + this + "\", regime K=" + k);
		String exponent = getExponent();
		int expVal = Integer.parseUnsignedInt(exponent,2);
		// System.out.println( "Posit \"" + this + "\", expVal=" + expVal);
		String fraction = getFraction();
		long fracVal = Long.parseUnsignedLong(fraction,2);
		// System.out.println( "Posit \"" + this + "\", fracVal=" + expVal);
		double val = -1.0;
		if ( isPositive() ) { 
			val = 1.0;
		}
		if (k > 0) 
			val *= useed.pow(k).doubleValue(); // sign*regime
		else
			val /= useed.pow(Math.abs(k)).doubleValue(); // sign*regime
		val *= BigInteger.valueOf(2).pow(expVal).doubleValue(); // sign*regime*exp
		double fracDouble = 1.0 + (fracVal/useed.doubleValue()); // sign*regime*exp*fraction
		val *= fracDouble;
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
		return PositDomain.getRegimeK(PositDomain.getRegime(internal));
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
		return this.maxExponentSize;
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
		return PositDomain.getExponent(getExponentFraction(),getMaxExponentSize());
	}


	@Override
	/**
	 * @see Posit#getFraction()
	 */
	public String getFraction() {
		return PositDomain.getFraction(getExponentFraction(),getMaxExponentSize());
	}		

	@Override
	/**
	 * @see Posit#getUseed()
	 */
	public BigInteger getUseed() {
		// System.out.println( "Posit \"" + internal + "\", exponent=\"" + getExponent() + "\"");
		return PositDomain.getUseed(getExponent().length());
	}
}