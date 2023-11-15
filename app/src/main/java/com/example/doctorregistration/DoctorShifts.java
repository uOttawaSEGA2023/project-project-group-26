package com.example.doctorregistration;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class DoctorShifts {
    private List<ShiftValidity> shifts;

    public DoctorShifts() {
        this.shifts = new ArrayList<>();
    }

    public void addShift(String shiftDate, String startTime, String endTime) { //might have to switch to string
        ShiftValidity newShift = null;
        try {
            newShift = new ShiftValidity(shiftDate,startTime, endTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        if( ValidShiftDuration(newShift) == false) {
            Log.d("DoctorShifts","Your shift must last atleast 30 minutes");
        }
        if (!isConflict(newShift)) {
            shifts.add(newShift);
        } else {
            Log.d("DoctorShifts","shift conflict! Check your calendar and make sure you have no shifts at the same Start time or endTime");
        }
    }

    private boolean ValidShiftDuration(ShiftValidity newShift) {
        long duration= newShift.shiftDuration(); //given in milliseconds

        return duration >=  30 * 60 * 1000; //converts  30 minutes in ms and compares
    }

    private boolean isConflict(ShiftValidity newShift) {
        for (ShiftValidity savedShifts : shifts) {
            if (newShift.ValidShift(savedShifts)) {
                return true; //
            }
        }
        return false; // No conflicts
    }

}
