package com.example.customermanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String LOG_TAG = RegisterActivity.class.getName();
    private static final String PREF_KEY = RegisterActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 99;

    EditText userNameET;
    EditText userEmailET;
    EditText passwordET;
    EditText passwordAgainET;
    EditText phoneET;
    Spinner phoneSpinner;
    EditText addressET;
    RadioGroup accountTypeGroup;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Bundle bundle = getIntent().getExtras();
        // int secret_key = bundle.getInt("SECRET_KEY");
        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secret_key != 99) {
            finish();
        }

        userNameET = findViewById(R.id.userNameEditText);
        userEmailET = findViewById(R.id.userEmailEditText);
        passwordET = findViewById(R.id.passwordEditText);
        passwordAgainET = findViewById(R.id.passwordAgainEditText);
        phoneET = findViewById(R.id.phoneEditText);
        phoneSpinner = findViewById(R.id.phoneSpinner);
        addressET = findViewById(R.id.addressEditText);
        accountTypeGroup = findViewById(R.id.accountTypeGroup);
        accountTypeGroup.check(R.id.customerRadioButton);


        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String userName = preferences.getString("userName", "");
        String password = preferences.getString("password", "");

        userEmailET.setText(userName);
        passwordET.setText(password);
        passwordAgainET.setText(password);

        phoneSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.phone_modes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        phoneSpinner.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();

        Log.i(LOG_TAG, "onCreate");
    }

    public void registerProfile(View view) {
        String userName = userNameET.getText().toString();
        String userEmail= userEmailET.getText().toString();
        String password = passwordET.getText().toString();
        String passwordAgain = passwordAgainET.getText().toString();

        if (!password.equals(passwordAgain)) {
            Log.e(LOG_TAG, "A 2 jelszó nem egyezik meg.");
            return;
        }

        String phoneNumber = phoneET.getText().toString();
        String phoneType = phoneSpinner.getSelectedItem().toString();
        String address = addressET.getText().toString();

        int checkedId = accountTypeGroup.getCheckedRadioButtonId();
        RadioButton radioButton = accountTypeGroup.findViewById(checkedId);
        String accountType = radioButton.getText().toString();

        Log.i(LOG_TAG , "Regisztrált: " + userName + ", e-mail: " + userEmail);
        //startManaging();

        mAuth.createUserWithEmailAndPassword(userEmail, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(LOG_TAG, "User created successfully");
                    startManaging();
                } else {
                    Log.d(LOG_TAG, "User wasn't created successfully");
                    Toast.makeText(RegisterActivity.this, "User wasn't created successfully: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void cancel(View view) {
        finish();
    }

    public void startManaging(/* registered user data */) {
        Intent intent = new Intent(this, CustomerListActivity.class);
        //intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_TAG, "onRestart");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();
        Log.i(LOG_TAG, selectedItem);
        // TODO
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO
    }
}