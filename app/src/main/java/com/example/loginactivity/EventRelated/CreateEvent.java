package com.example.loginactivity.EventRelated;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.loginactivity.Classes.EventData;
import com.example.loginactivity.Classes.UserData;
import com.example.loginactivity.ExtraActivities.HomePage;
import com.example.loginactivity.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class CreateEvent extends AppCompatActivity {

    TextView toolbarTitle;
    EditText groupName;
    EditText eventName;
    EditText description;
    TextView datePickerText;
    TextView timePickerText;
    EditText duration;
    EditText eventLocation;
    Spinner categorySpinner;
    ImageButton backButton;
    ImageView companyPic;
    Button createEvent;

    DatePickerDialog.OnDateSetListener dateSetListener;
    TimePickerDialog.OnTimeSetListener timeSetListener;

    FirebaseAuth fAuth;
    FirebaseUser fUser;
    DatabaseReference fDatabase;

    String hostName;
    String category;
    String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        eventId = getIntent().getStringExtra("status");

        toolbarTitle = findViewById(R.id.create_event_title);
        groupName = findViewById(R.id.group_name_text);
        eventName = findViewById(R.id.event_name_text);
        description = findViewById(R.id.description_text);
        categorySpinner = findViewById(R.id.category_spinner);
        duration = findViewById(R.id.duration_info);
        eventLocation = findViewById(R.id.event_location_new_event);

        datePickerText = findViewById(R.id.date_info);
        timePickerText = findViewById(R.id.time_info);
        backButton = findViewById(R.id.toolbar_back_button);
        companyPic = findViewById(R.id.company_pic);
        createEvent = findViewById(R.id.create_event_button);

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fDatabase = FirebaseDatabase.getInstance().getReference();
        fDatabase.keepSynced(true);

        String[] categories = new String[]{"- Select -", "Outdoors & Adventure", "Technology", "Health & Wellness", "Sports & Fitness", "Learning", "Food & Drink", "Language & Culture", "Music", "Film", "Book Clubs", "Dance", "Fashion", "Social", "Career & Business"};
        ArrayAdapter<String> cat_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        categorySpinner.setAdapter(cat_adapter);

        if (!eventId.equals("new")){

            toolbarTitle.setText("Edit Event Details");
            createEvent.setText("Apply Changes");

            fDatabase.child("Event Information").child(eventId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    EventData eventData = dataSnapshot.getValue(EventData.class);

                    groupName.setText(eventData.getGroupName());
                    eventName.setText(eventData.getEventName());
                    description.setText(eventData.getDescription());
                    category = eventData.getCategory();
                    duration.setText(String.valueOf(eventData.getDuration()));
                    eventLocation.setText(eventData.getAddress());
                    datePickerText.setText(eventData.getDateOfEvent());
                    timePickerText.setText(eventData.getStartTime());

                    fDatabase.child("Event Information").child(eventId).removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 1:
                        category = "Outdoors & Adventure";
                        break;
                    case 2:
                        category = "Technology";
                        break;
                    case 3:
                        category = "Health & Wellness";
                        break;
                    case 4:
                        category = "Sports & Fitness";
                        break;
                    case 5:
                        category = "Learning";
                        break;
                    case 6:
                        category = "Food & Drink";
                        break;
                    case 7:
                        category = "Language & Culture";
                        break;
                    case 8:
                        category = "Music";
                        break;
                    case 9:
                        category = "Film";
                        break;
                    case 10:
                        category = "Book Clubs";
                        break;
                    case 11:
                        category = "Dance";
                        break;
                    case 12:
                        category = "Fashion";
                        break;
                    case 13:
                        category = "Social";
                        break;
                    case 14:
                        category = "Career & Business";
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if (eventId.equals("new"))
                    category = "Social";
            }
        });

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

                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String date = String.format("%02d",day) + "/" + String.format("%02d",month+1) + "/" + year;
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

        fDatabase.child("User Information").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserData userData = dataSnapshot.getValue(UserData.class);
                hostName = userData.getName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void afterCreateButtonPressed(View view){

        String group_name = groupName.getText().toString().trim();
        String event_name = eventName.getText().toString().trim();
        String descrip = description.getText().toString().trim();
        String dateOfEvent = datePickerText.getText().toString().trim();
        String timeOfEvent = timePickerText.getText().toString().trim();
        double duratn = Double.parseDouble(duration.getText().toString().trim());
        String address = eventLocation.getText().toString().trim();

        String id;
        if (eventId.equals("new"))
            id = fDatabase.child("Event Information").push().getKey();
        else
            id = eventId;

        EventData evData = new EventData(id, hostName, group_name, event_name, descrip, dateOfEvent, timeOfEvent, duratn, address, category);

        fDatabase.child("Event Information").child(id).setValue(evData);
        Toast.makeText(this, "Event Created", Toast.LENGTH_SHORT).show();

        if (!eventId.equals("new")) {
            Toast.makeText(this, "Changes Applied", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, HomePage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        finish();
    }
}
