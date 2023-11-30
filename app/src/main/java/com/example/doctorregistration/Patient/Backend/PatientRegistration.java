package com.example.doctorregistration.Patient.Backend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doctorregistration.Other.Firebase;
import com.example.doctorregistration.Launcher.Login;
import com.example.doctorregistration.R;
import com.example.doctorregistration.Launcher.RegisterDorP;
import com.example.doctorregistration.Other.Validate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


/**
 *
 * This activity is the Patient registration page
 * Launches after user selects to register as a Patient
 *
 */
import static android.app.ProgressDialog.show;


public class PatientRegistration extends AppCompatActivity {

    public static final String TAG = "TAG";

    private EditText etFirstName, etLastName, etEmail, etPhoneNumber, etStreetAddress, etCountry, etPostalCode, etCity, etPassword, etHealthCardNum;
    private Button btnRegister;
    private TextView tvBack;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_registration);

        //Firebase related initialization
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        //Declaring all edit text boxes
        etFirstName = findViewById(R.id.firstName);
        etLastName = findViewById(R.id.lastName);
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.passwordfinal);
        etPhoneNumber = findViewById(R.id.phoneNumber);
        etStreetAddress = findViewById(R.id.streetAddress);
        etPostalCode = findViewById(R.id.postalCode);
        etCountry = findViewById(R.id.country);
        etCity = findViewById(R.id.city);
        etHealthCardNum = findViewById(R.id.healthCardNum);

        //Text View initialization
        tvBack = findViewById(R.id.textViewBack);

        //Declaring all buttons
        btnRegister = findViewById(R.id.submitButton);


        /*
         *
         * This method executes when TextView Back is clicked
         * Sends the back to registration or login selection page  ********************
         *
         */
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterDorP.class);
                startActivity(intent);
                finish();
            }
        });


        /*
         *
         * This method executes when Register button is clicked
         * It contains the bulk of the registration page's functionality
         * If successful, sends user to Login page to enter created credentials
         *
         */

        btnRegister.setOnClickListener(new View.OnClickListener() {
            /**
             * @param view The Register button that was clicked.
             */
            @Override
            public void onClick(View view) {

                String firstName = etFirstName.getText().toString();
                String lastName = etLastName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String phoneNumberString = etPhoneNumber.getText().toString();
                String street = etStreetAddress.getText().toString();
                String postalCode = etPostalCode.getText().toString();
                String country = etCountry.getText().toString();
                String city = etCity.getText().toString();
                String healthCardNumberString = etHealthCardNum.getText().toString();


                Validate v = new Validate(healthCardNumberString, phoneNumberString, email);

                /*
                 *********** All EditText Filled Validation ************
                 *
                 * This block validates if all Edit Text fields are complete
                 */

                //******** Checks if all fields are filled *********
                boolean allFieldsFilled = true;
                ArrayList<EditText> etFieldsList = new ArrayList<>(); //Creates ArrayList of Edit Texts


                etFieldsList.add(etFirstName);
                etFieldsList.add(etLastName);
                etFieldsList.add(etEmail);
                etFieldsList.add(etPassword);
                etFieldsList.add(etPhoneNumber);
                etFieldsList.add(etStreetAddress);
                etFieldsList.add(etPostalCode);
                etFieldsList.add(etCountry);
                etFieldsList.add(etCity);
                etFieldsList.add(etHealthCardNum);

                allFieldsFilled = v.allTextFieldsFilled(etFieldsList);


                /*
                 ************ Employee Number Validation *************
                 *
                 * This block checks if Employee number contains only numbers
                 */
                boolean validHealthCardNumber = false;
                int healthCardNumber = 0;

                //If the healthCardNum is not valid format and field is not blank -> Invalid healthCardNum error
                //If HealthCardNum is blank -> Blank field error dominates
                if (!v.isFieldBlank(etHealthCardNum)) {
                    if (!v.isStringToIntValid(healthCardNumberString))
                        etHealthCardNum.setError("Invalid health card number");
                    else {
                        healthCardNumber = Integer.parseInt(healthCardNumberString);  //Converts string to int
                        validHealthCardNumber = true;
                    }
                }

                /*
                 *********** Phone Number Validation ****************
                 *
                 * This block checks if phone number contains only numbers
                 */
                boolean validPhoneNumber = false;
                int phoneNumber = 0;

                //If the phone contains non-numbers and field is not blank -> Invalid phone number error
                //If phone number is blank -> Blank field error dominates
                if (!v.isFieldBlank(etPhoneNumber)) {
                    if (!v.isStringToIntValid(phoneNumberString))
                        etPhoneNumber.setError("Invalid phone number");
                    else {
                        phoneNumber = Integer.parseInt(phoneNumberString); //Converts string to int
                        validPhoneNumber = true;
                    }
                }


                /*
                 * ******* Password Length Validation *************
                 *
                 * This block checks if pass word length is larger than 6
                 * Password length MUST be minimum of 6
                 */
                boolean validPassword = false;

                if (!v.isFieldBlank(etPassword)) {
                    validPassword = v.minPasswordLength(etPassword);
                }


                /*
                 ************* Email Format Validation  *************
                 *
                 * This block checks if email is in the valid format
                 * ie. ______@_____.com
                 */
                boolean validEmail = false;

                if (!v.isFieldBlank(etEmail)) {
                    validEmail = v.isEmailValid(email);

                    if (!validEmail)
                        etEmail.setError("Invalid email (ie. john.doe@domain.com)");
                }

                /*
                 ********** Registration Attempt ***********
                 *
                 * If the form is complete; the employee number, phone number and email is valid;
                 * the password is long enough and a speciality is selected. Then Doctor user is created
                 *
                 * The email and password is stored in the Firebase database for authentication
                 * All Doctor variables (parameters in Doctor constructor) is store in the Firestore database
                 *
                 */


                if (allFieldsFilled && validHealthCardNumber && validEmail && validPhoneNumber && validPassword) {
                    Firebase firebase = new Firebase();

                    firebase.createNewUser(PatientRegistration.this, "Patient", healthCardNumber, null,
                            firstName, lastName, email, password, phoneNumber, country, city, street, postalCode,
                            new Firebase.AuthenticationCallback() {
                                @Override
                                public void onSuccess(String userType) {
                                    Intent intent = new Intent (PatientRegistration.this, Login.class);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onFailure(String message) {
                                    Log.d(TAG, "ERROR when creating Patient profile in firebase");
                                }
                            });

                } else
                    Toast.makeText(getApplicationContext(), "Registration Unsuccessful.", Toast.LENGTH_SHORT).show();

            }
        });

    }
}





