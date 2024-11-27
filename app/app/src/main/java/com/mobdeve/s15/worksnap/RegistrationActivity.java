package com.mobdeve.s15.worksnap;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Transaction;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RegistrationActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private boolean validEmployerID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();//get FireBase Authentication instance
        validEmployerID = false;
        EditText fullNameEditText = findViewById(R.id.et_name);
        EditText emailEditText = findViewById(R.id.et_email);
        EditText passwordEditText = findViewById(R.id.et_password);
        RadioGroup roleRadioGroup = findViewById(R.id.rg_role);
        EditText employerIDEditText = findViewById(R.id.et_employer_id);
        EditText titleEditText = findViewById(R.id.et_title);
        Button registerButton = findViewById(R.id.btn_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = fullNameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                int selectedRoleId = roleRadioGroup.getCheckedRadioButtonId();
                String employerID = employerIDEditText.getText().toString().trim();
                String title = titleEditText.getText().toString().trim();
                if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || selectedRoleId == -1 || title.isEmpty()) {
                    Toast.makeText(RegistrationActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                RadioButton selectedRadioButton = findViewById(selectedRoleId);
                String selectedRole = selectedRadioButton.getText().toString();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                if (selectedRole.equals("Employee")){
                    ArrayList<String> documentIDs = new ArrayList<>();
                    db.collection("users")
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        documentIDs.add(document.getId()); // Add document ID to the list
                                    }

                                    // Print or use the document IDs
                                    System.out.println("Document IDs: " + documentIDs);
                                    Log.d("Document IDs", "" + documentIDs);
                                    if (employerID.isEmpty() || !documentIDs.contains(employerID)) {
                                        Toast.makeText(RegistrationActivity.this, "Invalid Employer ID!", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        validEmployerID = true;
                                    }
                                } else {
                                    // Handle the error
                                    System.err.println("Error getting documents: " + task.getException().getMessage());
                                    Log.w("Error getting documents", task.getException().getMessage());
                                }
                            });
                }
                mAuth.createUserWithEmailAndPassword(email, password) //Try to add user's credentials to Auth
                        .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser(); //get the current user
                                    Map<String, Object> User = new HashMap<>();
                                    ArrayList<badgesData> badges = new ArrayList<>();
                                    ArrayList<String> employeeIDs = new ArrayList<>();
                                    User.put("username", fullName);
                                    User.put("profilePhoto", "https://firebasestorage.googleapis.com/v0/b/worksnap-9bdb3.firebasestorage.app/o/images%2Fdanda.jpeg?alt=media&token=651730df-277c-4c9e-b08a-8c36acaec419");
                                    User.put("title", title);
                                    User.put("email", email);
                                    if (validEmployerID)
                                        User.put("employer", employerID);
                                    else
                                        User.put("employer", "");
                                    User.put("employees", employeeIDs);
                                    User.put("work_start", "");
                                    User.put("work_end", "");
                                    User.put("image_count_today", 0);
                                    User.put("image_count_week", 0);
                                    User.put("image_count_year", 0);
                                    User.put("badges", badges);

                                    assert user != null;
                                    String uid = user.getUid();
                                    db.collection("users").document(uid)
                                            .set(User)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "Registered User successfully added!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error writing document", e);
                                                }
                                            });
                                    if (validEmployerID){
                                        db.collection("users")
                                                .document(employerID)
                                                .update("employees", FieldValue.arrayUnion(uid))
                                                .addOnSuccessListener(aVoid -> {
                                                    // Successfully updated
                                                    System.out.println("Employee ID added to the Employer's employee list!");
                                                })
                                                .addOnFailureListener(e -> {
                                                    // Handle the error
                                                    System.err.println("Error updating array field: " + e.getMessage());
                                                });
                                    }
                                    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
    public void showEmployerIDInput (View v){
        TextView employerID = findViewById(R.id.employer_id);
        EditText employerIDInput = findViewById(R.id.et_employer_id);
        employerID.setVisibility(View.VISIBLE);
        employerIDInput.setVisibility(View.VISIBLE);
    }

    public void hideEmployerIDInput (View v){
        TextView employerID = findViewById(R.id.employer_id);
        EditText employerIDInput = findViewById(R.id.et_employer_id);
        employerID.setVisibility(View.GONE);
        employerIDInput.setVisibility(View.GONE);
    }

}