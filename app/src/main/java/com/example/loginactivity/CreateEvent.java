package com.example.loginactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class CreateEvent extends AppCompatActivity {

    ImageButton backButton;
    ImageView companyPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        backButton = findViewById(R.id.toolbar_back_button);
        companyPic = findViewById(R.id.company_pic);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), HomePage.class));
            }
        });

        /*companyPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EventInfo.class));
            }
        });*/
    }
}
