package com.computermall.project.POJO;

public class MainGoodSort {
    private String mgsort;

    public MainGoodSort() {
    }

    public MainGoodSort(String mgsort) {
        this.mgsort = mgsort;
    }

    public String getMgsort() {
        return mgsort;
    }

    public void setMgsort(String mgsort) {
        this.mgsort = mgsort;
    }

    @Override
    public String toString() {
        return "MainGoodSort{" +
                "mgsort='" + mgsort + '\'' +
                '}';
    }
}
