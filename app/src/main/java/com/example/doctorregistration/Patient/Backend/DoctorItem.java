package com.example.doctorregistration.Patient.Backend;

import com.example.doctorregistration.Doctor.Doctor;
import com.google.firebase.Timestamp;

import java.util.Calendar;

//This class stores
public class DoctorItem {
    private Timestamp startTime;
    private Timestamp endTime;
    private Timestamp date;
    private Doctor doctor;
    private String userID;

    public DoctorItem(Timestamp startTime, Timestamp endTime, Timestamp date, Doctor doctor) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.doctor = doctor;
    }

    public DoctorItem(){}

    public Timestamp getStartTime() {
        return startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    public void setDoctor(Doctor doctor){this.doctor = doctor;}

}
