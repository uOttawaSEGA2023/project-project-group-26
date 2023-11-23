package com.example.doctorregistration.Other;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.ArrayList;

/**
 *
 * This class holds all variables and methods needed to validate
 * text fields and inputted information
 *
 */
public class Validate {

    private String idNumber;
    private String phoneNumber;
    private String email;

    /**
     * Constructs a validate object with specific user information
     *
     * @param idNumber either employeeNumber for Doctor or healthcardNumber for Patient
     * @param phoneNumber phoneNumber the phoneNumber of the user
     * @param email email the email the of user
     */
    public Validate(String idNumber, String phoneNumber, String email) {
        this.idNumber = idNumber;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    /**
     * This method checks if all the EditText fields are filled
     *
     * @param etList the ArrayList of Edit Text that contains all input fields to be checked
     * @return true if all fields filled, false if minimum one is blank
     */
    public boolean allTextFieldsFilled(ArrayList<EditText> etList) {
        boolean filled = true;

        for (EditText et : etList) {
            if (isFieldBlank(et)) {
                et.setError("This field can not be blank");
                filled = false;
            }
        }
        return filled;
    }

    /**
     * Checks if inputted email is of the correct format (ie. _____@_____.com)
     *
     * @param emailToCheck the email to validate
     * @return true if email is valid, false otherwise
     */
    public boolean isEmailValid(String emailToCheck) {
        return !emailToCheck.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailToCheck).matches();
    }

    /**
     * This method checks if a specific EditText is blank
     *
     * @param textField the EditText field to be checked
     * @return true if blank, false if filled
     */
    public boolean isFieldBlank(EditText textField) {
        String text = textField.getText().toString().trim();
        return (TextUtils.isEmpty(text));  //checks if text field is empty
    }

    /**
     * This method checks if a string contains only integers
     *
     * @param string the string to check
     * @return true if only integers, false otherwise
     * @throws NumberFormatException if string contains characters
     */
    public boolean isStringToIntValid(String string) {
        try {
            int number = Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * This method checks if the password length is larger than minimum of 6 characters
     *
     * @param etPassword the EditText field password to check
     * @return true if larger or equal to 6, false otherwise
     */
    public boolean minPasswordLength(EditText etPassword) {
        if (etPassword.getText().toString().trim().length() >= 6) //password must be minimum 6 characters
            return true;
        else {
            etPassword.setError("Password must be a minimum of six characters");
            return false;
        }
    }

    /**
     * This method checks if at least one check box is selected
     * Primarily used to validate that the Doctor user has selected a minimum of one specialty
     *
     * @param cbList the ArrayList of CheckBoxs to check
     * @return true if at least one is checked, false if none checked
     */
    public boolean minCheckBoxChecked(ArrayList<CheckBox> cbList){
        boolean minCheckBoxChecked = false;

        for (CheckBox cb : cbList) {
            if (cb.isChecked()) {
                minCheckBoxChecked = true;
                break;
            }
        }
        return minCheckBoxChecked;
    }

}
