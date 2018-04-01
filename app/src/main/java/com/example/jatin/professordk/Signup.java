package com.example.jatin.professordk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Signup extends AppCompatActivity {

    EditText email, password;
    Button register;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    TextView tt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        register = findViewById(R.id.btn_register);
        tt = findViewById(R.id.tt);

        tt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Signup.this, Signin.class));
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                display();
                password.setText("");
            }
        });
    }


    private void display() {
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final String s_name = email.getText().toString();
        final String s_pass = password.getText().toString();
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        boolean s_email = true;
        boolean s_password = true;

        if (TextUtils.isEmpty(s_name) || !Patterns.EMAIL_ADDRESS.matcher(s_name).matches()) {
            email.setError("please enter a valid eamil");
            s_email = false;
        }

        if (TextUtils.isEmpty(s_pass) || password.getText().toString().length() < 6) {
            password.setError("please enter a 6 digit password ");
            s_password = false;
        }

        if (!s_email || !s_password)
            return;//Stops the execution is either one is false

        final ProgressDialog progressDialog = new ProgressDialog(this);  ///
        progressDialog.setMessage("Signing Up..");
        progressDialog.show();

        try {
            mAuth.createUserWithEmailAndPassword(s_name, s_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();

                    if (task.isSuccessful()) {
                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (firebaseUser == null) {
                            initUser();
                            System.out.println("something went wrong");
                            return;//Stops the execution here
                        }

                        firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(Signup.this, "Email verification sent", Toast.LENGTH_SHORT).show();
                            }
                        });

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            Intent intent = new Intent(Signup.this, ProfileActivity.class);
                            startActivity(intent);
                        }
                    } else {//Error
                        Toast.makeText(Signup.this, "" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage() + "catch", Toast.LENGTH_SHORT).show();
        }
    }

    private void initUser() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        System.out.println("intUser Callled by email vertication method");
        display();
    }


}
