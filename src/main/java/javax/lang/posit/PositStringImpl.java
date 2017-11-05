package javax.lang.posit;

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

	/**
	 * internal representation
	 */
	private String internal;

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
		// TODO Auto-generated method stub
		return 0.0;
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
		if ( null == s) {
			internal = "";
			return;
		}
		String local = s.trim();
		if ( local.startsWith("0b")) {
			local = local.substring(2);
		}
		for ( int i= 0; i < local.length(); i++ ) {
			if ( '0' != local.charAt(i) && '1' != local.charAt(i) ) {
				throw new NumberFormatException("illegal character in \"" + local + "\"" );				
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
		return isInfinite( internal );
	}

	/** Checks if a string of binary 0 and 1 characters represents infinity. */
	public static boolean isInfinite( String instance ) {
		// '1' followed by zero or more '0' ("1+0*") 
		if ( null == instance || instance.length() < 1) {
			return false;
		}
		if(  '1' != instance.charAt(0) ) {
			return false;
		}
		for ( int i= 1; i < instance.length(); i++ ) {
			if ( '0' != instance.charAt(i) ) {
				return false;
			}			
		}
		return true;		
	}
	
	@Override
	/**
	 * @see Posit#isZero()
	 */
	public boolean isZero() {
		return isZero( internal );
	}

	/** Checks if a string of binary 0 and 1 characters represents infinity. */
	public static boolean isZero( String instance ) {
		// One or more '0' ("0+") 
		if ( null == instance || instance.length() < 1) {
			return false;
		}
		for ( int i= 0; i < instance.length(); i++ ) {
			if ( '0' != instance.charAt(i) ) {
				return false;
			}			
		}
		return true;		
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
		if ( obj instanceof PositStringImpl) {
			PositStringImpl other = (PositStringImpl) obj;
			return internal.equals(other.internal);			
		}
		return false;
	}

	/**
	 * @see Posit#toString
	 */
	@Override
	public String toString() {
		return this.stringValue();
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
		return isPositive( internal );
	}

	/** Checks if a string of binary 0 and 1 characters is positive. */
	public static boolean isPositive( String instance ) {
		// One or more '0' ("0+") 
		if ( instance.length() < 1) {
			return false;
		}
		return '0' == instance.charAt(0);
		
	}
	
	@Override
	/**
	 * @see Posit#getRegime()
	 */
	public String getRegime() {
		return getRegime( internal );
	}
	
	/** Returns the regime portion of a string of binary 0 and 1 characters. */
	public static String getRegime( String instance ) {
		// If the bit size is less than 2, returns empty String.
		// First char of Posit is sign bit.
		// Regime is first char until terminated by end of string or until opposite char.
		// Examples "0", "1", "00", "01", "10", "11"
		if ( null == instance || instance.length() < 2) {
			return "";
		}
		char first = instance.charAt(1);
		final StringBuilder sb = new StringBuilder(first);
		for (int i = 1; i < instance.length(); i++) {
			final char current = instance.charAt(i);
			sb.append(current);
			if ( first != current) {
				break;
			}
		}
		return sb.toString();		
	}

	@Override
	/**
	 * @see Posit#getRegimeK()
	 */
	public int getRegimeK() {
		return getRegimeK( internal );
	}

	/** Returns the regime value of a string of binary 0 and 1 characters. */
	public static int getRegimeK( String regime ) {
		// Returns the regime value K.
		// Let m be the number of identical bits starting the regime;
		// if the bits are 0, then k = −m;
		// if the bits are 1, then k = m − 1.
		// Examples (regime = K):
		// 0000=-4, 0001=-3,001x=-2,01xx=-1,10xx=110x=1,1110=2,1111=3
		if ( null == regime || regime.length() < 1) {
			return 0;
		}
		char first = regime.charAt(0);
		int k = first == '0' ? -1 : 0;
		for( int i = 1; i < regime.length(); i++) {
			char current = regime.charAt(i);
			if ( current != first ) {
				break;
			}
			if ( '0' == first ) {
				k--;
			}	else if ( '1' == first ) {
				k++;
			}
		}
		return k;		
	}

	@Override
	/**
	 * @see Posit#getUseed()
	 */
	public long getUseed() {
		return getUseed( internal );
	}

	/** Get the useed of a regime (2^2^regimeLength). */
	public static long getUseed( String regime ) {
		if ( null != regime ) {
			final int regimeLength = regime.length();
			if ( 0 == regimeLength ) {
				return 2;
			}
			return (long) Math.pow(2, Math.pow(2, regimeLength));			
		}
		return 0;		
	}
	
	@Override
	/**
	 * @see Posit#getExponent()
	 */
	public String getExponent() {
		return getExponent( internal );
	}
	
	/** Return the exponent part of a given string of 0 and 1 characters. */
	public static String getExponent( String instance ) {
		// Returns the exponent bits of this Posit as a String of "0" and "1".
		// If the regime fills the bit size, the exponent may be empty string.
		if ( null == instance || instance.length() < 2) {
			return "";
		}
		String regime = getRegime( instance );
		return regime.substring(regime.length() + 1);
		
	}
}