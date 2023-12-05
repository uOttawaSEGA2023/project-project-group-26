package com.example.doctorregistration.Doctor.Backend;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.doctorregistration.Doctor.Frontend.DoctorCreateShift;
import com.example.doctorregistration.Doctor.Frontend.DoctorViewShift;
import com.example.doctorregistration.Other.EventItem;
import com.example.doctorregistration.Other.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class DoctorShiftManager {
    Firebase firebase = new Firebase();
    private String userID = firebase.getCurrentUser();
    private EventItem shift;

    static FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();

    public DoctorShiftManager() {}


    public void addShift(EventItem shiftToAdd, Context context){
        DoctorCreateShift doctorCreateShift = new DoctorCreateShift();

        //Updates the shift and availability ArrayList in both the "user" and "Approved Requests" collection
        firebase.addElementToArrayList(context, "user", userID, "shifts", shiftToAdd, null);
        firebase.addElementToArrayList(context, "Approved Requests", userID, "shifts", shiftToAdd, null);

    }

    public void addAvailability(EventItem availabilityToAdd, Context context){
        DoctorCreateShift doctorCreateShift = new DoctorCreateShift();

        ArrayList<EventItem> availability = splitAvailability(availabilityToAdd.getStartTime(),
                                                              availabilityToAdd.getEndTime());

        for (EventItem interval : availability) {
            // Create a new EventItem for each 30-minute interval
            EventItem newAvailabilityItem = new EventItem();

            newAvailabilityItem.setStartTime(interval.getStartTime());
            newAvailabilityItem.setEndTime(interval.getEndTime());
            newAvailabilityItem.setEventDate(availabilityToAdd.getDate());
            newAvailabilityItem.setAssociatedWithPatient(false);

            // Add the new availability item to Firebase
            firebase.addToAvailabilityArrayList(context, "user", userID, newAvailabilityItem);
            firebase.addToAvailabilityArrayList(context, "Approved Requests", userID, newAvailabilityItem);
        }

    }

    public static ArrayList<EventItem> splitAvailability(Timestamp startTime, Timestamp endTime) {
        ArrayList<EventItem> intervals = new ArrayList<>();

        // Create a Calendar instance for the start time
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTime.toDate().getTime());

        // Iterate through 30-minute intervals until the end time is reached
        while (calendar.getTimeInMillis() < endTime.toDate().getTime()) {
            // Create a new Timestamp for the current interval's start time
            Timestamp intervalStartTime = new Timestamp(new Date(calendar.getTimeInMillis()));

            // Add 30 minutes to the current time to get the end time
            calendar.add(Calendar.MINUTE, 30);

            // Create a new Timestamp for the current interval's end time
            Timestamp intervalEndTime = new Timestamp(new Date(calendar.getTimeInMillis()));

            // Create an EventItem for the current interval
            EventItem intervalItem = new EventItem(intervalStartTime, intervalEndTime);

            // Add the interval to the list
            intervals.add(intervalItem);
        }

        return intervals;
    }



    public void deleteShift(EventItem shiftToDelete, Context context){

        if (firebase.canDeleteShift(shiftToDelete, firebase.getCurrentUser(), context)){
            firebase.deleteElementFromArrayList(context, "Approved Requests",
                    firebase.getCurrentUser(), "shifts", shiftToDelete, null);

            Toast.makeText(context, "Shift Deleted", Toast.LENGTH_SHORT).show();


        }

        else{
            //Toast.makeText(context, "Shift associated with Patient\nCan't Delete!",
                //    Toast.LENGTH_SHORT).show();
        }

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
