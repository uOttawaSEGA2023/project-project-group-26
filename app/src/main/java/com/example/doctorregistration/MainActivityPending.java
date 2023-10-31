package com.example.doctorregistration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * The AdminDeniedRequest class sets up the activity where
 * administrators can view denied requests,
 * including initializing the RecyclerView and the adapter.
 *
 */
public class AdminDeniedRequest extends AppCompatActivity {

    private Button buttonbacktoAWP;
    private RecyclerView recyclerView;
    private DeniedRequestsAdapter adapter;
    private List<DeniedRequestItem> deniedRequestsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_denied_request);

        buttonbacktoAWP = findViewById(R.id.backtoAWP);
        buttonbacktoAWP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAdminWelcome();
            }
        });

        // Initialize RecyclerView and data
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        deniedRequestsList = new ArrayList<>();

        // Initialize and set the adapter
        adapter = new DeniedRequestsAdapter(deniedRequestsList);
        recyclerView.setAdapter(adapter);

        // Populate the deniedRequestsList with data from Firebase Firestore
        fetchDeniedRequestsFromFirestore();
    }

    public void openAdminWelcome() {
        Intent intent = new Intent(getApplicationContext(), AdminWelcome.class);
        startActivity(intent);
    }

    private void fetchDeniedRequestsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Query Firestore for denied requests collection
        db.collection("Denied Requests")
                .whereEqualTo("accountStatus", "denied") // Filter based on your criteria
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            // Create DeniedRequestItem objects from the retrieved data
                            String userId = document.getId(); // Assuming user ID is the document ID
                            DeniedRequestItem deniedRequestItem = new DeniedRequestItem(userId, "denied");

                            // Add the item to the deniedRequestsList
                            deniedRequestsList.add(deniedRequestItem);
                        }

                        // Notify the adapter that the data has changed
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
