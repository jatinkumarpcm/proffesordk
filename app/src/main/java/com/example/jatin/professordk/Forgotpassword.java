package com.example.jatin.professordk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by jatin on 4/1/2018.
 */

public class Forgotpassword extends AppCompatActivity {

    EditText  et_Email;
    Button   btn_reset_password;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        et_Email = findViewById(R.id.et_forgot_password);
        btn_reset_password  =findViewById(R.id.btn_submit);

        firebaseAuth = FirebaseAuth.getInstance();
        btn_reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{

                    String email = et_Email.getText().toString();
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {
                                Toast.makeText(Forgotpassword.this, "password reset email sent", Toast.LENGTH_SHORT).show(); finish();
                                startActivity(new Intent(getApplicationContext(),Signin.class));
                                
                            }
                            else {

                                Toast.makeText(Forgotpassword.this, "Error in sending reset email", Toast.LENGTH_SHORT).show();
                            }
                            
                        }
                    });



                }catch (Exception e)
                {
                    System.out.println(e);
                    Toast.makeText(Forgotpassword.this, ""+e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
