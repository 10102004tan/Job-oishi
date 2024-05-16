package com.example.joboishi.Models;

public class WorkForm {
    private String workFormName;
    private boolean isChosen;

    public WorkForm(String workFormName, boolean isChosen) {
        this.workFormName = workFormName;
        this.isChosen = isChosen;
    }

    public String getWorkFormName() {
        return workFormName;
    }

    public void setWorkFormName(String workFormName) {
        this.workFormName = workFormName;
    }

    public boolean isChosen() {
        return isChosen;
    }

    public void setChosen(boolean chosen) {
        isChosen = chosen;
    }
}
