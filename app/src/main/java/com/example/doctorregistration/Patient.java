package com.example.doctorregistration;

/**
 *
 * This class holds all variables necessary to create a Doctor user
 * Inherits variables from parent class User
 *
 */
public class Patient extends User{


    private int healthCardNum;

    /**
     * Constructs user of type Patient
     *
     * @param healthCardNum the employee number of the Doctor
     * @param firstName the first name of the Doctor
     * @param lastName the last name of the Doctor
     * @param email the email/username of the Doctor
     * @param password the password of the Doctor
     * @param phoneNumber the phone number of the Doctor
     * @param address the object Address which holds variables street, postal code, city, and country
     */

    public Patient(String firstName, String lastName, String email, int healthCardNum, int phoneNum, Address address, String password){
        super(firstName, lastName, email, phoneNum, password, address);
        this.healthCardNum = healthCardNum;

        /*
         * Even though parent User constructor is called, each inherited variable must be set in child class
         * This is to ensure that all information is serialized and stored to the Firestore database
         */
        setFirstName(firstName);
        setLastName(lastName);
        setPassword(password);
        setPhoneNumber(phoneNumber);
        setAddress(address);
        setEmail(email);

    }

    public int getHealthCardNum(){return healthCardNum;}
    public void setHealthCardNum(int healthCardNum){this.healthCardNum = healthCardNum;}
}