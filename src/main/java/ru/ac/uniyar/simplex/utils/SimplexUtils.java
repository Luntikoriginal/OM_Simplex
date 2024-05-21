package ru.ac.uniyar.simplex.utils;

import javafx.geometry.Point2D;
import ru.ac.uniyar.simplex.domain.Fraction;
import ru.ac.uniyar.simplex.domain.SimplexEntity;
import ru.ac.uniyar.simplex.domain.TaskEntity;
import ru.ac.uniyar.simplex.exceptions.FractionCreateException;

import java.util.ArrayList;
import java.util.HashMap;

import static ru.ac.uniyar.simplex.utils.FractionUtils.amount;
import static ru.ac.uniyar.simplex.utils.FractionUtils.multiplication;

public class SimplexUtils {

    public static void createST(Fraction[][] gMatrix, Fraction[] originalFunc, SimplexEntity se) throws FractionCreateException {
        Fraction[][] sT = new Fraction[se.getBV().size() + 1][se.getFV().size() + 1];
        readGMatrix(gMatrix, se, sT);
        solveFunc(originalFunc, se, sT);
        se.setST(sT);
    }

    public static void createFVAndBases(TaskEntity task, Fraction[][] gMatrix, SimplexEntity se) throws FractionCreateException {
        ArrayList<Integer> fV = new ArrayList<>();
        HashMap<Integer, Fraction> base = new HashMap<>();
        int k = 0;
        for (int i = 1; i <= task.getVariables(); i++) {
            if (!se.getBV().contains(i)) {
                fV.add(i);
                base.put(i, new Fraction(0, 1));
            } else {
                base.put(i, gMatrix[k][task.getVariables()]);
                k++;
            }
        }
        se.setFV(fV);
        se.setBase(base);
    }

    public static void findPossibleFields(SimplexEntity se) throws FractionCreateException {
        ArrayList<Point2D> pF = new ArrayList<>();
        int rows = se.getST().length;
        int columns = se.getST()[0].length;
        ArrayList<Integer> possibleColumns = new ArrayList<>();
        for (int i = 0; i < columns - 1; i++) {
            if (se.getST()[rows - 1][i].getNumerator() < 0)
                possibleColumns.add(i);
        }
        for (int i : possibleColumns) {
            Point2D minPoint = null;
            Fraction minFraction = null;
            for (int j = 0; j < rows - 1; j++) {
                if (se.getST()[j][i].getNumerator() > 0) {
                    Fraction fraction = FractionUtils.division(se.getST()[j][columns - 1], se.getST()[j][i]);
                    if (minFraction == null) {
                        minFraction = fraction;
                        minPoint = new Point2D(j, i);
                    } else {
                        FractionUtils.toCommonDenominator(fraction, minFraction);
                        if (fraction.getNumerator() < minFraction.getNumerator()) {
                            minPoint = new Point2D(j, i);
                            minFraction = fraction;
                            minFraction.reduction();
                        }
                    }
                }
            }
            if (minPoint != null) pF.add(minPoint);
        }
        se.setPF(pF);
    }

    public static SimplexEntity step(SimplexEntity oldSE, Point2D rs) throws FractionCreateException {
        SimplexEntity newSE = new SimplexEntity(oldSE);

        return newSE;
    }

    private static void solveFunc(Fraction[] originalFunc, SimplexEntity se, Fraction[][] sT) throws FractionCreateException {
        for (int i = 0; i < sT[se.getBV().size()].length; i++) {
            Fraction sum = new Fraction(0, 1);
            if (i != sT[se.getBV().size()].length - 1) {
                for (int j = 0; j < sT.length - 1; j++) {
                    sum = amount(sum, multiplication(sT[j][i].multiply(-1), originalFunc[se.getBV().get(j) - 1]));
                }
                sT[se.getBV().size()][i] = amount(originalFunc[se.getFV().get(i) - 1], sum);
            } else {
                for (int j = 0; j < sT.length - 1; j++) {
                    sum = amount(sum, multiplication(sT[j][i], originalFunc[se.getBV().get(j) - 1]));
                }
                sT[se.getBV().size()][i] = sum.multiply(-1);
            }
        }
    }

    private static void readGMatrix(Fraction[][] gMatrix, SimplexEntity se, Fraction[][] sT) {
        for (int i = 0; i < gMatrix.length; i++) {
            int k = 0;
            for (int j = 0; j < gMatrix[i].length; j++) {
                if (!se.getBV().contains(j + 1)) {
                    sT[i][k] = gMatrix[i][j];
                    k++;
                }
            }
        }
    }
}
