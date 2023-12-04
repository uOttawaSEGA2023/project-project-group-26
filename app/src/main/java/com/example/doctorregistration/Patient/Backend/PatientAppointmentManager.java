package com.example.doctorregistration.Patient.Backend;



import android.app.Activity;
import android.content.Context;

import com.example.doctorregistration.Doctor.Frontend.DoctorCreateShift;
import com.example.doctorregistration.Other.EventItem;
import com.example.doctorregistration.Other.Firebase;
import com.example.doctorregistration.Patient.Frontend.PatientCreateAppointmentSlots;

public class PatientAppointmentManager{
    Firebase firebase = new Firebase();
    private String userID = firebase.getCurrentUser();

    private EventItem appointment;

    //Adds EventItem to upcoming appointment list, updates that time slot in doctor availability
    public void addAppointment(EventItem appointmentToAdd, Context context){
        //will update availability array of the selected doctor

        String doctorUserID = appointmentToAdd.getPatientDoctor().getUserID();

        firebase.addElementToArrayList(context, "Approved Requests", userID, "upcomingAppointments", appointmentToAdd,
                doctorUserID);
        //firebase.addElementToArrayList(patientCreateAppointment, "user", userID, "upcomingAppointments", appointmentToAdd);

        firebase.updateUserField(null, "Approved Requests",
                doctorUserID, "associatedWithPatient", true);

        //firebase.deleteElementFromArrayList(patientCreateAppointment,"user", doctorUserID, "availability", appointmentToAdd);
    }


    //Deletes EventItem to upcoming appointment list, adds to past appointments
    // updates that time slot in doctor availability
    public void deleteAppointment(Context context, EventItem appointmentToDelete){
        String doctorUserID = appointmentToDelete.getPatientDoctor().getUserID();

        firebase.deleteElementFromArrayList(context, "Approved Requests", userID,
                "upcomingAppointments", appointmentToDelete, doctorUserID);

        firebase.addElementToArrayList(context, "Approved Requests", userID,
                "pastAppointments", appointmentToDelete, doctorUserID);
    }


}
