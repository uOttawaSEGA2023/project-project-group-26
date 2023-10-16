package com.example.doctorregistration;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

                //Intent intent = new Intent(getApplicationContext(), DoctorRegistration.class, PatientRegistration.class);
                /*
                if (editEmailAddress.getText().toString().equals("admin@telewelness.ca") && editPassword.getText().toString().equals("1234")) {
                    Toast.makeText(Login.this, "Logged in successfully. Redirecting to new page.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Login.this, "Login Failed, Please Try Again.", Toast.LENGTH_SHORT).show();
                }
*/
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
                            Toast.makeText(Login.this, "Logged in successfully. Redirecting to new page.", Toast.LENGTH_SHORT).show();

                            String userId = fAuth.getCurrentUser().getUid(); //Obtains current users userID
                            DocumentReference docRef = db.collection("user").document(userId); //Navigates to users data in Firestore

                            docRef.get().addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    String userType = documentSnapshot.getString("userType");

                                    if (userType != null) {
                                        if (userType.equals("Doctor")) {
                                            Intent intentDoctor = new Intent(Login.this, DoctorLogout.class);
                                            startActivity(intentDoctor);
                                            finish();

                                        } else if (userType.equals("Patient")) {
                                            Intent intentPatient = new Intent(Login.this, PatientLogout.class);
                                            startActivity(intentPatient);
                                            finish();

                                        } else {
                                            Intent intentAdmin = new Intent(Login.this, AdminLogout.class);
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
}
