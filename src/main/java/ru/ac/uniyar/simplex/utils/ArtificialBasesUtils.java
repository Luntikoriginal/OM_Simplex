package ru.ac.uniyar.simplex.utils;

import ru.ac.uniyar.simplex.domain.Fraction;
import ru.ac.uniyar.simplex.domain.SimplexEntity;
import ru.ac.uniyar.simplex.exceptions.FractionCreateException;

import java.util.HashMap;

import static ru.ac.uniyar.simplex.utils.FractionUtils.amount;
import static ru.ac.uniyar.simplex.utils.SimplexUtils.findPossibleFields;

public class ArtificialBasesUtils {

    public static void createAB(Fraction[][] matrix, SimplexEntity se) throws FractionCreateException {
        Fraction[][] sT = new Fraction[se.getBV().size() + 1][se.getFV().size() + 1];
        readMatrix(matrix, sT);
        checkB(sT);
        negativeAmountColumn(sT);
        se.setST(sT);
    }

    public static void createBases(SimplexEntity se) throws FractionCreateException {
        HashMap<Integer, Fraction> base = new HashMap<>();
        for (int i = 1; i <= se.getFV().size(); i++) {
            base.put(i, new Fraction(0, 1));
        }
        int k = 0;
        for (int i = se.getFV().size() + 1; i <= se.getFV().size() + se.getBV().size(); i++) {
            base.put(i, se.getST()[k][se.getST()[0].length - 1]);
            k++;
        }
        se.setBase(base);
    }

    public static void deleteColumn(SimplexEntity se, int column) throws FractionCreateException {
        Fraction[][] originalMatrix = se.getST();
        int rows = originalMatrix.length;
        int cols = originalMatrix[0].length;
        Fraction[][] newMatrix = new Fraction[rows][cols - 1];
        for (int i = 0; i < rows; i++) {
            int newColIndex = 0;
            for (int j = 0; j < cols; j++) {
                if (j != column) {
                    newMatrix[i][newColIndex] = originalMatrix[i][j];
                    newColIndex++;
                }
            }
        }
        se.setST(newMatrix);
        se.getBase().remove(se.getFV().get(column));
        se.getFV().remove(column);
        findPossibleFields(se);
    }

    private static void checkB(Fraction[][] matrix) throws FractionCreateException {
        for (int i = 0; i < matrix.length - 1; i++) {
            if (matrix[i][matrix[i].length - 1].getNumerator() < 0)
                matrix[i] = SimplexUtils.convertFunc(matrix[i]);
        }
    }

    private static void negativeAmountColumn(Fraction[][] matrix) throws FractionCreateException {
        for (int j = 0; j < matrix[0].length; j++) {
            Fraction sum = new Fraction(0, 1);
            for (int i = 0; i < matrix.length - 1; i++) {
                sum = amount(sum, matrix[i][j]);
            }
            matrix[matrix.length - 1][j] = sum.multiply(-1);
        }
    }

    private static void readMatrix(Fraction[][] matrix, Fraction[][] sT) {
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, sT[i], 0, matrix[i].length);
        }
    }
}
