
package com.example.doctorregistration;

import java.util.Map;

/**
 *
 * The DeniedRequestItem class defines the model for each denied request item,
 * including the user's status, and user ID.
 *
 */

public class DeniedRequestItem {
    private String status;
    private String userId;
    private String firstName;
    private String lastName;
    private String city;
    private String country;
    private String postalCode;
    private String street;
    private Map<String, String> address;

    public DeniedRequestItem(String userId, String status, String firstName, String lastName, String city, String country, String postalCode, String street, Map<String, String> address) {
        this.userId = userId;
        this.status = status;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;


    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getUserId() {
        return userId;
    }
    public String getFirstName() { return firstName;}
    public String getLastName() {return lastName;}
    public Map<String, String> getAddress() {return address;}
}