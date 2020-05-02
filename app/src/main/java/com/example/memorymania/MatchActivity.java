package com.example.memorymania;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.memorymania.model.RetroPhoto;
import com.example.memorymania.network.GetDataService;
import com.example.memorymania.network.RetrofitClientInstance;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchActivity extends AppCompatActivity {

    ProgressDialog progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        progressDoalog = new ProgressDialog(MatchActivity.this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<RetroPhoto>> call = service.getAllPhotos();
        call.enqueue(new Callback<List<RetroPhoto>>() {
            @Override
            public void onResponse(Call<List<RetroPhoto>> call, Response<List<RetroPhoto>> response) {
                progressDoalog.dismiss();
                String imgURL = response.body().get(0).getThumbnailUrl();
                TextView textView = findViewById(R.id.api_res_JSON);
                ImageView imageView = findViewById(R.id.image_back);

                textView.setText(imgURL);
                Picasso.get().load(imgURL).into(imageView);
            }

            @Override
            public void onFailure(Call<List<RetroPhoto>> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(MatchActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
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
        ObjectAnimator flip3 = ObjectAnimator.ofFloat(imageBack, "rotationY", -90f, 0f);
        flip3.setDuration(250);

        animatorSet.playSequentially(flip,flip3);
        animatorSet.start();
    }
}
