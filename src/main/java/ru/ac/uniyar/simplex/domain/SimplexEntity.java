package ru.ac.uniyar.simplex.domain;

import ru.ac.uniyar.simplex.exceptions.FractionCreateException;
import ru.ac.uniyar.simplex.utils.FractionUtils;

import java.util.ArrayList;
import java.util.HashMap;

import static ru.ac.uniyar.simplex.utils.FractionUtils.amount;
import static ru.ac.uniyar.simplex.utils.FractionUtils.multiplication;

public class SimplexEntity {

    private ArrayList<Integer> bV;  //basesVars

    private ArrayList<Integer> fV;  //freeVars

    private Fraction[][] sT;  //simplexTable

    private HashMap<Integer, Fraction> base;

    public SimplexEntity(TaskEntity task, Fraction[][] gMatrix) throws FractionCreateException {
        bV = new ArrayList<>(task.getBases());
        fV = new ArrayList<>();
        base = new HashMap<>();
        int k = 0;
        for (int i = 1; i <= task.getVariables(); i++) {
            if (!bV.contains(i)) {
                fV.add(i);
                base.put(i, new Fraction(0, 1));
            } else {
                base.put(i, gMatrix[k][task.getVariables()]);
                k++;
            }

        }
        createST(gMatrix, task.getFunction());
    }

    private void createST(Fraction[][] gMatrix, Fraction[] originalFunc) throws FractionCreateException {
        sT = new Fraction[bV.size() + 1][fV.size() + 1];
        for (int i = 0; i < gMatrix.length; i++) {
            int k = 0;
            for (int j = 0; j < gMatrix[i].length; j++) {
                if (!bV.contains(j + 1)) {
                    sT[i][k] = gMatrix[i][j];
                    k++;
                }
            }
        }
        for (int i = 0; i < sT[bV.size()].length; i++) {
            Fraction sum = new Fraction(0, 1);
            if (i != sT[bV.size()].length - 1) {
                for (int j = 0; j < sT.length - 1; j++) {
                    sum = amount(sum, multiplication(sT[j][i].multiply(-1), originalFunc[bV.get(j) - 1]));
                }
                sT[bV.size()][i] = amount(originalFunc[fV.get(i) - 1], sum);
            } else {
                for (int j = 0; j < sT.length - 1; j++) {
                    sum = amount(sum, multiplication(sT[j][i], originalFunc[bV.get(j) - 1]));
                }
                sT[bV.size()][i] = sum.multiply(-1);
            }
        }
    }

    public ArrayList<Integer> getBV() {
        return bV;
    }

    public void setBV(ArrayList<Integer> bV) {
        this.bV = bV;
    }

    public ArrayList<Integer> getFV() {
        return fV;
    }

    public void setFV(ArrayList<Integer> fV) {
        this.fV = fV;
    }

    public Fraction[][] getST() {
        return sT;
    }

    public void setST(Fraction[][] sT) {
        this.sT = sT;
    }

    public HashMap<Integer, Fraction> getBase() {
        return base;
    }

    public void setBase(HashMap<Integer, Fraction> base) {
        this.base = base;
    }
}
