package com.example.doctorregistration.Doctor;

import com.example.doctorregistration.Other.Address;
import com.example.doctorregistration.Other.User;

import java.util.ArrayList;

/**
 *
 * This class holds all variables necessary to create a Doctor user
 * Inherits variables from parent class User
 *
 */
public class Doctor extends User {

    //private int employeeNumber;
    private ArrayList<String> specialty;

    /**
     * Constructs user of type Doctor
     *
     * @param employeeNumber the employee number of the Doctor
     * @param specialty the Array list of specialties of the Doctor
     * @param firstName the first name of the Doctor
     * @param lastName the last name of the Doctor
     * @param email the email/username of the Doctor
     * @param password the password of the Doctor
     * @param phoneNumber the phone number of the Doctor
     * @param address the object Address which holds variables street, postal code, city, and country
     */
    public Doctor(int employeeNumber, ArrayList<String> specialty, String firstName,
                  String lastName, String email, String password, int phoneNumber,
                  Address address){

        super(firstName, lastName, email, phoneNumber, password, employeeNumber, address); //calls parent User constructor
        this.specialty = specialty;

        /*
         * Even though parent User constructor is called, each inherited variable must be set in child class
         * This is to ensure that all information is serialized and stored to the Firestore database
         */
        setUserType("Doctor");
        setFirstName(firstName);
        setLastName(lastName);
        setPassword(password);
        setPhoneNumber(phoneNumber);
        setAddress(address);
        setEmail(email);
        setIdNumber(employeeNumber);
    }

    public Doctor(){}

    //public int getEmployeeNumber() {
     //   return employeeNumber;
    //}
    public ArrayList<String> getSpecialty(){
        return specialty;
    }

    //public void setEmployeeNumber(int employeeNumber) {
    //    this.employeeNumber = employeeNumber;
    //}
    public void setSpecialty(ArrayList<String> specialty) {  //TODO May cause errors
        this.specialty = specialty;
    }

    @Override
    public String displayUserInformation() {
        return ("Name: " + getLastName() + ", " + getFirstName() +
                "\nEmail: " + getEmail() +
                "\nPhone Number: " + getPhoneNumber() +
                "\nAddress: " + getAddress().displayAddress() +
                "\nEmployee Number: " + getIdNumber() +
                "\nSpecialty(s) : " + getSpecialty());
    }

}
