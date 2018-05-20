package com.example.mrezaei.pegahwarehouse;

public class Barcode {
    String deliverId;
    String gtin;
    String exp;
    String lot;
    String serial;
    String complete_barcode;
    int levelId;


    public String getDeliverId() {
        return deliverId;
    }

    public void setDeliverId(String deliverId) {
        this.deliverId = deliverId;
    }

    public Barcode(String gtin, String exp, String lot, String serial,
                   String complete_barcode, int level_id
            , String deliverId) {
        this.gtin = gtin;
        this.exp = exp;
        this.lot = lot;
        this.serial = serial;
        this.complete_barcode = complete_barcode;
        this.levelId = level_id;
        this.deliverId = deliverId;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public Barcode() {
    }

    public String getGtin() {
        return gtin;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getComplete_barcode() {
        return complete_barcode;
    }

    public void setComplete_barcode(String complete_barcode) {
        this.complete_barcode = complete_barcode;
        this.gtin = complete_barcode.substring(0,18);
        this.exp = complete_barcode.substring(18,28);
        this.lot = complete_barcode.substring(28,39);
        this.serial = complete_barcode.substring(39,52);
    }

    public Barcode(String complete_barcode, String deliverId, int level_id) {
        this.complete_barcode = complete_barcode;
        this.levelId = level_id;
        this.gtin = complete_barcode.substring(0,18);
        this.exp = complete_barcode.substring(18,28);
        this.lot = complete_barcode.substring(28,39);
        this.serial = complete_barcode.substring(39,52);
        this.deliverId = deliverId;
    }

}
