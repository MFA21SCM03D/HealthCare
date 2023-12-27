package com.example.rohanspc.healthcare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rohanspc.healthcare.Models.User;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity{

    private EditText emailEditText,passwordEditText,usernameEditText,contactEditText;
    private Button btnSignup;
    private TextView toggleTextView;
    private RadioButton mRadioButton,fRadioButton;
    private static final String TAG = "SignupActivity";
    private static final String KEY_FULL_NAME = "Full Name";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_PASSWORD = "Password";
    private static final String KEY_CONTACT = "Contact";
    private static final String KEY_SEX = "Sex";


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore db ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailEditText = findViewById(R.id.signup_email);
        passwordEditText = findViewById(R.id.signup_password);
        btnSignup  = findViewById(R.id.btn_signup);
        toggleTextView = findViewById(R.id.sign_up_show);
        mRadioButton = findViewById(R.id.male);
        fRadioButton = findViewById(R.id.female);

        usernameEditText = findViewById(R.id.signup_name);

        contactEditText = findViewById(R.id.editText);

        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        toggleTextView.setVisibility(View.INVISIBLE);

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (passwordEditText.getText().length() > 0){
                    toggleTextView.setVisibility(View.VISIBLE);
                }
                else{
                    toggleTextView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        toggleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toggleTextView.getText() == "SHOW"){
                    toggleTextView.setText("HIDE");
                    passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordEditText.setSelection(passwordEditText.length());

                }
                else{
                    toggleTextView.setText("SHOW");
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordEditText.setSelection(passwordEditText.length());
                }
            }
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        setupFirebaseAuth();


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                Log.d(TAG, "onClick: email: " + email + " password: " + password);
                signup(email,password);

            }
        });

    }


    private void signup(String email,String password){
        Log.d(TAG, "onClick: email: " + email + " password: " + password);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(SignupActivity.this,"Account created",Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onComplete: calling saveData");


                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(SignupActivity.this,"Verification email sent",Toast.LENGTH_SHORT).show();

                                    }
                                    else {
                                        Log.d(TAG, "onComplete: verification email sent");
                                    }

                                }
                            });
                            saveData(user);





                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                });

        
    }

     public void saveData(FirebaseUser user1){
         Log.d(TAG, "saveData: starting");
        String username = usernameEditText.getText().toString().trim();
         String email = emailEditText.getText().toString().trim();

         String contact = contactEditText.getText().toString().trim();
         String gender = "";
         Log.d(TAG, "saveData: " + username + " " + email + " " + contact + " " + gender);
         if (mRadioButton.isChecked()){
            gender = "male";
         }
         else if(fRadioButton.isChecked()){
             gender = "female";
         }

         User user = new User(username,email,gender,contact);
         Log.d(TAG, "saveData: " + user.toString());

        // Add a new document with a generated ID
        db.collection("Users").document(user1.getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
             @Override
             public void onSuccess(Void aVoid) {
                 mAuth.signOut();
                 Log.d(TAG,"Document saved");
                 finish();
             }
         }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 Log.w(TAG,"Document not saved");
             }
         });

     }
    private void setupFirebaseAuth(){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();

                if(user != null){
                    Log.d(TAG, "onAuthStateChanged: Signed in");


                }
                else{
                    Log.d(TAG, "onAuthStateChanged: Signed out");

                }
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy: Starting");
    }
}
