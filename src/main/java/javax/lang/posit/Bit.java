package javax.lang.posit;

/**
 * Bit
 * <p>
 * Utility methods for bit manipulations.
 *
 * @author <a href="mailto://dan@danbecker.info>Dan Becker</a>
 */
public final class Bit {
	// Utility
	public static String twosComplement(String input) {
		if (null == input || input.length() < 1) {
			return input;
		}
		final char[] output = input.toCharArray();
		// Flip bits
		for (int i = 0; i < output.length; i++) {
			output[i] = Bit.invert(output[i]);
		}
		// Add one
		for (int i = output.length - 1; i >= 0; i--) {
			output[i] = Bit.invert(output[i]);
			if (output[i] == '1') {
				// no carry means we are done.
				break;
			}
		}
		return new String(output);
	}

	// Returns '0' for '1' and '1' for '0'
	public static char invert(char c) {
		return (c == '0') ? '1' : '0';
	}
	
    /**
     * Integer power function, base^power
     * @param base
     * @param power
     * @return
     */
    public static int pow( int base, int power) {
        if (power < 0) {
            throw new IllegalArgumentException( "power must be >= 0, power value was " + power);
        }
        if ( power == 0 ) {
            return 1;
        }
        int result = base;
        for ( int i = 1; i < power; i++ ) {
            result *= base;
        }
        return result;
    }
    

}