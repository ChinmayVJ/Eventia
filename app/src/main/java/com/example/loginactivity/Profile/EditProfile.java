package com.example.loginactivity.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.loginactivity.Classes.UserData;
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
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.chip.Chip;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {

    NachoTextView nachoTextView;
    CircleImageView profilePic;
    ImageButton backButton;
    EditText usersName;
    EditText usersAddress;
    Spinner genderSpinner;
    ProgressBar progressBar;
    ImageView editProf;

    FirebaseAuth fAuth;
    FirebaseUser fUser;
    DatabaseReference fDatabase;
    StorageReference fReference;
    StorageTask<UploadTask.TaskSnapshot> uploadTask;

    String gender = "Prefer Not to Say";;
    ArrayList<String> ints = new ArrayList<>();
    private static final int PICK_IMAGE_REQUEST = 1;
    String imageUrl;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fDatabase = FirebaseDatabase.getInstance().getReference().child("User Information").child(fUser.getUid());
        fReference = FirebaseStorage.getInstance().getReference().child("User Information").child(fUser.getUid());

        backButton = findViewById(R.id.toolbar_back_button_edit_profile);
        profilePic = findViewById(R.id.profile_pic_edit_profile);
        editProf = findViewById(R.id.edit_profile_pic);
        usersName = findViewById(R.id.name_entry);
        usersAddress = findViewById(R.id.address_entry);

        genderSpinner = findViewById(R.id.gender_spinner);
        nachoTextView = findViewById(R.id.nacho_text_view);
        progressBar = findViewById(R.id.profile_pic_loading);

        progressBar.setVisibility(View.INVISIBLE);
        gender = "Prefer Not to Say";
        imageUrl = "Not uploading";
        ints = new ArrayList<>();

        String[] items = new String[]{"- Select -", "Male", "Female", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        genderSpinner.setAdapter(adapter);

        String[] suggestions = new String[]{"Technology", "Tours", "Travel", "Nature", "Adventures", "Start Ups","Outdoors"};
        ArrayAdapter<String> suggestionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, suggestions);
        nachoTextView.setAdapter(suggestionAdapter);
        nachoTextView.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
        nachoTextView.enableEditChipOnTouch(true, true);

        fDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserData userData = dataSnapshot.getValue(UserData.class);
                usersName.setText(userData.getName());
                gender = userData.getGender();
                if(!userData.getAddress().equals("")){
                    usersAddress.setText(userData.getAddress());
                }
                try {
                    if (!userData.getImageUrl().equals("Not uploading")) {
                        Picasso.get().load(userData.getImageUrl()).into(profilePic);
                    }
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                }
                if(String.valueOf(userData.getUserInterests()) != null){
                    nachoTextView.setText(userData.getUserInterests());
                }

                fDatabase.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 1:
                        gender = "Male";
                        break;
                    case 2:
                        gender = "Female";
                        break;
                    case 3:
                        gender = "Other";
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
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

    private void uploadImage(){

        progressBar.setVisibility(View.VISIBLE);

        if(imageUri != null){
            final StorageReference fileReference = fReference.child("profilePic." + getFileExtension(imageUri));

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
                        Log.e("imageupl", imageUrl );

                        readData();
                    }
                    finish();
                }
            });

            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressBar.setProgress((int) progress);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();

            Picasso.get().load(imageUri).into(profilePic);
        }
    }

    public void afterEditProfileButtonPressed(View view){

        uploadImage();

        readData();
    }

    public void readData(){


        final String user_name = usersName.getText().toString();
        final String user_address = usersAddress.getText().toString();

        try {
            for (Chip chip : nachoTextView.getAllChips()) {
                Object data = chip.getData();
                String interest = data.toString();
                ints.add(interest);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        fDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserData userData = dataSnapshot.getValue(UserData.class);
                userData.setName(user_name);
                userData.setGender(gender);
                userData.setAddress(user_address);
                userData.setUserInterests(ints);

                fDatabase.setValue(userData);
                fDatabase.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(imageUrl.equals("Not uploading")) {
            Toast.makeText(this, "Data Updated", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
