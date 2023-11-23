package com.example.doctorregistration.Doctor.Backend;
import android.annotation.SuppressLint;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;



public class ShiftValidity {
    private String shiftDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @SuppressLint("NewApi")
    private static final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); //have to change this based on the format of the strings

    @SuppressLint("NewApi")
    public ShiftValidity(String date, String startTime, String endTime) throws ParseException {

            this.shiftDate =  date;
            this.startTime =  LocalDateTime.parse(startTime,timeFormat);
            this.endTime =  LocalDateTime.parse(endTime,timeFormat);

    }
    public long shiftDuration() {
        @SuppressLint({"NewApi", "LocalSuppress"}) long endTimemilliseconds = endTime.getHour() * 60 * 60 * 1000 +
                endTime.getMinute() * 60 * 1000 +
                endTime.getSecond() * 1000;

        @SuppressLint({"NewApi", "LocalSuppress"}) long startTimemilliseconds = startTime.getHour() * 60 * 60 * 1000 +
                startTime.getMinute() * 60 * 1000 +
                startTime.getSecond() * 1000;

        return endTimemilliseconds- startTimemilliseconds;
    }
    public boolean ValidShift (ShiftValidity otherShift ) {
        if (this.shiftDate != otherShift.shiftDate) {
            return false;
        } else{
           @SuppressLint({"NewApi", "LocalSuppress"}) boolean valid = this.endTime.isAfter(otherShift.startTime) && otherShift.endTime.isAfter(this.startTime);

           return valid;
        }
    }


}
