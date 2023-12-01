package com.example.doctorregistration.Doctor.Backend;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class DoctorShift {
    private Timestamp startTime;
    private Timestamp endTime;
    //private Date date;
    private Timestamp date;

    public DoctorShift(Timestamp startTime, Timestamp endTime, Timestamp date) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
    }

    public DoctorShift(){}

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

    public void setShiftDate(Timestamp date){
        this.date = date;
    }

    public boolean overlapsWith(DoctorShift otherShift) {
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

    private static Date extractDateFromTimestamp(Timestamp timestamp) {
        // Convert Timestamp to Date
        return timestamp.toDate();
    }


}
