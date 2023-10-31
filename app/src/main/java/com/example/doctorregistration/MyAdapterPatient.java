package com.example.doctorregistration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

      public class MyAdapterPatient extends RecyclerView.Adapter<MyAdapterPatient.MyViewHolder>{


        Context context;
        ArrayList<Patient> list;

        private FirebaseFirestore db;


          public MyAdapterPatient(Context context, ArrayList<Patient> list) {
            this.context = context;
            this.list = list;
            db = FirebaseFirestore.getInstance();

          }

        @NonNull
        @Override
        public MyAdapterPatient.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.pendinglistitempatient,parent,false);
            return new MyViewHolder(v);
        }

        @Override// Get object from arraylist and display
        public void onBindViewHolder(@NonNull com.example.doctorregistration.MyAdapterPatient.MyViewHolder holder, int position) {

            Patient user = list.get(position);
            holder.firstName.setText(user.getFirstName());
            holder.lastName.setText(user.getLastName());
            holder.healthNumber.setText(user.getHealthCardNum());
            holder.phoneNumber.setText(user.getPhoneNumber());
            holder.city.setText(user.getAddress().getCity());
            holder.country.setText(user.getAddress().getCountry());
            holder.postalCode.setText(user.getAddress().getPostalCode());
            holder.street.setText(user.getAddress().getStreet());

        }

        @Override // patients in list
        public int getItemCount() {
            return list.size();
        }

        public static class MyViewHolder extends RecyclerView.ViewHolder{

            TextView firstName,lastName,healthNumber,email,city,country,postalCode,street, phoneNumber;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                //display user inout
                firstName = itemView.findViewById(R.id.firstName);
                lastName = itemView.findViewById(R.id.lastName);
                healthNumber = itemView.findViewById(R.id.healthCardNum);
                email = itemView.findViewById(R.id.email);
                phoneNumber = itemView.findViewById(R.id.phoneNumber);
                city = itemView.findViewById(R.id.city);
                country = itemView.findViewById(R.id.country);
                postalCode = itemView.findViewById(R.id.postalCode);
                street = itemView.findViewById(R.id.street);

            }
        }

}
