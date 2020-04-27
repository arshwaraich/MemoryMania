package com.example.memorymania;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

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

    public void flipAnimation(View view) {
        ImageView imageBack = findViewById(R.id.image_back);

        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator flip = ObjectAnimator.ofFloat(view, "rotationY", 0f, 90f);
        flip.setDuration(250);
        ObjectAnimator flip3 = ObjectAnimator.ofFloat(imageBack, "rotationY", 90f, 180f);
        flip3.setDuration(250);

        animatorSet.playSequentially(flip,flip3);
        animatorSet.start();
    }
}
