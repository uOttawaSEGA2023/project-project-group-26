package com.example.doctorregistration;

/**
 *
 * This class holds the specific variables related to an address
 *
 */
public class Address {

    private String street;
    private String postalCode;
    private String city;
    private String country;

    /**
     * Constructs the address of the user, which is passed to the constructor of said user
     *
     * @param street the street address of the user
     * @param postalCode the postal code of the user
     * @param city the city of the user
     * @param country the country of the user
     */
    public Address(String street, String postalCode, String city, String country){
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
    }

    public String getStreet() {
        return street;
    }
    public String getPostalCode() {
        return postalCode;
    }
    public String getCity() {
        return city;
    }
    public String getCountry() {
        return country;
    }

    public void setStreet(String street) {
        this.street = street;
    }
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public void setCountry(String country) {
        this.country = country;
    }

}
