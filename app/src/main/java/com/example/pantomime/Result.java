package com.example.pantomime;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class Result extends AppCompatActivity {

    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.result);

        HashMap<String, Integer> scores = Store.getScores();

        int maxPoints = -1;
        String bestTeam = "";
        for (Map.Entry<String, Integer> team : scores.entrySet()){
            if (team.getValue() > maxPoints) {
                bestTeam = team.getKey();
                maxPoints = team.getValue();
            }
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Team " + bestTeam + " with score: " + maxPoints + "\n");
        boolean moreThanOne = false;
        for (Map.Entry<String, Integer> team : scores.entrySet()){
            if (team.getValue() == maxPoints && !team.getKey().equals(bestTeam)) {
                moreThanOne = true;
                stringBuilder.append("Team " + team.getKey() + " with score: " + team.getValue() + "\n");
            }
        }

        if (moreThanOne) {
            boolean flag = true;
            result = (TextView) findViewById(R.id.result);
            result.append(stringBuilder.toString());
            for (Map.Entry<String, Integer> team : scores.entrySet()) {
                if (!stringBuilder.toString().contains(team.getKey())) {
                    if (flag) {
                        result.append("\n\nOther Teams\n");
                        flag = false;
                    }
                    result.append("Team " + team.getKey() + " with score: " + team.getValue() + "\n");
                }
            }
        } else {
            result = (TextView) findViewById(R.id.result);
            result.append("Team " + bestTeam + " with score: " + maxPoints + "\n\nOther Teams\n");
            for (Map.Entry<String, Integer> team : scores.entrySet()) {
                if (!team.getKey().equals(bestTeam))
                    result.append("Team " + team.getKey() + " with score: " + team.getValue() + "\n");
            }
        }
    }

    public void exit(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
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