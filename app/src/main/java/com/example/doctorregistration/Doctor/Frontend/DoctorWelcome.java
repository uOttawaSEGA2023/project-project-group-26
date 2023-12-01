package com.example.doctorregistration.Doctor.Frontend;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.doctorregistration.Doctor.Backend.DoctorAppointmentsList;
import com.example.doctorregistration.Launcher.MainWelcome;
import com.example.doctorregistration.R;
import com.google.firebase.auth.FirebaseAuth;

public class DoctorWelcome extends AppCompatActivity {
    private Button logout;
    FirebaseAuth doctorLogoutAuth;

    private Button upcomingAppointmentsbtn;
    private Button pastAppointmentsbtn;
    private Button createShiftbtn;
    private Button viewShiftbtn;

    public static String test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_welcome);

        logout = (Button) findViewById(R.id.signOut); // connecting logout variable to UI

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), MainWelcome.class)); //Switch from one page to the other

        }
    });


        upcomingAppointmentsbtn = (Button) findViewById(R.id.upcomingAppointments);
        upcomingAppointmentsbtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), DoctorAppointmentsList.class);
                startActivity(intent);
                test = "upcomingAppointments";
                finish();


            }
        });

        pastAppointmentsbtn = (Button) findViewById(R.id.pastAppointments);
        pastAppointmentsbtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                Intent intent = new Intent(getApplicationContext(), DoctorAppointmentsList.class);
                startActivity(intent);
                test = "pastAppointments";
                finish();

            }
        });

        createShiftbtn = (Button) findViewById(R.id.createShift);

        createShiftbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DoctorCreateShift.class);
                startActivity(intent);
            }
        });


        //not set up
        viewShiftbtn = (Button) findViewById(R.id.viewShifts);
        viewShiftbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

}}

