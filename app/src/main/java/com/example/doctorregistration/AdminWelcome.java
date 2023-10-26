package com.example.doctorregistration;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;

public class AdminWelcome extends AppCompatActivity {

    private Button logout;
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



    }
}