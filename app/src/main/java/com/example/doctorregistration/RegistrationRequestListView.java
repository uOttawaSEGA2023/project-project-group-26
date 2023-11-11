package com.example.doctorregistration;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.app.Activity;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * The DeniedRequestsAdapter class serves as the RecyclerView adapter,
 * responsible for displaying the denied request items,
 * handling the "Change Status" button click, and updating Firestore accordingly.
 */
public class RegistrationRequestListView extends ArrayAdapter<RegistrationRequestItem> {
    private ArrayList<RegistrationRequestItem> requests;
    private Activity context;

    public RegistrationRequestListView(Activity context, ArrayList<RegistrationRequestItem> requests) {
        super(context, R.layout.layout_request_list);
        this.requests = requests;
        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("Debug", "Entering getView " + requests.size() + " items.");
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_request_list, null, true);

        TextView tvFirstName = (TextView) listViewItem.findViewById(R.id.textViewFirstName);
        TextView tvLastName = (TextView) listViewItem.findViewById(R.id.textViewLastName);

        RegistrationRequestItem request = requests.get(position);

        if(request.getUserType().equals("Doctor")) {
            Doctor doctor = request.getDoctor();

            tvFirstName.setText(doctor.getFirstName());
            tvLastName.setText(doctor.getLastName());
        }

        if(request.getUserType().equals("Patient")) {
            Patient patient = request.getPatient();

            tvFirstName.setText(patient.getFirstName());
            tvLastName.setText(patient.getLastName());
        }

        return listViewItem;
    }

    public void displayItems() {
        for (RegistrationRequestItem rq : requests) {
            User user = rq.getDoctor();
            Log.d("Debug", user.getFirstName());
        }
    }

    public int getCount() {
        return requests.size();
    }

}
