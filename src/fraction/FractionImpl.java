package fraction;

import java.lang.ArithmeticException;

/**
 * Representation of a rational number, using Euclid's algorithm.
 *
 * @author Fernando Silva (fdealm02>)
 */
public class FractionImpl implements Fraction {

    private int numerator, denominator;

    /**
     * Creates a fraction from an int. The fraction is wholeNumber / 1.
     *
     * @param wholeNumber the numerator.
     */
    public FractionImpl(int wholeNumber) {
        this.numerator = wholeNumber;
        this.denominator = 1;
    }

    /**
     * Creates a fraction given the numerator and denominator. Fraction will be normalised to lowest terms with
     * Euclid's algorithm.
     *
     * @param numerator the numerator.
     * @param denominator the denominator.
     * @throws ArithmeticException if denominator is zero.
     */
    public FractionImpl(int numerator, int denominator) throws ArithmeticException {
        if (denominator == 0) {
            throw new ArithmeticException("Divide by zero");
        } else if (denominator < 0) {
            numerator *= -1;
            denominator *= -1;
        }

        if (numerator == 0) {
            this.numerator = 0;
            this.denominator = 1;
        } else {
            int common = gcd(Math.abs(numerator), denominator);
            this.numerator = numerator / common;
            this.denominator = denominator / common;
        }
    }

    /**
     * Creates a fraction from a String, with the format "numerator/denominator" or "whole number". Fraction will be
     * normalised to lowest terms with Euclid's algorithm.
     *
     * @param fraction the fraction (or whole number) as a String.
     * @throws ArithmeticException if denominator is zero.
     * @throws NumberFormatException if fraction is malformed.
     */
    public FractionImpl(String fraction) throws ArithmeticException, NumberFormatException {
        String[] parts = fraction.split("/");

        if (parts.length > 2) {
            throw new NumberFormatException("Fraction is malformed.");
        }

        int numerator = Integer.parseInt(parts[0].trim());
        if (parts.length == 1) {
            this.numerator = numerator;
            this.denominator = 1;
        } else {
            int denominator = Integer.parseInt(parts[1].trim());

            if (denominator == 0) {
                throw new ArithmeticException("Divide by zero");
            } else if (denominator < 0) {
                numerator *= -1;
                denominator *= -1;
            }

            if (numerator == 0) {
                this.numerator = 0;
                this.denominator = 1;
            } else {
                int common = gcd(Math.abs(numerator), denominator);
                this.numerator = numerator / common;
                this.denominator = denominator / common;
            }
        }
    }

    /**
     * Calculates the Greatest Common Divisor for two numbers using Euclid's algorithm.
     *
     * @param number1 the first number.
     * @param number2 the second number.
     * @return the Greatest Common Divisor.
     * @throws ArithmeticException if number1 or number2 are zero.
     */
    private int gcd(int number1, int number2) throws ArithmeticException {
        if (number1 == 0 || number2 == 0) throw new ArithmeticException("Numbers must be non-zero.");

        if (number1 == number2) {
            return number1;
        } else if (number2 > number1) {
            int temp = number1;
            number1 = number2;
            number2 = temp;
        }

        while (number2 != 0) {
            int temp = number2;
            number2 = number1 % number2;
            number1 = temp;
        }

        return number1;
    }

    /**
     * Adds the value of another fraction to this one.
     *
     * @param f the fraction to add.
     * @return a Fraction instance.
     */
    public Fraction add(Fraction f) {
        return null;
    }

    /**
     * Subtracts the value of another fraction from the value of this one.
     *
     * @param f the fraction to subtract.
     * @return a Fraction instance.
     */
    public Fraction subtract(Fraction f) {
        return null;
    }

    /**
     * Multiplies the value of another fraction with this one.
     *
     * @param f the fraction to multiply by.
     * @return a Fraction instance.
     */
    public Fraction multiply(Fraction f) {
        return null;
    }

    /**
     * Divides the value of this fraction by another.
     *
     * @param f the fraction to divide by.
     * @return a Fraction instance.
     */
    public Fraction divide(Fraction f) {
        return null;
    }

    /**
     * Returns the absolute value of this fraction.
     *
     * @return a Fraction instance.
     */
    public Fraction abs() {
        return null;
    }

    /**
     * Returns a fraction that has same numeric value of this fraction, but the opposite sign.
     *
     * @return a Fraction instance.
     */
    public Fraction negate() {
        return null;
    }

    /**
     * Returns the inverse of this fraction.
     *
     * @return a Fraction instance.
     */
    public Fraction inverse() {
        return null;
    }

    /**
     * Gets a hashCode for the fraction.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Test for the equality of two fractions. If the lowest term numerator and denominators are the same for both
     * fractions, the two fractions are considered to be equal.
     *
     * @param obj fraction to test for equality to this fraction.
     * @return true if two fractions are equal, false if object is null, not an instance of Fraction,
     *         or not equal to this fraction instance.
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Compares this Fraction to another object.
     *
     * @param o the fraction to compare to.
     * @return -1 if this is less than object, +1 if this is greater than object, 0 if they are equal.
     */
    @Override
    public int compareTo(Fraction o) {
        return 0;
    }

    /**
     * Returns the String representing this fraction, e.g, "num / dem" or just "num" if the denominator is one.
     *
     * @return a string representation of the fraction.
     */
    @Override
    public String toString() {
        return numerator + "/" + denominator;
    }
}
