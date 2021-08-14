package com.example.pantomime;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

public class Play extends AppCompatActivity {

    private TextView titlePantomime;
    private TextView whoIsPlaying;

    private char[] categories;

    private String timer;
    private String teams;
    private String team;
    private String rounds;

    private static int lines;

    private Button found;
    private Button notFound;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private boolean timerRunning;
    private TextView timerText;
    private Button timerButton;

    private static boolean translatorDownloaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.play);

        Store.decreaseRound();

        Bundle bundle = this.getIntent().getExtras();
        categories = bundle.getCharArray("categories");
        timer = bundle.getString("timer");
        teams = bundle.getString("teams");
        rounds = bundle.getString("rounds");
        team = bundle.getString("team");

        found = (Button) findViewById(R.id.Found);
        notFound = (Button) findViewById(R.id.notFound);

        timeLeftInMillis = Integer.parseInt(timer) * 1000;
        timerText = (TextView) findViewById(R.id.timer);
        timerButton = (Button) findViewById(R.id.timerButton);

        timerButton.setOnClickListener(v -> startStop());

        String[] movies = null;
        String[] songs = null;
        String[] series = null;
        String[] proverbs = null;

        if (categories[0] == '1')
            movies = read("movies.txt");
        if (categories[1] == '1')
            songs = read("songs.txt");
        if (categories[2] == '1')
            series = read("series.txt");
        if (categories[3] == '1')
            proverbs = read("proverbs.txt");

        whoIsPlaying = (TextView) findViewById(R.id.team);
        whoIsPlaying.setText("Team " + team + "\n");

        boolean flag = false;
        while(!flag) {
            Random randomCategory = new Random();
            int randCategory = randomCategory.nextInt(4);
            System.out.println("Random number: " + randCategory);

            if (categories[randCategory] == '1') {
                Random random = new Random();
                int rand = random.nextInt(lines);
                System.out.println("Random number: " + rand);

                String title = "";
                if (randCategory == 0)
                    title = movies[rand];
                else if (randCategory == 1)
                    title = songs[rand];
                else if (randCategory == 2)
                    title = series[rand];
                else
                    title = proverbs[rand];

                System.out.println("Title: " + title);

                titlePantomime = (TextView) findViewById(R.id.titlePantomime);

                //TRANSLATION-START
                TranslatorOptions translateOptions = new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.GREEK)
                        .build();

                final Translator englishToGreek = Translation.getClient(translateOptions);

                if (!translatorDownloaded) {
                    DownloadConditions conditions = new DownloadConditions.Builder()
                            .requireWifi()
                            .build();
                    englishToGreek.downloadModelIfNeeded(conditions)
                            .addOnSuccessListener(
                                    (OnSuccessListener) o -> translatorDownloaded = true)
                            .addOnFailureListener(
                                    e -> translatorDownloaded = false);
                }

                String failTitle = title;

                englishToGreek.translate(title)
                        .addOnSuccessListener(
                                (OnSuccessListener) o -> titlePantomime.setText(o.toString()))
                        .addOnFailureListener(
                                e -> titlePantomime.setText(failTitle));
                //TRANSLATION-END

                flag = true;
            } else {
                continue;
            }
        }

        found.setOnClickListener(v -> {

            if (Store.getSound()) {
                MediaPlayer player = MediaPlayer.create(this, R.raw.correct_buzzer);
                player.start();


                try {
                    Thread.sleep(1300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (timerRunning)
                startStop();

            if (Store.anotherRound()) {
                Store.State(team, true);

                String nextTeam = "";
                int teamToPlay = Integer.parseInt(team) + 1;
                if (teamToPlay > Integer.parseInt(teams))
                    nextTeam = "1";
                else
                    nextTeam = String.valueOf(teamToPlay);

                Intent intent = new Intent(this, BeforeEachRound.class);

                Bundle bundle2 = new Bundle();
                bundle2.putCharArray("categories", categories);
                bundle2.putString("timer", timer);
                bundle2.putString("teams", teams);
                bundle2.putString("team", nextTeam);
                intent.putExtras(bundle2);

                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(this, Result.class);
                startActivity(intent);
                finish();
            }
        });

        notFound.setOnClickListener(v -> {

            if (Store.getSound()) {
                MediaPlayer player = MediaPlayer.create(this, R.raw.wrong_buzzer);
                player.start();

                try {
                    Thread.sleep(1100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (timerRunning)
                startStop();

            if (Store.anotherRound()) {
                Store.State(team, false);

                String nextTeam = "";
                int teamToPlay = Integer.parseInt(team) + 1;
                if (teamToPlay > Integer.parseInt(teams))
                    nextTeam = "1";
                else
                    nextTeam = String.valueOf(teamToPlay);

                Intent intent = new Intent(this, BeforeEachRound.class);

                Bundle bundle2 = new Bundle();
                bundle2.putCharArray("categories", categories);
                bundle2.putString("timer", timer);
                bundle2.putString("teams", teams);
                bundle2.putString("team", nextTeam);
                intent.putExtras(bundle2);

                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(this, Result.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public String[] read(String category) {

        String[] array;
        try {
            lines = 0;
            BufferedReader s = new BufferedReader(new InputStreamReader(getAssets().open(category), "UTF-8"));
            while (s.readLine() != null) {
                lines++;
                s.readLine();
            }
            s.close();

            BufferedReader s1 = new BufferedReader(new InputStreamReader(getAssets().open(category), "UTF-8"));
            array = new String[lines];
            for (int i = 0; i < array.length; i++) {
                array[i] = s1.readLine();
            }
            s1.close();

        } catch (IOException e) {
            System.err.println("File Not Found");
            array = new String[0];
        }
        return array;
    }

    public void startStop() {
        if (timerRunning)
            stop();
        else
            start();
    }

    public void start() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {

            }
        }.start();

        timerButton.setText("PAUSE");
        timerRunning = true;
    }

    public void stop(){
        countDownTimer.cancel();
        timerButton.setText("START");
        timerRunning = false;
    }

    public void updateTimer(){
        int seconds = (int) timeLeftInMillis % (Integer.parseInt(timer) * 1000) / 1000;
        int minutes = seconds / 60;
        seconds = seconds % 60;

        String timeLeft;
        timeLeft = "0" + minutes;
        timeLeft += ":";
        if (seconds < 10)
            timeLeft += "0";
        timeLeft += seconds;

        timerText.setText(timeLeft);
        if (timeLeft.equals("00:00")){
            if (Store.getVibration()) {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(500);
                }
            }
        }
    }

    public void exit(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }

}