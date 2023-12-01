package com.example.doctorregistration.Doctor.Frontend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.doctorregistration.Doctor.Backend.DoctorShift;
import com.example.doctorregistration.Doctor.Backend.DoctorShiftManager;
import com.example.doctorregistration.Doctor.Doctor;
import com.example.doctorregistration.R;
import com.google.firebase.Timestamp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DoctorCreateShift extends AppCompatActivity {
    private CalendarView calendarView;
    private Calendar calendar;
    private Button createShiftbtn;
    private EditText startTimePicker;
    private EditText endTimePicker;
    private DoctorShiftManager doctorShiftManager;
    private DoctorShift doctorShift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_create_shift);

        calendarView = findViewById(R.id.calendarView);
        startTimePicker = findViewById(R.id.startTime);
        endTimePicker = findViewById(R.id.endTime);
        createShiftbtn = findViewById(R.id.createShift);

        calendar = Calendar.getInstance();
        doctorShift = new DoctorShift();


        setDate(1,11,2023); //set initial date of the calendar (currently set to November 1)
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendarShift = Calendar.getInstance();
                calendarShift.set(Calendar.YEAR, year);
                calendarShift.set(Calendar.MONTH, month);
                calendarShift.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                //Timestamp automatically stores the Date AND Time, set the time related variables to 0
                //so that it is consistent with other Date variables
                calendarShift.set(Calendar.HOUR_OF_DAY, 0);
                calendarShift.set(Calendar.MINUTE, 0);
                calendarShift.set(Calendar.SECOND, 0);
                calendarShift.set(Calendar.MILLISECOND, 0);

                // Convert Calendar to Date
                Date date = calendarShift.getTime();
                // Convert Date to Timestamp
                Timestamp timestamp = new Timestamp(date);
                doctorShift.setShiftDate(timestamp);
            }
        });

        startTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(true);
            }
        });

        endTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(false);
            }
        });

        createShiftbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doctorShiftManager.addShift(doctorShift);
            }
        });

    }


    private void showTimePickerDialog(final boolean isStartTime) {
        // Get the current time
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a time picker dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Round the minute to the nearest 30 minutes
                        doctorShiftManager = new DoctorShiftManager();
                        minute = doctorShiftManager.roundToNearest30Minutes(minute);

                        // Create a Calendar instance
                        Calendar selectedTimeCalendar = Calendar.getInstance();
                        selectedTimeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedTimeCalendar.set(Calendar.MINUTE, minute);
                        selectedTimeCalendar.set(Calendar.SECOND, 0);
                        selectedTimeCalendar.set(Calendar.MILLISECOND, 0);

                        // Convert Calendar to Date
                        Date selectedTimeDate = selectedTimeCalendar.getTime();

                        // Create a Timestamp from the Date
                        Timestamp selectedTimestamp = new Timestamp(selectedTimeDate);

                        // Update the corresponding EditText with the selected time
                        if (isStartTime) {
                            doctorShift.setStartTime(selectedTimestamp);
                            startTimePicker.setText(formatTimestamp(selectedTimestamp));
                        } else {
                            doctorShift.setEndTime(selectedTimestamp);
                            endTimePicker.setText(formatTimestamp(selectedTimestamp));
                        }
                    }
                },
                hour,
                minute,
                false // 24-hour format
        );

        timePickerDialog.show();
    }

    private String formatTimestamp(Timestamp timestamp) {
        Date date = timestamp.toDate();
        // Format the date as needed
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US); //displays in 24 hour format
        return sdf.format(date);
    }

    public void setDate(int day, int month, int year){
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        long milli = calendar.getTimeInMillis();

        calendarView.setDate(milli);
    }

}