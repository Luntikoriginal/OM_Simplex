package ru.ac.uniyar.simplex.domain;

import ru.ac.uniyar.simplex.exceptions.FractionCreateException;

public class Fraction {

    private int numerator;
    private int denominator;

    public Fraction() {}

    public Fraction(int numerator, int denominator) throws FractionCreateException {
        if (denominator == 0) throw new FractionCreateException("Знаменатель дроби не может быть равен 0");
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public void reduction() {
        if ((numerator < 0 && denominator < 0) || (numerator > 0 && denominator < 0)) {
            numerator *= -1;
            denominator *= -1;
        }
        for (int i = 2; i <= Math.abs(numerator); i++) {
            if (numerator % i == 0 && denominator % i == 0) {
                numerator /= i;
                denominator /= i;
                i = 1;
            }
        }
    }

    public void multiple(int i) {
        numerator *= i;
        denominator *= i;
    }

    public Fraction multiply(int i) throws FractionCreateException {
        int newNumerator = numerator * i;
        return new Fraction(newNumerator, denominator);
    }

    @Override
    public String toString() {
        if (numerator == 0) {
            return "0";
        } else if (denominator == 1) {
            return String.valueOf(numerator);
        } else {
            return numerator + "/" + denominator;
        }
    }

    public int getNumerator() {
        return numerator;
    }

    public void setNumerator(int numerator) {
        this.numerator = numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    public void setDenominator(int denominator) {
        this.denominator = denominator;
    }

    public Fraction abs() throws FractionCreateException {
        return new Fraction(Math.abs(numerator), denominator);
    }
}
