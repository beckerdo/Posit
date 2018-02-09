package javax.lang.posit;

/*
* Two argument operation signatures
* @author <a href="mailto://dan@danbecker.info">Dan Becker</a>
*/
public interface TwoArgOperations {

    /**
     * Add two posits
     *
     * @param x
     *            first parameter
     * @param y
     *            first parameter
     * @return x + y
     */
    Posit add(final Posit x, final Posit y);

    /**
     * Subtract two posits
     *
     * @param x
     *            first parameter
     * @param y
     *            first parameter
     * @return x - y
     */
    Posit sub(final Posit x, final Posit y);

    /**
     * Multiply two posits
     *
     * @param x
     *            first parameter
     * @param y
     *            first parameter
     * @return x * y
     */
    Posit mul(final Posit x, final Posit y);

    /**
     * Divide two posits
     *
     * @param x
     *            first parameter
     * @param y
     *            first parameter
     * @return x / y
     */
    Posit div(final Posit x, final Posit y);
}
