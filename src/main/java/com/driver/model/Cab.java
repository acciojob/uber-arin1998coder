package com.driver.model;

import javax.persistence.*;

@Entity
public class Cab {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int perKmRate;

    private boolean available;


    @OneToOne
    @JoinColumn
    private Driver driver; //reference of the child class

    public Cab(int id, int perKmRate, boolean available) {
        this.id = id;
        this.perKmRate = perKmRate;
        this.available = available;
    }

    public Cab(){

    }

    public int getId() {
        return id;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPerKmRate() {
        return perKmRate;
    }

    public void setPerKmRate(int perKmRate) {
        this.perKmRate = perKmRate;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}