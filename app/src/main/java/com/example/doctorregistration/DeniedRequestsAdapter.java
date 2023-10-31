package com.example.doctorregistration;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

/**
 *
 * The DeniedRequestsAdapter class serves as the RecyclerView adapter,
 * responsible for displaying the denied request items,
 * handling the "Change Status" button click, and updating Firestore accordingly.
 *
 */
public class DeniedRequestsAdapter extends RecyclerView.Adapter<DeniedRequestsAdapter.ViewHolder> {
    private List<DeniedRequestItem> deniedRequestsList;
    private FirebaseFirestore db;

    public DeniedRequestsAdapter(List<DeniedRequestItem> deniedRequestsList) {
        this.deniedRequestsList = deniedRequestsList;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_denied_request, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DeniedRequestItem item = deniedRequestsList.get(position);

        // Bind data to views
        String userIdWithPrefix = "ID: " + item.getUserId();
        holder.userNameId.setText(userIdWithPrefix);
        String firstNameWithPrefix = "First Name: " + item.getFirstName();
        holder.userFirstName.setText(firstNameWithPrefix);
        String lastNameWithPrefix = "Last Name: " + item.getLastName();
        holder.userLastName.setText(lastNameWithPrefix);
        // Retrieve the address map
        Map<String, String> addressMap = item.getAddress();
        if (addressMap != null) {
            // Access subfields from the "address" map
            String street = (String) addressMap.get("street");
            String city = (String) addressMap.get("city");
            String country = (String) addressMap.get("country");
            String postalCode = (String) addressMap.get("postalCode");
        String fullAddressWithPrefix = "Address: " + street +"," + city + "," + country + "," + postalCode;
        holder.userAddress.setText(fullAddressWithPrefix);}


        // Change Status Button Click Event
        holder.changeStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the status change for the corresponding item
                String newStatus = "approved"; // You can customize this based on user selection
                item.setStatus(newStatus);
                // Call the Firestore update method
                updateStatusInFirestore(item.getUserId(), newStatus);
                notifyDataSetChanged(); // Refresh the RecyclerView
            }
        });
    }

    @Override
    public int getItemCount() {
        return deniedRequestsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userNameId;
        private TextView userFirstName;
        private TextView userLastName;
        private TextView userAddress;
        private Button changeStatusButton;

        public ViewHolder(View itemView) {
            super(itemView);
            userNameId = itemView.findViewById(R.id.userNameId);
            changeStatusButton = itemView.findViewById(R.id.changeStatusButton);
            userFirstName = itemView.findViewById(R.id.userFirstName);
            userLastName = itemView.findViewById(R.id.userLastName);
            userAddress = itemView.findViewById(R.id.userAddress);
        }
    }

    // Firestore update method
    public void updateStatusInFirestore(final String userId, final String newStatus) {
        DocumentReference userRef = db.collection("Denied Requests").document(userId);

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String currentStatus = documentSnapshot.getString("accountStatus");

                    if ("approved".equals(newStatus) && !"approved".equals(currentStatus)) {
                        // Move the user to the "user" collection
                        DocumentReference newUserRef = db.collection("user").document(userId);

                        // Copy user data to the new document in the "user" collection
                        newUserRef.set(documentSnapshot.getData())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Delete the user's document from the "Denied Requests" collection
                                        userRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // User moved successfully
                                                // Update the "accountStatus" field
                                                newUserRef.update("accountStatus", "approved");
                                            }
                                        });
                                    }
                                });
                    }
                }
            }
        });
    }
}
