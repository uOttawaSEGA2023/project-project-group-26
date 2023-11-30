package com.example.doctorregistration.Admin.Frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

import com.example.doctorregistration.Admin.Backend.AdminDeniedRequest;
import com.example.doctorregistration.Admin.Backend.AdminPendingRequest;
import com.example.doctorregistration.Launcher.MainWelcome;
import com.example.doctorregistration.R;
import com.google.firebase.auth.FirebaseAuth;

public class AdminWelcome extends AppCompatActivity {

    private Button logout;
    private Button deniedRequests;
    private Button pendingRequests;
    FirebaseAuth adminLogoutAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_welcome);


        logout = (Button) findViewById(R.id.signOut);   // connecting logout variable to UI
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adminLogoutAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), MainWelcome.class)); //Switch from one page to the other
            }
        });

        deniedRequests = findViewById(R.id.buttonDeniedRequests);
        deniedRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdminDeniedRequest.class);
                startActivity(intent);
            }
        });


        pendingRequests = findViewById(R.id.buttonPendingRequests);
        pendingRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdminPendingRequest.class);
                 startActivity(intent);
            }
        });
    }

}