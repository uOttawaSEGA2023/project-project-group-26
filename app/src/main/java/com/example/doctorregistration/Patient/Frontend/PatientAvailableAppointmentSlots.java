package com.example.doctorregistration.Patient.Frontend;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.example.doctorregistration.Other.EventItem;
import com.example.doctorregistration.Doctor.Doctor;
import com.example.doctorregistration.Other.Firebase;
import com.example.doctorregistration.Patient.Backend.DoctorItem;
import com.example.doctorregistration.Patient.Backend.PatientAppointment;
import com.example.doctorregistration.Patient.Backend.PatientAppointmentListView;
import com.example.doctorregistration.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PatientAvailableAppointmentSlots extends AppCompatActivity {
    ListView listViewAppointment;
    SearchView searchView;
    PatientAppointmentListView adapter;
    ArrayList<DoctorItem> doctorList;
    ArrayList<PatientAppointment> selectedAppointments;

    CollectionReference collectionRef;
    Firebase firebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_available_appointment_slots);

        listViewAppointment = (ListView) findViewById(R.id.listView);
        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();

        doctorList = new ArrayList<>();
        firebase = new Firebase();
        collectionRef = firebase.getCollectionRef("Approved Requests");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });



        //When item is clicked, alert box will appear displaying user information
        listViewAppointment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click
                DoctorItem doctor = doctorList.get(position);
                String userID = doctor.getUserID();

                showAvailabilityInfo(doctor, userID);


            }
        });
    }

    private void filterList(String text) {
        ArrayList<DoctorItem> filteredList = new ArrayList<>();

        for(DoctorItem doctor : doctorList){
            ArrayList<String> doctorSpecialty = doctor.getDoctor().getSpecialty();

            for(String speciality : doctorSpecialty){
                if(speciality.toLowerCase().contains(text.toLowerCase())){
                    filteredList.add(doctor);
                }
            }
        }

        if(filteredList.isEmpty()){
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }else{
            adapter.setFilteredList(filteredList);
        }
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
                doctorList.clear();

                //Grabs documents from "Approved Requests" collection
                for (QueryDocumentSnapshot documentSnapshot : value) {
                    DoctorItem doc = new DoctorItem();

                    doc.setUserID(documentSnapshot.getId());
                    String userType = documentSnapshot.getString("userType");

                    if ("Doctor".equals(userType)) {
                        Doctor doctor = new Doctor();

                        doctor.setFirstName(documentSnapshot.getString("Doctor.firstName"));
                        doctor.setLastName(documentSnapshot.getString("Doctor.lastName"));
                        doctor.setEmail(documentSnapshot.getString("Doctor.email"));

                        ArrayList<String> specialtyList = (ArrayList<String>) documentSnapshot.get("Doctor.specialty");
                        doctor.setSpecialty(new ArrayList<>(specialtyList));

                        ArrayList<EventItem> availability = (ArrayList<EventItem>) documentSnapshot.get("Doctor.availability");
                        doctor.setAvailability(new ArrayList<>(availability));

                        doc.setDoctor(doctor);
                    }

                    else
                        Log.e("ERROR","no userType");

                    doctorList.add(doc);
                }

                if (adapter == null) {
                    //If there is no adapter, create one
                    adapter = new PatientAppointmentListView(PatientAvailableAppointmentSlots.this, doctorList);
                    listViewAppointment.setAdapter(adapter);

                } else {
                    // Adapter already exists, update the data and notifyDataSetChanged
                    adapter.clear();
                    adapter.addAll(doctorList);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }


    /**
     * This method displays the Alert Box when clicked
     */
    private void showAvailabilityInfo(DoctorItem doc, String userID) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.create_appointment_doctor_availability_info, null);
        dialogBuilder.setView(dialogView);

        CalendarView calenderView = findViewById(R.id.calendarView);
        ListView availableSlots = findViewById(R.id.listView);
        Button btConfirm = (Button) dialogView.findViewById(R.id.buttonConfirm);

        calenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                availableSlots.removeAllViews();

                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);

                // Generate checkboxes based on doctor's shifts for the selected date
                int i = 0;
                for(DoctorItem item : doctorList){
                    EventItem shift = item.getDoctor().getAvailability().get(i);
                    i++;
/*
                    if(shift.getDate().equals(selectedDate)) {
                        generateCheckboxesForDoctorShifts(shift, availableSlots, item, selectedDate);
                        break;
                    }*/
                }
            }
        });

        dialogBuilder.setTitle("Dr. " + doc.getDoctor().getFirstName() + ", " + doc.getDoctor().getLastName() + " Availability");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAppointment(selectedAppointments);
            }
        });
    }


    public void generateCheckboxesForDoctorShifts(EventItem shift, ListView checkboxList, DoctorItem docInfo, Calendar date){
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        //FIXXXX
        Calendar calendar = null;//(Calendar) shift.getStartTime().clone();

        try {
            // Iterate through 30-minute increments and generate checkboxes
            while (calendar.before(shift.getEndTime())) {
                Calendar startTime = (Calendar) calendar.clone();

                // Add 30 minutes to the current time to get the end time
                calendar.add(Calendar.MINUTE, 30);

                // Create a Calendar instance for the end time of the interval
                Calendar endTime = (Calendar) calendar.clone();

                // Create a string representation of the interval (e.g., "09:00 AM - 09:30 AM")
                String interval = sdf.format(startTime.getTime()) + " - " + sdf.format(endTime.getTime());

                CheckBox checkBox = new CheckBox(this);
                checkBox.setText(interval);
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Handle checkbox selection (e.g., book the appointment)
                        PatientAppointment appointment = new PatientAppointment(startTime, endTime, docInfo.getDoctor(), date);
                        selectedAppointments.add(appointment);

                    }
                });

                // Add the checkbox to the container
                checkboxList.addView(checkBox);

                // Move to the next 30-minute interval
                calendar.add(Calendar.MINUTE, 30);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addAppointment(ArrayList<PatientAppointment> appointmentList){

        for(PatientAppointment app : appointmentList) {
            //firebase.addElementToArrayList("Approved Requests", null, app);
            //firebase.addElementToArrayList("user", firebase.getCurrentUser(), app);  //WATCH OUT, userID may not be properly initialized!
        }

        //TODO: remove slot from doctor availability


    }

    public void deleteAppointment(){
        //will update availability array of the selected doctor


    }



}
