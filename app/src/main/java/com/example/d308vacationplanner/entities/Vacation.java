package com.example.d308vacationplanner.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "vacations")
public class Vacation {
    @PrimaryKey(autoGenerate = true)
    private Integer vacationID; // Change int to Integer
    private String vacationName;
    private String hotel; // New field for hotel
    private Date startDate; // New field for start date
    private Date endDate; // New field for end date

    // Constructor
    public Vacation(Integer vacationID, String vacationName, String hotel, Date startDate, Date endDate) {
        this.vacationID = vacationID;
        this.vacationName = vacationName;
        this.hotel = hotel;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    @NonNull
    public String toString() {
        return vacationName;
    }


    // Getters and Setters
    public Integer getVacationID() {
        return vacationID;
    }

    public void setVacationID(Integer vacationID) {
        this.vacationID = vacationID;
    }

    public String getVacationName() {
        return vacationName;
    }

    public void setVacationName(String vacationName) {
        this.vacationName = vacationName;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
