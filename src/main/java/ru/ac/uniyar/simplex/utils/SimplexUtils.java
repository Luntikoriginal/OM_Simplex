package ru.ac.uniyar.simplex.utils;

import javafx.geometry.Point2D;
import ru.ac.uniyar.simplex.domain.Fraction;
import ru.ac.uniyar.simplex.domain.SimplexEntity;
import ru.ac.uniyar.simplex.domain.TaskEntity;
import ru.ac.uniyar.simplex.exceptions.FractionCreateException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static ru.ac.uniyar.simplex.utils.FractionUtils.*;

public class SimplexUtils {

    public static void createST(Fraction[][] gMatrix, Fraction[] originalFunc, String taskType, SimplexEntity se) throws FractionCreateException {
        Fraction[][] sT = new Fraction[se.getBV().size() + 1][se.getFV().size() + 1];
        readMatrix(gMatrix, se, sT);
        if (taskType.equals("max")) {
            Fraction[] convertedFunc = convertFunc(originalFunc);
            solveFunc(convertedFunc, se, sT);
        } else solveFunc(originalFunc, se, sT);
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
        swapBF(newSE.getBV(), newSE.getFV(), rs);
        newSE.getST()[(int) rs.getX()][(int) rs.getY()].swapND();
        multiplyRow(newSE.getST()[(int) rs.getX()], (int) rs.getY());
        Fraction[] supColumn = deepCopyColumn(newSE.getST(), (int) rs.getY());
        multiplyColumn(newSE.getST(), rs);
        differenceRows(newSE.getST(), supColumn, rs);
        reSolveBases(newSE);
        findPossibleFields(newSE);
        return newSE;
    }

    private static void swapBF(ArrayList<Integer> bV, ArrayList<Integer> fV, Point2D rs) {
        int bIndex = (int) rs.getX();
        int fIndex = (int) rs.getY();
        if (bIndex >= 0 && bIndex < bV.size() && fIndex >= 0 && fIndex < fV.size()) {
            int temp = bV.get(bIndex);
            bV.set(bIndex, fV.get(fIndex));
            fV.set(fIndex, temp);
        }
    }

    private static void multiplyRow(Fraction[] row, int element) throws FractionCreateException {
        for (int i = 0; i < row.length; i++) {
            if (i != element)
                row[i] = multiplication(row[i], row[element]);
        }
    }

    private static Fraction[] deepCopyColumn(Fraction[][] table, int columnIndex) throws FractionCreateException {
        Fraction[] savedColumn = new Fraction[table.length];
        for (int i = 0; i < table.length; i++) {
            savedColumn[i] = new Fraction(table[i][columnIndex].getNumerator(), table[i][columnIndex].getDenominator());
        }
        return savedColumn;
    }

    private static void multiplyColumn(Fraction[][] table, Point2D rs) throws FractionCreateException {
        for (int i = 0; i < table.length; i++) {
            if (i != rs.getX()) {
                Fraction negative = table[(int) rs.getX()][(int) rs.getY()].multiply(-1);
                table[i][(int) rs.getY()] = multiplication(table[i][(int) rs.getY()], negative);
            }
        }
    }

    private static void differenceRows(Fraction[][] table, Fraction[] supColumn, Point2D rs) throws FractionCreateException {
        int rIndex = (int) rs.getX();
        int cIndex = (int) rs.getY();
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++) {
                if (i != rIndex && j != cIndex) {
                    Fraction multiF = multiplication(table[rIndex][j], supColumn[i]);
                    table[i][j] = difference(table[i][j], multiF);
                }
            }
        }
    }

    private static void reSolveBases(SimplexEntity se) throws FractionCreateException {
        for (Map.Entry<Integer, Fraction> entry : se.getBase().entrySet()) {
            if (se.getFV().contains(entry.getKey())) {
                entry.setValue(new Fraction(0, 1));
            } else {
                entry.setValue(se.getST()[se.getBV().indexOf(entry.getKey())][se.getFV().size()]);
            }
        }
    }

    public static Fraction[] convertFunc(Fraction[] original) throws FractionCreateException {
        Fraction[] convertedFunc = new Fraction[original.length];
        for (int i = 0; i < original.length; i++) {
            convertedFunc[i] = original[i].multiply(-1);
        }
        return convertedFunc;
    }

    public static void solveFunc(Fraction[] originalFunc, SimplexEntity se, Fraction[][] sT) throws FractionCreateException {
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

    private static void readMatrix(Fraction[][] gMatrix, SimplexEntity se, Fraction[][] sT) {
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
