package com.example.loginactivity.Authentication;

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

import com.example.loginactivity.Classes.UserData;
import com.example.loginactivity.ExtraActivities.HomePage;
import com.example.loginactivity.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText password;
    TextView signUpText;

    FirebaseAuth fAuth;
    DatabaseReference fDatabase;

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

        fAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance().getReference().child("User Information");
        fDatabase.keepSynced(true);

        mDialog = new ProgressDialog(this);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String femail = email.getText().toString().trim();
                String fpass = password.getText().toString().trim();
                final String fname = name.getText().toString().trim();

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

                final UserData u_data = new UserData(fname);

                mDialog.setMessage("Signing in ...");
                mDialog.show();

                fAuth.createUserWithEmailAndPassword(femail, fpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            FirebaseUser user = fAuth.getCurrentUser();
                            String fUid = user.getUid();

                            fDatabase.child(fUid).setValue(u_data);

                            mDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Account Created Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), HomePage.class));
                            finish();
                        }
                        else{
                            mDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Couldn't Create Account", Toast.LENGTH_SHORT).show();
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
