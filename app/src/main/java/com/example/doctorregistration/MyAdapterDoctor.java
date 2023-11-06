package com.example.doctorregistration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapterDoctor extends RecyclerView.Adapter<MyAdapterDoctor.MyViewHolder>{

    Context context;
    ArrayList<Doctor> list;

    public MyAdapterDoctor(Context context, ArrayList<Doctor> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.pendinglistitemdoctor,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //gets data from the user and sets it in specific text fields
        Doctor user = list.get(position);
        holder.firstName.setText(user.getFirstName());
        holder.lastName.setText(user.getLastName());
        holder.employeeNumber.setText(user.getEmployeeNumber());
        holder.phoneNumber.setText(user.getPhoneNumber());
        String specialty = "";
        for(String item: user.getSpecialty()){
            specialty += item+",";
        }
        holder.doctorSpecialities.setText(specialty);
        holder.city.setText(user.getAddress().getCity());
        holder.country.setText(user.getAddress().getCountry());
        holder.postalCode.setText(user.getAddress().getPostalCode());
        holder.street.setText(user.getAddress().getStreet());



    }

    @Override
    public int getItemCount() {
        //to know number of user in pending list
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView firstName,lastName,employeeNumber,email,city,country,postalCode,street,doctorSpecialities, phoneNumber;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            firstName = itemView.findViewById(R.id.firstName);
            lastName = itemView.findViewById(R.id.lastName);
            employeeNumber = itemView.findViewById(R.id.employeeNumber);
            email = itemView.findViewById(R.id.email);
            doctorSpecialities = itemView.findViewById(R.id.doctorSpecialities);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            city = itemView.findViewById(R.id.city);
            country = itemView.findViewById(R.id.country);
            postalCode = itemView.findViewById(R.id.postalCode);
            street = itemView.findViewById(R.id.street);

        }
    }


}
