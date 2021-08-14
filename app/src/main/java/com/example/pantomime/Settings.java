package com.example.pantomime;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {

    private Switch sound;
    private Switch vibration;

    private String parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.settings);

        Bundle bundle = this.getIntent().getExtras();
        parent = bundle.getString("context");

        sound = (Switch) findViewById(R.id.soundSwitch);
        vibration = (Switch) findViewById(R.id.vibrationSwitch);

        if (Store.getSound()){
            sound.setChecked(true);
        } else {
            sound.setChecked(false);
        }

        sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sound.setChecked(true);
                    Store.setSound(true);
                } else {
                    sound.setChecked(false);
                    Store.setSound(false);
                }
            }
        });

        if (Store.getVibration()){
            vibration.setChecked(true);
        } else {
            vibration.setChecked(false);
        }

        vibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    vibration.setChecked(true);
                    Store.setVibration(true);
                } else {
                    vibration.setChecked(false);
                    Store.setVibration(false);
                }
            }
        });

    }

    public void exit(View v){
        System.out.println("Parent: " + parent);
        if (parent.equals("About")) {
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
            finish();
            overridePendingTransition(0, 0);
        } else if (parent.equals("Directions")) {
            Intent intent = new Intent(this, Directions.class);
            startActivity(intent);
            finish();
            overridePendingTransition(0, 0);
        } else if (parent.equals("MainActivity")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(0, 0);
        } else if (parent.equals("Result")) {
            Intent intent = new Intent(this, Result.class);
            startActivity(intent);
            finish();
            overridePendingTransition(0, 0);
        }
    }
}
