package ru.ac.uniyar.simplex.domain;

public class TaskEntity {

    private Integer variables;

    private Integer limitations;

    private String taskType;

    private String solutionWay;

    public void refresh() {
        variables = null;
        limitations = null;
        taskType = null;
        solutionWay = null;
    }

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
}
