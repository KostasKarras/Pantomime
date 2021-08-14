package com.example.pantomime;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BeforeEachRound extends AppCompatActivity {

    private TextView beforeEachRound;

    private Button go;

    private char[] categories;

    private String timer;
    private String teams;
    private String team;
    private String rounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.before_each_round);

        Bundle bundle = this.getIntent().getExtras();
        categories = bundle.getCharArray("categories");
        timer = bundle.getString("timer");
        teams = bundle.getString("teams");
        rounds = bundle.getString("rounds");
        team = bundle.getString("team");

        beforeEachRound = (TextView) findViewById(R.id.beforeEachRound);
        beforeEachRound.setText("Team " + team + " is your turn\n");

        go = (Button) findViewById(R.id.go);
        go.setOnClickListener(v -> {
            Intent intent = new Intent(this, Play.class);

            Bundle bundle2 = new Bundle();
            bundle2.putCharArray("categories", categories);
            bundle2.putString("timer", timer);
            bundle2.putString("teams", teams);
            bundle2.putString("rounds", rounds);
            bundle2.putString("team", team);
            intent.putExtras(bundle2);

            startActivity(intent);
            finish();
        });
    }

    public void exit(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }
}