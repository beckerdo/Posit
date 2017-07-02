package javax.lang.posit;

import java.io.IOException;
import java.util.BitSet;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Posit
 * <p>
 * An implementation of Posit numbers for Java
 * <p>In addition, this class provides several methods for converting a
 * {@code Posit} to a {@code String} and a
 * {@code String} to a {@code Posit}, as well as other
 * constants and methods useful when dealing with a
 * {@code Posit}s.
 * <p>
 * More information from John Gustafson: 
 * <a href="http://www.johngustafson.net/pdfs/BeatingFloatingPoint.pdf">Beating Floating Point</a>
 * 
 * @author <a href="mailto://dan@danbecker.info>Dan Becker</a>
 */
public final class Posit extends Number implements Comparable<Posit> {
	/** Version 1 serialization */
	private static final long serialVersionUID = 1L;

	/** log exceptions */
	public static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Posit.class);

	// Some constants for posits
    /**
     * A constant holding infinity of type {@code Posit}.
     * For Posits, infinity is the special case 1 followed by zeros.
     * All Posit infinity representations, whether 1 bit, or many,
     * are considered equal.
     */
//    public static final Posit INFINITY = new Posit("∞");

    /**
     * A constant holding zero of type {@code Posit}.
     * For Posits, zero is the special case of all zeroes.
     * All Posit zero representations, whether 1 bit, or many,
     * are considered equal.
     */
//    public static final Posit ZERO = new Posit("0");

    /**
     * A constant holding a Not-a-Number (NaN) value of type {@code Posit}.
     */
