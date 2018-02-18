package javax.lang.posit;

import java.math.BigInteger;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Posit elements that are immutable or generally not changed.
 * Typically these are data of the Posit class or type.
 * <p>
 * In the paper "Posit Arithmetic", 2017-10-10, Gustafson refers to this as the
 * Posit Environment. 
 * <p>
 * The following elements are generally not changed in an instance of Posit.
 * <ul>
 * <li>nBits - number of bits in this Posit
 * <li>maxEs - maximum exponent size
 * </ul>
 * <p>
 * The following elements are derived from the above fields.
 * <ul>
 * <li>containerBits - this machine, this languages smallest integer that can hold this Posit.
 * <li>containerClass - this language class representation of this Posit
 * <li>useed = 2^2^es
 * <li>nPat = number of patterns = 2^nbits
 * <li>minPos, maxPos = useed^(-nbits+2), useed^(nbits-2)
 * <li>qSize = quireSize = 2^(Log(2,(nbits-2)*2^(es+2)+5)
 * <li>qExtra = quireExtra = quireSize - (nbits-2) * 2^(es+2) 
 * </ul>
 * <p>
 * Since the environment is shared by all Posits of a particular
 * bit and exponent size, this class caches all environments
 * ikn a REGISTRY with key of (nbits,es) and value of PositEnvironment.
 *
 * @see Posit
 * @author <a href="mailto://dan@danbecker.info">Dan Becker</a>
 */
public final class PositEnv implements Comparable<PositEnv> {
    /** This is a registry of immutable classes that all instances can share. */
    private static ConcurrentMap<PositEnv.KeyPair,PositEnv> REGISTRY = new ConcurrentHashMap<>();

    private byte nBits;
    private byte maxEs;
    
    private final long containerBits;
    private final Class<?> containerClass;

    private final BigInteger useed;
    private final BigInteger nPat;
    private final BigInteger minPos;
    private final BigInteger maxPos;
    private long qSize;
    private long qExtra;

    // Constructors
    @SuppressWarnings("unused")
    private PositEnv() {
        throw new AssertionError();
    }

    /** Returns a singleton PositEnv for this {bits,maxExponentSize}.
     * The PositEnv is constructed if it is not in the REGISTRY.
     * (This implementation is similar to REGISTRY.computeIfAbsent().)
     * @param nBits
     * @param maxEs
     * @return
     */
    public static PositEnv getPositEnv(byte nBits, byte maxEs) {
        PositEnv.KeyPair key = new PositEnv.KeyPair(nBits,maxEs);
        PositEnv oldValue = REGISTRY.get(key);
        if (null==oldValue) {
            PositEnv newValue = new PositEnv(nBits,maxEs);
            oldValue = REGISTRY.putIfAbsent(key, newValue);
            return null == oldValue ? newValue : oldValue;
        }
        return oldValue;
    }
       
    /** Returns PositEnv.REGISTRY.size()     */
    public static int getRegistrySize() {
        return REGISTRY.size();
    }
       
    /**
     * Instantiates a new Posit from the bit and max exponent size.
     * <p>
     * To share instances of this instance, and not generate one of these
     * for each Posit instance, always use the REGISTRY method getEnv
     * before instantiating a new PositEnv
     * @see PositEnv#getEnv.
     *  
     * @param nBits
     * @param maxEs
     */
    public PositEnv(byte nBits, byte maxEs) {
        // check inputs - bytes are >= 0.
        this.nBits = nBits;
        this.maxEs = maxEs;        
        
        // Perform one time calculations
        this.containerBits = getContainerSize( nBits ); // find nearest platform size
        this.containerClass = getContainerClass( nBits ); // find nearest platform object
        
        this.useed = getUseed( nBits ); 
        this.nPat = getNumPat(nBits); 
        this.minPos = getMinPos(useed,nBits);
        this.maxPos = getMaxPos(useed,nBits);
        this.qSize = getQuireSize(nBits, maxEs);
        this.qExtra = getQuireExtra(qSize, nBits, maxEs);
    }

    // Posit domain interface
    public int getBitSize() {
        return nBits;
    }
    public int getMaxExponentSize() {
        return maxEs;
    }
    public long getContainerBitSize() {
        return containerBits;
    }
    public Class<?> getContainerClass() {
        return containerClass;
    }    
    public final BigInteger getUseed() {
        return useed;
    }
    public final BigInteger getNumberPatterns() {
        return nPat;
    }
    public final BigInteger getMinPos() {
        return minPos;
    }
    public final BigInteger getMaxPos() {
        return maxPos;
    }
    public final long getQuireSize() {
        return qSize;
    }
    public final long getQuireExtra() {
        return qExtra;
    }
        
    // Object methods
    /**
     * Returns the hash code value for this map entry.
     */
    @Override
    public int hashCode() {
        return 1023 * nBits + maxEs;
    }

    /**
     * Compares the specified object with this entry for equality.
     */
    @Override
    public boolean equals(final Object other) {
        if (other instanceof PositEnv) {
            return equals((PositEnv) other);
        }
        return false;
    }
    
    /**
     * Compares the specified object with this entry for equality.
     */
    public boolean equals(final PositEnv other) {
       return nBits == other.nBits && maxEs == other.maxEs;
    }
    
    @Override
    public String toString() {
        return "PositEnv: bits=" + nBits + ", maxEs=" + maxEs;
    }

    // Comparable
    @Override
    public int compareTo(PositEnv other) {
        if ( this.nBits == other.nBits) {
            return this.maxEs - other.maxEs;
        }
        return this.nBits - other.nBits;
    }

    // Posit domain
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
     * Get the the number of bits needed to contain this Posit.
     * <p>
     * This is dependent on machine and language types.
     * Generally, this is the smallest power of 2 larger or equal than nBits.
     */
    public static long getContainerSize(byte nBits) {
        if (nBits <= 8) {
            return 8;
        } else if (nBits <= 16) {
            return 16;
        } else if (nBits <= 32) {
            return 32;
        } else if (nBits <= 64) {
            return 64;
        } else if (nBits <= 128) {
            return 128;
        } else if (nBits <= 256) {
            return 256;
        } else {
            throw new IllegalArgumentException( "nBits=" + nBits + "is too large for this implementation");
        }
    }
    
    /**
     * Get the the number of bits needed to contain this Posit.
     * <p>
     * This is dependent on machine and language types.
     * Generally, this is the smallest power of 2 larger or equal than nBits.
     */
    public static Class<?> getContainerClass(byte nBits) {
        if (nBits <= 8) {
            return Byte.class;
        } else if (nBits <= 16) {
            return Short.class;
        } else if (nBits <= 32) {
            return Integer.class;
        } else if (nBits <= 64) {
            return Long.class;
        } else if (nBits <= 256) {
            return String.class;
        } else {
            throw new IllegalArgumentException( "nBits=" + nBits + "is too large for this implementation");
        }
    }
        
    /**
     * Get the number of patterns, this is 2^nBits
     */
    public static BigInteger getNumPat(byte nBits) {
        return (new BigInteger("2")).pow(nBits);
    }

    /**
     * Get the minimum positive number, this is useed^(-nBits+2)
     */
    public static BigInteger getMinPos(BigInteger useed, byte nBits) {
        int power = -nBits + 2;
        if ( power >= 0) {
            return useed.pow(power);
        } else {
            return BigInteger.ONE.divide( useed.pow( -power ));            
        }
    }
    
    /**
     * Get the maximum positive number, this is useed^(nBits-2)
     */
    public static BigInteger getMaxPos(BigInteger useed, byte nBits) {
        int power = nBits - 2;
        if ( power >= 0) {
            return useed.pow(power);
        } else {
            return BigInteger.ONE.divide( useed.pow( -power ));            
        }
    }
    
    /**
     * Get the quire size in bits, quireSize = 2^(Log(2,(nbits-2)*2^(es+2)+5)
     * <p>
     * A lookup for common posit nbits,es is given on page 83 of "Posit Arithmetic".
     * 8,0=>64,38,
     * 16,1=>256,143
     * 32,2=>512,30
     * 64,3=>2048,62
     * 128,4=>8192,126
     * 256,5=>32k,254
     */
    public static long getQuireSize(byte nBits, byte maxEs) {
        // long power = Bit.pow(2, maxEs+2);
        // long range = (nBits -2) * (power +5);
        // double log2 = Math.log(range) / Math.log( 2.0 ); 
        // long quireBits = 
        // return quireBits
        // Cheat for now
        if (nBits <= 8) {
            return 64;
        } else if (nBits <= 16) {
            return 256;
        } else if (nBits <= 32) {
            return 512;
        } else if (nBits <= 64) {
            return 2048;
        } else if (nBits <= 128) {
            return 8192;
        } else if (nBits <= 256) {
            return 32768;
        } else {
            throw new IllegalArgumentException( "nBits=" + nBits + "is too large for this implementation");
        }
        
    }
    
    /**
     * Get the quire extra size in bits, quireSize - (nbits-2) * 2^(es+2)
     */
    public static long getQuireExtra( long quireSize, byte nBits, byte maxEs) {
        return quireSize - (nBits-2) * Bit.pow(2, maxEs+2);
    }
    

    /**
     * Defines a pair of keys (based on bits and maxExponentSize) that uniquely identify PositEnv characteristics. 
     */
    public static class KeyPair {
        private byte nBits;
        private byte maxEs;

        @SuppressWarnings("unused")
        private KeyPair() {
            throw new AssertionError();
        }

        public KeyPair( byte nBits, byte maxEs ) {
            this.nBits = nBits;
            this.maxEs = maxEs;
        }
        
        public byte getBitSize() {
            return nBits;
        }
        public byte getMaxEs() {
            return maxEs;
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
            return nBits == other.nBits && maxEs == other.maxEs;
        }

        /**
         * Returns the hash code value for this map entry.
         */
        public int hashCode() {
            return 1023 * nBits + maxEs;
        }
        
        @Override
        public String toString() {
            return "PositEnv.KeyPair: bits=" + nBits + ", maxEs=" +maxEs;
        }
        
        // Comparable
        public int compareTo(KeyPair other) {
            if ( this.nBits == other.nBits) {
                return this.maxEs - other.maxEs;
            }
            return this.nBits - other.nBits;
        }
    } // KeyPair
}