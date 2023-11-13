package com.example.doctorregistration;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.time.LocalTime;

public class CalendarUtils {
    public static LocalDate selectedDate;

    public static String monthYearFromDate(LocalDate date) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
            return date.format(formatter);
        }

        return null;
    }

    public static ArrayList<LocalDate> daysInMonthArray(LocalDate date) {
        ArrayList<LocalDate> daysInMonthArray = new ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            YearMonth yearMonth = YearMonth.from(date);
            int daysInMonth = yearMonth.lengthOfMonth();

            LocalDate firstOfMonth = CalendarUtils.selectedDate.withDayOfMonth(1);
            int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

            for (int i = 1; i <= 42; i++) {

                if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                    daysInMonthArray.add(null);
                } else {
                    daysInMonthArray.add(LocalDate.of(selectedDate.getYear(), selectedDate.getMonth(), i - dayOfWeek));
                }
            }

            return daysInMonthArray;

        }
        return null;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<LocalDate> daysInWeekArray(LocalDate selectedDate) {
        ArrayList<LocalDate> days = new ArrayList<>();
        LocalDate current = sundayForDate(selectedDate);

        LocalDate endDate = current.plusWeeks(1);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            while (current.isBefore(endDate)) {
                days.add(current);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    current = current.plusDays(1);
                }

            }
        }

        return days ;


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static LocalDate sundayForDate(LocalDate current) {

        LocalDate oneWeekAgo = current.minusWeeks(1);
        {
            while (current.isAfter(oneWeekAgo)) {

                if (current.getDayOfWeek() == DayOfWeek.SUNDAY)
                    return current;


                current = current.minusDays(1);
            }
            return null;
        }
    }

    public static String formattedDate(LocalDate selectedDate) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        }
    }
}

