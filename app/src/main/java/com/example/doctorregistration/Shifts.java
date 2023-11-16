package com.example.doctorregistration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Shifts {
    public static ArrayList<Shifts> eventsList = new ArrayList<>();

    public static ArrayList<Shifts> eventsForDate(LocalDate date) {
        ArrayList<Shifts> events = new ArrayList<>();
        for (Shifts event : eventsList) {
            if (event.getDate().equals(date))
                events.add(event);
        }
        return events;
    }


    private String name;
    private LocalDate date;
    private LocalTime time;

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public Shifts(String name, LocalDate date, LocalTime time) {
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }
}
