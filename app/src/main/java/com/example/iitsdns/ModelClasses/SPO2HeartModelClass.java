package com.example.iitsdns.ModelClasses;

public class SPO2HeartModelClass {
    private int SPO2;
    private int HeartBeat;

    public SPO2HeartModelClass(int SPO2, int heartBeat) {
        this.SPO2 = SPO2;
        HeartBeat = heartBeat;
    }

    @Override
    public String toString() {
        return "SPO2HeartModelClass{" +
                "SPO2=" + SPO2 +
                ", HeartBeat=" + HeartBeat +
                '}';
    }

    public int getSPO2() {
        return SPO2;
    }

    public void setSPO2(int SPO2) {
        this.SPO2 = SPO2;
    }

    public int getHeartBeat() {
        return HeartBeat;
    }

    public void setHeartBeat(int heartBeat) {
        HeartBeat = heartBeat;
    }
}
