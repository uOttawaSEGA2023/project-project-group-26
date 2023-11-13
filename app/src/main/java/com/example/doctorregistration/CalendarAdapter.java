package com.example.doctorregistration;

import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {

    private final OnItemListener onItemListener;
    private final ArrayList<LocalDate> days;


    public CalendarAdapter(ArrayList<LocalDate> days, OnItemListener onItemListener) {
        this.days = days;
        this.onItemListener = onItemListener;
    }

    public CalendarAdapter(ArrayList<LocalDate> days, WeekViewActivity onItemListener) {
        this.days = days;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if(days.size()> 15) //monthview
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        else //week view
            layoutParams.height = (int) (parent.getHeight());

        return new CalendarViewHolder(view, onItemListener,days);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        {
            final LocalDate date = days.get(position);
            if (date == null)
                holder.dayOfMonth.setText("");
            else {
                holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));
                if (date.equals(CalendarUtils.selectedDate)) {
                    holder.parentView.setBackgroundColor(Color.LTGRAY);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public interface OnItemListener{
        void onItemClick(int position, LocalDate date);
    }
}
