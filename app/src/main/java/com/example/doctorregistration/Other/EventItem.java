package com.example.doctorregistration.Other;

import com.example.doctorregistration.Doctor.Doctor;
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

    private ArrayList<Patient> doctorPatients;
    private Doctor doctor;
    private Patient patient;

    public EventItem(Timestamp startTime, Timestamp endTime, Timestamp date) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
    }

    public EventItem(Timestamp startTime, Timestamp endTime, Timestamp date, ArrayList<Patient> doctorPatients) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.doctorPatients = doctorPatients;
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

    public Doctor getEventDoctor() {
        return doctor;
    }

    public void setEventDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void setEventPatient(Patient patient){
        this.patient = patient;
    }

    public ArrayList<Patient> getDoctorPatients() {
        return doctorPatients;
    }

    public void setDoctorPatients(ArrayList<Patient> doctorPatients) {
        this.doctorPatients = doctorPatients;
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
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        EventItem other = (EventItem) obj;

        // Compare the Timestamps for equality
        return startTime.equals(other.startTime) &&
                endTime.equals(other.endTime) &&
                date.equals(other.date);
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
        return("Dr. " + doctor.getLastName() +
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

}
