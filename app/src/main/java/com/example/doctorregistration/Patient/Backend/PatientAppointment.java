package com.example.doctorregistration.Patient.Backend;

import com.example.doctorregistration.Doctor.Doctor;

import java.util.Calendar;

public class PatientAppointment {
    private Calendar startTime;
    private Calendar endTime;
    private Calendar date;
    private Doctor doctor;
    private String userID;

    public PatientAppointment(Calendar startTime, Calendar endTime, Doctor doctor, Calendar date) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.doctor = doctor;
    }

    public PatientAppointment(){}

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




