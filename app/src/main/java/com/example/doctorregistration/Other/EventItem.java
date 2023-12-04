package com.example.doctorregistration.Other;

import com.example.doctorregistration.Doctor.Doctor;
import com.example.doctorregistration.Patient.Backend.DoctorItem;
import com.example.doctorregistration.Patient.Patient;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EventItem {
    private Timestamp startTime;
    private Timestamp endTime;
    private Timestamp date;

    private Doctor doctor;
    private boolean associatedWithPatient;
    private DoctorItem patientDoctor;

    public EventItem(Timestamp startTime, Timestamp endTime, Timestamp date, boolean associatedWithPatient) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.associatedWithPatient = associatedWithPatient;
    }

    public EventItem(Timestamp startTime, Timestamp endTime, Timestamp date) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
    }



    public EventItem(){}

    public EventItem(Timestamp intervalStartTime, Timestamp intervalEndTime) {
        this.startTime = intervalStartTime;
        this.endTime = intervalEndTime;
    }

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

    public Doctor getEventDoctor() {
        return doctor;
    }

    public void setEventDoctor(Doctor doctor) {
        this.doctor = doctor;
    }



    public DoctorItem getPatientDoctor() {
        return patientDoctor;
    }

    public void setPatientDoctor(DoctorItem patientDoctor) {
        this.patientDoctor = patientDoctor;
    }


    public boolean getAssociatedWithPatient(){
        return associatedWithPatient;
    }

    public void setAssociatedWithPatient(boolean associatedWithPatient){
        this.associatedWithPatient = associatedWithPatient;
    }

    //This method checks if EventItem conflicts with another EventItem
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

    //Displays time in readable format
    public static String formatTimestamp(Timestamp timestamp) {
        // Convert the Firestore timestamp to a Date
        Date date = timestamp.toDate();

        // Format the Date in 24-hour format
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
        return sdf.format(date);
    }

    //This method converts timestamp to date (date is more manageable in code, timestamp is best
    //stored in firebase)
    public static Date extractDateFromTimestamp(Timestamp timestamp) {
        // Convert Timestamp to Date
        return timestamp.toDate();
    }

    //This method formats date into readable format
    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        return sdf.format(date);
    }

    //This method is a custom comparator for EventItem type
    @Override
    public boolean equals(Object obj) {
            EventItem event = (EventItem) obj;

            Date otherDate = event.date.toDate();
            Date thisDate = this.date.toDate();

            Date otherStartTime = event.getStartTime().toDate();
            Date otherEndTime = event.getEndTime().toDate();

            Date thisStartTime = this.getStartTime().toDate();
            Date thisEndTime = this.getEndTime().toDate();


            return (isSameDate(thisDate, otherDate) && areSameTime(otherStartTime, thisStartTime) &&
                    areSameTime(otherEndTime, thisEndTime));
        }


    //Display Doctor Shifts
    public String displayDoctorEventInfo(){
        return("Dr. " + doctor.getLastName() +
                "\nDate: " + formatDate(extractDateFromTimestamp(date)) +
                "\nStart Time: " + formatTimestamp(startTime) +
                "\nEnd Time: " + formatTimestamp(endTime));
    }


    //Display PatientAppointments
    public String displayPatientEventInfo(){
        //maybe add display of the doctors specialities
        return("Dr. " + this.getPatientDoctor().getDoctor().getLastName() + ", " + this.getPatientDoctor().getDoctor().getFirstName() +
                "\nSpeciality(s): " + this.getPatientDoctor().getDoctor().getSpecialty() +
                "\nDate: " + formatDate(extractDateFromTimestamp(date)) +
                "\nStart Time: " + formatTimestamp(startTime) +
                "\nEnd Time: " + formatTimestamp(endTime));
    }

    //This method checks if selected date hasn't already passed
    public static boolean isDateValid(Calendar selectedDate) {
        // Get the current date
        Calendar currentDate = Calendar.getInstance();
        // Compare the selected date with the current date
        return !selectedDate.before(currentDate);
    }

    //This method checks if start time comes before end time
    public boolean isValidTime() {
        Date startDate = startTime.toDate();
        Date endDate = endTime.toDate();

        return (startDate.before(endDate));
    }
    public boolean isSameDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }

    private static boolean areSameTime(Date timestamp1, Date timestamp2) {
        // Format the time portions using SimpleDateFormat
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String time1 = timeFormat.format(timestamp1);
        String time2 = timeFormat.format(timestamp2);

        // Compare the formatted time strings
        return time1.equals(time2);
    }

}
