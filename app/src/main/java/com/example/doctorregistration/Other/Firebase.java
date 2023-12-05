package com.example.doctorregistration.Other;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.doctorregistration.Doctor.Doctor;
import com.example.doctorregistration.Patient.Backend.PatientAppointment;
import com.example.doctorregistration.Patient.Patient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The Firebase class holds all variables and methods related to firebase and firestore
 */
public class Firebase {
    static FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    CollectionReference collectionRef;
    private String userID;

    //This method authenticates and logins in users
    public void loginAuthentication(Context context, String emailAddress, String password, AuthenticationCallback callback) {

        fAuth.signInWithEmailAndPassword(emailAddress, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String userId = fAuth.getCurrentUser().getUid(); //Obtains current users userID
                    DocumentReference docRef = fStore.collection("user").document(userId); //Navigates to users data in Firestore

                    docRef.get().addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String userType = documentSnapshot.getString("userType");
                            String accountStatus = documentSnapshot.getString("accountStatus");
                            String email = documentSnapshot.getString("email");

                            if (userType != null) {
                                if (userType.equals("Doctor") && isAccountStatusApproved(context, accountStatus, email)) {
                                    callback.onSuccess("Doctor");

                                } else if (userType.equals("Patient") && isAccountStatusApproved(context, accountStatus, email)) {
                                    callback.onSuccess("Patient");

                                } else if (userType.equals("Admin")) {
                                    callback.onSuccess("Admin");
                                }

                            } else {
                                Log.d(TAG, "Document Data is null");
                            }
                        } else {
                            Log.d(TAG, "Document does not exist");
                        }

                    });

                } else {
                    callback.onFailure("Invalid");
                }
            }
        });
    }

    /**
     * This method accesses the users information in Firestore and finds if the account status has been
     * approved, denied or is still pending by the Administrator
     *
     * @param accountStatus The status of the registration
     * @param email         The users email that will receive the email
     * @return true if the status is approved, false otherwise
     */
    public boolean isAccountStatusApproved(Context context, String accountStatus, String email) {
        SendEmail task = new SendEmail();
        String message = "";

        if (accountStatus.equals("approved")) {
            message = "Your registration request for the Tellewellness health app" +
                    " has been approved! You can now login with your credentials";
            //task.sendEmail(email, message);
            Toast.makeText(context, "Registration was approved by Administrator",
                    Toast.LENGTH_SHORT).show();
            return true;
        } else if (accountStatus.equals("denied")) {
            message = "Unfortunately, your registration request has been rejected by the Administrator." +
                    " Please contact us to resolve this issue, (613) 123 - 4567";
            //task.sendEmail(email, message);
            Toast.makeText(context, "Registration was rejected by Administrator. Please contact" +
                    "the Administrator resolve this issue, (613) 123 - 4567", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Registration has not yet been approved by Administrator",
                    Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    public interface AuthenticationCallback {
        void onSuccess(String userType);

        void onFailure(String message);
    }

    /**
     * This method creates new users, used when registering Doctors and Patients
     */
    public void createNewUser(Context context, String userType, int idNumber, ArrayList<CheckBox> specialtyList, String firstName,
                              String lastName, String email, String password, int phoneNumber, String country, String city,
                              String street, String postalCode, AuthenticationCallback callback) {

        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Registration Successful.", Toast.LENGTH_SHORT).show();

                    userID = fAuth.getCurrentUser().getUid();
                    DocumentReference documentReferenceUser = fStore.collection("user").document(userID);
                    DocumentReference documentReferencePending = fStore.collection("Pending Requests").document(userID);

                    Map<String, Object> user = new HashMap<>();
                    Map<String, Object> pendingRequests = new HashMap<>();

                    if (userType.equals("Doctor")) {

                        ArrayList<String> doctorSpecialtyList = new ArrayList<>(); //create ArrayList of Doctor user specialties
                        for (CheckBox cb : specialtyList) {
                            if (cb.isChecked())
                                doctorSpecialtyList.add(cb.getText().toString()); //copies all checked boxes from previously defined specialty ArrayList

                        }

                        ArrayList<EventItem> shifts = new ArrayList<>();
                        ArrayList<EventItem> availability = new ArrayList<>();

                        Address address = new Address(street, postalCode, city, country);   //create Doctor user address
                        Doctor doctorUser = new Doctor(idNumber, doctorSpecialtyList, firstName, lastName, //create Doctor user
                                email, password, phoneNumber, address, shifts, availability);

                        //Places user data into Firestore collection "user"
                        user.put(userType, doctorUser);
                        user.put("userType", userType); //Stores Doctor user information if Firestore database
                        user.put("accountStatus", "pending");
                        user.put("shifts", shifts);
                        user.put("availability", availability);

                        //Places user data into Firestore collection "Pending Requests"
                        pendingRequests.put(userType, doctorUser);
                        pendingRequests.put("userType", userType);
                        pendingRequests.put("accountStatus", "pending");
                        pendingRequests.put("shifts", shifts);
                        pendingRequests.put("availability", availability);


                    } else if (userType.equals("Patient")) {
                        ArrayList<EventItem> upcomingAppointments = new ArrayList<>();
                        ArrayList<EventItem> pastAppointments = new ArrayList<>();

                        Address address = new Address(street, postalCode, city, country);
                        Patient patientUser = new Patient(firstName, lastName, email, idNumber, phoneNumber, address, password,
                                upcomingAppointments, pastAppointments);

                        user.put(userType, patientUser);       //Stores Patient user information in Firestore database
                        user.put("userType", userType);
                        user.put("accountStatus", "pending");
                        user.put("upcomingAppointments", upcomingAppointments);
                        user.put("pastAppointments", pastAppointments);

                        //Places user data into Firestore collection "Pending Requests"
                        pendingRequests.put(userType, patientUser);
                        pendingRequests.put("userType", userType);
                        pendingRequests.put("accountStatus", "pending");
                        pendingRequests.put("upcomingAppointments", upcomingAppointments);
                        pendingRequests.put("pastAppointments", pastAppointments);

                    } else {
                        Log.d(TAG, "Invalid userType in Firebase.class -> createNewUser()");
                    }

                    documentReferenceUser.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "User profile created " + userID);
                        }
                    });

                    documentReferencePending.set(pendingRequests).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "User profile created and account is pending" + userID);
                        }
                    });

                    callback.onSuccess(userType);

                } else
                    task.getException(); //Check Logcat if task is unsuccessful or app crashes for error message
            }
        });
    }

    /**
     * This method updates document fields
     * (May cause issue if you attempt to change field inside an Object, may require testing!)
     */
    public static <T> void updateUserField(Context context, String collectionPath,
                                           String userID, String fieldName, T newValue) {
        CollectionReference collectionReference = fStore.collection(collectionPath);
        DocumentReference documentReference = collectionReference.document(userID);

        documentReference.update(fieldName, newValue).addOnSuccessListener(aVoid -> {
            //Toast.makeText(context, "Information updated", Toast.LENGTH_SHORT).show();

        }).addOnFailureListener(e -> {
            // Handle errors
        });
    }


    public void addElementToArrayList(Context context, String collectionPath, String userID, String arrayFieldName, Object elementToBeAdded,
                                      String doctorUserID) { //doctorUserID is ONLY used for patient functionality
        CollectionReference collectionReference = fStore.collection(collectionPath);
        DocumentReference documentReference = collectionReference.document(userID);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String userType = documentSnapshot.getString("userType");

                    if (userType.equals("Doctor")) {
                        ArrayList<HashMap<String, Object>> existingAppRaw = (ArrayList<HashMap<String, Object>>) documentSnapshot.get("Doctor.shifts");
                        EventItem shiftToAdd = (EventItem) elementToBeAdded;

                        for (HashMap<String, Object> existingAppMap : existingAppRaw) {
                            Timestamp existingDate = (Timestamp) existingAppMap.get("date");
                            Timestamp existingAppStartTime = (Timestamp) existingAppMap.get("startTime");
                            Timestamp existingEndTime = (Timestamp) existingAppMap.get("endTime");
                            boolean existingPatient = (boolean) existingAppMap.get("associatedWithPatient");

                            EventItem shift = new EventItem(existingAppStartTime, existingEndTime, existingDate, existingPatient);

                            if (shift.overlapsWith(shiftToAdd)) {
                                Toast.makeText(context, "Shift Conflict!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        updateUserFieldToAdd(null, collectionPath, userID, "Doctor." + arrayFieldName, shiftToAdd);
                        Toast.makeText(context, "Shift Created", Toast.LENGTH_SHORT).show();

                    } else if (userType.equals("Patient")) {

                        EventItem appointmentToAdd = (EventItem) elementToBeAdded;
                        updateUserFieldToAdd(null, collectionPath, userID, arrayFieldName, appointmentToAdd);

                        Timestamp appointmentToAddDate = appointmentToAdd.getDate();

                        DocumentReference documentReferenceDoctor = collectionReference.document(doctorUserID);
                        documentReferenceDoctor.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    String userType = documentSnapshot.getString("userType");

                                    if (userType.equals("Doctor")) {

                                        ArrayList<HashMap<String, Object>> existingAppRaw = (ArrayList<HashMap<String, Object>>) documentSnapshot.get("Doctor.availability");
                                        ArrayList<EventItem> appList = new ArrayList<>();
                                        EventItem appUpdated;
                                        EventItem app;
                                        Timestamp existingAppDate = null;

                                        for (HashMap<String, Object> existingAppMap : existingAppRaw) {
                                            existingAppDate = (Timestamp) existingAppMap.get("date");
                                            Timestamp existingAppStartTime = (Timestamp) existingAppMap.get("startTime");
                                            Timestamp existingAppEndTime = (Timestamp) existingAppMap.get("endTime");
                                            boolean existingPatient = (boolean) existingAppMap.get("associatedWithPatient");

                                            app = new EventItem(existingAppStartTime, existingAppEndTime, existingAppDate, existingPatient);

                                            if (appointmentToAdd.equals(app)) {
                                                appUpdated = new EventItem(existingAppStartTime, existingAppEndTime, existingAppDate,true);
                                                appList.add(appUpdated);
                                    } else
                                        appList.add(app);
                                }
                                        updateUserField(null, "Approved Requests", doctorUserID, "Doctor.availability", appList);

                                ArrayList<HashMap<String, Object>> existingShiftsRaw = (ArrayList<HashMap<String, Object>>) documentSnapshot.get("Doctor.shifts");
                                        ArrayList<EventItem> shiftsList = new ArrayList<>();
                                        EventItem shiftUpdated;
                                        EventItem shift;

                                        for (HashMap<String, Object> existingShiftMap : existingShiftsRaw) {
                                            Timestamp existingDate = (Timestamp) existingShiftMap.get("date");
                                            Timestamp existingStartTime = (Timestamp) existingShiftMap.get("startTime");
                                            Timestamp existingEndTime = (Timestamp) existingShiftMap.get("endTime");
                                            boolean existingPatient = (boolean) existingShiftMap.get("associatedWithPatient");

                                            shift = new EventItem(existingStartTime, existingEndTime, existingDate, existingPatient);

                                            if (isSameDate(existingDate.toDate(), existingAppDate.toDate())) {
                                                shiftUpdated = new EventItem(existingStartTime, existingEndTime, existingDate,true);
                                                shiftsList.add(shiftUpdated);
                                            } else
                                                shiftsList.add(shift);
                                        }
                                        updateUserField(null, "Approved Requests", doctorUserID, "Doctor.shifts", shiftsList);

                                    }
                                }
                                Toast.makeText(context, "Appointment Created", Toast.LENGTH_SHORT).show();
                            }

                        });
                    }

                } else
                    Log.e(TAG, "Document not found!");
            }
        });
    }


    public void addToAvailabilityArrayList(Context context, String collectionPath, String userID, Object elementToBeAdded) {
        CollectionReference collectionReference = fStore.collection(collectionPath);
        DocumentReference documentReference = collectionReference.document(userID);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String userType = documentSnapshot.getString("userType");

                    if (userType.equals("Doctor")) {
                        //ArrayList<HashMap<String, Object>> existingAvailabilityRaw = (ArrayList<HashMap<String, Object>>) documentSnapshot.get("Doctor.availability");
                        EventItem slotToAdd = (EventItem) elementToBeAdded;

                        // Update the specific slot in the "Doctor.availability" field
                        updateUserFieldToAdd(null, collectionPath, userID, "Doctor.availability",
                                slotToAdd);
                    }
                } else {
                    Log.e(TAG, "Document not found!");
                }
            }
        });
    }

    public void updateUserFieldToAdd(Context context, String collectionPath, String userID, String fieldName,
                                     EventItem newValue) {
        CollectionReference collectionReference = fStore.collection(collectionPath);
        DocumentReference documentReference = collectionReference.document(userID);

        documentReference.update(fieldName, FieldValue.arrayUnion(newValue))
                .addOnSuccessListener(aVoid -> {
                    // Handle success
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                });
    }


    public void deleteElementFromArrayList(Context context, String collectionPath, String userID, String arrayFieldName,
                                           Object elementToBeDeleted, String doctorUserID) { //doctorUserID is ONLY used for patient functionality
        CollectionReference collectionReference = fStore.collection(collectionPath);
        DocumentReference documentReference = collectionReference.document(userID);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String userType = documentSnapshot.getString("userType");

                    if (userType.equals("Doctor")) {
                        ArrayList<HashMap<String, Object>> existingShiftsRaw = (ArrayList<HashMap<String, Object>>) documentSnapshot.get("Doctor." + arrayFieldName);
                        EventItem shiftToBeDeleted = (EventItem) elementToBeDeleted;
                        ArrayList<EventItem> existingShifts = new ArrayList<>();

                        for (HashMap<String, Object> existingShiftMap : existingShiftsRaw) {
                            Timestamp existingDate = (Timestamp) existingShiftMap.get("date");
                            Timestamp existingStartTime = (Timestamp) existingShiftMap.get("startTime");
                            Timestamp existingEndTime = (Timestamp) existingShiftMap.get("endTime");
                            boolean existingPatient = (boolean) existingShiftMap.get("associatedWithPatient");


                            EventItem existingShift = new EventItem(existingStartTime, existingEndTime, existingDate, existingPatient);
                            existingShifts.add(existingShift);

                            if (existingShift.equals(shiftToBeDeleted))
                                existingShifts.remove(existingShift); //shift was found and remove
                        }

                        //update the new shifts with the deleted element
                        updateUserField(context, collectionPath, userID, "Doctor." + arrayFieldName, existingShifts);
                        Toast.makeText(context, "Shift Deleted", Toast.LENGTH_SHORT).show();

                    } else if (userType.equals("Patient")) {

                        ArrayList<HashMap<String, Object>> existingAppRaw = (ArrayList<HashMap<String, Object>>) documentSnapshot.get(arrayFieldName);
                        EventItem appToBeDeleted = (EventItem) elementToBeDeleted;
                        ArrayList<EventItem> existingApp = new ArrayList<>();

                        for (HashMap<String, Object> existingShiftMap : existingAppRaw) {
                            Timestamp existingDate = (Timestamp) existingShiftMap.get("date");
                            Timestamp existingStartTime = (Timestamp) existingShiftMap.get("startTime");
                            Timestamp existingEndTime = (Timestamp) existingShiftMap.get("endTime");

                            EventItem existingShift = new EventItem(existingStartTime, existingEndTime, existingDate);
                            existingApp.add(existingShift);

                            if (existingShift.equals(appToBeDeleted))
                                existingApp.remove(existingShift); //shift was found and remove
                        }

                        //update the new shifts with the deleted element
                        updateUserField(context, collectionPath, userID, arrayFieldName, existingApp);
                        Toast.makeText(context, "Appointment Deleted", Toast.LENGTH_SHORT).show();

                        Timestamp appointmentToAddDate = appToBeDeleted.getDate();

                        DocumentReference documentReferenceDoctor = collectionReference.document(doctorUserID);
                        documentReferenceDoctor.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    String userType = documentSnapshot.getString("userType");

                                    if (userType.equals("Doctor")) {
                                        ArrayList<HashMap<String, Object>> existingAppRaw = (ArrayList<HashMap<String, Object>>) documentSnapshot.get("Doctor.availability");
                                        ArrayList<EventItem> appList = new ArrayList<>();
                                        EventItem appUpdated;
                                        EventItem app;
                                        //Timestamp existingAppDate = null;

                                        for (HashMap<String, Object> existingAppMap : existingAppRaw) {
                                            Timestamp existingAppDate = (Timestamp) existingAppMap.get("date");
                                            Timestamp existingAppStartTime = (Timestamp) existingAppMap.get("startTime");
                                            Timestamp existingAppEndTime = (Timestamp) existingAppMap.get("endTime");
                                            boolean existingPatient = (boolean) existingAppMap.get("associatedWithPatient");

                                            app = new EventItem(existingAppStartTime, existingAppEndTime, existingAppDate, existingPatient);

                                            if (appToBeDeleted.equals(app)) {
                                                appUpdated = new EventItem(existingAppStartTime, existingAppEndTime, existingAppDate,false);
                                                appList.add(appUpdated);
                                            } else
                                                appList.add(app);
                                        }
                                        updateUserField(null, "Approved Requests", doctorUserID, "Doctor.availability", appList);

                                    }
                                }
                            }

                        });
                    }

                } else
                    Log.e(TAG, "Document not found!");
            }
        });

    }


    public boolean canDeleteShift(EventItem shiftToDelete, String userID) {
        AtomicBoolean canDelete = new AtomicBoolean(false);

        CollectionReference collectionReference = fStore.collection("Approved Requests");
        DocumentReference documentReference = collectionReference.document(userID);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String userType = documentSnapshot.getString("userType");

                    if (userType.equals("Doctor")) {
                        ArrayList<HashMap<String, Object>> existingShiftsRaw = (ArrayList<HashMap<String, Object>>) documentSnapshot.get("Doctor.shifts");
                        ArrayList<EventItem> shiftsList = new ArrayList<>();

                        for (HashMap<String, Object> existingShiftMap : existingShiftsRaw) {
                            Timestamp existingDate = (Timestamp) existingShiftMap.get("date");
                            Timestamp existingStartTime = (Timestamp) existingShiftMap.get("startTime");
                            Timestamp existingEndTime = (Timestamp) existingShiftMap.get("endTime");
                            boolean existingPatient = (boolean) existingShiftMap.get("associatedWithPatient");

                            EventItem shift = new EventItem(existingStartTime, existingEndTime, existingDate, existingPatient);
                            shiftsList.add(shift);

                            if (shift.equals(shiftToDelete)) {
                                if (existingPatient == false) { //no patients associated with particular shift, can delete
                                    deleteElementFromArrayList(null, "Approved Requests", userID, "shifts", shift, null);

                                    //Deletes all associated available time slots
                                    ArrayList<HashMap<String, Object>> existingAppRaw = (ArrayList<HashMap<String, Object>>) documentSnapshot.get("Doctor.availability");
                                    ArrayList<EventItem> appList = new ArrayList<>();

                                    for (HashMap<String, Object> existingAppMap : existingAppRaw) {
                                        Timestamp existingAppDate = (Timestamp) existingAppMap.get("date");
                                        Timestamp existingAppStartTime = (Timestamp) existingAppMap.get("startTime");
                                        Timestamp existingAppEndTime = (Timestamp) existingAppMap.get("endTime");
                                        boolean existingAppPatient = (boolean) existingAppMap.get("associatedWithPatient");

                                        EventItem app = new EventItem(existingAppDate, existingAppStartTime, existingAppEndTime, true);
                                        appList.add(app);

                                        updateUserField(null, "Approved Requests", userID, "Doctor.availability", appList);
                                    }


                                } else {
                                    return;
                                }
                            }

                        }

                    }

                } else
                    Log.e(TAG, "Document not found!");
            }
        });

        return canDelete.get();
    }

    private static Date getDateWithoutTime(Timestamp timestamp) {
        // Convert the Timestamp to Date
        Date fullDate = timestamp.toDate();

        // Create a Calendar instance and set it to the given date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fullDate);

        // Set the time portion to midnight (start of the day)
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Get the date without time
        return calendar.getTime();
    }

    //returns Firestore collection specified by String
    public CollectionReference getCollectionRef(String collection) {
        return collectionRef = fStore.collection(collection);
    }

    /**
     * Moves a specific document(specified by userID) from one collection to another
     * Note: it does NOT COPY to another collection, document in OG collection will be deleted
     */
    public void moveUserToAnotherCollection(String sourceCollection, String destinationCollection,
                                            String userID) {

        DocumentReference sourceDocumentRef = fStore.collection(sourceCollection).document(userID);
        DocumentReference destinationDocumentRef = fStore.collection(destinationCollection).document(userID);

        sourceDocumentRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Document exists in the source collection

                // Write the document to the destination collection
                destinationDocumentRef.set(documentSnapshot.getData())
                        .addOnSuccessListener(aVoid -> {
                            // Document successfully written to the destination collection
                            // You can perform additional actions if needed

                            Log.i(TAG, "User document moved from " + sourceCollection +
                                    " to " + destinationCollection);

                            removeUserFromCollection(sourceCollection, userID);
                        })
                        .addOnFailureListener(e -> {
                            // Handle errors during the write operation to the destination collection
                        });
            } else {
                // Document does not exist in the source collection
                Log.e(TAG, "User document does not exist " + sourceCollection);
            }
        }).addOnFailureListener(e -> {
            // Handle errors during the read operation from the source collection
        });
    }

    /**
     * Deletes specific document(specified by userID) from specific collection
     */
    public void removeUserFromCollection(String collectionPath, String userId) {
        // Reference to the document in the specified collection
        DocumentReference documentReference = fStore.collection(collectionPath).document(userId);

        // Delete the document
        documentReference.delete()
                .addOnSuccessListener(aVoid -> {
                    // Document successfully deleted
                    Log.i(TAG, "User document deleted from" + collectionPath);
                })
                .addOnFailureListener(e -> {
                    // Handle errors during the delete operation
                });
    }

    public void copyUserToAnotherCollection(String sourceCollection, String destinationCollection,
                                            String userID) {

        DocumentReference sourceDocumentRef = fStore.collection(sourceCollection).document(userID);
        DocumentReference destinationDocumentRef = fStore.collection(destinationCollection).document(userID);

        sourceDocumentRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // checking to see that document exists in the source collection

                // writing the document to the destination collection
                destinationDocumentRef.set(documentSnapshot.getData())
                        .addOnSuccessListener(aVoid -> {
                            // document successfully written to the destination collection!!!
                            Log.i(TAG, "User document copied from " + sourceCollection +
                                    " to " + destinationCollection);
                        });
            } else {
                // or if document does not exist in the source collection
                Log.e(TAG, "User document does not exist " + sourceCollection);
            }
        });
    }

    public String getCurrentUser() {
        return fAuth.getCurrentUser().getUid();
    }

    public boolean isSameDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }
}
