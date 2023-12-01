package com.example.doctorregistration.Doctor.Frontend;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.doctorregistration.Admin.Backend.AdminDeniedRequest;
import com.example.doctorregistration.Doctor.Doctor;
import com.example.doctorregistration.Other.Address;
import com.example.doctorregistration.Other.EventItem;
import com.example.doctorregistration.Other.EventListView;
import com.example.doctorregistration.Other.Firebase;
import com.example.doctorregistration.Other.RegistrationRequestItem;
import com.example.doctorregistration.Other.RegistrationRequestListView;
import com.example.doctorregistration.Other.User;
import com.example.doctorregistration.Patient.Patient;
import com.example.doctorregistration.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class DoctorViewShift extends AppCompatActivity {
    ListView listViewShifts;
    EventListView adapter;
    ArrayList<EventItem> shifts;
    CollectionReference collectionRef;
    Firebase firebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_shift);

        listViewShifts = (ListView) findViewById(R.id.listViewShifts);
        shifts = new ArrayList<>();

        firebase = new Firebase();
        collectionRef = firebase.getCollectionRef("Approved Requests");

        //When item is clicked, alert box will appear displaying user information
        listViewShifts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click
                EventItem shift = shifts.get(position);

                String userID = firebase.getCurrentUser();
                String userType = shift.getEventUserType();;

                if (userType.equals("Doctor"))
                    showShiftInformation(shift, userType, userID);

            }
        });
    }




    @Override
    protected void onStart() {
        super.onStart();

        // Add a real-time listener to the collectionRef
        collectionRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e("Debug", "Error listening for changes", error);
                return;
            }

            if (value != null) {
                shifts.clear();

                //Grabs documents from collection
                for (QueryDocumentSnapshot documentSnapshot : value) {
                        EventItem doctorShift = new EventItem();

                    doctorShift.setEventUserID(documentSnapshot.getId());
                    String userType = documentSnapshot.getString("userType");

                    if ("Doctor".equals(userType)) {

                        doctorShift.setStartTime(documentSnapshot.getTimestamp("Doctor.startTime"));
                        doctorShift.setEndTime(documentSnapshot.getTimestamp("Doctor.endTime"));
                        doctorShift.setEventDate(documentSnapshot.getTimestamp("Doctor.date"));

                    }

                    else
                        Log.e("ERROR","no userType");

                    shifts.add(doctorShift);
                }


                if (adapter == null) {
                    //If there is no adapter, create one
                    adapter = new EventListView(DoctorViewShift.this, shifts);
                    listViewShifts.setAdapter(adapter);
                } else {
                    // Adapter already exists, update the data and notifyDataSetChanged
                    adapter.clear();
                    adapter.addAll(shifts);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }


    /**
     * This method displays the Alert Box when clicked
     */
    private void showShiftInformation(EventItem doctorShiftEvent, String userType, String userID) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.view_event_info, null);
        dialogBuilder.setView(dialogView);

        TextView tvShiftInfo = (TextView) dialogView.findViewById(R.id.eventInformation);
        Button btDelete = (Button) dialogView.findViewById(R.id.deleteEvent);

        dialogBuilder.setTitle("Next Shift: ");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        tvShiftInfo.setText(doctorShiftEvent.displayDoctorEventInfo());

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //firebase.updateUserField(DoctorViewShift.this, "user", userID,
                //        "accountStatus", "approved");

                //firebase.removeUserFromCollection("Denied Requests", userID);
            }
        });
    }


}