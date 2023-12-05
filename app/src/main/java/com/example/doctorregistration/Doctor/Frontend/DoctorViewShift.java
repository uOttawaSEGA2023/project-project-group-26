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
import com.example.doctorregistration.Doctor.Backend.DoctorShiftManager;
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
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class DoctorViewShift extends AppCompatActivity {
    ListView listViewShifts;
    EventListView adapter;
    ArrayList<EventItem> shifts;
    CollectionReference collectionRef;
    Firebase firebase;
    DoctorShiftManager shiftManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_shift);

        listViewShifts = (ListView) findViewById(R.id.listViewShifts);
        shifts = new ArrayList<>();

        firebase = new Firebase();
        collectionRef = firebase.getCollectionRef("Approved Requests");

        shiftManager = new DoctorShiftManager();

        //When item is clicked, alert box will appear displaying user information
        listViewShifts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click
                EventItem shift = shifts.get(position);

                showShiftInformation(shift);

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        // Add a real-time listener to the collectionRef
        collectionRef.document(firebase.getCurrentUser()).addSnapshotListener((document, error) -> {
            if (error != null) {
                Log.e("Debug", "Error listening for changes", error);
                return;
            }

            if (document != null && document.exists()) {
                shifts.clear();

                Doctor doctor = new Doctor();
                doctor.setLastName(document.getString("Doctor.lastName"));

                //To extract the Event data, must extract has HashMap
                ArrayList<HashMap<String, Object>> existingShiftsRaw = (ArrayList<HashMap<String, Object>>) document.get("Doctor.shifts");

                //Copies all values in firestore into manageable and displayable ArrayList<EventItem>
                for (HashMap<String, Object> existingShiftMap : existingShiftsRaw) {
                    EventItem doctorShift = new EventItem();

                    doctorShift.setEventDate((Timestamp) existingShiftMap.get("date"));
                    doctorShift.setStartTime((Timestamp) existingShiftMap.get("startTime"));
                    doctorShift.setEndTime((Timestamp) existingShiftMap.get("endTime"));

                    doctorShift.setEventDoctor(doctor);
                    shifts.add(doctorShift);
                }
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

        });
    }



     // This method displays the Alert Box when clicked

    private void showShiftInformation(EventItem doctorShiftEvent) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.view_event_info2, null);
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
                shiftManager.deleteShift(doctorShiftEvent, getApplicationContext());
                adapter.notifyDataSetChanged();
            }
        });


    }


}