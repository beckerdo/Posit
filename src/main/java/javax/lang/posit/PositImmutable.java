package javax.lang.posit;

import java.math.BigInteger;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Posit elements that are immutable or generally not changed.
 * Typically these are data of the Posit class or type.
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
 * <p>
 * A @see KeyPair class is defined to allow these PositImmutable
 * to be uniquely identified by bits and maxExponentSize
 *
 * @see Posit
 * @author <a href="mailto://dan@danbecker.info>Dan Becker</a>
 */
public final class PositImmutable implements Comparable<PositImmutable> {
    /** This is a registry of immutable classes that all instances can share. */
    private static ConcurrentMap<PositImmutable.KeyPair,PositImmutable> REGISTRY = new ConcurrentHashMap<>();

    private byte bits;
    private byte maxExponentSize;
    
    private final byte containerBits;
    private final Class<?> containerClass;

    private final BigInteger useed;

    // Constructors
    @SuppressWarnings("unused")
    private PositImmutable() {
        throw new AssertionError();
    }

    /** Returns a singleton PositImmutable for this {bits,maxExponentSize}.
     * The PositImmutable is constructed if it is not in the REGISTRY.
     * (This implementation is similar to REGISTRY.computeIfAbsent().)
     * @param bits
     * @param maxExponentSize
     * @return
     */
    public static PositImmutable getPositImmutable(byte bits, byte maxExponentSize) {
        PositImmutable.KeyPair key = new PositImmutable.KeyPair(bits,maxExponentSize);
        PositImmutable oldValue = REGISTRY.get(key);
        if (null==oldValue) {
            // PositImmutable newValue = mappingFunction.apply(key);
            PositImmutable newValue = new PositImmutable(bits,maxExponentSize);
            oldValue = REGISTRY.putIfAbsent(key, newValue);
            return null == oldValue ? newValue : oldValue;
        }
        return oldValue;
    }
       
    /** Returns PositImmutable.REGISTRY.size()     */
    public static int getRegistrySize() {
        return REGISTRY.size();
    }
       
    public PositImmutable(byte bits, byte maxExponentSize) {
        // check inputs
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
    /**
     * Returns the hash code value for this map entry.
     */
    @Override
    public int hashCode() {
        return 1023 * bits + maxExponentSize;
    }

    /**
     * Compares the specified object with this entry for equality.
     */
    @Override
    public boolean equals(final Object other) {
        if (other instanceof PositImmutable) {
            return equals((PositImmutable) other);
        }
        return false;
    }
    
    /**
     * Compares the specified object with this entry for equality.
     */
    public boolean equals(final PositImmutable other) {
       return bits == other.bits && maxExponentSize == other.maxExponentSize;
    }
    
    @Override
    public String toString() {
        return "PositImmutable: bits=" + bits + ", maxEs=" + maxExponentSize;
    }

    // Comparable
    @Override
    public int compareTo(PositImmutable other) {
        if ( this.bits == other.bits) {
            return this.maxExponentSize - other.maxExponentSize;
        }
        return this.bits - other.bits;
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

    /**
     * Defines a unique pair of keys based on bits and maxExponentSize. 
     */
    public static class KeyPair {
        private byte bits;
        private byte maxExponentSize;

        @SuppressWarnings("unused")
        private KeyPair() {
            throw new AssertionError();
        }

        public KeyPair( byte bits, byte maxExponentSize ) {
            this.bits = bits;
            this.maxExponentSize = maxExponentSize;
        }
        
        public byte getBitSize() {
            return bits;
        }
        public byte getMaxExponentSize() {
            return maxExponentSize;
        }
        
        /**
         * Compares the specified object with this entry for equality.
         */
        @Override
        public boolean equals(Object other) {
            if (other instanceof KeyPair) {
                return equals((KeyPair) other);
            }
            return false;            
        }

        /**
         * Compares the specified object with this entry for equality.
         */
        public boolean equals(KeyPair other) {
            return bits == other.bits && maxExponentSize == other.maxExponentSize;
        }

        /**
         * Returns the hash code value for this map entry.
         */
        public int hashCode() {
            return 1023 * bits + maxExponentSize;
        }
        
        @Override
        public String toString() {
            return "PositImmutable.KeyPair: bits=" + bits + ", maxEs=" +maxExponentSize;
        }
        
        // Comparable
        public int compareTo(KeyPair other) {
            if ( this.bits == other.bits) {
                return this.maxExponentSize - other.maxExponentSize;
            }
            return this.bits - other.bits;
        }
    } // KeyPair
}