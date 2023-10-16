package com.example.doctorregistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText editEmailAddress;
    EditText editPassword;
    Button loginButton;

    FirebaseAuth firebaseAuthentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmailAddress = findViewById(R.id.emailAddress);
        editPassword = findViewById(R.id.password);
        loginButton = findViewById(R.id.buttonLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), PatientRegistration.class);
                //DoctorResgistration.class,

                if (editEmailAddress.getText().toString().equals("admin@telewelness.ca") && editPassword.getText().toString().equals("1234")){
                    Toast.makeText(Login.this, "Logged in successfully. Redirecting to new page.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Login.this, "Login Failed, Please Try Again.", Toast.LENGTH_SHORT).show();
                }

                String emailAddress, password;

                emailAddress = String.valueOf(editEmailAddress.getText());
                password = String.valueOf(editPassword.getText());

                if (TextUtils.isEmpty(emailAddress)){
                    Toast.makeText(Login.this, "Please Enter Email Associated With Account", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    Toast.makeText(Login.this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuthentication.signInWithEmailAndPassword(emailAddress, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Login.this, "Logged in successfully. Redirecting to new page.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(),  AdminLogOut.class);
                                    //DoctorLogOut.class, PatientLogOut.class,
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(Login.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

        }); {

        }
    }
}