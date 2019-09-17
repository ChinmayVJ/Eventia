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

public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText pass;
    private TextView loginText;
    private Button loginButton;

    FirebaseAuth fAuth;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.logIn_email);
        pass = findViewById(R.id.logIn_password);
        loginButton = findViewById(R.id.logIn_button);
        loginText = findViewById(R.id.logIn_text);

        fAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String femail = email.getText().toString().trim();
                String fpass = pass.getText().toString().trim();

                if(TextUtils.isEmpty(femail)){
                    email.setError("*Email Required");
                    return;
                }
                if(TextUtils.isEmpty(fpass)){
                    pass.setError("*Password Required");
                    return;
                }

                dialog.setMessage("Logging in ...");
                dialog.show();

                fAuth.signInWithEmailAndPassword(femail, fpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(), HomePage.class));
                            finish();
                            dialog.dismiss();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });
    }
}
