package com.example.memorymania.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;

import com.example.memorymania.R;

public class ResultActivity extends AppCompatActivity {
    private long scorel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        scorel = intent.getLongExtra(MatchActivity.EXTRA_SCORE, 0);

        Chronometer chronometer = findViewById(R.id.chronometer_score);
        chronometer.setBase(SystemClock.elapsedRealtime() - scorel);
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
        String score = String.valueOf(scorel/1000);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TITLE, getString(R.string.result_send_title));
        sendIntent.putExtra(Intent.EXTRA_TEXT, String.format(getString(R.string.result_send_msg), score));
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, getString(R.string.result_card_title));
        startActivity(shareIntent);
    }
}
