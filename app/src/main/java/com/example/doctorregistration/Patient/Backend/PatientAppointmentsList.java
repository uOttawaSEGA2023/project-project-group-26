package com.example.doctorregistration.Patient.Backend;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;

import com.example.doctorregistration.Other.Address;
import com.example.doctorregistration.Other.Firebase;
import com.example.doctorregistration.Patient.Frontend.PatientWelcome;
import com.example.doctorregistration.Other.RegistrationRequestItem;
import com.example.doctorregistration.Other.RegistrationRequestListView;
import com.example.doctorregistration.Patient.Patient;
import com.example.doctorregistration.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import java.util.ArrayList;



public class PatientAppointmentsList extends AppCompatActivity {
    ListView listViewRequests;
    RegistrationRequestListView adapter;
    ArrayList<RegistrationRequestItem> appointments;
    CollectionReference collectionRef;
    Firebase firebase;
    private Button buttonpasttopatientwelcome;
    private Button buttonupcomingtopatientwelcome;
    String getListType = PatientWelcome.test;
    String loggedInPatientID = "123456789"; // need to replace  with the actual way to get logged-in patient ID (one im using is a test case one for the jpp email one)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // decides whether to run the upcoming appointments activity or past appointment,
        // based on what button user pressed
        if (getListType.equals("upcomingAppointments")) {
            setContentView(R.layout.activity_patient_upcomingappointments_list);
            listViewRequests = findViewById(R.id.listView2);
            buttonupcomingtopatientwelcome = findViewById(R.id.backtoPW2);
            buttonupcomingtopatientwelcome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openPatientWelcome();
                }
            });
        } else {
            setContentView(R.layout.activity_patient_pastappointments_list);
            listViewRequests = findViewById(R.id.listView1);
            buttonpasttopatientwelcome = findViewById(R.id.backtoPW1);
            buttonpasttopatientwelcome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openPatientWelcome();
                }
            });
        }

        appointments = new ArrayList<>();
        firebase = new Firebase();
        collectionRef = firebase.getCollectionRef("Approved Appointments");

        // this is the starter code for the code that decides what happens when we click on a doctor
        listViewRequests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click
                handleItemClick(position);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        collectionRef.addSnapshotListener((value, error) ->{
            if (error != null) {
                Log.e("Debug", "Error Listening for changes", error);
                return;
            }
            if (value != null) {
                // clears the existing list of appointments
                appointments.clear();

                for (QueryDocumentSnapshot documentSnapshot : value) {
                    RegistrationRequestItem appointment = new RegistrationRequestItem();
                    appointment.setUserID(documentSnapshot.getId());
                    String userType = documentSnapshot.getString("userType");

                    if ("Patient".equals(userType) && loggedInPatientID.equals(documentSnapshot.getId())){
                        Patient patient = new Patient();

                        // creates a patient using the data from Firestore
                        patient.setFirstName(documentSnapshot.getString("Patient.firstName"));
                        patient.setLastName(documentSnapshot.getString("Patient.lastName"));
                        patient.setPhoneNumber(documentSnapshot.getLong("Patient.phoneNumber").intValue());
                        patient.setEmail(documentSnapshot.getString("Patient.email"));
                        patient.setIdNumber(documentSnapshot.getLong("Patient.idNumber").intValue());
                        Address address = new Address();
                        address.setCity(documentSnapshot.getString("Patient.address.city"));
                        address.setCountry(documentSnapshot.getString("Patient.address.country"));
                        address.setStreet(documentSnapshot.getString("Patient.address.street"));
                        address.setPostalCode(documentSnapshot.getString("Patient.address.postalCode"));
                        patient.setAddress(address);

                        // determines if the patient has been treated by the doctor
                        boolean treatmentStatus = documentSnapshot.getBoolean("Patient.treatmentStatus");

                        // if they are treated add them to the past appointment page
                        if (treatmentStatus && getListType.equals("pastAppointments")) {
                            appointment.setPatient(patient);
                            appointment.setUserType("Patient");
                            appointments.add(appointment);
                        }
                        // if the patient is not treated adds them to upcoming appointments list
                        else if (!treatmentStatus && getListType.equals("upcomingAppointments")) {
                            appointment.setPatient(patient);
                            appointment.setUserType("Patient");
                            appointments.add(appointment);
                        }

                    }
                }
                if (adapter == null) {
                    // if there is no adapter, a new one is created
                    adapter = new RegistrationRequestListView(PatientAppointmentsList.this, appointments);
                    listViewRequests.setAdapter(adapter);
                } else {
                    // the adapter already exists, update the data and notifyDataSetChanged
                    adapter.clear();
                    adapter.addAll(appointments);
                    adapter.notifyDataSetChanged();
                }
            }


        });
    }


    // code that decides what happens when clicking on patient info (should be changed to see the appointments)
    private void handleItemClick(int position) {
        // Getting clicked item
        RegistrationRequestItem clickedItem = appointments.get(position);

        // Extracting patient info
        Patient patient = clickedItem.getPatient();
        String userID = clickedItem.getUserID();

        if (patient != null) {
            // Displaying appointment info
            showAppointmentInformation(patient, userID);
        }
    }

    // showing appointment info (should be changed to see the appointments)
    private void showAppointmentInformation(Patient patient, String userID) {
        // Show the doctor information in a dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Appointment Information");
        builder.setMessage(patient.displayUserInformation()); // add extra if you want to show other stuff
        builder.setPositiveButton("OK", null);


        builder.show();
    }

    public void openPatientWelcome() {
        Intent intent = new Intent(getApplicationContext(), PatientWelcome.class);
        startActivity(intent);
    }

}
