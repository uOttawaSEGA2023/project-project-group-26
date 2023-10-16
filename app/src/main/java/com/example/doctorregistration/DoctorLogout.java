package com.example.doctorregistration;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class DoctorLogout extends AppCompatActivity {
    private Button logout;
    FirebaseAuth doctorLogoutAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_logout);

        logout = (Button) findViewById(R.id.signOut); // connecting logout variable to UI
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(DoctorLogout.this, AdminLogout.class)); //Switch from one page to the other
    }
});
    }}