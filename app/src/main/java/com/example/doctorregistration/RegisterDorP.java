package com.example.doctorregistration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterDorP extends AppCompatActivity {
    private Button buttonfordoc;
    private Button buttonforpatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_d_or_p);

        buttonfordoc = findViewById(R.id.buttonDoctor);
        buttonfordoc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                openDoctorRegistration();
            }
        });

        buttonforpatient = findViewById(R.id.buttonPatient);
        buttonforpatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPatientRegistration();
            }
        });
    }
    public void openDoctorRegistration(){
        Intent intent = new Intent(getApplicationContext(),DoctorRegistration.class);
        startActivity(intent);
    }
    public void openPatientRegistration(){
        Intent intent = new Intent(getApplicationContext(),PatientRegistration.class);
        startActivity(intent);
    }

}