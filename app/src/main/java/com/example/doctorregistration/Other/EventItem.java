package com.example.doctorregistration.Other;

import com.example.doctorregistration.Doctor.Doctor;
import com.example.doctorregistration.Patient.Patient;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EventItem {
    private Timestamp startTime;
    private Timestamp endTime;
    private Timestamp date;

    private Doctor doctor;
    private Patient patient;
    private String userType;

    private String userID;

    public EventItem(Timestamp startTime, Timestamp endTime, Timestamp date) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
    }

    public EventItem(){}

    public Timestamp getStartTime() {
        return startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public Timestamp getDate(){return date;}

    public void setStartTime(Timestamp startTime){
        this.startTime = startTime;
    }

    public void setEndTime(Timestamp endTime){
        this.endTime = endTime;
    }

    public void setEventDate(Timestamp date){
        this.date = date;
    }
    public String getEventUserType(){return userType;}

    public void setEventUserType(String userType){
        this.userType = userType;
    }

    public String getEventUserID() {
        return userID;
    }

    public void setEventUserID(String userID) {
        this.userID = userID;
    }


    public boolean overlapsWith(EventItem otherShift) {
        // Check if the dates are the same
        if (!this.date.equals(otherShift.getDate())) {
            return false;  // Different dates, no overlap
        }

        // Check if the time ranges overlap
        return isOverlappingTimeRange(this.startTime, this.endTime, otherShift.getStartTime(), otherShift.getEndTime());
    }

    private boolean isOverlappingTimeRange(Timestamp start1, Timestamp end1, Timestamp start2, Timestamp end2) {
        // Convert Timestamps to Date objects for easier comparison
        Date startDate1 = start1.toDate();
        Date endDate1 = end1.toDate();
        Date startDate2 = start2.toDate();
        Date endDate2 = end2.toDate();

        // Check for overlap
        return startDate1.before(endDate2) && endDate1.after(startDate2);
    }

    //Use these methods when you want to extract data from database and display in readable format
    //is currently un tested
    //may no
    public static String extractTimeIn24HourFormat(Timestamp timestamp) {
        // Convert the Firestore timestamp to a Date
        Date date = timestamp.toDate();

        // Format the Date in 24-hour format
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
        return sdf.format(date);
    }

    public static Date extractDateFromTimestamp(Timestamp timestamp) {
        // Convert Timestamp to Date
        return timestamp.toDate();
    }

    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        return sdf.format(date);
    }

    //Display Doctor Shifts
    public String displayDoctorEventInfo(){
        return("Dr. " + doctor.getFirstName() +
                "\nDate: " + formatDate(extractDateFromTimestamp(date)) +
                "\nStart Time: " + extractTimeIn24HourFormat(startTime) +
                "\nEnd Time: " + extractTimeIn24HourFormat(endTime));
    }


    //Display PatientAppointments
    public String displayPatientEventInfo(){
        //maybe add display of the doctors specialities
        return("Dr. " + doctor.getFirstName() +
                "\nDate: " + formatDate(extractDateFromTimestamp(date)) +
                "\nStart Time: " + extractTimeIn24HourFormat(startTime) +
                "\nEnd Time: " + extractTimeIn24HourFormat(endTime));
    }

}
