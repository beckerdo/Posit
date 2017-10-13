package javax.lang.posit;

import java.util.BitSet;

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
	private BitSet bitSet;

	/**
	 * BitSet has the weirdest size rules. Size returns the internal representation
	 * size. Length returns the highest 1 set. Tracking on our own.
	 */
	private int bitSetSize;

	// Constructors
	/**
	 * Constructs a newly allocated {@code Posit} object with the internal
	 * representation given by the String. The length of the String is the number of
	 * bits in the Posit. The first character is the sign bit. The next N characters
	 * are the regime bits. The next S characters are the exponent bits, if any. The
	 * next F characters are the fraction bits, if any.
	 *
	 * @param s
	 *            a string of the format ("0","1")*
	 * @throws NumberFormatException
	 *             if the string does not contain a parsable number.
	 */
	public PositStringImpl(String s) throws NumberFormatException {
		super(s);
		parsePosit(s);
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

	// Conversion
	/**
	 * Returns a {@code Float} object holding the {@code float} value represented by
	 * the argument string {@code s}.
	 *
	 * <p>
	 * If {@code s} is {@code null}, then a {@code NullPointerException} is thrown.
	 *
	 * <p>
	 * Leading and trailing whitespace characters in {@code s} are ignored.
	 * Whitespace is removed as if by the {@link String#trim} method; that is, both
	 * ASCII space and control characters are removed. The rest of {@code s} should
	 * constitute a <i>FloatValue</i> as described by the lexical syntax rules:
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
	 * <dd><i>HexSignificand BinaryExponent FloatTypeSuffix<sub>opt</sub></i>
	 * </dl>
	 *
	 * <dl>
	 * <dt><i>HexSignificand:</i>
	 * <dd><i>HexNumeral</i>
	 * <dd><i>HexNumeral</i> {@code .}
	 * <dd>{@code 0x} <i>HexDigits<sub>opt</sub> </i>{@code .}<i> HexDigits</i>
	 * <dd>{@code 0X}<i> HexDigits<sub>opt</sub> </i>{@code .} <i>HexDigits</i>
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
	 * where <i>Sign</i>, <i>FloatingPointLiteral</i>, <i>HexNumeral</i>,
	 * <i>HexDigits</i>, <i>SignedInteger</i> and <i>FloatTypeSuffix</i> are as
	 * defined in the lexical structure sections of <cite>The Java&trade; Language
	 * Specification</cite>, except that underscores are not accepted between
	 * digits. If {@code s} does not have the form of a <i>FloatValue</i>, then a
	 * {@code NumberFormatException} is thrown. Otherwise, {@code s} is regarded as
	 * representing an exact decimal value in the usual "computerized scientific
	 * notation" or as an exact hexadecimal value; this exact numerical value is
	 * then conceptually converted to an "infinitely precise" binary value that is
	 * then rounded to type {@code float} by the usual round-to-nearest rule of IEEE
	 * 754 floating-point arithmetic, which includes preserving the sign of a zero
	 * value.
	 *
	 * Note that the round-to-nearest rule also implies overflow and underflow
	 * behaviour; if the exact value of {@code s} is large enough in magnitude
	 * (greater than or equal to ({@link #MAX_VALUE} + {@link Math#ulp(float)
	 * ulp(MAX_VALUE)}/2), rounding to {@code float} will result in an infinity and
	 * if the exact value of {@code s} is small enough in magnitude (less than or
	 * equal to {@link #MIN_VALUE}/2), rounding to float will result in a zero.
	 *
	 * Finally, after rounding a {@code Float} object representing this
	 * {@code float} value is returned.
	 *
	 * <p>
	 * To interpret localized string representations of a floating-point value, use
	 * subclasses of {@link java.text.NumberFormat}.
	 *
	 * <p>
	 * Note that trailing format specifiers, specifiers that determine the type of a
	 * floating-point literal ({@code 1.0f} is a {@code float} value; {@code 1.0d}
	 * is a {@code double} value), do <em>not</em> influence the results of this
	 * method. In other words, the numerical value of the input string is converted
	 * directly to the target floating-point type. In general, the two-step sequence
	 * of conversions, string to {@code double} followed by {@code double} to
	 * {@code float}, is <em>not</em> equivalent to converting a string directly to
	 * {@code float}. For example, if first converted to an intermediate
	 * {@code double} and then to {@code float}, the string<br>
	 * {@code "1.00000017881393421514957253748434595763683319091796875001d"}<br>
	 * results in the {@code float} value {@code 1.0000002f}; if the string is
	 * converted directly to {@code float}, <code>1.000000<b>1</b>f</code> results.
	 *
	 * <p>
	 * To avoid calling this method on an invalid string and having a
	 * {@code NumberFormatException} be thrown, the regular expression below can be
	 * used to screen the input string:
	 *
	 * <pre>
	 * {
	 * 	&#64;code
	 * 	final String Digits = "(\\p{Digit}+)";
	 * 	final String HexDigits = "(\\p{XDigit}+)";
	 * 	// an exponent is 'e' or 'E' followed by an optionally
	 * 	// signed decimal integer.
	 * 	final String Exp = "[eE][+-]?" + Digits;
	 * 	final String fpRegex = ("[\\x00-\\x20]*" + // Optional leading "whitespace"
	 * 			"[+-]?(" + // Optional sign character
	 * 			"NaN|" + // "NaN" string
	 * 			"Infinity|" + // "Infinity" string
	 *
	 * 			// A decimal floating-point string representing a finite positive
	 * 			// number without a leading sign has at most five basic pieces:
	 * 			// Digits . Digits ExponentPart FloatTypeSuffix
	 * 			//
	 * 			// Since this method allows integer-only strings as input
	 * 			// in addition to strings of floating-point literals, the
	 * 			// two sub-patterns below are simplifications of the grammar
	 * 			// productions from section 3.10.2 of
	 * 			// The Java Language Specification.
	 *
	 * 			// Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
	 * 			"(((" + Digits + "(\\.)?(" + Digits + "?)(" + Exp + ")?)|" +
	 *
	 * 			// . Digits ExponentPart_opt FloatTypeSuffix_opt
	 * 			"(\\.(" + Digits + ")(" + Exp + ")?)|" +
	 *
	 * 			// Hexadecimal strings
	 * 			"((" +
	 * 			// 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
	 * 			"(0[xX]" + HexDigits + "(\\.)?)|" +
	 *
	 * 			// 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
	 * 			"(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +
	 *
	 * 			")[pP][+-]?" + Digits + "))" + "[fFdD]?))" + "[\\x00-\\x20]*");// Optional trailing "whitespace"
	 *
	 * 	if (Pattern.matches(fpRegex, myString))
	 * 		Double.valueOf(myString); // Will not throw NumberFormatException
	 * 	else {
	 * 		// Perform suitable alternative action
	 * 	}
	 * }
	 * </pre>
	 * 
	 * .
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
	 * Sets internal representation to the given String
	 * 
	 * @param s
	 *            a string of the format ("0","1")*. If the string has whitespace,
	 *            it is trimmed. If the string starts with "0b" it is trimmed.
	 * @throws NumberFormatException
	 *             if the string does not contain a parsable binary number.
	 */
	@Override
	public void parsePosit(String s) {
	}

	/**
	 * Gives the internal representation of the Posit as a String of zero and one
	 * characters
	 * <p>
	 * If the Posit bit length is zero, an empty String is returned.
	 *
	 * @return a string representation of the object.
	 */
	@Override
	public String toBinaryString() {
		final StringBuilder sb = new StringBuilder();
		return sb.toString();
	}

	// Math interface
	@Override
	/**
	 * @see Posit#isInfinite()
	 */
	public boolean isInfinite() {
		if (null != bitSet) {
			final int length = getBitSize();
			if (length > 0) {
				if (!bitSet.get(0)) {
					return false;
				}
			} else {
				return false;
			}
			for (int i = 1; i < length; i++) {
				final boolean bit = bitSet.get(i);
				if (bit) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	/**
	 * @see Posit#isZero()
	 */
	public boolean isZero() {
		if (0 < getBitSize()) {
			return bitSet.isEmpty();
		}
		return false;
	}

	@Override
	/**
	 * @see Posit#isNaN()
	 */
	public boolean isNaN() {
		return false;
	}

	// Math Consider +, -, *, /

	// Comparable interface
	/**
	 * @see Posit#compareTo
	 */
	public int compareTo(PositStringImpl anotherPosit) {
		return PositStringImpl.compare(this, anotherPosit);
	}

	// Object methods
	/**
	 * @see Posit#compare
	 */
	public static int compare(PositStringImpl p1, PositStringImpl p2) {
		return 0;
	}

	/**
	 * @see Posit#hashCode
	 */
	@Override
	public int hashCode() {
		return 0;
	}

	/**
	 * @see Posit#equals
	 */
	@Override
	public boolean equals(Object obj) {
		return true;
	}

	/**
	 * @see Posit#toString
	 */
	@Override
	public String toString() {
		return "Posit:";
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
		return bitSetSize;
	}

	@Override
	/**
	 * @see Posit#isPositive()
	 */
	public boolean isPositive() {
		if (getBitSize() > 0) {
			return bitSet.get(0);
		}
		return false;
	}

	@Override
	/**
	 * @see Posit#getRegime()
	 */
	public String getRegime() {
		final StringBuilder sb = new StringBuilder();
		String first = null;
		for (int i = 1; i < getBitSize(); i++) {
			final String bit = bitSet.get(i) ? "1" : "0";
			if (null == first) {
				first = bit;
			}
			sb.append(bit);
			if (!bit.equals(first)) {
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
		final String regime = getRegime();
		int k = 0;
		String first = null;
		if (null != regime && regime.length() > 0) {
			for (int i = 0; i < regime.length(); i++) {
				final String bit = regime.substring(i, i + 1);
				if (null == first) {
					first = bit;
				}
				if (!bit.equals(first)) {
					break;
				}
				if ("0".equals(first)) {
					k--;
				}
				if ("1".equals(first) && i != 0) {
					k++;
				}
			}
		}
		return k;
	}

	@Override
	/**
	 * @see Posit#getRegimeUseed()
	 */
	public long getRegimeUseed() {
		final int regimeLength = getRegime().length();
		return (long) Math.pow(2, Math.pow(2, regimeLength));
	}

	@Override
	/**
	 * @see Posit#getExponent()
	 */
	public String getExponent() {
		final StringBuilder sb = new StringBuilder();
		String first = null;
		for (int i = 1; i < getBitSize(); i++) {
			// TODO Fix this. Copied code.
			final String bit = bitSet.get(i) ? "1" : "0";
			if (null == first) {
				first = bit;
			}
			sb.append(bit);
			if (!bit.equals(first)) {
				break;
			}
		}
		return sb.toString();
	}

	// Utility
}