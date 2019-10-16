package com.example.loginactivity.EventRelated;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.loginactivity.Classes.EventData;
import com.example.loginactivity.Classes.UserData;
import com.example.loginactivity.ExtraActivities.HomePage;
import com.example.loginactivity.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

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
    CircleImageView companyPic;
    ImageView editCompanyPic;
    Button createEvent;
    ProgressBar progressBar;

    DatePickerDialog.OnDateSetListener dateSetListener;
    TimePickerDialog.OnTimeSetListener timeSetListener;

    FirebaseAuth fAuth;
    FirebaseUser fUser;
    DatabaseReference fDatabase;
    StorageReference fReference;
    StorageTask<UploadTask.TaskSnapshot> uploadTask;

    String hostName;
    String category;
    String eventId;
    private static final int PICK_IMAGE_REQUEST = 1;
    String imageUrl;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        eventId = getIntent().getStringExtra("status");

        imageUrl = "Not uploading";

        toolbarTitle = findViewById(R.id.create_event_title);
        groupName = findViewById(R.id.group_name_text);
        eventName = findViewById(R.id.event_name_text);
        description = findViewById(R.id.description_text);
        categorySpinner = findViewById(R.id.category_spinner);
        duration = findViewById(R.id.duration_info);
        eventLocation = findViewById(R.id.event_location_new_event);

        progressBar = findViewById(R.id.company_pic_loading);
        datePickerText = findViewById(R.id.date_info);
        timePickerText = findViewById(R.id.time_info);
        backButton = findViewById(R.id.toolbar_back_button);
        companyPic = findViewById(R.id.company_pic);
        editCompanyPic = findViewById(R.id.edit_company_pic);
        createEvent = findViewById(R.id.create_event_button);

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fDatabase = FirebaseDatabase.getInstance().getReference();
        fReference = FirebaseStorage.getInstance().getReference().child("Event Information");
        fDatabase.keepSynced(true);

        progressBar.setVisibility(View.INVISIBLE);

        String[] categories = new String[]{"- Select -", "Outdoors & Adventure", "Technology", "Health & Wellness", "Sports & Fitness", "Learning", "Food & Drink", "Language & Culture", "Music", "Film", "Book Clubs", "Dance", "Fashion", "Social", "Career & Business"};
        ArrayAdapter<String> cat_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        categorySpinner.setAdapter(cat_adapter);

        if (!eventId.equals("new")){

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
                    try {
                        String uri = eventData.getImageUrl();
                        if(!uri.equals("Not uploading")) {
                            imageUrl = uri;
                            Picasso.get().load(uri).into(companyPic);
                        }
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }

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

        editCompanyPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        /*backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/

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

    public void chooseImage(){
        imageUrl = "Still Uploading";
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();

            Picasso.get().load(imageUri).into(companyPic);
        }
    }

    private void uploadImage(){

        if(imageUri != null){

            progressBar.setVisibility(View.VISIBLE);

            final StorageReference fileReference = fReference.child("companyPic." + getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        imageUrl = downloadUri.toString();

                        readData();
                    }
                }
            });
        }
    }

    public void afterCreateButtonPressed(View view){

        if (imageUrl.equals("Still Uploading"))
            uploadImage();
        else
            readData();

    }

    public void readData() {
        String group_name = groupName.getText().toString().trim();
        String event_name = eventName.getText().toString().trim();
        String description_ = description.getText().toString().trim();
        String dateOfEvent = datePickerText.getText().toString().trim();
        String timeOfEvent = timePickerText.getText().toString().trim();
        double duration_ = Double.parseDouble(duration.getText().toString().trim());
        String address = eventLocation.getText().toString().trim();

        String id;
        if (eventId.equals("new"))
            id = fDatabase.child("Event Information").push().getKey();
        else
            id = eventId;

        EventData evData = new EventData(id, hostName, group_name, event_name, description_, dateOfEvent, timeOfEvent, duration_, address, category);
        if (!imageUrl.equals("Not uploading") && !imageUrl.equals("Still Uploading"))
            evData.setImageUrl(imageUrl);

        fDatabase.child("Event Information").child(id).setValue(evData);
        Toast.makeText(getApplicationContext(), "Event Created", Toast.LENGTH_SHORT).show();

        if (!eventId.equals("new")) {
            Toast.makeText(getApplicationContext(), "Changes Applied", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), HomePage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        finish();
    }

}
