package com.example.doctorregistration.Doctor.Backend;

import com.google.firebase.Timestamp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
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


    /*
    public DoctorShift(Map<String, Object> data) {

        //Start and end times are stored as HashMaps in firebase automatically
        //Convert to use values
        Map<String, Object> startTimeData = (Map<String, Object>) data.get("startTime");
        Map<String, Object> endTimeData = (Map<String, Object>) data.get("endTime");

        int startHour = ((Long) startTimeData.get("hour")).intValue();
        int startMinute = ((Long) startTimeData.get("minute")).intValue();

        int endHour = ((Long) endTimeData.get("hour")).intValue();
        int endMinute = ((Long) endTimeData.get("minute")).intValue();

        this.startTime = LocalTime.of(startHour, startMinute);
        this.endTime = LocalTime.of(endHour, endMinute);

        //Date is stored as Timestamp in firebase automatically
        //You have to convert back to Date if you want to use the data
        Timestamp timestampValue = (Timestamp) data.get("date");
        this.date = timestampValue.toDate();
    }*/

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

    /*
    public boolean overlapsWith(DoctorShift otherShift) {
        // Check if this shift overlaps with another shift
        return this.date.equals(otherShift.getDate()) &&
                (this.startTime.isBefore(otherShift.getEndTime()) && this.endTime.isAfter(otherShift.getStartTime()));
    }*/

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
    private static int extractHourFromTimestamp(Timestamp timestamp) {
        // Convert Timestamp to Date
        Date date = timestamp.toDate();

        // Extract hour using Calendar
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    private static int extractMinuteFromTimestamp(Timestamp timestamp) {
        // Convert Timestamp to Date
        Date date = timestamp.toDate();

        // Extract minute using Calendar
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

    private static Date extractDateFromTimestamp(Timestamp timestamp) {
        // Convert Timestamp to Date
        return timestamp.toDate();
    }


}
