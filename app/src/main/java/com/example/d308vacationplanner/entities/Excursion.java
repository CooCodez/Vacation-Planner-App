package com.example.d308vacationplanner.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "excursions")
public class Excursion {
    @PrimaryKey(autoGenerate = true)
    private int excursionID;
    private String excursionName;
    private String hotel;
    private int vacationID;
    private Date excursionDate;

    public Excursion(String excursionName, String hotel, int vacationID, Date excursionDate) {
        this.excursionName = excursionName;
        this.hotel = hotel;
        this.vacationID = vacationID;
        this.excursionDate = excursionDate;
    }

    public int getExcursionID() {
        return excursionID;
    }

    public void setExcursionID(int excursionID) {
        this.excursionID = excursionID;
    }

    public String getExcursionName() {
        return excursionName;
    }

    public void setExcursionName(String excursionName) {  // Added setter
        this.excursionName = excursionName;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public int getVacationID() {
        return vacationID;
    }

    public void setVacationID(int vacationID) {
        this.vacationID = vacationID;
    }

    public Date getExcursionDate() {
        return excursionDate;
    }
}
