package javax.lang.posit;

/**
 * Posit data and method
 * <p>
 * Encodes information from Posit papers. Items in this class should be usable
 * by all Posit classes: Posit, PositStringImpl, etc.
 *
 * @see Posit
 *
 * @author <a href="mailto://dan@danbecker.info>Dan Becker</a>
 */
public final class PositModel {

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
		if (instance.length() < 1) {
			return false;
		}
		return '0' == instance.charAt(0);

	}

	/** Returns the regime portion of a string of binary 0 and 1 characters. */
	public static String getRegime(String instance) {
		// If the bit size is less than 2, returns empty String.
		// First char of Posit is sign bit.
		// Regime is first char until terminated by end of string or until opposite
		// char.
		// Examples "0", "1", "00", "01", "10", "11"
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

	/** Get the useed of a regime (2^2^regimeLength). */
	public static long getUseed(String regime) {
		if (null != regime) {
			final int regimeLength = regime.length();
			if (0 == regimeLength) {
				return 2;
			}
			return (long) Math.pow(2, Math.pow(2, regimeLength));
		}
		return 0;
	}

	/** Return the exponent part of a given string of 0 and 1 characters. */
	public static String getExponent(String instance) {
		// Returns the exponent bits of this Posit as a String of "0" and "1".
		// If the regime fills the bit size, the exponent may be empty string.
		if (null == instance || instance.length() < 2) {
			return "";
		}
		final String regime = getRegime(instance);
		return regime.substring(regime.length() + 1);

	}
}