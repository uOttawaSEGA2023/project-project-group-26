package com.example.doctorregistration.Patient;

//Appointment class used to store patient's appointment details
public class Appointment {
    private String date;
    private String startTime;
    private String endTime;

    public Appointment(String date, String startTime, String endTime) {

        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;

    }

    //getters
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

}
