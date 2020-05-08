package com.example.memorymania.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import com.example.memorymania.R;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        long score = intent.getLongExtra(MatchActivity.EXTRA_SCORE, 0);

        Chronometer chronometer = findViewById(R.id.chronometer_score);
        chronometer.setBase(score);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void goHome(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void shareResult(View view) {
        Chronometer chronometer = findViewById(R.id.chronometer_score);
        long scorel = SystemClock.elapsedRealtime() - chronometer.getBase();
        String score = (scorel/1000) + " seconds \uD83C\uDF89\uD83C\uDF89!!";

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TITLE, "I am a memory god");
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "I beat *MemoryMania*! The hottest app in the PauseStore in " + score
                        + "\nCheckout the creator arshwaraich.com");
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, "Let your friends know!");
        startActivity(shareIntent);
    }
}
