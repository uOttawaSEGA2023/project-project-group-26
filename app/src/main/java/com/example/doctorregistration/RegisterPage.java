package com.example.doctorregistration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterPage extends AppCompatActivity {
    private Button buttonfordoc;
    private Button buttonforpatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerpage);

        buttonfordoc = findViewById(R.id.buttonDoctor);
        buttonfordoc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //       switch DummyActivity1 with the activity you want the button to bring you to (Doctor Register Page)
                openDoctorRegistration();
            }
        });

        buttonforpatient = findViewById(R.id.buttonPatient);
        buttonforpatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //       switch DummyActivity2 with the activity you want the button to bring you to (Patient Register Page)
                openDummyActivity2();
            }
        });
    }
    public void openDoctorRegistration(){
    //        switch DummyActivty1 here again with the name of the activity you want opened (Doctor)
        Intent intent = new Intent(this,DoctorRegistration.class);
        startActivity(intent);
    }
    public void openDummyActivity2(){
        //        switch DummyActivty2 here again with the name of the activity you want opened (Patient)
        Intent intent = new Intent(this,DummyActivity2.class);
        startActivity(intent);
    }

}