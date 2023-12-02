package com.example.doctorregistration.Patient.Backend;

import com.example.doctorregistration.Doctor.Doctor;

import java.util.Calendar;

public class DoctorItem {
    private Calendar startTime;
    private Calendar endTime;
    private Doctor doctor;
    private String userID;

    public DoctorItem(Calendar startTime, Calendar endTime, Doctor doctor) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.doctor = doctor;
    }

    public DoctorItem(){}

    public Calendar getStartTime() {
        return startTime;
    }

    public Calendar getEndTime() {
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
