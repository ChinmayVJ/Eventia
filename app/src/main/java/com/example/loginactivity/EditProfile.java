package com.example.loginactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.chip.Chip;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;

public class EditProfile extends AppCompatActivity {
    Spinner dropdown;
    NachoTextView nachoTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_profile);
        nachoTextView = findViewById(R.id.nacho_text_view);
        dropdown = findViewById(R.id.spinner);
        String[] items = new String[]{"Male", "Female", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        String[] suggestions = new String[]{"Technology", "Tours", "Travel", "Nature", "Adventures", "Startups","Outdoors"};
        ArrayAdapter<String> adaptersuggest = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, suggestions);
        nachoTextView.setAdapter(adaptersuggest);
        nachoTextView.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
        nachoTextView.enableEditChipOnTouch(false, true);
        nachoTextView.setOnChipClickListener(new NachoTextView.OnChipClickListener() {
            @Override
            public void onChipClick(Chip chip, MotionEvent motionEvent) {
                // Do something
            }
        });
    }
}
