package javax.lang.posit;

/*
* Unary operation signatures
* @author <a href="mailto://dan@danbecker.info">Dan Becker</a>
*/
public interface UnaryOperations {
    /**
     * Reciprocal or 1/x
     *
     * @param x
     *            input value
     * @return reciprocal or /x
     */
    Posit recip(final Posit x);

    /**
     * Square root or √x
     *
     * @param x
     *            input value
     * @return square root or √x
     */
    Posit sqrt(final Posit x);

    /**
     * Square or x²
     *
     * @param x
     *            input value
     * @return square or x^2
     */
    Posit square(final Posit x);

    /**
     * Log₂x
     *
     * @param x
     *            input value
     * @retun log2x
     */
    Posit log2(final Posit x);

    /**
     * Power of 2 or 2^x
     *
     * @param x
     *            input value
     * @return power or 2^x
     */
    Posit pow2(final Posit x);

}
