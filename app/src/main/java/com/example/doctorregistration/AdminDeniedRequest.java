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
                            String firstName = document.getString("firstName");
                            String lastName = document.getString("lastName");
                            String email = document.getString("email");
                            String phoneNumber = document.getString("phoneNUmber");
                            String userType = document.getString("userType");
                            Map<String, String> addressMap = (Map<String, String>) document.get("address");
                            if (addressMap != null) {
                                String city = addressMap.get("city");
                                String country = addressMap.get("country");
                                String postalCode = addressMap.get("postalCode");
                                String street = addressMap.get("street");
                                DeniedRequestItem deniedRequestItem = new DeniedRequestItem(userId, "denied", firstName, lastName, city, country, postalCode, street, addressMap, email, phoneNumber, userType);

                                // Add the item to the deniedRequestsList
                                deniedRequestsList.add(deniedRequestItem);
                            }
                        }

                        // Notify the adapter that the data has changed
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}

