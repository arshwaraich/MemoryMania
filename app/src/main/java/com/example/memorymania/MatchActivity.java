package com.example.memorymania;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MatchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
    }

    public void showResult(View view) {
        Intent intent = new Intent(this, ResultActivity.class);
        startActivity(intent);
    }
}
