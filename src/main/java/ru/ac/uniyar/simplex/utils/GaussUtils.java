package ru.ac.uniyar.simplex.utils;

import ru.ac.uniyar.simplex.domain.Fraction;
import ru.ac.uniyar.simplex.exceptions.FractionCreateException;

import java.util.ArrayList;

public class GaussUtils {

    public static Fraction[][] gauss(Fraction[][] originalMatrix, ArrayList<Integer> bases) throws FractionCreateException {
        int rows = originalMatrix.length;
        int cols = originalMatrix[0].length;

        Fraction[][] newMatrix = deepCopyMatrix(originalMatrix);

        for (int i = 0; i < rows; i++) {
            int baseIndex = bases.get(i) - 1;
            Fraction pivot = newMatrix[i][baseIndex];

            if (pivot.getNumerator() != 0) {
                divisionRow(newMatrix, cols, pivot, i);
            }

            differenceLines(newMatrix, rows, i, baseIndex, cols);
        }
        return newMatrix;
    }

    private static Fraction[][] deepCopyMatrix(Fraction[][] originalMatrix) throws FractionCreateException {
        Fraction[][] newMatrix = new Fraction[originalMatrix.length][];
        for (int i = 0; i < originalMatrix.length; i++) {
            newMatrix[i] = new Fraction[originalMatrix[i].length];
            for (int j = 0; j < originalMatrix[i].length; j++) {
                newMatrix[i][j] = new Fraction(originalMatrix[i][j].getNumerator(), originalMatrix[i][j].getDenominator());
            }
        }
        return newMatrix;
    }

    private static void divisionRow(Fraction[][] matrix, int cols, Fraction pivot, int i) throws FractionCreateException {
        for (int j = 0; j < cols; j++) {
            matrix[i][j] = FractionUtils.division(matrix[i][j], pivot);
        }
    }

    private static void differenceLines(Fraction[][] matrix, int rows, int i, int baseIndex, int cols) throws FractionCreateException {
        for (int k = 0; k < rows; k++) {
            if (k != i) {
                Fraction factor = matrix[k][baseIndex];
                for (int j = 0; j < cols; j++) {
                    matrix[k][j] = FractionUtils.difference(matrix[k][j], FractionUtils.multiplication(factor, matrix[i][j]));
                }
            }
        }
    }

    private static void printResult(Fraction[][] matrix, ArrayList<Integer> bases) throws FractionCreateException {

        if (isCompatibility(matrix)) return;

        for (int i = 0; i < bases.size(); i++) {
            Fraction constant = matrix[i][matrix[i].length - 1];
            StringBuilder builder = new StringBuilder("X" + bases.get(i) + " = ");

            buildResultLine(matrix, i, bases, builder, constant);
        }
    }

    private static void buildResultLine(Fraction[][] matrix, int i, ArrayList<Integer> basesColumns, StringBuilder builder, Fraction constant) throws FractionCreateException {
        for (int j = 0; j < matrix[i].length - 1; j++) {
            if (!basesColumns.contains(j + 1)) {
                Fraction coefficient = matrix[i][j];
                if (coefficient.getNumerator() < 0) {
                    builder.append("+ ").append(coefficient.abs()).append("X").append(j + 1).append(" ");
                } else {
                    builder.append("- ").append(coefficient.abs()).append("X").append(j + 1).append(" ");
                }
            }
        }
        if (constant.getNumerator() > 0) {
            builder.append("+ ").append(constant.abs());
        } else {
            builder.append("- ").append(constant.abs());
        }
    }

    private static boolean isCompatibility(Fraction[][] matrix) {
        for (Fraction[] row : matrix) {
            boolean isNonZeroRow = true;
            for (int i = 0; i < row.length - 1; i++) {
                if (row[i].getNumerator() != 0) {
                    isNonZeroRow = false;
                    break;
                }
            }
            if (isNonZeroRow && row[row.length - 1].getNumerator() != 0) {
                return true;
            }
        }
        return false;
    }
}
