package javax.lang.posit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.junit.Test;

/**
 * General test of this class.
 * <p>
 * Tests in this class should work with all implementations of Posits. More
 * specific tests may be in specific test implementations.
 *
 * @author <a href="mailto://dan@danbecker.info>Dan Becker</a>
 */
public class PositImmutableTest {
    @Test
    public void testKeyPair() {
        final PositImmutable.KeyPair key1 = new PositImmutable.KeyPair( (byte) 1, (byte) 2 );
        final PositImmutable.KeyPair key2 = new PositImmutable.KeyPair( (byte) 3, (byte) 6 );
        final PositImmutable.KeyPair key3 = new PositImmutable.KeyPair( (byte) 3, (byte) 6 );
        final PositImmutable.KeyPair key4 = new PositImmutable.KeyPair( (byte) 4, (byte) 8 );
        final PositImmutable.KeyPair key5 = new PositImmutable.KeyPair( (byte) 4, (byte) 10 );
        
        assertEquals( (byte) 1, key1.getBitSize() );
        assertEquals( (byte) 2, key1.getMaxExponentSize() );
        assertNotEquals( key1, null );
        assertNotEquals( key1, "foo" );
        
        assertEquals( key2, key3 );
        assertNotEquals( key1, key2 );
        assertNotEquals( key4, key5 );
        
        assertEquals( 0, key2.compareTo(key3) );
        assertTrue( key2.compareTo(key1) > 1 );
        assertTrue( key3.compareTo(key4) < 1 );
        assertTrue( key4.compareTo(key5) < 1 );

        assertNotEquals( key1.hashCode(), key2.hashCode() );
        assertEquals( key2.hashCode(), key3.hashCode() );
        
        assertTrue( key1.toString().contains("bits="));
        assertTrue( key1.toString().contains("maxEs="));        
    }
    
    @Test
    public void testPositImmutable() {
        final PositImmutable pi1 = new PositImmutable( (byte) 1, (byte) 2 );
        final PositImmutable pi2 = new PositImmutable( (byte) 3, (byte) 6 );
        final PositImmutable pi3 = new PositImmutable( (byte) 3, (byte) 6 );
        final PositImmutable pi4 = new PositImmutable( (byte) 4, (byte) 8 );
        final PositImmutable pi5 = new PositImmutable( (byte) 4, (byte) 10 );
        
        assertEquals( (byte) 1, pi1.getBitSize() );
        assertEquals( (byte) 2, pi1.getMaxExponentSize() );
        assertNotEquals( pi1, null );
        assertNotEquals( pi1, "foo" );
        
        assertEquals( pi2, pi3 );
        assertNotEquals( pi1, pi2 );
        assertNotEquals( pi4, pi5 );
        
        assertEquals( 0, pi2.compareTo(pi3) );
        assertTrue( pi2.compareTo(pi1) > 1 );
        assertTrue( pi3.compareTo(pi4) < 1 );
        assertTrue( pi4.compareTo(pi5) < 1 );

        assertNotEquals( pi1.hashCode(), pi2.hashCode() );
        assertEquals( pi2.hashCode(), pi3.hashCode() );
        
        assertTrue( pi1.toString().contains("bits="));
        assertTrue( pi1.toString().contains("maxEs="));
        
        // Useed
        assertEquals( BigInteger.ZERO, PositImmutable.getUseed(-1) );
        assertEquals( new BigInteger("2"), PositImmutable.getUseed(0) );
        assertEquals( new BigInteger("16"), PositImmutable.getUseed(2) );
        
    }
    
    @Test
    public void testPositImmutableRegistry() {
        assertTrue( 0 == PositImmutable.getRegistrySize());
        final PositImmutable pi1 = PositImmutable.getPositImmutable( (byte) 1, (byte) 2 );
        assertTrue( 1 == PositImmutable.getRegistrySize());
        final PositImmutable pi2 = PositImmutable.getPositImmutable( (byte) 3, (byte) 6 );
        assertTrue( 2 == PositImmutable.getRegistrySize());
        final PositImmutable pi3 = PositImmutable.getPositImmutable( (byte) 3, (byte) 6 );
        assertTrue( 2 == PositImmutable.getRegistrySize());
        
        assertEquals( pi1, PositImmutable.getPositImmutable( (byte) 1, (byte) 2 ) );
        assertEquals( pi2, pi3 );
        assertEquals( Integer.toHexString(pi2.hashCode()), Integer.toHexString(pi3.hashCode()) );
    }

}
