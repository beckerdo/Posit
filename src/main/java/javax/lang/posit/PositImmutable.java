package javax.lang.posit;

import java.math.BigInteger;

/**
 * Posit elements that are immutable or generally not changed
 * <p>
 * The following elements are generally not changed in an instance of Posit.
 * <ul>
 * <li>bits
 * <li>maxEs
 * <li>containerBits
 * <li>containerClass
 * </ul>
 * <p>
 * The following elements are derived from the above fields.
 * <ul>
 * <li>useed = 2^2^es
 * </ul>
 *
 * @see Posit
 * @author <a href="mailto://dan@danbecker.info>Dan Becker</a>
 */
public final class PositImmutable implements Comparable<PositImmutable> {

    private final byte bits;
    private final byte maxExponentSize;
    
    private final byte containerBits;
    private final Class<?> containerClass;

    private final BigInteger useed;
    
    // Constructors
    @SuppressWarnings("unused")
    private PositImmutable() {
        throw new AssertionError();
    }

    public PositImmutable(byte bits, byte maxExponentSize) {
        // check inputs
        // Consider hashMap of these elements
        this.bits = bits;
        this.maxExponentSize = maxExponentSize;
        
        this.containerBits = bits; // find nearest platform size
        this.containerClass = this.getClass(); // find nearest platform object
        this.useed = getUseed( bits ); 
    }

    // Posit domain interface
    public int getBitSize() {
        return bits;
    }
    public int getMaxExponentSize() {
        return maxExponentSize;
    }
    public int getContainerBitSize() {
        return containerBits;
    }
    public Class<?> getContainerClass() {
        return containerClass;
    }    
    public final BigInteger getUseed() {
        return useed;
    }
        
    // Object methods
    @Override
    public int hashCode() {
        return 1023 * bits + maxExponentSize;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof PositImmutable) {
            final PositImmutable other = (PositImmutable) obj;
            return bits == other.bits && maxExponentSize == other.maxExponentSize;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "PositImmutable: bits=" + bits + ", maxEs=" + maxExponentSize;
    }

    // Comparable
    @Override
    public int compareTo(PositImmutable o) {
        if ( this.bits == o.bits) {
            return this.maxExponentSize - o.maxExponentSize;
        }
        return this.bits - o.bits;
    }

    /*
     * A lookup table of 2^2^N. 
     * <table>
     * <th><td>N<td>2^2^N</td>
     * <tr><td>0<td>2^2^0=2^1=2</tr>
     * <tr><td>1<td>2^2^1=2^2=4</tr>
     * <tr><td>2<td>2^2^2=2^4=16</tr>
     * <tr><td>3<td>2^2^3=2^8=256</tr>
     * <tr><td>4<td>2^2^4=2^16=65536</tr>
     * </table>
     * <p>
     * Puny long runs out at N=6, puny double rounds at N=6. BigInteger goes to 9.
     */
    public static final BigInteger[] LOOKUP_2_2_N = new BigInteger[]{new BigInteger("2"),new BigInteger("4"),
            new BigInteger("16"),new BigInteger("256"),new BigInteger("65536"),new BigInteger("4294967296"),
            new BigInteger("18446744073709551616"),new BigInteger("340282366920938463463374607431768211456"),
            new BigInteger("115792089237316195423570985008687907853269984665640564039457584007913129639936")};

    /**
     * Get the useed value of a given exponent size es.
     * <p>
     * Useed is a scaling component equal to 2^2^es.
     * <p>
     * Implementation detail.
     * Consider making es part of factory method or class/static value.
     */
    public static BigInteger getUseed(int es) {
        if (es < 0) {
            return BigInteger.ZERO;
        }
        if (es < LOOKUP_2_2_N.length) {
            return LOOKUP_2_2_N[es];
        }
        // Perform a loop of squaring 2, n times.
        BigInteger previous = BigInteger.valueOf(2);
        for (int i = 0; i < es; i++) {
            previous = previous.pow(2);
        }
        return previous;
    }

}