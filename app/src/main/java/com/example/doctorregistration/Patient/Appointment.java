package com.example.doctorregistration.Patient;


import com.google.firebase.Timestamp;

//Appointment class used to store patient's appointment details
public class Appointment {
    private Timestamp appointmentTime;
    private String date;
    private String startTime;
    private String endTime;

    public Appointment(Timestamp appointmentTime) {

        this.appointmentTime = appointmentTime;

    }

    //getters
    /*


    public String getDate(){
        return this.date;
    }

    public String getStartTime(){
        return this.startTime;
    }

    public String getEndTime(){
        return this.endTime;
    }

    //setters
    public void setDate(String date){
        this.date = date;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime){
        this.endTime = endTime;
    }

     */

    public Timestamp getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Timestamp appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

}
