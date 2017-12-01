package javax.lang.posit;

/**
 * PositEnum
 * <p>
 * This enum is used to enumerate components of a Posit number
 * <p>
 * <ul>
 * <li>sign - sign of the number (first bit)
 * <li>regime - regime of the number (second bit to end or first flip)
 * <li>exponent - exponent of the number ( rs + 1 bit to end or esMax)
 * <li>fraction - fraction ( rs + es to end )
 * </ul>
 *
 * @author <a href="mailto://dan@danbecker.info>Dan Becker</a>
 */
public enum PositEnum {
    SIGN(0),
    REGIME(1),
    EXPONENT(2),
    FRACTION(3);

    private final int value;

    PositEnum(int value) {
        this.value = value;
    }

    // @Override
    public int v() {
        return value;
    }
}
