package com.example.mrezaei.pegahwarehouse;

public class DriverPerson {
    public DriverPerson() {
    }

    String id;
    String driverCode;
    String firstName;
    String lastName;
    String photo;

    public DriverPerson(String id, String driverCode, String firstName, String lastName, String photo) {
        this.id = id;
        this.driverCode = driverCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDriverCode() {
        return driverCode;
    }

    public void setDriverCode(String driverCode) {
        this.driverCode = driverCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
