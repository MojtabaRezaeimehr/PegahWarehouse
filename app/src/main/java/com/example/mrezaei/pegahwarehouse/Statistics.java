package com.example.mrezaei.pegahwarehouse;

public class Statistics {
    int totalCount;
    int shrinkCount;
    int palletCont;

    public Statistics() {
    }

    public Statistics(int totalCount, int shrinkCount, int palletCont) {
        this.totalCount = totalCount;
        this.shrinkCount = shrinkCount;
        this.palletCont = palletCont;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getShrinkCount() {
        return shrinkCount;
    }

    public void setShrinkCount(int shrinkCount) {
        this.shrinkCount = shrinkCount;
    }

    public int getPalletCont() {
        return palletCont;
    }

    public void setPalletCont(int palletCont) {
        this.palletCont = palletCont;
    }
}
