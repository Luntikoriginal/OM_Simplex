package ru.ac.uniyar.simplex.utils;

import ru.ac.uniyar.simplex.domain.Fraction;

import java.util.ArrayList;

public class GaussUtils {

    /**
     * Метод преобразования минора к диагональному виду методом Гаусса.
     * По очереди берет базисный элемент, делит на него строку и вычитает
     * получившуюся строку из всех других необходимое кол-во раз.
     *
     * @param matrix матрица, в которой выполняются преобразования
     * @param bases  массив с базисными переменными
     */
    public static void gauss(Fraction[][] matrix, ArrayList<Integer> bases) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        for (int i = 0; i < rows; i++) {
            int baseIndex = bases.get(i) - 1;
            Fraction pivot = matrix[i][baseIndex];

            if (pivot.getNumerator() != 0) {
                divisionRow(matrix, cols, pivot, i);
            }

            differenceLines(matrix, rows, i, baseIndex, cols);
        }

        printResult(matrix, bases);
    }

    private static void divisionRow(Fraction[][] matrix, int cols, Fraction pivot, int i) {
        for (int j = 0; j < cols; j++) {
            matrix[i][j] = FractionUtils.division(matrix[i][j], pivot);
            matrix[i][j].reduction();
        }
    }

    private static void differenceLines(Fraction[][] matrix, int rows, int i, int baseIndex, int cols) {
        for (int k = 0; k < rows; k++) {
            if (k != i) {
                Fraction factor = matrix[k][baseIndex];
                for (int j = 0; j < cols; j++) {
                    matrix[k][j] = FractionUtils.difference(matrix[k][j], FractionUtils.multiplication(factor, matrix[i][j]));
                    matrix[k][j].reduction();
                }
            }
        }
    }

    /**
     * Печатает результат программы, выражая базисные переменные.
     * Дополнительно проверят совместность системы.
     *
     * @param matrix полученная в ходе программы матрица
     * @param bases  массив базисных переменных
     */
    private static void printResult(Fraction[][] matrix, ArrayList<Integer> bases) {
        System.out.println("\nОтвет: ");

        if (isCompatibility(matrix)) return;

        for (int i = 0; i < bases.size(); i++) {
            Fraction constant = matrix[i][matrix[i].length - 1];
            StringBuilder builder = new StringBuilder("X" + bases.get(i) + " = ");

            buildResultLine(matrix, i, bases, builder, constant);

            System.out.println(builder);
        }
    }

    private static void buildResultLine(Fraction[][] matrix, int i, ArrayList<Integer> basesColumns, StringBuilder builder, Fraction constant) {
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
                System.out.println("Система уравнений несовместна.");
                return true;
            }
        }
        return false;
    }
}
