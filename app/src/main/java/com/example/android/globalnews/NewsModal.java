package com.example.android.globalnews;

import java.util.ArrayList;

public class NewsModal {
    private int totelResults;
    private String status;
    private ArrayList<Articles> articles;

    public int getTotelResults() {
        return totelResults;
    }

    public void setTotelResults(int totelResults) {
        this.totelResults = totelResults;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Articles> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<Articles> articles) {
        this.articles = articles;
    }

    public NewsModal(int totelResults, String status, ArrayList<Articles> articles) {
        this.totelResults = totelResults;
        this.status = status;
        this.articles = articles;
    }
}
