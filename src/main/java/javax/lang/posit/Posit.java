package javax.lang.posit;

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Posit
 * <p>
 * An implementation of Posit numbers for Java
 * <p>
 * In addition, this class provides several methods for converting a
 * {@code Posit} to a {@code String} and a {@code String} to a {@code Posit}, as
 * well as other constants and methods useful when dealing with a
 * {@code Posit}s.
 * <p>
 * More information from John Gustafson:
 * <a href="http://www.johngustafson.net/pdfs/BeatingFloatingPoint.pdf">Beating
 * Floating Point</a>
 * <p>
 * Implementation classes of Posit decide which primitive type to use to support
 * size and dynamic range:
 * <ul>
 * <li>byte (8 bit) - most compact, lowest range,
 * <li>short (16 bit),
 * <li>int (32 bit),
 * <li>long (64 bit) - less compact, higher range,
 * <li>String - arbitrary length, least compact, arbitrary range.
 * </ul>
 * <p>
 * APIs for this class fall into these groups:
 * <ul>
 * <li>Constructors - methods for creating Posits,
 * <li>Number interface - methods for converting to/from Java Number types,
 * <li>Conversion - methods for converting to/from other types such as String,
 * <li>Math - value testing, arithmetic operations,
 * <li>Object methods - Java hash, equals, toString, compare,
 * <li>Posit domain - methods such as regime, useed, exponent, size,
 * implementation
 * <li>Utility - twosComplement, etc.
 * </ul>
 *
 * @author <a href="mailto://dan@danbecker.info>Dan Becker</a>
 */
public abstract class Posit extends Number implements Comparable<Posit> {
	/** Serialization version */
	private static final long serialVersionUID = 1L;

	/** LOGGER */
	public static final Logger LOGGER = LoggerFactory.getLogger(Posit.class);

	// Some constants for posits
	/**
	 * A constant holding infinity of type {@code Posit}. For Posits, infinity is
	 * the special case 1 followed by zeros. All Posit infinity representations,
	 * whether 1 bit, or many, are considered equal.
	 */
	public static final String INFINITY_STR = "∞";
	// public static final Posit INFINITY = new Posit(INFINITY_STR);

	/**
	 * A constant holding zero of type {@code Posit}. For Posits, zero is the
	 * special case of all zeroes. All Posit zero representations, whether 1 bit, or
	 * many, are considered equal.
	 */
	public static final String ZERO_STR = "0";
	// public static final Posit ZERO = new Posit(ZERO_STR);

	/**
	 * A constant holding a Not-a-Number (NAN) value of type {@code Posit}.
	 */
	public static final String NAN_STR = "NaN";
	// public static final Posit NAN = new Posit(NAN_STR);

	// Constructors
	/**
	 * Constructs a newly allocated {@code Posit} object.
	 */
	protected Posit() {
	}

	/**
	 * Constructs a newly allocated {@code Posit} object with the internal
	 * representation given by the instance.
	 *
	 * @param s
	 *            a string of the format ("0","1")*
	 * @throws NumberFormatException
	 *             if the string does not contain a parsable number.
	 */
	protected Posit(Object instance) throws NumberFormatException {
	}

	// Number interface
	@Override
	/**
	 * Returns the value of this {@code Posit} as an {@code byte} (8 bits) after a
	 * primitive conversion.
	 *
	 * @return the {@code Posit} value represented by this object converted to type
	 *         {@code byte}
	 * @jls 5.1.3 Narrowing Primitive Conversions
	 */
	public abstract byte byteValue();

	@Override
	/**
	 * Returns the value of this {@code Posit} as an {@code short} (16 bits) after a
	 * primitive conversion.
	 *
	 * @return the {@code Posit} value represented by this object converted to type
	 *         {@code short}
	 * @jls 5.1.3 Narrowing Primitive Conversions
	 */
	public abstract short shortValue();

