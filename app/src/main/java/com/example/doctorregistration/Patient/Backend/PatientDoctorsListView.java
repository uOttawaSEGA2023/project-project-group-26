package com.example.doctorregistration.Patient.Backend;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.doctorregistration.Doctor.Doctor;
import com.example.doctorregistration.Other.User;
import com.example.doctorregistration.R;

import java.util.ArrayList;

//This class is an adapter class to show the list of doctors when searched
public class PatientDoctorsListView extends ArrayAdapter<DoctorItem> {
    private ArrayList<DoctorItem> doctorList;
    private Activity context;

    public PatientDoctorsListView(Activity context, ArrayList<DoctorItem> doctor) {
        super(context, R.layout.appointment_doctor_item);
        this.doctorList = doctor;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.appointment_doctor_item, null, true);

        TextView tvName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView tvEmail = (TextView) listViewItem.findViewById(R.id.textViewEmail);
        TextView tvSpecialty = (TextView) listViewItem.findViewById(R.id.textViewSpecialty);

        DoctorItem doctorItem = doctorList.get(position);

        Doctor doctor = doctorItem.getDoctor();
        tvName.setText("Dr. " + doctor.getLastName() + ", " + doctor.getFirstName());
        tvEmail.setText("Email: " + doctor.getEmail());
        tvSpecialty.setText("Specialty(s): " + TextUtils.join(", ", doctor.getSpecialty()));

        return listViewItem;
    }

    public void setFilteredList(ArrayList<DoctorItem> filteredListList){
        this.doctorList = filteredListList;
        notifyDataSetChanged();
    }


    public void displayItems() {
        for (DoctorItem rq : doctorList) {
            User user = rq.getDoctor();
            Log.d("Debug", user.getFirstName());
        }
    }

    public int getCount() {
        return doctorList.size();
    }




}
