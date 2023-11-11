package com.example.doctorregistration;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * This activity is the Doctor registration page
 * Launches after user selects to register as a doctor
 *
 */
public class DoctorRegistration extends AppCompatActivity {

    public static final String TAG = "TAG";
    private EditText etFirstName, etLastName, etEmail, etPassword, etPhoneNumber, etStreetAddress, etPostalCode, etCountry, etCity, etEmployeeNumber;
    private Button btnRegister;
    private CheckBox cbPediatrics, cbFamilyMedicine, cbDermatology, cbObgyn, cbCardiology, cbNeurology, cbOrthopedic, cbOphthalmology;
    private TextView tvSpecialtyText, tvBack;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_registration);

        //Firebase related initialization
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        //Edit text initialization
        etFirstName = findViewById(R.id.editTextFirstName);
        etLastName = findViewById(R.id.editTextLastName);
        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
        etPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        etStreetAddress = findViewById(R.id.editTextStreetAddress);
        etPostalCode = findViewById(R.id.editTextPostalCode);
        etCountry = findViewById(R.id.editTextCountry);
        etCity = findViewById(R.id.editTextCity);
        etEmployeeNumber = findViewById(R.id.editTextEmployeeNumber);

        //Text View initialization
        tvSpecialtyText = findViewById(R.id.textViewSpecialtyText);
        tvBack = findViewById(R.id.textViewBack);

        //Check Box initialization
        cbPediatrics = findViewById(R.id.checkBoxPediatrics);
        cbFamilyMedicine = findViewById(R.id.checkBoxFamilyMedicine);
        cbDermatology = findViewById(R.id.checkBoxDermatology);
        cbObgyn = findViewById(R.id.checkBoxOBGYN);
        cbCardiology = findViewById(R.id.checkBoxCardiology);
        cbNeurology = findViewById(R.id.checkBoxNeurology);
        cbOrthopedic = findViewById(R.id.checkBoxOrthopedic);
        cbOphthalmology = findViewById(R.id.checkBoxOphthalmology);

        //Button initialization
        btnRegister = findViewById(R.id.buttonRegister);


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
                String employeeNumberString = etEmployeeNumber.getText().toString();

                Validate v = new Validate(employeeNumberString, phoneNumberString, email);

                /*
                 *********** All EditText Filled Validation ************
                 *
                 * This block validates if all Edit Text fields are complete
                 */
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
                etFieldsList.add(etEmployeeNumber);

                allFieldsFilled = v.allTextFieldsFilled(etFieldsList);


                /*
                 ************ Employee Number Validation *************
                 *
                 * This block checks if Employee number contains only numbers
                 */
                boolean validEmployeeNumber = false;
                int employeeNumber = 0;

                //If the email is not valid format and field is not blank -> Invalid email error
                //If email is blank -> Blank field error dominates
                if (!v.isFieldBlank(etEmployeeNumber)) {
                    if (!v.isStringToIntValid(employeeNumberString))
                        etEmployeeNumber.setError("Invalid employee number");
                    else {
                        employeeNumber = Integer.parseInt(employeeNumberString);  //Converts string to int
                        validEmployeeNumber = true;
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
                 *********** Minimum CheckBox Checked Validation *******
                 *
                 * This block checks if at least one Doctor specialty checkbox is checked
                 * Doctors can have one or more specialties
                 */
                boolean minCheckBox = false;
                ArrayList<CheckBox> specialtyList = new ArrayList<>();

                specialtyList.add(cbPediatrics);
                specialtyList.add(cbFamilyMedicine);
                specialtyList.add(cbDermatology);
                specialtyList.add(cbObgyn);
                specialtyList.add(cbCardiology);
                specialtyList.add(cbNeurology);
                specialtyList.add(cbOrthopedic);
                specialtyList.add(cbOphthalmology);

                //checks if at least one check box is checked
                minCheckBox = v.minCheckBoxChecked(specialtyList);

                if (!minCheckBox)
                    tvSpecialtyText.setError("Please select a specialty");


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


                if (allFieldsFilled && validEmployeeNumber && validEmail && validPhoneNumber && minCheckBox && validPassword) {
                    Firebase firebase = new Firebase();

                    firebase.createNewUser(DoctorRegistration.this, "Doctor", employeeNumber, specialtyList,
                            firstName, lastName, email, password, phoneNumber, country, city, street, postalCode,
                            new Firebase.AuthenticationCallback() {
                                @Override
                                public void onSuccess(String userType) {
                                    Intent intent = new Intent (DoctorRegistration.this, Login.class);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onFailure(String message) {
                                    Log.d(TAG, "ERROR when creating Doctor profile in firebase");
                                }
                            });

                } else
                    Toast.makeText(DoctorRegistration.this, "Registration Unsuccessful.", Toast.LENGTH_SHORT).show();

            }

        });

    }

}