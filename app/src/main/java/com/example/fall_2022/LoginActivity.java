package com.example.fall_2022;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
//views

    EditText mEmailEt, mPasswordEt;
    TextView notHaveAccntTv;
    TextView mRecoverPassTv;

    Button mLoginBtn;
    private FirebaseAuth mAuth;
    ProgressDialog pd;
// ...
// Initialize Firebase Auth
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Account");
        //enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        //init
        mEmailEt = findViewById(R.id.EmailEt);
        mAuth = FirebaseAuth.getInstance();

        mPasswordEt = findViewById(R.id.passwordET);
        notHaveAccntTv = findViewById(R.id.nothave_accountTv);
        mRecoverPassTv=findViewById(R.id.recoverPassTv);
        mLoginBtn = findViewById(R.id.LoginBtn);

//login button click

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //input data
                String email = mEmailEt.getText ().toString();
                String passw = mPasswordEt.getText().toString() .trim();
                if (!Patterns.EMAIL_ADDRESS.matcher (email) .matches()) {
//invalid email paatern set error
                    mEmailEt.setError("Invalid Email");
                    mEmailEt.setFocusable (true) ;
                }
                else {
loginUser(email,passw);
                }
        }});

        notHaveAccntTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, MainActivity2.class));
                finish();
            }
        });

        // recover password text view
        mRecoverPassTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRecoverPassworDialog();

            }
        });


//intin progress dialog
        pd=new ProgressDialog(this);
    }

    private void showRecoverPassworDialog() {
        //alert dialog
        AlertDialog.Builder builer=new AlertDialog.Builder(this);
        builer.setTitle("Recover Password");
        //set layout linear layout
        LinearLayout linearLayout=new LinearLayout(this);
        //views to set in dialog
       final  EditText emailEt= new EditText(this);
        emailEt.setHint("Email");
        emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        emailEt.setMinEms(10);
        linearLayout.addView(emailEt);
        linearLayout.setPadding(10,10,10,10);
        builer.setView(linearLayout);
        //button Recover
        builer.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//input email
                String email=emailEt.getText().toString().trim();
                beginRecovery(email);
            }
        });
        //button Cancel
        builer.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
//dismiss dialog
dialog.dismiss();
            }
        });
// show dialog
        builer.create().show();
    }

    private void beginRecovery(String email) {
        // show progress dialog
        pd.setMessage("Sending Email...");
        pd.show();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
if(task.isSuccessful()){
    Toast.makeText(LoginActivity.this, "Email sent", Toast.LENGTH_SHORT).show();
}
else{
    Toast.makeText(LoginActivity.this, "Failed....", Toast.LENGTH_SHORT).show();
}
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
// show complete error message
                Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loginUser(String email, String passw) {
//show progress dialog
        pd.setMessage("Logging In....");
        pd.show();
            mAuth.signInWithEmailAndPassword(email, passw)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //dismiss progress log
                                pd.dismiss();
                                //sign in success , update UI with signed in user information
                                FirebaseUser user= mAuth.getCurrentUser();
                                // user is logged to start login activty
startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
finish();
                            } else {
                                pd.dismiss();

                                Toast.makeText(LoginActivity.this, "Authenciation failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//error get and show message
                    pd.dismiss();

                    Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    public boolean onSupportNavigateUp(){
        onBackPressed();// go previous activity
        return super.onSupportNavigateUp();
    }
}