	@Override
	/**
	 * Returns the value of this {@code Posit} as an {@code int} (32 bits) after a
	 * primitive conversion.
	 *
	 * @return the {@code Posit} value represented by this object converted to type
	 *         {@code int}
	 * @jls 5.1.3 Narrowing Primitive Conversions
	 */
	public abstract int intValue();

	@Override
	/**
	 * Returns value of this {@code Posit} as a {@code long} (64 bits) after a
	 * primitive conversion.
	 *
	 * @return the {@code Posit} value represented by this object converted to type
	 *         {@code long}
	 * @jls 5.1.3 Narrowing Primitive Conversions
	 */
	public abstract long longValue();

	@Override
	/**
	 * Returns value of this {@code Posit} as a {@code float} after a primitive
	 * conversion.
	 *
	 * @return the {@code Posit} value represented by this object converted to type
	 *         {@code float}
	 * @jls 5.1.3 Narrowing Primitive Conversions
	 */
	public abstract float floatValue();

	@Override
	/**
	 * Returns value of this {@code Posit} as a {@code double} after a primitive
	 * conversion.
	 *
	 * @return the {@code Posit} value represented by this object converted to type
	 *         {@code double}
	 * @jls 5.1.3 Narrowing Primitive Conversions
	 */
	public abstract double doubleValue();

	// Conversion
	/**
	 * From java.lang.Long Returns a Posit object holding the value of the specified
	 * String. The argument is interpreted as representing a Posit, exactly as if
	 * the argument were given to the parsePosit(java.lang.String) method. The
	 * result is a Posit object that represents the integer value specified by the
	 * string.
	 * <p>
	 * In other words, this method returns a Long object equal to the value of: new
	 * Post(Posit.parsePosit(s))
	 */
	/**
	 * From java.lang.Float Returns a {@code Float} object holding the {@code float}
	 * value represented by the argument string {@code s}. .... <Extremely long
	 * comment on regex for Floats>
	 *
	 * @param s
	 *            the string to be parsed.
	 * @return a {@code Posit} object holding the value represented by the
	 *         {@code String} argument.
	 * @throws NumberFormatException
	 *             if the string does not contain a parsable number.
	 */
	public static Posit valueOf(String s) throws NumberFormatException {
		return new PositStringImpl(s);
	}

	/**
	 *
	 * Sets internal representation to the given String
	 *
	 * @param s
	 *            a string of the format ("0","1")*. If the string has whitespace,
	 *            it is trimmed. If the string starts with "0b" it is trimmed.
	 * @throws NumberFormatException
	 *             if the string does not contain a parsable binary number.
	 */
	public void parsePosit(String s) {
	}

	/*
	 * Gives the internal representation of the Posit as a String of zero and one
	 * characters <p> If the Posit bit length is zero, an empty String is returned.
	 *
	 * @return a string representation of the object.
	 */
	public String toBinaryString() {
		final StringBuilder sb = new StringBuilder();
		return sb.toString();
	}

	// Math interface
	/**
	 * Returns {@code true} if this {@code Posit} value is infinitely large in
	 * magnitude, {@code false} otherwise.
	 * <p>
	 * Regardless of bit size, infinity is bit[0] == 1, all others zero.
	 * <p>
	 * Returns false for uninitialized or bitSize 0.
	 *
	 * @return {@code true} if the value represented by this object is positive
	 *         infinity or negative infinity; {@code false} otherwise.
	 */
	public boolean isInfinite() {
		return false;
	}

	/**
	 * Returns {@code true} if this {@code Posit} value is zero, {@code false}
	 * otherwise.
	 * <p>
	 * Regardless of bit size, zero is all bits zero.
	 * <p>
	 * Returns false for uninitialized or bitSize 0.
	 *
	 * @return {@code true} if the value represented by this object is zero;
	 *         {@code false} otherwise.
	 */
	public boolean isZero() {
		return false;
	}

