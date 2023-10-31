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
import java.util.Map;

/**
 *
 * The AdminDeniedRequest class sets up the activity where
 * administrators can view denied requests,
 * including initializing the RecyclerView and the adapter.
 *
 */
public class AdminPendingRequest extends AppCompatActivity {

    private Button buttonbacktoAWP;
    private RecyclerView recyclerView;
    private PendingRequestsAdapter adapter;
    private List<PendingRequestItem> pendingRequestsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pending_request);

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
        pendingRequestsList = new ArrayList<>();

        // Initialize and set the adapter
        adapter = new PendingRequestsAdapter(pendingRequestsList);
        recyclerView.setAdapter(adapter);

        // Populate the deniedRequestsList with data from Firebase Firestore
        fetchPendingRequestsFromFirestore();
    }

    public void openAdminWelcome() {
        Intent intent = new Intent(getApplicationContext(), AdminWelcome.class);
        startActivity(intent);
    }

    private void fetchPendingRequestsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Query Firestore for pending requests collection
        db.collection("Pending Requests")
                .whereEqualTo("accountStatus", "Pending") // Filter based on your criteria
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            // Create PendingRequestItem objects from the retrieved data
                            String userId = document.getId(); // Assuming user ID is the document ID
                            String firstName = document.getString("firstName");
                            String lastName = document.getString("lastName");
                            Map<String, String> addressMap = (Map<String, String>) document.get("address");
                            if (addressMap != null) {
                                String city = addressMap.get("city");
                                String country = addressMap.get("country");
                                String postalCode = addressMap.get("postalCode");
                                String street = addressMap.get("street");
                                PendingRequestItem pendingRequestItem = new PendingRequestItem(userId, "pending", firstName, lastName, city, country, postalCode, street, addressMap);

                                // Add the item to the deniedRequestsList
                                pendingRequestItem.add(pendingRequestItem);
                            }
                        }

                        // Notify the adapter that the data has changed
                        adapter.notifyDataSetChanged();
                    }
                });

    }
}

