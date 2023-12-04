package com.example.doctorregistration.Patient;

import com.example.doctorregistration.Other.Address;
import com.example.doctorregistration.Other.EventItem;
import com.example.doctorregistration.Other.User;

import java.util.ArrayList;

/**
 *
 * This class holds all variables necessary to create a Doctor user
 * Inherits variables from parent class User
 *
 */
public class Patient extends User {


    private int healthCardNum;

    //instance variable
    private boolean treatmentStatus;
    private ArrayList<EventItem> upcomingAppointments;
    private ArrayList<EventItem> pastAppointments;

    /**
     * Constructs user of type Patient
     *
     * @param healthCardNum the employee number of the Patient
     * @param firstName the first name of the Patient
     * @param lastName the last name of the Patient
     * @param email the email/username of the Patient
     * @param password the password of the Patient
     * @param phoneNumber the phone number of the Patient
     * @param address the object Address which holds variables street, postal code, city, and country
     */

    public Patient(String firstName, String lastName, String email, int healthCardNum,
                   int phoneNumber, Address address, String password, ArrayList<EventItem> upcomingAppointments,
                   ArrayList<EventItem> pastAppointments){
        super(firstName, lastName, email, phoneNumber, password, healthCardNum, address);
        this.upcomingAppointments = upcomingAppointments;
        this.pastAppointments = pastAppointments;

        /*
         * Even though parent User constructor is called, each inherited variable must be set in child class
         * This is to ensure that all information is serialized and stored to the Firestore database
         */
        setUserType("Patient");
        setFirstName(firstName);
        setLastName(lastName);
        setPassword(password);
        setPhoneNumber(phoneNumber);
        setAddress(address);
        setEmail(email);
        setIdNumber(healthCardNum);
        setTreatmentStatus(false);


    }

    public Patient(){}

    public void setTreatmentStatus(boolean status){
        treatmentStatus = status;
    }

    public boolean getTreatmentStatus(){
        return treatmentStatus;
    }

    @Override
    public String displayUserInformation() {
        return ("Name: " + getLastName() + ", " + getFirstName() +
                "\nEmail: " + getEmail() +
                "\nPhone Number: " + getPhoneNumber() +
                "\nAddress: " + getAddress().displayAddress() +
                "\nHealth Card Number: " + getIdNumber());
    }

}
