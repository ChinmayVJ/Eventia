package com.example.loginactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText password;
    TextView signUpText;

    FirebaseAuth mAuth;
    ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        name = findViewById(R.id.signIn_name);
        email = findViewById(R.id.signIn_email);
        password = findViewById(R.id.signIn_password);
        Button signInButton = findViewById(R.id.signIn_button);
        signUpText = findViewById(R.id.signIn_text);

        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String femail = email.getText().toString().trim();
                String fpass = password.getText().toString().trim();
                String fname = name.getText().toString().trim();

                if(TextUtils.isEmpty(femail)){
                    email.setError("*Email Required");
                    return;
                }
                if(TextUtils.isEmpty(fpass)){
                    password.setError("*Password Required");
                    return;
                }

                if(TextUtils.isEmpty(fname)){
                    name.setError("*Name Required");
                    return;
                }

                mDialog.setMessage("Signing in ...");
                mDialog.show();

                mAuth.createUserWithEmailAndPassword(femail, fpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), HomePage.class));
                            mDialog.dismiss();
                            finish();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Not Successful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }
}
