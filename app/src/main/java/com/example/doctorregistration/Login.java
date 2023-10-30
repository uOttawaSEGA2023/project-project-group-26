package com.example.doctorregistration;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class Login extends AppCompatActivity {

    EditText editEmailAddress;
    EditText editPassword;
    Button loginButton;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmailAddress = findViewById(R.id.emailAddress);
        editPassword = findViewById(R.id.password);
        loginButton = findViewById(R.id.buttonLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress, password;

                emailAddress = editEmailAddress.getText().toString();
                password = editPassword.getText().toString();

                if (TextUtils.isEmpty(emailAddress)) {
                    Toast.makeText(Login.this, "Please Enter Email Associated With Account", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseAuth fAuth = FirebaseAuth.getInstance();

                /*
                 *Admin Login Credentials
                 *    email: admin@telewellness.ca
                 *    password: 123456
                 */


                fAuth.signInWithEmailAndPassword(emailAddress, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(Login.this, "Logged in successfully. Redirecting to new page.", Toast.LENGTH_SHORT).show();

                            String userId = fAuth.getCurrentUser().getUid(); //Obtains current users userID
                            DocumentReference docRef = db.collection("user").document(userId); //Navigates to users data in Firestore

                            docRef.get().addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    String userType = documentSnapshot.getString("userType");
                                    String accountStatus = documentSnapshot.getString("accountStatus");
                                    String email = documentSnapshot.getString("email");

                                    if (userType != null) {
                                        if (userType.equals("Doctor") && accountStatusApproved(accountStatus, email)) {
                                            Intent intentDoctor = new Intent(Login.this, DoctorWelcome.class);
                                            startActivity(intentDoctor);
                                            finish();

                                        } else if (userType.equals("Patient") && accountStatusApproved(accountStatus, email)) {
                                            Intent intentPatient = new Intent(Login.this, PatientWelcome.class);
                                            startActivity(intentPatient);
                                            finish();

                                        } else if(userType.equals("Admin")){
                                            Intent intentAdmin = new Intent(Login.this, AdminWelcome.class);
                                            startActivity(intentAdmin);
                                            finish();
                                        }

                                    } else
                                        Log.d(TAG, "Document Data is null");
                                }

                                else
                                    Log.d(TAG, "Document does not exist");

                            });


                        } else {
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                });
            }
        });


    }

    /**
     * This method accesses the users information in Firestore and finds if the account status has been
     * approved, denied or is still pending by the Administrator
     *
     * @param accountStatus
     *              The status of the registration
     * @param email
     *           The users email that will receive the email
     * @return true if the status is approved, false otherwise
     */
    private boolean accountStatusApproved(String accountStatus, String email){
        SendEmail task = new SendEmail();
        String message = "";

        if (accountStatus.equals("approved")){
            message = "Your registration request for the Tellewellness health app" +
                    " has been approved! You can now login with your credentials";
            task.sendEmail(email, message);
            return true;
        }

        else if (accountStatus.equals("denied")){
            message = "Unfortunately, your registration request has been rejected by the Administrator." +
                    " Please contact us to resolve this issue, (613) 123 - 4567";
            task.sendEmail(email, message);
            Toast.makeText(Login.this, "Registration was rejected by Administrator. Please contact" +
                            "the Administrator resolve this issue, (613) 123 - 4567", Toast.LENGTH_SHORT).show();
        }

        else {
            Toast.makeText(Login.this, "Registration has not yet been approved by Administrator",
                    Toast.LENGTH_SHORT).show();
        }

       return false;
    }

}
