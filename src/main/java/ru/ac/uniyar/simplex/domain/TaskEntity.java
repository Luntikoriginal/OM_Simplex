package ru.ac.uniyar.simplex.domain;

import java.util.ArrayList;
import java.util.List;

public class TaskEntity {

    private Integer variables;

    private Integer limitations;

    private String taskType;

    private String solutionWay;

    private Boolean autoBases;

    private Fraction[] function;

    private ArrayList<Integer> bases;

    private Fraction[][] limitsMatrix;

    public Integer getVariables() {
        return variables;
    }

    public void setVariables(Integer variables) {
        this.variables = variables;
    }

    public Integer getLimitations() {
        return limitations;
    }

    public void setLimitations(Integer limitations) {
        this.limitations = limitations;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getSolutionWay() {
        return solutionWay;
    }

    public void setSolutionWay(String solutionWay) {
        this.solutionWay = solutionWay;
    }

    public Boolean getAutoBases() {
        return autoBases;
    }

    public void setAutoBases(Boolean autoBases) {
        this.autoBases = autoBases;
    }

    public Fraction[][] getMatrix() {
        return limitsMatrix;
    }

    public void setMatrix(Fraction[][] limitsMatrix) {
        this.limitsMatrix = limitsMatrix;
    }

    public Fraction[] getFunction() {
        return function;
    }

    public void setFunction(Fraction[] function) {
        this.function = function;
    }

    public ArrayList<Integer> getBases() {
        return bases;
    }

    public void setBases(ArrayList<Integer> bases) {
        this.bases = bases;
    }
}
