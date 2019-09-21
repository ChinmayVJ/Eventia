package com.example.loginactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.loginactivity.Classes.EventData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateEvent extends AppCompatActivity {

    EditText groupName;
    EditText eventName;
    EditText description;
    TextView datePickerText;
    TextView timePickerText;
    EditText duration;
    EditText eventLocationBldg;
    EditText interests;
    ImageButton backButton;
    ImageView companyPic;
    Button createEvent;

    DatePickerDialog.OnDateSetListener dateSetListener;
    TimePickerDialog.OnTimeSetListener timeSetListener;

    FirebaseDatabase fData;
    DatabaseReference fDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        groupName = findViewById(R.id.group_name_text);
        eventName = findViewById(R.id.event_name_text);
        description = findViewById(R.id.description_text);
        interests = findViewById(R.id.interests_edit_text);

        duration = findViewById(R.id.duration_info);
        eventLocationBldg = findViewById(R.id.event_location_bldg);

        datePickerText = findViewById(R.id.date_info);
        timePickerText = findViewById(R.id.time_info);
        backButton = findViewById(R.id.toolbar_back_button);
        companyPic = findViewById(R.id.company_pic);
        createEvent = findViewById(R.id.create_event_button);


        fData = FirebaseDatabase.getInstance();
        fDatabase = fData.getReference().child("Event Information");
        fDatabase.keepSynced(true);

        datePickerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CreateEvent.this,
                        dateSetListener,
                        year, month, day);

                dialog.show();
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String date = String.format("%02d",day) + "/" + String.format("%02d",month) + "/" + year;
                datePickerText.setText(date);
            }
        };


        timePickerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);

                TimePickerDialog timeDialog = new TimePickerDialog(
                        CreateEvent.this,
                        timeSetListener,
                        hour, minute,
                        DateFormat.is24HourFormat(CreateEvent.this));

                timeDialog.show();
            }
        });
        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                String time = String.format("%02d",hour)+ ":"+ String.format("%02d",minute);
                timePickerText.setText(time);
            }
        };


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), HomePage.class));
                finish();
            }
        });

    }

    public void afterCreateButtonPressed(View view){
        String host_name = "CJ";
        String group_name = groupName.getText().toString().trim();
        String event_name = eventName.getText().toString().trim();
        String descrip = description.getText().toString().trim();
        String dateOfEvent = datePickerText.getText().toString().trim();
        String timeOfEvent = timePickerText.getText().toString().trim();
        int duratn = Integer.parseInt(duration.getText().toString().trim());
        String address = eventLocationBldg.getText().toString().trim();
        ArrayList<String> interests_array = new ArrayList<>();
        interests_array.add(interests.getText().toString().trim());

        EventData evData = new EventData(host_name, group_name, event_name, descrip, dateOfEvent, timeOfEvent, duratn, address, interests_array);

        String id = fDatabase.push().getKey();
        fDatabase.child(id).setValue(evData);
        Toast.makeText(this, "Data Inserted", Toast.LENGTH_SHORT).show();
    }
}
