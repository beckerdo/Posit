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
 * @author <a href="mailto://dan@danbecker.info">Dan Becker</a>
 */
public class PositEnvTest {
    @Test
    public void testKeyPair() {
        final PositEnv.KeyPair key1 = new PositEnv.KeyPair( (byte) 1, (byte) 2 );
        final PositEnv.KeyPair key2 = new PositEnv.KeyPair( (byte) 3, (byte) 6 );
        final PositEnv.KeyPair key3 = new PositEnv.KeyPair( (byte) 3, (byte) 6 );
        final PositEnv.KeyPair key4 = new PositEnv.KeyPair( (byte) 4, (byte) 8 );
        final PositEnv.KeyPair key5 = new PositEnv.KeyPair( (byte) 4, (byte) 10 );
        
        assertEquals( (byte) 1, key1.getBitSize() );
        assertEquals( (byte) 2, key1.getMaxEs() );
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
    public void testPositEnv() {
        final PositEnv pi1 = new PositEnv( (byte) 2, (byte) 1 );
        final PositEnv pi2 = new PositEnv( (byte) 6, (byte) 3 );
        final PositEnv pi3 = new PositEnv( (byte) 6, (byte) 3 );
        final PositEnv pi4 = new PositEnv( (byte) 8, (byte) 4 );
        final PositEnv pi5 = new PositEnv( (byte) 10, (byte) 4 );
        
        assertEquals( (byte) 2, pi1.getBitSize() );
        assertEquals( (byte) 1, pi1.getMaxExponentSize() );
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
        assertEquals( BigInteger.ZERO, PositEnv.getUseed(-1) );
        assertEquals( new BigInteger("2"), PositEnv.getUseed(0) );
        assertEquals( new BigInteger("16"), PositEnv.getUseed(2) );
        
    }
    
    @Test
    public void testPositEnvRegistry() {
        assertTrue( 0 == PositEnv.getRegistrySize());
        final PositEnv pi1 = PositEnv.getPositEnv( (byte) 1, (byte) 2 );
        assertTrue( 1 == PositEnv.getRegistrySize());
        final PositEnv pi2 = PositEnv.getPositEnv( (byte) 3, (byte) 6 );
        assertTrue( 2 == PositEnv.getRegistrySize());
        final PositEnv pi3 = PositEnv.getPositEnv( (byte) 3, (byte) 6 );
        assertTrue( 2 == PositEnv.getRegistrySize());
        
        assertEquals( pi1, PositEnv.getPositEnv( (byte) 1, (byte) 2 ) );
        assertEquals( pi2, pi3 );
        assertEquals( Integer.toHexString(pi2.hashCode()), Integer.toHexString(pi3.hashCode()) );
    }

}