	/**
	 * Returns {@code true} if this {@code Posit} value is a Not-a-Number (NaN),
	 * {@code false} otherwise.
	 *
	 * @return {@code true} if the value represented by this object is NaN;
	 *         {@code false} otherwise.
	 */
	public boolean isNaN() {
		return false;
	}

	// Math Consider +, -, *, /

	// Comparable interface
	/**
	 * Compares two {@code Float} objects numerically. There are two ways in which
	 * comparisons performed by this method differ from those performed by the Java
	 * language numerical comparison operators ({@code <, <=, ==, >=, >}) when
	 * applied to primitive {@code float} values:
	 *
	 * <ul>
	 * <li>{@code Float.NaN} is considered by this method to be equal to itself and
	 * greater than all other {@code float} values (including
	 * {@code Float.POSITIVE_INFINITY}).
	 * <li>{@code 0.0f} is considered by this method to be greater than
	 * {@code -0.0f}.
	 * </ul>
	 *
	 * This ensures that the <i>natural ordering</i> of {@code Float} objects
	 * imposed by this method is <i>consistent with equals</i>.
	 *
	 * @param anotherFloat
	 *            the {@code Float} to be compared.
	 * @return the value {@code 0} if {@code anotherFloat} is numerically equal to
	 *         this {@code Float}; a value less than {@code 0} if this {@code Float}
	 *         is numerically less than {@code anotherFloat}; and a value greater
	 *         than {@code 0} if this {@code Float} is numerically greater than
	 *         {@code anotherFloat}.
	 *
	 * @since 1.2
	 * @see Comparable#compareTo(Object)
	 */
	@Override
	public int compareTo(Posit anotherPosit) {
		return Posit.compare(this, anotherPosit);
	}

	// Object methods
	/**
	 * Compares the two specified {@code float} values. The sign of the integer
	 * value returned is the same as that of the integer that would be returned by
	 * the call:
	 *
	 * <pre>
	 * new Float(f1).compareTo(new Float(f2))
	 * </pre>
	 *
	 * @param f1
	 *            the first {@code float} to compare.
	 * @param f2
	 *            the second {@code float} to compare.
	 * @return the value {@code 0} if {@code f1} is numerically equal to {@code f2};
	 *         a value less than {@code 0} if {@code f1} is numerically less than
	 *         {@code f2}; and a value greater than {@code 0} if {@code f1} is
	 *         numerically greater than {@code f2}.
	 * @since 1.4
	 */
	public static int compare(Posit p1, Posit p2) {
		return 0;
	}

	/**
	 * Returns a hash code for this {@code Float} object. The result is the integer
	 * bit representation, exactly as produced by the method
	 * {@link #floatToIntBits(float)}, of the primitive {@code float} value
	 * represented by this {@code Float} object.
	 *
	 * @return a hash code value for this object.
	 */
	@Override
	public int hashCode() {
		return 0;
	}

	/**
	 * Compares this object against the specified object. The result is {@code true}
	 * if and only if the argument is not {@code null} and is a {@code Float} object
	 * that represents a {@code float} with the same value as the {@code float}
	 * represented by this object. For this purpose, two {@code float} values are
	 * considered to be the same if and only if the method
	 * {@link #floatToIntBits(float)} returns the identical {@code int} value when
	 * applied to each.
	 *
	 * <p>
	 * Note that in most cases, for two instances of class {@code Float}, {@code f1}
	 * and {@code f2}, the value of {@code f1.equals(f2)} is {@code true} if and
	 * only if
	 *
	 * <blockquote>
	 *
	 * <pre>
	 * f1.floatValue() == f2.floatValue()
	 * </pre>
	 *
	 * </blockquote>
	 *
	 * <p>
	 * also has the value {@code true}. However, there are two exceptions:
	 * <ul>
	 * <li>If {@code f1} and {@code f2} both represent {@code Float.NaN}, then the
	 * {@code equals} method returns {@code true}, even though
	 * {@code Float.NaN==Float.NaN} has the value {@code false}.
	 * <li>If {@code f1} represents {@code +0.0f} while {@code f2} represents
	 * {@code -0.0f}, or vice versa, the {@code equal} test has the value
	 * {@code false}, even though {@code 0.0f==-0.0f} has the value {@code true}.
	 * </ul>
	 *
	 * This definition allows hash tables to operate properly.
	 *
	 * @param obj
	 *            the object to be compared
	 * @return {@code true} if the objects are the same; {@code false} otherwise.
	 * @see java.lang.Float#floatToIntBits(float)
	 */
	@Override
	public boolean equals(Object obj) {
		return true;
	}

