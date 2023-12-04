package com.example.doctorregistration.Patient.Frontend;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.example.doctorregistration.Doctor.Frontend.DoctorCreateShift;
import com.example.doctorregistration.Other.EventItem;
import com.example.doctorregistration.Doctor.Doctor;
import com.example.doctorregistration.Other.Firebase;
import com.example.doctorregistration.Patient.Backend.DoctorItem;
import com.example.doctorregistration.Patient.Backend.PatientAppointment;
import com.example.doctorregistration.Patient.Backend.PatientAppointmentManager;
import com.example.doctorregistration.Patient.Backend.PatientDoctorsListView;
import com.example.doctorregistration.Patient.Patient;
import com.example.doctorregistration.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

//This Activity is the where the patient can search for doctors based on speciality and
public class PatientCreateAppointmentSlots extends AppCompatActivity {
    ListView listViewAppointment;
    SearchView searchView;
    PatientDoctorsListView adapter;
    DoctorItem selectedDoctor;
    ArrayList<DoctorItem> doctorList;
    EventItem selectedAppointment;
    PatientAppointmentManager appointmentManager;
    CollectionReference collectionRef;
    Firebase firebase;
    private Timestamp selectedStartTime;
    private Timestamp selectedEndTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_available_appointment_slots);

        listViewAppointment = (ListView) findViewById(R.id.listView);
        searchView = findViewById(R.id.searchView);

        doctorList = new ArrayList<>();

        selectedAppointment = new EventItem();

        appointmentManager = new PatientAppointmentManager();

        firebase = new Firebase();
        collectionRef = firebase.getCollectionRef("Approved Requests");

        searchView.requestFocus();
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

                selectedAppointment.setPatientDoctor(doctor);

                showAvailabilityInfo(doctor, userID);


            }
        });
    }

    private void filterList(String text) {
        ArrayList<DoctorItem> filteredList = new ArrayList<>();

        for (DoctorItem doctor : doctorList) {
            ArrayList<String> doctorSpecialty = doctor.getDoctor().getSpecialty();

            for (String speciality : doctorSpecialty) {
                if (speciality.toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(doctor);
                    break;
                }
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        } else {
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

                    //yudoc.setUserID(documentSnapshot.getId());
                    String userType = documentSnapshot.getString("userType");

                    if ("Doctor".equals(userType)) {
                        doc.setUserID(documentSnapshot.getId());

                        Doctor doctor = new Doctor();

                        doctor.setFirstName(documentSnapshot.getString("Doctor.firstName"));
                        doctor.setLastName(documentSnapshot.getString("Doctor.lastName"));
                        doctor.setEmail(documentSnapshot.getString("Doctor.email"));

                        ArrayList<String> specialtyList = (ArrayList<String>) documentSnapshot.get("Doctor.specialty");
                        doctor.setSpecialty(specialtyList);

                        //ArrayList<EventItem> availability = (ArrayList<EventItem>) documentSnapshot.get("Doctor.availability");
                        //doctor.setAvailability(availability);

                        doc.setDoctor(doctor);
                        doctorList.add(doc);
                    } else
                        Log.e("ERROR", "no userType");
                }

                if (adapter == null) {
                    //If there is no adapter, create one
                    adapter = new PatientDoctorsListView(PatientCreateAppointmentSlots.this, doctorList);
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
        final View dialogView = inflater.inflate(R.layout.test_spinner, null);
        dialogBuilder.setView(dialogView);

        CalendarView calenderView = dialogView.findViewById(R.id.calendarView);
        Spinner availableSlots = dialogView.findViewById(R.id.spinner);
        Button btConfirm = (Button) dialogView.findViewById(R.id.buttonConfirm);

        dialogBuilder.setTitle("Dr. " + doc.getDoctor().getLastName() + ", " + doc.getDoctor().getFirstName() + " Availability");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        calenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);

                Date selectedDateAsDate = selectedDate.getTime();

                // Generate checkboxes based on doctor's shifts for the selected date
                DocumentReference documentReference = collectionRef.document(userID);
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String userType = documentSnapshot.getString("userType");

                            ArrayList<String> optionTimeSlots = new ArrayList<>();

                            ArrayList<HashMap<String, Object>> existingShiftsRaw = (ArrayList<HashMap<String, Object>>) documentSnapshot.get("Doctor.availability");

                            for (HashMap<String, Object> existingShiftMap : existingShiftsRaw) {
                                Timestamp existingDate = (Timestamp) existingShiftMap.get("date");
                                Timestamp existingStartTime = (Timestamp) existingShiftMap.get("startTime");
                                Timestamp existingEndTime = (Timestamp) existingShiftMap.get("endTime");
                                boolean existingPatient = (boolean) existingShiftMap.get("associatedWithPatient");

                                EventItem existingShift = new EventItem(existingStartTime, existingEndTime, existingDate, existingPatient);

                                Date doctorDate = existingDate.toDate();

                                if (isSameDate(selectedDateAsDate, doctorDate)) {
                                    if (existingShift.isDateValid(selectedDate) && existingPatient == false) { //Checks if the date selected has not passed

                                        Date date = selectedDate.getTime();
                                        Timestamp timestamp = new Timestamp(date);

                                        selectedAppointment.setEventDate(timestamp);
                                        selectedAppointment.setEventDoctor(doc.getDoctor());

                                        String startTimeFormatted = formatTimestamp(existingStartTime);
                                        String endTimeFormatted = formatTimestamp(existingEndTime);
                                        // Add the time slot to the list
                                        optionTimeSlots.add(startTimeFormatted + " - " + endTimeFormatted);

                                    } else {
                                        Toast.makeText(PatientCreateAppointmentSlots.this, "Select date that has passed"
                                                , Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }

                            generateSpinnerTimeSlots(optionTimeSlots, doc, availableSlots);

                        } else
                            Log.e(TAG, "Document not found!");
                    }


                });

            }
        });

        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Checks if all fields are set
                if (selectedAppointment.getEndTime() != null && selectedAppointment.getStartTime() != null && selectedAppointment.getDate() != null) {
                        appointmentManager.addAppointment(selectedAppointment, getApplicationContext());
                    Toast.makeText(PatientCreateAppointmentSlots.this, "Appointment Created", Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(PatientCreateAppointmentSlots.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void generateSpinnerTimeSlots(ArrayList<String> optionsList, DoctorItem doctor, Spinner spinnerOptions) {
        // Create an ArrayAdapter using the list of options
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, optionsList);

        // Set the dropdown layout style
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the ArrayAdapter on the Spinner
        spinnerOptions.setAdapter(adapter);

        spinnerOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selection of an item
                String selectedOption = optionsList.get(position);
                String[] timeParts = selectedOption.split(" - ");

                String startTimeString = timeParts[0];
                String endTimeString = timeParts[1];

                // Convert the time strings to Timestamp objects
                selectedStartTime = convertStringToTimestamp(startTimeString);
                selectedEndTime = convertStringToTimestamp(endTimeString);

                selectedAppointment.setStartTime(selectedStartTime);
                selectedAppointment.setEndTime(selectedEndTime);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

    }


    private Timestamp convertStringToTimestamp(String timeString) {
        try {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.US);
            Date parsedDate = timeFormat.parse(timeString);
            return new Timestamp(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String formatTimestamp(Timestamp timestamp) {
        // Convert the Firestore timestamp to a Date
        Date date = timestamp.toDate();

        // Format the Date in 24-hour format
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
        return sdf.format(date);
    }

    private boolean isSameDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }

}
