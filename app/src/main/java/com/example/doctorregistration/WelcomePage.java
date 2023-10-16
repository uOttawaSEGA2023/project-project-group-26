package com.example.doctorregistration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class WelcomePage extends AppCompatActivity {
    private CardView buttonForRegister;
    private CardView buttonForLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcomepage);

        buttonForRegister = findViewById(R.id.buttonForRegister);
        buttonForRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { openRegisterPage();
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
    public void openRegisterPage(){
        Intent intent = new Intent(getApplicationContext(),RegisterPage.class);
        startActivity(intent);
    }
    public void openLogin(){
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    }
}

