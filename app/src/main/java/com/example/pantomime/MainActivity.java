package com.example.pantomime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button play;
    private Button directions;
    private Button about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        play = (Button) findViewById(R.id.play);
        directions = (Button) findViewById(R.id.directions);
        about = (Button) findViewById(R.id.about);

        Store.setScores();
    }

    public void play(View v){
        Intent intent = new Intent(this, PrepareToPlay.class);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }

    public void directions(View v){
        Intent intent = new Intent(this, Directions.class);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }

    public void about(View v){
        Intent intent = new Intent(this, About.class);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }

    public void exit(View v){
        finish();
    }

    public void settings(View v){
        Intent intent = new Intent(this, Settings.class);

        Bundle bundle = new Bundle();
        bundle.putString("context", this.getClass().getSimpleName());
        intent.putExtras(bundle);

        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }

}