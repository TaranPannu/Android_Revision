package com.example.fall_2022;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity2 extends AppCompatActivity {
private TextView textview;
    //VLEWS
    private EditText mEmailEt, mPasswordEt;
    private Button mRegisterBtn;
    private FirebaseAuth mAuth;

    //progressbar to display while registering user
    ProgressDialog progressDialog;
// declare an instance of firebaseAUTH
TextView mHaveAccountTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
       // Action bar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar. setTitle ("Create Account");
        //enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        //init
        mHaveAccountTv=findViewById(R.id.have_accountTv);
        mEmailEt = findViewById(R.id.EmailEt) ;
        mPasswordEt = findViewById(R.id.passwordET) ;
        mRegisterBtn = findViewById(R.id.registerBtn) ;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage ("Registering User...");
        mAuth = FirebaseAuth.getInstance();


        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // input email, password
                String email = mEmailEt.getText().toString().trim();
                //String email = mEmailEt.getText().toString().trim();
                String password = mPasswordEt.getText().toString().trim();
                //validate
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    //set error and focuss to email edittext
                    mEmailEt.setError("Invalid Email");
                    mEmailEt.setFocusable(true);
                } else if (password.length() < 6) {
                    //set error and focuss to password edittext
                    mPasswordEt.setError("Password length at least 6 characters");
                    mPasswordEt.setFocusable(true);
                } else {
                 //   Toast.makeText(MainActivity2.this, "register call", Toast.LENGTH_SHORT).show();
                    registerUser(email, password); //register user
                }

            }
        });
        mHaveAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity2.this, LoginActivity.class));
                finish();
            }
        });

    }
            private void registerUser(String email, String password) {
                //email and password pattern is valid, show progress dialog and start registering use2|
          //      Toast.makeText(MainActivity2.this, "register", Toast.LENGTH_SHORT).show();
           //     startActivity(new Intent(MainActivity2.this, ProfileActivity.class));

                progressDialog.show();
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                     //   Toast.makeText(MainActivity2.this, "hhhhh", Toast.LENGTH_SHORT).show();

                        if(task.isSuccessful()){
                            progressDialog.dismiss();
        FirebaseUser user=mAuth.getCurrentUser();
                            Toast.makeText(MainActivity2.this, "Registered...\n"+user.getEmail(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity2.this,ProfileActivity.class));
                            finish();
                        }else{
                            progressDialog.dismiss();

                            Toast.makeText(MainActivity2.this, "Authenciation failed", Toast.LENGTH_SHORT).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity2.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

    public boolean onSupportNavigateUp(){
        onBackPressed();// go previous activity
        return super.onSupportNavigateUp();
    }
}