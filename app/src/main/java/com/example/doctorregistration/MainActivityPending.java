package com.example.doctorregistration;

import android.app.ProgressDialog;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivityPending {
    RecyclerView recyclerView;
    ArrayList<Patient> patientArrayList;

    MyAdapterPatient myAdapter;

    FirebaseFirestore db;

    protected void onCreate( Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainactivitypending);

        ProgressDialog progessDialog = new ProgressDialog(this);
        progressDialogsetCancelable(false);
        progressDialog.setMessage("Fetching data");
        progressDialog.show();

        recyclerView = findViewByID(recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager (this));

        db = FirebaseFirestore.getInstance();
        patientArrayList = new ArrayList<Patient>();
        myAdapter = new MyAdapterPatient(MainActivityPending.this, userArrayList);

        recyclerView.setAdapter(myAdapter);

        EventChangeListener();



    }
    private void EventChangeListener(){
        db.collection("user")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null)
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();

                            Log.e("Firebase error", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
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
