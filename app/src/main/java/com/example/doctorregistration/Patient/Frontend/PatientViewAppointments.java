package com.example.doctorregistration.Patient.Frontend;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.media.metrics.Event;
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
import com.example.doctorregistration.Doctor.Frontend.DoctorWelcome;
import com.example.doctorregistration.Other.Address;
import com.example.doctorregistration.Other.EventItem;
import com.example.doctorregistration.Other.EventListView;
import com.example.doctorregistration.Other.Firebase;
import com.example.doctorregistration.Other.RegistrationRequestItem;
import com.example.doctorregistration.Other.RegistrationRequestListView;
import com.example.doctorregistration.Other.User;
import com.example.doctorregistration.Patient.Backend.PatientAppointmentManager;
import com.example.doctorregistration.Patient.Patient;
import com.example.doctorregistration.R;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;



public class PatientViewAppointments extends AppCompatActivity {

    ListView listViewAppointments;
    EventListView adapter;
    ArrayList<EventItem> appointments;

    CollectionReference collectionRef;
    Firebase firebase;
    PatientAppointmentManager appointmentManager;

    String getListType = PatientWelcome.test;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //calls the UI page of upcoming appointments list if upcoming appointment button is pressed by user
        if(getListType.equals("upcomingAppointments")){
            setContentView(R.layout.activity_patient_upcomingappointmentsnew);
            listViewAppointments = (ListView) findViewById(R.id.listViewAppointments1);
        //calls the UI page of past appointments list if past appointment button is pressed by user
        }else{
            setContentView(R.layout.activity_patientpastappoinments);
            listViewAppointments = (ListView) findViewById(R.id.listViewAppointments2);


        }

        //stores appointments to be displayed
        appointments = new ArrayList<>();
        firebase = new Firebase();
        collectionRef = firebase.getCollectionRef("Approved Requests");

        appointmentManager = new PatientAppointmentManager();

        listViewAppointments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click
                EventItem appointment = appointments.get(position);

                showAppointmentInformation(appointment);

            }
        });



    }

    @Override
    protected void onStart(){
        super.onStart();
        collectionRef.document(firebase.getCurrentUser()).addSnapshotListener((document, error)->{
            if (error != null){

                Log.e("Debug", "Error listening for changes", error);
                return;

            }

            if(document != null&&document.exists()){
                appointments.clear();
                Patient patient = new Patient ();
                patient.setLastName(document.getString("Patient.lastName"));


                //if upcoming appointments button is pressed adds upcoming appointments to appointment array
                if(getListType.equals("upcomingAppointments")){
                    ArrayList<HashMap<String, Object>> existingAppointmentRaw = (ArrayList<HashMap<String, Object>>) document.get("Patient.upcomingAppointments");

                    for (HashMap<String, Object> existingAppointmentMap: existingAppointmentRaw){
                        EventItem patientAppointment = new EventItem();
                        patientAppointment.setEventDate((Timestamp) existingAppointmentMap.get("date"));
                        patientAppointment.setStartTime((Timestamp) existingAppointmentMap.get("startTime"));
                        patientAppointment.setEndTime((Timestamp) existingAppointmentMap.get("endTime"));
                        patientAppointment.setEventPatient(patient);
                        appointments.add(patientAppointment);
                    }

                }//if past appointments button is pressed adds past appointments to appointment array
                else if (getListType.equals("pastAppointments")){

                    ArrayList<HashMap<String, Object>> existingAppointmentRaw = (ArrayList<HashMap<String, Object>>) document.get("Patient.pastAppointments");

                    for (HashMap<String, Object> existingAppointmentMap: existingAppointmentRaw){
                        EventItem patientAppointment = new EventItem();
                        patientAppointment.setEventDate((Timestamp) existingAppointmentMap.get("date"));
                        patientAppointment.setStartTime((Timestamp) existingAppointmentMap.get("startTime"));
                        patientAppointment.setEndTime((Timestamp) existingAppointmentMap.get("endTime"));
                        patientAppointment.setEventPatient(patient);
                        appointments.add(patientAppointment);
                    }

                }


            }

            if(adapter == null){
                adapter = new EventListView(PatientViewAppointments.this,appointments);
                listViewAppointments.setAdapter(adapter);
            }else{
                adapter.clear();
                adapter.addAll(appointments);
                adapter.notifyDataSetChanged();
            }
        });

    }

    private void showAppointmentInformation(EventItem patientAppointmentEvent){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.view_event_info, null);
        dialogBuilder.setView(dialogView);
        TextView tvAppointmentInfo = (TextView) dialogView.findViewById(R.id.eventInformation);
        dialogBuilder.setTitle("Next Appointment: ");
        final AlertDialog b = dialogBuilder.create();
        b.show();
        tvAppointmentInfo.setText(patientAppointmentEvent.displayPatientEventInfo());

    }

}
