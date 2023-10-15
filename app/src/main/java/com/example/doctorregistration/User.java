package com.example.doctorregistration;

/**
 *
 * This class holds all information related to a general user
 * Class Patient and class Doctor are child classes
 * Variables are protected to be inherited as private
 *
 */
public class User {

    protected String firstName;
    protected String lastName;
    protected String email;
    protected int phoneNumber;
    protected String password;
    protected Address address;

    /**
     * Constructs a general user
     *
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @param email the email name of the user
     * @param phoneNumber the phone number of the user
     * @param password the password of the user
     * @param address the Address object of the user, contains specific
     *                information street, postal code, city, and country
     */
    public User(String firstName, String lastName, String email, int phoneNumber, String password, Address address){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public Address getAddress() {
        return address;
    }
    public String getEmail(){
        return email;
    }
    public int getPhoneNumber() {
        return phoneNumber;
    }
    public String getPassword() {
        return password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setAddress(Address address) {
        this.address = address;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
