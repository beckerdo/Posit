package javax.lang.posit;

/**
 * Bit
 * <p>
 * Utility methods for bit manipulations.
 *
 * @author <a href="mailto://dan@danbecker.info>Dan Becker</a>
 */
public final class Bit {
	// Constructors
	protected Bit() {
	}

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

}