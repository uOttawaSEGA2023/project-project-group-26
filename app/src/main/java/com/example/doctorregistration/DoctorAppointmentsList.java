package com.example.doctorregistration;



import android.content.Intent;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;

public class DoctorAppointmentsList extends AppCompatActivity {
    ListView listViewRequests;
    RegistrationRequestListView adapter;
    ArrayList<RegistrationRequestItem> appointments;
    CollectionReference collectionRef;
    Firebase firebase;





    String getListType = DoctorWelcome.test;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        //decides whether to run the upcoming appointments activity or past appointment, based on what button user pressed
        if(getListType.equals("upcomingAppointments")){
            setContentView(R.layout.activity_doctor_upcomingappointments_list);
            listViewRequests = (ListView) findViewById(R.id.listView2);
        }else{
            setContentView(R.layout.activity_doctor_pastappointments_list);
            listViewRequests = (ListView) findViewById(R.id.listView1);
        }

        appointments = new ArrayList<>();

        firebase = new Firebase();
        collectionRef = firebase.getCollectionRef("Approved Requests");

        //this is the starter code for the code that decides what happens when we click on a patient
        listViewRequests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click
                handleItemClick(position);
            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();

        //Add a real-time listener to the collectionRef
        collectionRef.addSnapshotListener((value,error)->{

            if(error != null){
                Log.e("Debug","Error Listening for changes", error);
                return;
            }

            if(value != null){
                //clears the existing list of appointments
                appointments.clear();

                //grabs data from "Approved Requests" collection in database
                for(QueryDocumentSnapshot documentSnapshot : value){
                    RegistrationRequestItem appointment = new RegistrationRequestItem();
                    appointment.setUserID(documentSnapshot.getId());
                    String userType = documentSnapshot.getString("userType");
                    if("Patient".equals(userType)){
                        Patient patient = new Patient();

                        //creates patient using the data from firestore
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

                        //determines if patient has been treated by the doctor
                        boolean treatmentStatus = documentSnapshot.getBoolean("Patient.treatmentStatus");

                        //if they are treated add them to past appointment page
                        if(treatmentStatus == true && getListType.equals("pastAppointments")){
                            appointment.setPatient(patient);
                            appointment.setUserType("Patient");
                            appointments.add(appointment);
                        }
                        //if patient is not treated adds them to upcoming appointments list
                        else if(treatmentStatus == false && getListType.equals("upcomingAppointments")) {
                            appointment.setPatient(patient);
                            appointment.setUserType("Patient");
                            appointments.add(appointment);

                        }

                    }

                }

                if(adapter == null){
                    //if there is no adapter, new one is created
                    adapter = new RegistrationRequestListView(DoctorAppointmentsList.this, appointments);
                    listViewRequests.setAdapter(adapter);
                }else{
                    //adapter already exists, update the data and notifyDataSetChanged
                    adapter.clear();
                    adapter.addAll(appointments);
                    adapter.notifyDataSetChanged();
                }

            }

        });
    }

    //code that decides what happens when clicking on patient info
    private void handleItemClick(int position) {
        // Getting clicked item
        RegistrationRequestItem clickedItem = appointments.get(position);

        // Extracting patients info
        Patient patient = clickedItem.getPatient();
        if (patient != null) {
            // Displaying patients info
            showPatientInformation(patient);
        }
    }

    //showing patients info
    private void showPatientInformation(Patient patient) {
        // Show the patient information in a dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Patient Information");
        builder.setMessage(patient.displayUserInformation()
                // add extra if you want to show other stuff
        );
        builder.setPositiveButton("OK", null);
        builder.show();
    }

}
