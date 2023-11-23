package com.example.doctorregistration.Admin.Backend;

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

import com.example.doctorregistration.Other.Address;
import com.example.doctorregistration.Doctor.Doctor;
import com.example.doctorregistration.Other.Firebase;
import com.example.doctorregistration.Patient.Patient;
import com.example.doctorregistration.R;
import com.example.doctorregistration.Other.RegistrationRequestItem;
import com.example.doctorregistration.Other.RegistrationRequestListView;
import com.example.doctorregistration.Other.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * The AdminPendingRequest class sets up the activity where
 * administrators can view denied requests,
 * including initializing the RecyclerView and the adapter.
 */
public class AdminPendingRequest extends AppCompatActivity {

    ListView listViewRequests;
    RegistrationRequestListView adapter;
    ArrayList<RegistrationRequestItem> pendingRequests;
    CollectionReference collectionRef;
    Firebase firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pending_request);

        listViewRequests = (ListView) findViewById(R.id.listView);
        pendingRequests = new ArrayList<>();

        firebase = new Firebase();
        collectionRef = firebase.getCollectionRef("Pending Requests");

        //When item is clicked, alert box will appear displaying user information
        listViewRequests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click
                RegistrationRequestItem userRequest = pendingRequests.get(position);

                String userID = userRequest.getUserID();

                User doctor = userRequest.getDoctor();
                User patient = userRequest.getPatient();
                String userType = userRequest.getUserType();

                if(userType.equals("Doctor"))
                    showUserInformation(doctor, userType, userID);

                else
                    showUserInformation(patient, userType, userID);

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
                // Clear the existing list of pending requests
                pendingRequests.clear();

                //Grabs documents from "Pending Requests" collection
                for (QueryDocumentSnapshot documentSnapshot : value) {
                    RegistrationRequestItem request = new RegistrationRequestItem();

                    request.setUserID(documentSnapshot.getId());
                    String userType = documentSnapshot.getString("userType");

                    if ("Doctor".equals(userType)) {
                        Doctor doctor = new Doctor();

                        //Initialize Doctor variables by grabbing info from firestore
                        doctor.setFirstName(documentSnapshot.getString("Doctor.firstName"));
                        doctor.setLastName(documentSnapshot.getString("Doctor.lastName"));
                        doctor.setPhoneNumber(documentSnapshot.getLong("Doctor.phoneNumber").intValue());
                        doctor.setEmail(documentSnapshot.getString("Doctor.email"));
                        doctor.setIdNumber(documentSnapshot.getLong("Doctor.idNumber").intValue());

                        ArrayList<String> specialtyList = (ArrayList<String>) documentSnapshot.get("Doctor.specialty");
                        doctor.setSpecialty(new ArrayList<>(specialtyList));

                        Address address = new Address();
                        address.setCity(documentSnapshot.getString("Doctor.address.city"));
                        address.setCountry(documentSnapshot.getString("Doctor.address.country"));
                        address.setStreet(documentSnapshot.getString("Doctor.address.street"));
                        address.setPostalCode(documentSnapshot.getString("Doctor.address.postalCode"));

                        doctor.setAddress(address);

                        request.setDoctor(doctor);
                        request.setUserType(userType);

                    } else {
                        Patient patient = new Patient();

                        //Initialize Patient variables by grabbing info from firestore
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

                        request.setPatient(patient);
                        request.setUserType(userType);
                    }

                    pendingRequests.add(request);
                }


                if (adapter == null) {
                    //If there is no adapter, create one
                    adapter = new RegistrationRequestListView(AdminPendingRequest.this, pendingRequests);
                    listViewRequests.setAdapter(adapter);
                } else {
                    // Adapter already exists, update the data and notifyDataSetChanged
                    adapter.clear();
                    adapter.addAll(pendingRequests);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * This method displays the Alert Box when clicked
     */
    private void showUserInformation(User user, String userType, String userID) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.pending_request_user_info, null);
        dialogBuilder.setView(dialogView);

        TextView tvFirstName = (TextView) dialogView.findViewById(R.id.userInformation);
        Button btApprove = (Button) dialogView.findViewById(R.id.buttonApprove);
        Button btReject = (Button) dialogView.findViewById(R.id.buttonReject);

        dialogBuilder.setTitle(userType + " Registration Info");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        tvFirstName.setText(user.displayUserInformation());

        btApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebase.updateUserField(AdminPendingRequest.this, "user", userID,
                        "accountStatus", "approved");
                firebase.moveUserToAnotherCollection("Pending Requests","Approved Requests",userID);
                //firebase.removeUserFromCollection("Pending Requests", userID);
            }
        });

        btReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebase.updateUserField(AdminPendingRequest.this, "user", userID,
                        "accountStatus", "denied");

                firebase.updateUserField(AdminPendingRequest.this, "Pending Requests", userID,
                        "accountStatus", "denied");

                firebase.moveUserToAnotherCollection("Pending Requests",
                        "Denied Requests", userID);

            }
        });
    }
}


