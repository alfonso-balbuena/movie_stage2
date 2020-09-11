package com.example.movies.models;

import java.util.ArrayList;
import java.util.List;

public class ResponseResults<T> {
    private int id;
    private List<T> results = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
