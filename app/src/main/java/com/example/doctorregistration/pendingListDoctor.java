package com.example.doctorregistration;
import java.util.ArrayList;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//gets users in pending list of type doctor

public class pendingListDoctor extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    MyAdapterDoctor myAdapter;
    ArrayList<Doctor> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_list);

        recyclerView = findViewById(R.id.pendingList);

        //locates the path to find pending request in database
        database = FirebaseDatabase.getInstance().getReference("Pending Requests");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //arraylist to store all the pending user of type doctor
        list = new ArrayList<>();
        myAdapter = new MyAdapterDoctor(this,list);
        recyclerView.setAdapter(myAdapter);

        database.addValueEventListener(new ValueEventListener(){

            //stores details of pending users of type doctor one by one in a list
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Doctor user = dataSnapshot.getValue(Doctor.class);
                    list.add(user);
                }

                myAdapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}