//    public static final Posit NaN = new Posit("NaN");

    
    /**
     * internal representation
     */
    private BitSet bitSet;

    
    /** BitSet has the weirdest size rules.
     * Size returns the internal representation size.
     * Length returns the highest 1 set.
     * Tracking on our own.
     */
    private int bitSetSize;

    // Constructors
    /**
     * Constructs a newly allocated {@code Posit} object 
     * with the internal representation given by the String.
     * The length of the String is the number of bits in
     * the Posit.
     * The first character is the sign bit.
     * The next N characters are the regime bits.
     * The next S characters are the exponent bits, if any.
     * The next F characters are the fraction bits, if any.
     *
     * @param   s  a string of the format ("0","1")*
     * @throws  NumberFormatException  if the string does not contain a
     *               parsable number.
     */
    public Posit(String s) throws NumberFormatException {
    	parseBinary(s);
    }

    
	// Number interface
    @Override
	/*
    * Returns the value of this {@code Float} as a {@code double}
    * after a widening primitive conversion.
    *
    * @return the {@code float} value represented by this
    *         object converted to type {@code double}
    */
	public double doubleValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
    /**
     * Returns the {@code float} value of this {@code Float} object.
     *
     * @return the {@code float} value represented by this object
     */
	public float floatValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
    /**
     * Returns the value of this {@code Float} as an {@code int} after
     * a narrowing primitive conversion.
     *
     * @return  the {@code float} value represented by this object
     *          converted to type {@code int}
     * @jls 5.1.3 Narrowing Primitive Conversions
     */
	public int intValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
    /**
     * Returns value of this {@code Float} as a {@code long} after a
     * narrowing primitive conversion.
     *
     * @return  the {@code float} value represented by this object
     *          converted to type {@code long}
     * @jls 5.1.3 Narrowing Primitive Conversions
     */
	public long longValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	// Float-like interface
    /**
     * Returns a {@code Float} object holding the
     * {@code float} value represented by the argument string
     * {@code s}.
     *
     * <p>If {@code s} is {@code null}, then a
     * {@code NullPointerException} is thrown.
     *
     * <p>Leading and trailing whitespace characters in {@code s}
     * are ignored.  Whitespace is removed as if by the {@link
     * String#trim} method; that is, both ASCII space and control
     * characters are removed. The rest of {@code s} should
     * constitute a <i>FloatValue</i> as described by the lexical
     * syntax rules:
     *
     * <blockquote>
     * <dl>
     * <dt><i>FloatValue:</i>
     * <dd><i>Sign<sub>opt</sub></i> {@code NaN}
     * <dd><i>Sign<sub>opt</sub></i> {@code Infinity}
     * <dd><i>Sign<sub>opt</sub> FloatingPointLiteral</i>
     * <dd><i>Sign<sub>opt</sub> HexFloatingPointLiteral</i>
     * <dd><i>SignedInteger</i>
     * </dl>
     *
     * <dl>
     * <dt><i>HexFloatingPointLiteral</i>:
     * <dd> <i>HexSignificand BinaryExponent FloatTypeSuffix<sub>opt</sub></i>
     * </dl>
     *
     * <dl>
     * <dt><i>HexSignificand:</i>
     * <dd><i>HexNumeral</i>
     * <dd><i>HexNumeral</i> {@code .}
     * <dd>{@code 0x} <i>HexDigits<sub>opt</sub>
     *     </i>{@code .}<i> HexDigits</i>
     * <dd>{@code 0X}<i> HexDigits<sub>opt</sub>
     *     </i>{@code .} <i>HexDigits</i>
     * </dl>
     *
     * <dl>
     * <dt><i>BinaryExponent:</i>
     * <dd><i>BinaryExponentIndicator SignedInteger</i>
     * </dl>
     *
     * <dl>
     * <dt><i>BinaryExponentIndicator:</i>
     * <dd>{@code p}
     * <dd>{@code P}
     * </dl>
     *
     * </blockquote>
     *
     * where <i>Sign</i>, <i>FloatingPointLiteral</i>,
     * <i>HexNumeral</i>, <i>HexDigits</i>, <i>SignedInteger</i> and
     * <i>FloatTypeSuffix</i> are as defined in the lexical structure
     * sections of
     * <cite>The Java&trade; Language Specification</cite>,
     * except that underscores are not accepted between digits.
     * If {@code s} does not have the form of
     * a <i>FloatValue</i>, then a {@code NumberFormatException}
     * is thrown. Otherwise, {@code s} is regarded as
     * representing an exact decimal value in the usual
     * "computerized scientific notation" or as an exact
     * hexadecimal value; this exact numerical value is then
     * conceptually converted to an "infinitely precise"
     * binary value that is then rounded to type {@code float}
     * by the usual round-to-nearest rule of IEEE 754 floating-point
     * arithmetic, which includes preserving the sign of a zero
     * value.
     *
     * Note that the round-to-nearest rule also implies overflow and
     * underflow behaviour; if the exact value of {@code s} is large
     * enough in magnitude (greater than or equal to ({@link
     * #MAX_VALUE} + {@link Math#ulp(float) ulp(MAX_VALUE)}/2),
     * rounding to {@code float} will result in an infinity and if the
     * exact value of {@code s} is small enough in magnitude (less
     * than or equal to {@link #MIN_VALUE}/2), rounding to float will
     * result in a zero.
     *
     * Finally, after rounding a {@code Float} object representing
     * this {@code float} value is returned.
     *
     * <p>To interpret localized string representations of a
     * floating-point value, use subclasses of {@link
     * java.text.NumberFormat}.
     *
     * <p>Note that trailing format specifiers, specifiers that
     * determine the type of a floating-point literal
     * ({@code 1.0f} is a {@code float} value;
     * {@code 1.0d} is a {@code double} value), do
     * <em>not</em> influence the results of this method.  In other
     * words, the numerical value of the input string is converted
     * directly to the target floating-point type.  In general, the
     * two-step sequence of conversions, string to {@code double}
     * followed by {@code double} to {@code float}, is
     * <em>not</em> equivalent to converting a string directly to
     * {@code float}.  For example, if first converted to an
     * intermediate {@code double} and then to
     * {@code float}, the string<br>
     * {@code "1.00000017881393421514957253748434595763683319091796875001d"}<br>
     * results in the {@code float} value
     * {@code 1.0000002f}; if the string is converted directly to
     * {@code float}, <code>1.000000<b>1</b>f</code> results.
     *
     * <p>To avoid calling this method on an invalid string and having
     * a {@code NumberFormatException} be thrown, the regular
     * expression below can be used to screen the input string:
     *
     * <pre>{@code
     *  final String Digits     = "(\\p{Digit}+)";
     *  final String HexDigits  = "(\\p{XDigit}+)";
     *  // an exponent is 'e' or 'E' followed by an optionally
     *  // signed decimal integer.
     *  final String Exp        = "[eE][+-]?"+Digits;
     *  final String fpRegex    =
     *      ("[\\x00-\\x20]*"+  // Optional leading "whitespace"
     *       "[+-]?(" + // Optional sign character
     *       "NaN|" +           // "NaN" string
     *       "Infinity|" +      // "Infinity" string
     *
     *       // A decimal floating-point string representing a finite positive
     *       // number without a leading sign has at most five basic pieces:
     *       // Digits . Digits ExponentPart FloatTypeSuffix
     *       //
     *       // Since this method allows integer-only strings as input
     *       // in addition to strings of floating-point literals, the
     *       // two sub-patterns below are simplifications of the grammar
     *       // productions from section 3.10.2 of
     *       // The Java Language Specification.
     *
     *       // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
     *       "((("+Digits+"(\\.)?("+Digits+"?)("+Exp+")?)|"+
     *
     *       // . Digits ExponentPart_opt FloatTypeSuffix_opt
     *       "(\\.("+Digits+")("+Exp+")?)|"+
     *
     *       // Hexadecimal strings
     *       "((" +
     *        // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
     *        "(0[xX]" + HexDigits + "(\\.)?)|" +
     *
     *        // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
     *        "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +
     *
     *        ")[pP][+-]?" + Digits + "))" +
     *       "[fFdD]?))" +
     *       "[\\x00-\\x20]*");// Optional trailing "whitespace"
     *
     *  if (Pattern.matches(fpRegex, myString))
     *      Double.valueOf(myString); // Will not throw NumberFormatException
     *  else {
     *      // Perform suitable alternative action
     *  }
     * }</pre>.
     *
     * @param   s   the string to be parsed.
     * @return  a {@code Posit} object holding the value
     *          represented by the {@code String} argument.
     * @throws  NumberFormatException  if the string does not contain a
     *          parsable number.
     */
    public static Posit valueOf(String s) throws NumberFormatException {
        return new Posit(s);
    }

    /**
     * Returns {@code true} if this {@code Posit} value is
     * infinitely large in magnitude, {@code false} otherwise.
     *
     * @return  {@code true} if the value represented by this object is
     *          positive infinity or negative infinity;
     *          {@code false} otherwise.
     */
    public boolean isInfinite() {
    	return true;
    }

    /**
     * Returns {@code true} if this {@code Posit} value is
     * infinitely large in magnitude, {@code false} otherwise.
     *
     * @return  {@code true} if the value represented by this object is
     *          positive infinity or negative infinity;
     *          {@code false} otherwise.
     */
    public boolean isZero() {
    	return true;
    }

    /**
    * Returns {@code true} if this {@code Posit} value is a
    * Not-a-Number (NaN), {@code false} otherwise.
    *
    * @return  {@code true} if the value represented by this object is
    *          NaN; {@code false} otherwise.
    */
   public boolean isNaN() {
   	return true;
   }

    /**
     * Adds two {@code float} values together as per the + operator.
     *
     * @param a the first operand
     * @param b the second operand
     * @return the sum of {@code a} and {@code b}
     * @jls 4.2.4 Floating-Point Operations
     * @see java.util.function.BinaryOperator
     * @since 1.8
     */
    public static float sum(float a, float b) {
        return a + b;
    }

    /**
     * Returns the greater of two {@code float} values
     * as if by calling {@link Math#max(float, float) Math.max}.
     *
     * @param a the first operand
     * @param b the second operand
     * @return the greater of {@code a} and {@code b}
     * @see java.util.function.BinaryOperator
     * @since 1.8
     */
    public static float max(float a, float b) {
        return Math.max(a, b);
    }

    /**
     * Returns the smaller of two {@code float} values
     * as if by calling {@link Math#min(float, float) Math.min}.
     *
     * @param a the first operand
     * @param b the second operand
     * @return the smaller of {@code a} and {@code b}
     * @see java.util.function.BinaryOperator
     * @since 1.8
     */
    public static float min(float a, float b) {
        return Math.min(a, b);
    }

	// Comparable interface
    /**
     * Compares two {@code Float} objects numerically.  There are
     * two ways in which comparisons performed by this method differ
     * from those performed by the Java language numerical comparison
     * operators ({@code <, <=, ==, >=, >}) when
     * applied to primitive {@code float} values:
     *
     * <ul><li>
     *          {@code Float.NaN} is considered by this method to
     *          be equal to itself and greater than all other
     *          {@code float} values
     *          (including {@code Float.POSITIVE_INFINITY}).
     * <li>
     *          {@code 0.0f} is considered by this method to be greater
     *          than {@code -0.0f}.
     * </ul>
     *
     * This ensures that the <i>natural ordering</i> of {@code Float}
     * objects imposed by this method is <i>consistent with equals</i>.
     *
     * @param   anotherFloat   the {@code Float} to be compared.
     * @return  the value {@code 0} if {@code anotherFloat} is
     *          numerically equal to this {@code Float}; a value
     *          less than {@code 0} if this {@code Float}
     *          is numerically less than {@code anotherFloat};
     *          and a value greater than {@code 0} if this
     *          {@code Float} is numerically greater than
     *          {@code anotherFloat}.
     *
     * @since   1.2
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(Posit anotherPosit) {
        return Posit.compare(this, anotherPosit);
    }

    // Object methods
   /**
     * Compares the two specified {@code float} values. The sign
     * of the integer value returned is the same as that of the
     * integer that would be returned by the call:
     * <pre>
     *    new Float(f1).compareTo(new Float(f2))
     * </pre>
     *
     * @param   f1        the first {@code float} to compare.
     * @param   f2        the second {@code float} to compare.
     * @return  the value {@code 0} if {@code f1} is
     *          numerically equal to {@code f2}; a value less than
     *          {@code 0} if {@code f1} is numerically less than
     *          {@code f2}; and a value greater than {@code 0}
     *          if {@code f1} is numerically greater than
     *          {@code f2}.
     * @since 1.4
     */
    public static int compare(Posit p1, Posit p2) {
    	return 0;
    }
    
    /**
     * Returns a hash code for this {@code Float} object. The
     * result is the integer bit representation, exactly as produced
     * by the method {@link #floatToIntBits(float)}, of the primitive
     * {@code float} value represented by this {@code Float}
     * object.
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return 0;
    }

    /**
     * Compares this object against the specified object.  The result
     * is {@code true} if and only if the argument is not
     * {@code null} and is a {@code Float} object that
     * represents a {@code float} with the same value as the
     * {@code float} represented by this object. For this
     * purpose, two {@code float} values are considered to be the
     * same if and only if the method {@link #floatToIntBits(float)}
     * returns the identical {@code int} value when applied to
     * each.
     *
     * <p>Note that in most cases, for two instances of class
     * {@code Float}, {@code f1} and {@code f2}, the value
     * of {@code f1.equals(f2)} is {@code true} if and only if
     *
     * <blockquote><pre>
     *   f1.floatValue() == f2.floatValue()
     * </pre></blockquote>
     *
     * <p>also has the value {@code true}. However, there are two exceptions:
     * <ul>
     * <li>If {@code f1} and {@code f2} both represent
     *     {@code Float.NaN}, then the {@code equals} method returns
     *     {@code true}, even though {@code Float.NaN==Float.NaN}
     *     has the value {@code false}.
     * <li>If {@code f1} represents {@code +0.0f} while
     *     {@code f2} represents {@code -0.0f}, or vice
     *     versa, the {@code equal} test has the value
     *     {@code false}, even though {@code 0.0f==-0.0f}
     *     has the value {@code true}.
     * </ul>
     *
     * This definition allows hash tables to operate properly.
     *
     * @param obj the object to be compared
     * @return  {@code true} if the objects are the same;
     *          {@code false} otherwise.
     * @see java.lang.Float#floatToIntBits(float)
     */
    public boolean equals(Object obj) {
    	return true;
    }

	/**
     * Returns a string representation of the {@code float}
     * argument. All characters mentioned below are ASCII characters.
     * <ul>
     * <li>If the argument is NaN, the result is the string
     * "{@code NaN}".
     * <li>Otherwise, the result is a string that represents the sign and
     *     magnitude (absolute value) of the argument. If the sign is
     *     negative, the first character of the result is
     *     '{@code -}' ({@code '\u005Cu002D'}); if the sign is
     *     positive, no sign character appears in the result. As for
     *     the magnitude <i>m</i>:
     * <ul>
     * <li>If <i>m</i> is infinity, it is represented by the characters
     *     {@code "Infinity"}; thus, positive infinity produces
     *     the result {@code "Infinity"} and negative infinity
     *     produces the result {@code "-Infinity"}.
     * <li>If <i>m</i> is zero, it is represented by the characters
     *     {@code "0.0"}; thus, negative zero produces the result
     *     {@code "-0.0"} and positive zero produces the result
     *     {@code "0.0"}.
     * <li> If <i>m</i> is greater than or equal to 10<sup>-3</sup> but
     *      less than 10<sup>7</sup>, then it is represented as the
     *      integer part of <i>m</i>, in decimal form with no leading
     *      zeroes, followed by '{@code .}'
     *      ({@code '\u005Cu002E'}), followed by one or more
     *      decimal digits representing the fractional part of
     *      <i>m</i>.
     * <li> If <i>m</i> is less than 10<sup>-3</sup> or greater than or
     *      equal to 10<sup>7</sup>, then it is represented in
     *      so-called "computerized scientific notation." Let <i>n</i>
     *      be the unique integer such that 10<sup><i>n</i> </sup>&le;
     *      <i>m</i> {@literal <} 10<sup><i>n</i>+1</sup>; then let <i>a</i>
     *      be the mathematically exact quotient of <i>m</i> and
     *      10<sup><i>n</i></sup> so that 1 &le; <i>a</i> {@literal <} 10.
     *      The magnitude is then represented as the integer part of
     *      <i>a</i>, as a single decimal digit, followed by
     *      '{@code .}' ({@code '\u005Cu002E'}), followed by
     *      decimal digits representing the fractional part of
     *      <i>a</i>, followed by the letter '{@code E}'
     *      ({@code '\u005Cu0045'}), followed by a representation
     *      of <i>n</i> as a decimal integer, as produced by the
     *      method {@link java.lang.Integer#toString(int)}.
     *
     * </ul>
     * </ul>
     * How many digits must be printed for the fractional part of
     * <i>m</i> or <i>a</i>? There must be at least one digit
     * to represent the fractional part, and beyond that as many, but
     * only as many, more digits as are needed to uniquely distinguish
     * the argument value from adjacent values of type
     * {@code float}. That is, suppose that <i>x</i> is the
     * exact mathematical value represented by the decimal
     * representation produced by this method for a finite nonzero
     * argument <i>f</i>. Then <i>f</i> must be the {@code float}
     * value nearest to <i>x</i>; or, if two {@code float} values are
     * equally close to <i>x</i>, then <i>f</i> must be one of
     * them and the least significant bit of the significand of
     * <i>f</i> must be {@code 0}.
     *
     * <p>To create localized string representations of a floating-point
     * value, use subclasses of {@link java.text.NumberFormat}.
     *
     * @param   f   the float to be converted.
     * @return a string representation of the argument.
     */
    public String toString() {
    	return "Posit:";
    }

    /** Sets internal representation to the given String
     * 
     * @param   s  a string of the format ("0","1")*. 
     * 			If the string has whitespace, it is trimmed.
     * 			If the string starts with "0b" it is trimmed.
     * @throws  NumberFormatException  if the string does not contain a
     *               parsable binary number.
     */
    public void parseBinary( String s ) {
    	if ( null == s )
    		throw new NullPointerException();
    	s = s.trim();
    	if ( s.startsWith( "0b" )) {
    		s = s.substring(2);
    	}
    	// Check
    	for ( int i = 0; i < s.length(); i++  ) {
    		if (-1 == "01".indexOf( s.charAt(i) ) ) {
    			throw new NumberFormatException( "Invalid binary character '" + s.charAt (i) + "' at position " + i );
    		}
    	}    
    	this.bitSet = new BitSet( s.length() );
    	this.bitSetSize = s.length();
    	for ( int i = 0; i < s.length(); i++  ) {
    		if ( s.charAt(i) == '1' ) {
    			bitSet.set(i);
    		} else {
    			bitSet.clear(i);
    		}
    	}
    }

    /*
    * Gives the internal representation of the Posit as a
    * String of zero and one characters
    * <p>
    * If the Posit bit length is zero, an empty String is returned.
    *
    * @return a string representation of the object.
    */
    public String toBinaryString() {
       StringBuilder sb = new StringBuilder();
	   // System.out.println( "   bitSet=" + bitSet + ", bitSet.length=" + bitSet.length() + ", bitSet.size=" + bitSet.size() + ", bitSetSize=" + this.bitSetSize ); 
       if ( null != bitSet ) {
    	   // Size is the number of bits in the bit set. Length is the highest "1"
    	   for ( int i = 0; i < this.bitSetSize; i++ ) {
    		   sb.append( bitSet.get(i) ? "1" : "0");
    	   }
       }
   	   return sb.toString();
    }

    /**
     * Returns the number of bits in this Posit.
     * @return number of bits in this Posit
     */
    public int getBitSize() {
    	return this.bitSetSize;
    }
    
    /**
     * Returns whether the sign bit is set or not.
     * If the bit size is 0, returns false
     * @return if this Posit is positive (has the sign bit set)
     */
    public boolean isPositive() {
    	if ( getBitSize() > 0 ) {
    		return bitSet.get(0);
    	}
    	return false;
    }
    
    /**
     * Returns the regime bits of this Posit as a String of "0" and "1"
     * If the bit size is less than 2, returns empty Sting.
     * @return if this Posit is positive (has the sign bit set
     */
    public String getRegime() {
    	StringBuilder sb = new StringBuilder();
    	String first = null;
    	for( int i = 1; i < getBitSize(); i++ ) {
    		String bit = bitSet.get(i) ? "1" : "0";
    		if ( null == first )
     		   first = bit;
    		sb.append( bit );
    		if ( !bit.equals( first ))
    			break;
    	}
    	return sb.toString();
    }
    
    /**
     * Returns the regime value K.
     * <p>
     * Let m be the number of identical bits starting the regime;
     * if the bits are 0, then k = −m; if they are 1, then k = m− 1.
     * 
     * If the bit size is less than 2, returns empty Sting.
     * @return if this Posit is positive (has the sign bit set
     */
    public int getRegimeK() {
    	String regime = getRegime();
    	int k = 0;
    	String first = null;
    	if ( null != regime && regime.length() > 0 ) {
    		for( int i = 0; i < regime.length(); i++ ) {
        		String bit = regime.substring( i, i+1);
        		if ( null == first )
         		   first = bit;
        		if ( !bit.equals( first ))
        			break;
        		if ( "0".equals(first)) 
        			k--;
        		if ( "1".equals(first) && i != 0) 
        			k++;
    		}    		
    	}
    	return k;
    }
    
    // Runtime
	public static void main(String[] args) throws Exception {
		LOGGER.debug("Hello");
		System.out.println("Posit");
		// Parse command line options
		parseOptions(args);
		LOGGER.debug("Bye");
	}

	/** Command line options for this application. */
	public static void parseOptions(String[] args) throws ParseException, IOException {
		// Parse the command line arguments
		Options options = new Options();
		// Use dash with shortcut (-h) or -- with name (--help).
		options.addOption("h", "help", false, "print the command line options");
		options.addOption("n", "numPatterns", true, "generates this many patterns");

		CommandLineParser cliParser = new DefaultParser();
		CommandLine line = cliParser.parse(options, args);

		// // Gather command line arguments for execution
		if (line.hasOption("help")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("java -jar posit.jar <options> javax.lang.posit.Posit",
					options);
			System.exit(0);
		}
//		if (line.hasOption("numPatterns")) {
//			numPatterns = Integer.parseInt(line.getOptionValue("numPatterns"));
//			System.out.println("   numPatterns=" + numPatterns);
//		}
	}
}