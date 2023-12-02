package com.example.doctorregistration.Doctor.Backend;

import android.widget.Toast;

import com.example.doctorregistration.Doctor.Frontend.DoctorCreateShift;
import com.example.doctorregistration.Doctor.Frontend.DoctorViewShift;
import com.example.doctorregistration.Other.EventItem;
import com.example.doctorregistration.Other.Firebase;

import java.util.Calendar;

public class DoctorShiftManager {
    Firebase firebase = new Firebase();
    private String userID = firebase.getCurrentUser();
    private EventItem shift;

    public DoctorShiftManager() {}


    public void addShift(EventItem shiftToAdd){
        DoctorCreateShift doctorCreateShift = new DoctorCreateShift();

        //Updates the shift and availability ArrayList in both the "user" and "Approved Requests" collection
        firebase.addElementToArrayList(doctorCreateShift, "user", userID, "shifts", shiftToAdd);
        firebase.addElementToArrayList(doctorCreateShift,"user", userID, "availability", shiftToAdd);

        firebase.addElementToArrayList(doctorCreateShift, "Approved Requests", userID, "shifts", shiftToAdd);
        firebase.addElementToArrayList(doctorCreateShift,"Approved Requests", userID, "availability", shiftToAdd);

    }

    public void deleteShift(EventItem shiftToDelete){
        DoctorViewShift doctorViewShift = new DoctorViewShift();

        //if (firebase.canDeleteShift(firebase.getCurrentUser())){
            firebase.deleteElementFromArrayList(doctorViewShift, "Approved Requests",
                    firebase.getCurrentUser(), "shifts", shiftToDelete);

            firebase.deleteElementFromArrayList(doctorViewShift, "user",
                    firebase.getCurrentUser(), "shifts", shiftToDelete);
        //}

        //else{
            //display the message
        //}

    }


    //Forces start time and end time to be in 30 minute increments
    public int roundToNearest30Minutes(int minute) {
        if (minute < 15) {
            return 0;
        } else if (minute < 45) {
            return 30;
        } else {
            return 0; // Wrap around to the next hour
        }
    }



}
