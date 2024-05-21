package ru.ac.uniyar.simplex.domain;

import javafx.geometry.Point2D;
import ru.ac.uniyar.simplex.exceptions.FractionCreateException;
import ru.ac.uniyar.simplex.utils.SimplexUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SimplexEntity {

    private ArrayList<Integer> bV;  //basesVars
    private ArrayList<Integer> fV;  //freeVars
    private Fraction[][] sT;  //simplexTable
    private ArrayList<Point2D> pF; //possibleFields
    private HashMap<Integer, Fraction> base;

    public SimplexEntity(TaskEntity task, Fraction[][] gMatrix) throws FractionCreateException {
        bV = new ArrayList<>(task.getBases());
        SimplexUtils.createFVAndBases(task, gMatrix, this);
        SimplexUtils.createST(gMatrix, task.getFunction(), this);
        SimplexUtils.findPossibleFields(this);
    }

    public SimplexEntity(SimplexEntity oldSE) throws FractionCreateException {
        this.bV = new ArrayList<>(oldSE.bV);
        this.fV = new ArrayList<>(oldSE.fV);
        this.pF = new ArrayList<>(oldSE.pF);
        int rows = oldSE.sT.length;
        int cols = oldSE.sT[0].length;
        this.sT = new Fraction[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.sT[i][j] = new Fraction(oldSE.sT[i][j].getNumerator(), oldSE.sT[i][j].getDenominator());
            }
        }
        this.base = new HashMap<>();
        for (Map.Entry<Integer, Fraction> entry : oldSE.base.entrySet()) {
            this.base.put(entry.getKey(), new Fraction(entry.getValue().getNumerator(), entry.getValue().getDenominator()));
        }
    }

    public boolean containsPoint(int x, int y) {
        for (Point2D point : pF) {
            if (point.getX() == x && point.getY() == y) {
                return true;
            }
        }
        return false;
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

    public ArrayList<Point2D> getPF() {
        return pF;
    }

    public void setPF(ArrayList<Point2D> pF) {
        this.pF = pF;
    }
}
