package javax.lang.posit;

import java.math.BigInteger;

/**
 * Posit data and method
 * <p>
 * Encodes information from Posit papers. Items in this class should be usable
 * by all Posit classes: Posit, PositStringImpl, etc.
 * <p>
 * More information from John Gustafson:
 * <a href="http://www.johngustafson.net/pdfs/BeatingFloatingPoint.pdf">Beating
 * Floating Point</a>
 * <p>
 * "Suppose we view the bit string for a posit p as a signed integer, ranging
 * from -2^(n-1) to 2^(n-1)-1. Let k be the integer represented by the regime
 * bits, let e be the unsigned integer represented by the exponent bits, if any
 * let f be the fraction bits, represented by 1.f1...fn, if any Then x=0, when
 * p=0 x=±∞, when p=-2^(n-1) x=sign(p)*useed^k*2^e*f,all other p."
 * <p>
 * For example p="0 0001 101 11011101" with es=3<br/>
 * x=1*256^(-3)*2^(5)*(1+221/256)=477/134217728 ~=3.55393*10^(-6)
 *
 * @see Posit
 *
 * @author <a href="mailto://dan@danbecker.info>Dan Becker</a>
 */
public final class PositDomain {
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
	 * Checks if a string of binary 0 and 1 characters is exact. Type II unums
	 * ending in 1 (the ubit) represent the open interval between adjacent exact
	 * points, the unums for which end in 0.
	 * <p>
	 * Posits of length 0, 1, 2, (0,1,∞,-1) are all exact.
	 */
	public static boolean isExact(String instance) {
		if (null == instance || instance.length() < 3) {
			return true;
		}
		return '0' == instance.charAt(instance.length() - 1);

	}

	/** Returns the regime portion of a string of binary 0 and 1 characters. */
	public static String getRegime(String instance) {
		// If the bit size is less than 2, returns empty String.
		// First char of Posit is sign bit.
		// Regime is second char until terminated by end of string or opposite char.
		if (null == instance || instance.length() < 2) {
			return "";
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
		// 0000=-4, 0001=-3,001x=-2,01xx=-1,10xx=110x=1,1110=2,1111=3
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
	 * Return the exponent and fraction part of a Posit after the sign and regime
	 * are removed.
	 */
	public static String getExponentFraction(String instance) {
		// Returns the exponentFraction bits of this Posit as a String of "0" and "1".
		// If the regime fills the bit size, the exponentFraction may be empty string.
		if (null == instance || instance.length() < 3) {
			return "";
		}
		final String regime = getRegime(instance);
		// System.out.println( "Posit is " + instance + ", regime is " + regime);
		return instance.substring(regime.length() + 1);
	}

	/** Return the exponent part of a given string of 0 and 1 characters. */
	public static String getExponent(String exponentFraction, int maxExponent) {
		// Returns the exponent bits of this Posit as a String of "0" and "1".
		// If the regime fills the bit size, the exponent may be empty string.
		if (null == exponentFraction || exponentFraction.length() < 1) {
			return "";
		}
		// System.out.println("ExponentFraction=" + exponentFraction + ",maxExponent=" +
		// maxExponent);
		final int max = Math.min(maxExponent, exponentFraction.length());
		return exponentFraction.substring(0, max);
	}

	/** Return the value of an exponent string of 0 and 1 characters. */
	public static double getExponentVal(String exponent, boolean positive) {
		// Returns the exponent bits of this Posit as a String of "0" and "1".
		// If the regime fills the bit size, the exponent may be empty string.
		if (null == exponent || exponent.length() < 1) {
			return 0.0;
		}
		if (positive) {
			return Integer.parseUnsignedInt(exponent, 2);
		} else {
			return 1.0 / Integer.parseUnsignedInt(Bit.twosComplement(exponent), 2);
		}
	}

	/** Return the fraction part of a given string of 0 and 1 characters. */
	public static String getFraction(String exponentFraction, int maxExponent) {
		// Returns the exponent bits of this Posit as a String of "0" and "1".
		// If the regime fills the bit size, the exponent may be empty string.
		if (null == exponentFraction || exponentFraction.length() < 1) {
			return "";
		}
		final int max = Math.min(maxExponent, exponentFraction.length());
		return exponentFraction.substring(max);
	}

	/** Return the value of a given fraction of 0 and 1 characters. */
	public static double getFractionVal(String fraction, boolean positive) {
		// Returns the exponent bits of this Posit as a String of "0" and "1".
		// If the regime fills the bit size, the exponent may be empty string.
		if (null == fraction || fraction.length() < 1) {
			return 0.0;
		}
		if (positive) {
			return Long.parseUnsignedLong(fraction, 2);
		} else {
			return Long.parseUnsignedLong(Bit.twosComplement(fraction), 2);
		}
	}

	// Puny long runs out at es=6, puny double rounds at es=6.
	// public static final BigInteger [] LOOKUP_2_2_N = new BigInteger [] { };
	public static final BigInteger[] LOOKUP_2_2_N = new BigInteger[] { new BigInteger("2"), new BigInteger("4"),
			new BigInteger("16"), new BigInteger("256"), new BigInteger("65536"), new BigInteger("4294967296"),
			new BigInteger("18446744073709551616"), new BigInteger("340282366920938463463374607431768211456"),
			new BigInteger("115792089237316195423570985008687907853269984665640564039457584007913129639936") };

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
}