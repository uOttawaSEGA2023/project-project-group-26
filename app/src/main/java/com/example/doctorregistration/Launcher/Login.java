package com.example.doctorregistration.Launcher;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doctorregistration.Admin.Frontend.AdminWelcome;
import com.example.doctorregistration.Doctor.Frontend.DoctorWelcome;
import com.example.doctorregistration.Other.Firebase;
import com.example.doctorregistration.Patient.Frontend.PatientWelcome;
import com.example.doctorregistration.R;


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

                //FirebaseFirestore db = FirebaseFirestore.getInstance();
               // FirebaseAuth fAuth = FirebaseAuth.getInstance();

                /*
                 *Admin Login Credentials
                 *    email: admin@telewellness.ca
                 *    password: 123456
                 */


                Firebase firebase = new Firebase();
                firebase.loginAuthentication(Login.this, emailAddress, password, new Firebase.AuthenticationCallback() {
                    @Override
                    public void onSuccess(String userType) {
                        // Handle authentication success here
                        switch (userType) {
                            case "Doctor":
                                //Toast.makeText(Login.this, "Registration was approved by Administrator", Toast.LENGTH_SHORT).show();
                                Intent intentDoctor = new Intent(Login.this, DoctorWelcome.class);
                                startActivity(intentDoctor);
                                finish();
                                break;
                            case "Patient":
                                Intent intentPatient = new Intent(Login.this, PatientWelcome.class);
                                startActivity(intentPatient);
                                finish();
                                break;
                            case "Admin":
                                Intent intentAdmin = new Intent(Login.this, AdminWelcome.class);
                                startActivity(intentAdmin);
                                finish();
                                break;
                            default:
                                // Handle other cases
                                break;
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        // Handle authentication failure here
                        switch (message) {
                            case "Invalid":
                                Toast.makeText(Login.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                // Handle other failure cases
                                break;
                        }
                    }
                });

            }
        });
    }
}