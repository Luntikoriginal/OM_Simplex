package ru.ac.uniyar.simplex.utils;

import ru.ac.uniyar.simplex.domain.Fraction;
import ru.ac.uniyar.simplex.exceptions.FractionCreateException;

/**
 * Библиотека методов для работы с дробями.
 */
public class FractionUtils {

    public static void toCommonDenominator(Fraction a, Fraction b) {
        if (a.getDenominator() != b.getDenominator()) {
            int temp = a.getDenominator();
            a.multiple(b.getDenominator());
            b.multiple(temp);
        }
    }

    public static Fraction amount(Fraction a, Fraction b) throws FractionCreateException {
        toCommonDenominator(a, b);
        return new Fraction(a.getNumerator() + b.getNumerator(), a.getDenominator()).reduction();
    }

    public static Fraction difference(Fraction a, Fraction b) throws FractionCreateException {
        toCommonDenominator(a, b);
        return new Fraction(a.getNumerator() - b.getNumerator(), a.getDenominator()).reduction();
    }

    public static Fraction multiplication(Fraction a, Fraction b) throws FractionCreateException {
        return new Fraction(a.getNumerator() * b.getNumerator(),
                a.getDenominator() * b.getDenominator()).reduction();
    }

    public static Fraction division(Fraction a, Fraction b) throws FractionCreateException {
        return new Fraction(a.getNumerator() * b.getDenominator(),
                a.getDenominator() * b.getNumerator()).reduction();
    }
}
