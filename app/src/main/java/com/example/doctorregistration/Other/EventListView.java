package com.example.doctorregistration.Other;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.doctorregistration.Doctor.Doctor;
import com.example.doctorregistration.EventEditActivity;
import com.example.doctorregistration.Patient.Patient;
import com.example.doctorregistration.R;

import com.google.firebase.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class EventListView extends ArrayAdapter<EventItem> {

    private ArrayList<EventItem> events;
    private Activity context;

    public EventListView(Activity context, ArrayList<EventItem> events) {
        super(context, R.layout.event_preview_list);
        this.events = events;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("Debug", "Entering getView " + events.size() + " items.");
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.event_preview_list, null, true);

        TextView tvEventDate = (TextView) listViewItem.findViewById(R.id.eventDate);

        EventItem event = events.get(position);

        tvEventDate.setText(event.formatDate(event.extractDateFromTimestamp(event.getDate())));

        return listViewItem;
    }

    public void displayItems() {
        for (EventItem item : events) {
            Timestamp date = item.getDate();
            Log.d("Debug", date.toString());
        }
    }

    public int getCount() {
        return events.size();
    }

}


