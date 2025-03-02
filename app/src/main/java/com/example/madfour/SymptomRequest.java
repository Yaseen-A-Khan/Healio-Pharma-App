package com.example.madfour;

import java.util.List;

public class SymptomRequest {
    private List<Integer> symptoms;

    public SymptomRequest(List<Integer> symptoms) {
        this.symptoms = symptoms;
    }

    public List<Integer> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(List<Integer> symptoms) {
        this.symptoms = symptoms;
    }
}
