package com.example.doctorregistration.Patient.Frontend;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doctorregistration.Doctor.Doctor;
import com.example.doctorregistration.Other.EventItem;
import com.example.doctorregistration.Other.EventListView;
import com.example.doctorregistration.Other.Firebase;
import com.example.doctorregistration.Patient.Backend.DoctorItem;
import com.example.doctorregistration.Patient.Backend.PatientAppointmentManager;
import com.example.doctorregistration.Patient.Patient;
import com.example.doctorregistration.R;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;

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
    private Button buttonpasttopatientwelcome;
    private Button buttonupcomingtopatientwelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //calls the UI page of upcoming appointments list if upcoming appointment button is pressed by user
        if(getListType.equals("upcomingAppointments")){
            setContentView(R.layout.activity_patient_upcomingappointmentsnew);
            listViewAppointments = (ListView) findViewById(R.id.listViewAppointments1);
            buttonupcomingtopatientwelcome = findViewById(R.id.backtoPW1);
            buttonupcomingtopatientwelcome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openPatientWelcome();
                }
            });
        //calls the UI page of past appointments list if past appointment button is pressed by user
        }else{
            setContentView(R.layout.activity_patientpastappoinments);
            listViewAppointments = (ListView) findViewById(R.id.listViewAppointments2);
            buttonpasttopatientwelcome = findViewById(R.id.backtoPW2);
            buttonpasttopatientwelcome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openPatientWelcome();
                }
            });


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
                    ArrayList<HashMap<String, Object>> existingAppointmentRaw = (ArrayList<HashMap<String, Object>>) document.get("upcomingAppointments");

                    for (HashMap<String, Object> existingAppointmentMap: existingAppointmentRaw){
                        EventItem patientAppointment = new EventItem();
                        patientAppointment.setEventDate((Timestamp) existingAppointmentMap.get("date"));
                        patientAppointment.setStartTime((Timestamp) existingAppointmentMap.get("startTime"));
                        patientAppointment.setEndTime((Timestamp) existingAppointmentMap.get("endTime"));

                        HashMap<String, Object> doctorInfoRaw = (HashMap<String, Object>) existingAppointmentMap.get("patientDoctor");
                        HashMap<String, Object> doctorObjectInfoRaw = (HashMap<String, Object>) doctorInfoRaw.get("doctor");

                        DoctorItem doctorItem = new DoctorItem();
                        Doctor doctor = new Doctor();

                        doctor.setLastName((String)doctorObjectInfoRaw.get("lastName"));
                        doctor.setFirstName((String)doctorObjectInfoRaw.get("firstName"));
                        doctor.setSpecialty((ArrayList<String>)doctorObjectInfoRaw.get("specialty"));

                        doctorItem.setDoctor(doctor);
                        doctorItem.setUserID((String)doctorInfoRaw.get("userID"));

                        patientAppointment.setPatientDoctor(doctorItem);

                        appointments.add(patientAppointment);
                    }

                }//if past appointments button is pressed adds past appointments to appointment array
                else if (getListType.equals("pastAppointments")){

                    ArrayList<HashMap<String, Object>> existingAppointmentRaw = (ArrayList<HashMap<String, Object>>) document.get("pastAppointments");

                    for (HashMap<String, Object> existingAppointmentMap: existingAppointmentRaw){
                        EventItem patientAppointment = new EventItem();
                        patientAppointment.setEventDate((Timestamp) existingAppointmentMap.get("date"));
                        patientAppointment.setStartTime((Timestamp) existingAppointmentMap.get("startTime"));
                        patientAppointment.setEndTime((Timestamp) existingAppointmentMap.get("endTime"));
                        patientAppointment.setRating(((Long) existingAppointmentMap.get("rating")).intValue());

                        HashMap<String, Object> doctorInfoRaw = (HashMap<String, Object>) existingAppointmentMap.get("patientDoctor");
                        HashMap<String, Object> doctorObjectInfoRaw = (HashMap<String, Object>) doctorInfoRaw.get("doctor");

                        DoctorItem doctorItem = new DoctorItem();
                        Doctor doctor = new Doctor();

                        doctor.setLastName((String)doctorObjectInfoRaw.get("lastName"));
                        doctor.setFirstName((String)doctorObjectInfoRaw.get("firstName"));
                        doctor.setSpecialty((ArrayList<String>)doctorObjectInfoRaw.get("specialty"));

                        doctorItem.setDoctor(doctor);
                        doctorItem.setUserID((String)doctorInfoRaw.get("userID"));

                        patientAppointment.setPatientDoctor(doctorItem);

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
        Button delete = (Button) dialogView.findViewById(R.id.deleteEvent);
        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        Button rate = dialogView.findViewById(R.id.rateEvent);


        dialogBuilder.setTitle("Next Appointment: ");
        final AlertDialog b = dialogBuilder.create();
        b.show();
        tvAppointmentInfo.setText(patientAppointmentEvent.displayPatientEventInfo());

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appointmentManager.deleteAppointment(getApplicationContext(), patientAppointmentEvent);
                adapter.notifyDataSetChanged();

            }
        });
        // Only show rating components for past appointments
        if (getListType.equals("pastAppointments")) {
            rate.setVisibility(View.VISIBLE);
            ratingBar.setVisibility(View.VISIBLE);

            rate.setOnClickListener(v -> {
                int selectedRating = (int) ratingBar.getRating();
                patientAppointmentEvent.setRating(selectedRating);
                showRatingMessage(selectedRating);
                b.dismiss();
            });
        } else {
            rate.setVisibility(View.GONE);
            ratingBar.setVisibility(View.GONE);
        }
    }
    public void openPatientWelcome() {
        Intent intent = new Intent(getApplicationContext(), PatientWelcome.class);
        startActivity(intent);
    }

    private void showRatingMessage(int rating) {
        String message = "Appointment rated " + rating + "/5 stars";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
