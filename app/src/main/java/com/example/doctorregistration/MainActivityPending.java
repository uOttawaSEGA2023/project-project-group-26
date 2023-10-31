package com.example.doctorregistration;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivityPending extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Patient> patientArrayList;

    MyAdapterPatient myAdapter;

    FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainactivitypending);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data");
        progressDialog.show();


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        patientArrayList = new ArrayList<>();
        myAdapter = new MyAdapterPatient(MainActivityPending.this, patientArrayList);

        recyclerView.setAdapter(myAdapter);


        // Populate the pendingRequestsList with data from Firebase Firestore
        EventChangeListener();



    }
    private void EventChangeListener(){

        db.collection("pending reqeuest")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null)
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();

                            Log.e("Firebase error", error.getMessage());
                            return;
                        }
                        for(DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.Added) {
                                patientArrayList.add(dc.getDocument().toObject(Patient.class));
                            }
                            myAdapter.notifyDataSetChanged();
                        if (error != null)
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();

                    }
                        }
                    }

                });
