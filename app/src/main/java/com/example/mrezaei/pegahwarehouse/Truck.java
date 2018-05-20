package com.example.mrezaei.pegahwarehouse;

public class Truck {
    String id;
    String plaque;
    String truckCode;
    String truckModel;

    public Truck(String id, String plaque, String truckCode, String truckModel) {
        this.id = id;
        this.plaque = plaque;
        this.truckCode = truckCode;
        this.truckModel = truckModel;
    }

    public Truck() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlaque() {
        return plaque;
    }

    public void setPlaque(String plaque) {
        this.plaque = plaque;
    }

    public String getTruckCode() {
        return truckCode;
    }

    public void setTruckCode(String truckCode) {
        this.truckCode = truckCode;
    }

    public String getTruckModel() {
        return truckModel;
    }

    public void setTruckModel(String truckModel) {
        this.truckModel = truckModel;
    }
}
