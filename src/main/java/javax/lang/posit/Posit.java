package javax.lang.posit;

import java.math.BigInteger;

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
 * </ul>
 *
 * @author <a href="mailto://dan@danbecker.info>Dan Becker</a>
 */
public abstract class Posit extends Number implements Comparable<Posit> {
	/** Serialization version */
	private static final long serialVersionUID = 1L;

	/** LOGGER */
	public static final Logger LOGGER = LoggerFactory.getLogger(Posit.class);

	/** Some interesting Posit strings */
	public static final String PLUS_MINUS = "±";
	public static final String PLUS = "+";
	public static final String NEG = "-";
	public static final String INFINITY = "∞";
	public static final String ZERO = "0";
	public static final String ONE = "1";
	public static final String NAN = "NaN";

	// Constructors
	/**
	 * Constructs a newly allocated {@code Posit} object.
	 */
	protected Posit() {
		// cannot instantiate abstract Posit
	}

	/**
	 * Constructs a newly allocated {@code Posit} object from the given input.
	 * representation given by the instance.
	 *
	 * @param s a legal implementation instance
	 * @throws NumberFormatException
	 *             if the string does not contain a parsable number.
	 */
	protected Posit(final Object instance) throws NumberFormatException {
		// cannot instantiate abstract Posit
	}

	/**
	 * Constructs a newly allocated {@code Posit} object from the given input
	 * and maximum exponent size, es.
	 *
	 * @param s a legal implementation instance
	 * @param es max exponent size
	 * @throws NumberFormatException
	 *             if the string does not contain a parsable number.
	 */
	protected Posit(final Object instance,int es) throws NumberFormatException {
		// cannot instantiate abstract Posit
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

	/**
	 * Returns value of this {@code Posit} as a {@code String} after a primitive
	 * conversion.
	 *
	 * @return the {@code Posit} value represented by this object converted to type
	 *         {@code String}
	 */
	public abstract String stringValue();

	// Conversion
	/**
	 *
	 * Sets internal representation from the given String.
	 *
	 * @param s
	 *            a string of the format ("0","1")*. If the string has whitespace,
	 *            it is trimmed. If the string starts with "0b" it is trimmed.
	 * @throws NumberFormatException
	 *             if the string does not contain a parsable binary number.
	 */
	public void parse(final String s) throws NumberFormatException {
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

	// Math Consider +, -, *, /, abs, 1/x,

	// Comparable interface
	/**
	 * @see Comparable#compareTo(Object)
	 */
	@Override
	public int compareTo(final Posit anotherPosit) {
		return Posit.compare(this, anotherPosit);
	}

	// Object methods
	/**
	 * Compares the two specified {@code Posit} values.
	 */
	public static int compare(final Posit p1, final Posit p2) {
		return 0;
	}

	/**
	 * @see Object#hashCode
	 */
	@Override
	public int hashCode() {
		return 0;
	}

	/**
	 * @see Object#equals
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Posit) {
			final Posit posit = (Posit) obj;
			return equals(posit);
		}
		return false;
	}

	/**
	 * @see Object#toString
	 */
	@Override
	public String toString() {
		return stringValue();
	}

	// Posit domain interface
	/**
	 * Returns the native implementation class of this Posit. For example: String,
	 * Byte, Short, Integer, Long, etc..
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
	 * Returns whether the sign bit is set or not.
	 * <p>
	 * If the bit size is 0, returns false.
	 * <p>
	 * Note that Zero is considered positive, Infinity is not.
	 *
	 * @return if this Posit is positive (has the sign bit set)
	 */
	public abstract boolean isPositive();

	/**
	 * Returns whether the Posit is an exact number or not. Numbers such as 0, ∞, 1,
	 * 2, 4, 1/2, 1/4 are exact. Other Posits are approximations lying in the
	 * interval between two exacts
	 * <p>
	 * Posits are exact when the last bit is 0.
	 *
	 * @return if this Posit is exact (has the last bit unset)
	 */
	public abstract boolean isExact();

	/**
	 * Returns the regime bits of this Posit as a String of "0" and "1".
	 * <p>
	 * If the bit size is less than 2, returns empty String.
	 * <p>
	 * First char of Posit is sign bit.
	 * <p>
	 * Regime is first char until terminated by end of string or until opposite
	 * char.
	 * <p>
	 * Examples "0", "1", "00", "01", "10", "11"
	 *
	 * @return a string of "0" and "1" representing the regime
	 */
	public abstract String getRegime();

	/**
	 * Returns the regime value K.
	 * <p>
	 * Let m be the number of identical bits starting the regime; if the bits are 0,
	 * then k = −m; if the bits are 1, then k = m − 1.
	 * <p>
	 * Examples (regime = K): 0000=-4,
	 * 0001=-3,001x=-2,01xx=-1,10xx=110x=1,1110=2,1111=3
	 *
	 * @return a value representing the K of the regime bits
	 */
	public abstract int getRegimeK();

    /**
     * Return the exponent and fraction part of this Posit after the sign and regime
     * are removed.
     * 
     * @return the exponent and fraction part of this Posit after the sign and
     *         regime are removed.
     */
    public abstract String getExponentFraction();

    /**
     * Returns the maximum exponent size or em in this Posit.
     * <p>
     * Normally any part of the Posit after the sign and the regime comprise the
     * exponent and the fraction. Both may be absent. However, when present, the
     * maximum number of exponent bits is given by this value.
     * <p>
     * Es or "exponent size" is the actual length of the exponent string in a posit
     * instance. For example, in a 5 bit posit with em of 2, there are posit
     * instances with es = 0, 1, and 2.
     *
     * @return maximum number of exponent bits in this Posit
     */
    public abstract byte getMaxExponentSize();

	/**
	 * Sets the maximum exponent size or em in this Posit.
	 */
	public abstract void setMaxExponentSize(byte maxExponentSize);

	/**
	 * Returns the exponent bits of this Posit as a String of "0" and "1".
	 * <p>
	 * If the sign and regime bits fill the bit size, the exponent may be empty
	 * string. If the exponent and fraction are not zero length, es or "exponent
	 * size" will be the length of this String.
	 *
	 * @return a string of "0" and "1" representing the exponent
	 */
	public abstract String getExponent();

	/**
	 * Returns the exponent bits of this Posit as a String of "0" and "1".
	 * <p>
	 * If the sign and regime bits fill the bit size, the exponent may be empty
	 * string. If the exponent and fraction are not zero length, es or "exponent
	 * size" will be the length of this String.
	 *
	 * @return a string of "0" and "1" representing the exponent
	 */
	public abstract String getFraction();

    /**
     * Returns the fraction to scale the Posit value.
     * <p>
     * In general, fractional bits of a posit are used to scale the value.
     * If a fraction is given as f0..fn fraction bits, the multiplier
     * is 1.0 + val( fn ) / 2^n
     * <p>
     * For example, a one bit fraction (n=1) will have multipliers
     * fm(0,1)=1.0,1.5.
     * A two bit fraction (n=2) will have multipliers of
     * fm(00,01,10,11)=1.0,1.25,1.5,1.75
     *
     * @return the fraction scaling multiplier
     */
    public abstract double getFractionMultiplier();

	/**
	 * Returns the useed of this Posit Useed is 2 ** 2 ** run length of regime.
	 * <p>
	 * Given es as the size of the exponent, useed = 2^2^es, and scale = useed^k
	 * Examples (es = useed): 0=2,1=2^2=4,2=4^2=16,3=16^2=256,4=256^2=65536
	 *
	 * @return a BigInteger representing the Useed.
	 */
	public abstract BigInteger getUseed();

}