	/**
	 * Returns a string representation of the {@code Posit} argument. All characters
	 * mentioned below are ASCII characters.
	 * <ul>
	 * <li>If the argument is NaN, the result is the string "{@code NaN}".
	 * <li>Otherwise, the result is a string that represents the sign and magnitude
	 * (absolute value) of the argument. If the sign is negative, the first
	 * character of the result is '{@code -}' ({@code '\u005Cu002D'}); if the sign
	 * is positive, no sign character appears in the result. As for the magnitude
	 * <i>m</i>:
	 * <ul>
	 * <li>If <i>m</i> is infinity, it is represented by the characters
	 * {@code "Infinity"}; thus, positive infinity produces the result
	 * {@code "Infinity"} and negative infinity produces the result
	 * {@code "-Infinity"}.
	 * <li>If <i>m</i> is zero, it is represented by the characters {@code "0.0"};
	 * thus, negative zero produces the result {@code "-0.0"} and positive zero
	 * produces the result {@code "0.0"}.
	 * <li>If <i>m</i> is greater than or equal to 10<sup>-3</sup> but less than
	 * 10<sup>7</sup>, then it is represented as the integer part of <i>m</i>, in
	 * decimal form with no leading zeroes, followed by '{@code .}'
	 * ({@code '\u005Cu002E'}), followed by one or more decimal digits representing
	 * the fractional part of <i>m</i>.
	 * <li>If <i>m</i> is less than 10<sup>-3</sup> or greater than or equal to
	 * 10<sup>7</sup>, then it is represented in so-called "computerized scientific
	 * notation." Let <i>n</i> be the unique integer such that 10<sup><i>n</i>
	 * </sup>&le; <i>m</i> {@literal <} 10<sup><i>n</i>+1</sup>; then let <i>a</i>
	 * be the mathematically exact quotient of <i>m</i> and 10<sup><i>n</i></sup> so
	 * that 1 &le; <i>a</i> {@literal <} 10. The magnitude is then represented as
	 * the integer part of <i>a</i>, as a single decimal digit, followed by
	 * '{@code .}' ({@code '\u005Cu002E'}), followed by decimal digits representing
	 * the fractional part of <i>a</i>, followed by the letter '{@code E}'
	 * ({@code '\u005Cu0045'}), followed by a representation of <i>n</i> as a
	 * decimal integer, as produced by the method
	 * {@link java.lang.Integer#toString(int)}.
	 * </ul>
	 * <p>
	 * How many digits must be printed for the fractional part of <i>m</i> or
	 * <i>a</i>? There must be at least one digit to represent the fractional part,
	 * and beyond that as many, but only as many, more digits as are needed to
	 * uniquely distinguish the argument value from adjacent values of type
	 * {@code float}. That is, suppose that <i>x</i> is the exact mathematical value
	 * represented by the decimal representation produced by this method for a
	 * finite nonzero argument <i>f</i>. Then <i>f</i> must be the {@code float}
	 * value nearest to <i>x</i>; or, if two {@code float} values are equally close
	 * to <i>x</i>, then <i>f</i> must be one of them and the least significant bit
	 * of the significand of <i>f</i> must be {@code 0}.
	 * <p>
	 * To create localized string representations of a floating-point value, use
	 * subclasses of {@link java.text.NumberFormat}.
	 *
	 * @return a string representation of the argument.
	 */
	@Override
	public String toString() {
		return "Posit";
	}

