package com.example.doctorregistration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainWelcome extends AppCompatActivity {
    private CardView buttonForRegister;
    private CardView buttonForLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_welcome);

        buttonForRegister = findViewById(R.id.buttonForRegister);
        buttonForRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegister();
            }
        });

        buttonForLogin = findViewById(R.id.buttonForLogin);
        buttonForLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLogin();
            }
        });

    }
    public void openRegister(){
        //Intent intent = new Intent(getApplicationContext(), RegisterDorP.class);
        //startActivity(intent);

        Intent intent = new Intent(getApplicationContext(), pendingListDoctor.class);
        startActivity(intent);
    }
    public void openLogin(){
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    }
}

