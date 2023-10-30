package com.example.doctorregistration;
import java.util.ArrayList;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class pendingListpatient extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    MyAdapterPatient myAdapter;
    ArrayList<Patient> list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_list);

        recyclerView = findViewById(R.id.pendingList);
        database = FirebaseDatabase.getInstance().getReference("Pending Requests");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        myAdapter = new MyAdapterPatient(this,list);
        recyclerView.setAdapter(myAdapter);

        database.addValueEventListener(new ValueEventListener(){

            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Patient user = dataSnapshot.getValue(Patient.class);
                    list.add(user);
                }

                myAdapter.notifyDataSetChanged();

            }



            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}