	// Posit domain interface
	/**
	 * Returns the implementation class of this Posit
	 *
	 * @return Class of this implementation
	 */
	public abstract Class<?> getImplementation();

	/**
	 * Returns the number of bits in this Posit.
	 *
	 * @return number of bits in this Posit
	 */
	public abstract int getBitSize();

	/**
	 * Returns whether the sign bit is set or not. If the bit size is 0, returns
	 * false
	 *
	 * @return if this Posit is positive (has the sign bit set)
	 */
	public abstract boolean isPositive();

	/**
	 * Returns the regime bits of this Posit as a String of "0" and "1". If the bit
	 * size is less than 2, returns empty String.
	 *
	 * @return a string of "0" and "1" representing the regime
	 */
	public abstract String getRegime();

	/**
	 * Returns the regime value K.
	 * <p>
	 * Let m be the number of identical bits starting the regime; if the bits are 0,
	 * then k = −m; if they are 1, then k = m− 1.
	 *
	 * If the bit size is less than 2, returns empty Sting.
	 *
	 * @return a value representing the K of the regime bits
	 */
	public abstract int getRegimeK();

	/**
	 * Returns the useed of this Posit Useed is 2 ** 2 ** run length of regime.
	 * <ul>
	 * <li>es==0 => 2
	 * <li>es==1 => 2 * 2
	 * <li>es==2 => 4 * 4
	 * <li>es==3 => 16 * 16
	 * <li>es==4 => 256 * 256
	 * <li>es==5 => 65536 * 65536
	 * </ul>
	 * If the bit size is less than 2, returns empty String.
	 *
	 * @return a string of "0" and "1" representing the regime
	 */
	public abstract long getRegimeUseed();

	/**
	 * Returns the exponent bits of this Posit as a String of "0" and "1". If the
	 * regime fills the bit size, the exponent may be empty string.
	 *
	 * @return a string of "0" and "1" representing the exponent
	 */
	public abstract String getExponent();

	// Utility
	public static String twosComplement(String bin) {
		String ones = "";

		for (int i = 0; i < bin.length(); i++) {
			ones += flip(bin.charAt(i));
		}
		final StringBuilder twos = new StringBuilder(ones);
		boolean b = false;
		for (int i = ones.length() - 1; i > 0; i--) {
			if (ones.charAt(i) == '1') {
				twos.setCharAt(i, '0');
			} else {
				twos.setCharAt(i, '1');
				b = true;
				break;
			}
		}
		if (!b) {
			twos.append("1", 0, 7);
		}
		return twos.toString();
	}

	// Returns '0' for '1' and '1' for '0'
	public static char flip(char c) {
		return (c == '0') ? '1' : '0';
	}

	// Runtime
	public static void main(String[] args) throws Exception {
		LOGGER.info("Posit");
		// Parse command line options
		parseOptions(args);
		LOGGER.debug("Posit bye");
	}

	/** Command line options for this application. */
	public static void parseOptions(String[] args) throws ParseException, IOException {
		// Parse the command line arguments
		final Options options = new Options();
		// Use dash with shortcut (-h) or -- with name (--help).
		options.addOption("h", "help", false, "print the command line options");
		options.addOption("n", "numPatterns", true, "generates this many patterns");

		final CommandLineParser cliParser = new DefaultParser();
		final CommandLine line = cliParser.parse(options, args);

		// // Gather command line arguments for execution
		if (line.hasOption("help")) {
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("java -jar posit.jar <options> javax.lang.posit.Posit", options);
			System.exit(0);
		}
		// if (line.hasOption("numPatterns")) {
		// numPatterns = Integer.parseInt(line.getOptionValue("numPatterns"));
		// System.out.println(" numPatterns=" + numPatterns);
		// }
	}
}