package com.example.memorymania.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableInt;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.memorymania.R;
import com.example.memorymania.data.Product;
import com.example.memorymania.databinding.ActivityMatchBinding;
import com.example.memorymania.util.MatchRecycleAdapter;
import com.example.memorymania.data.Products;
import com.example.memorymania.util.GetDataService;
import com.example.memorymania.util.RetrofitClientInstance;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Observable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchActivity extends AppCompatActivity {

    ActivityMatchBinding activityMatchBinding;
    ProgressDialog progressDialog;
    final ObservableInt numMatched = new ObservableInt();
    final ObservableInt numMax = new ObservableInt();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMatchBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_match
        );

        activityMatchBinding.setNumMatched(numMatched);
        activityMatchBinding.setNumMax(numMax);

        progressDialog = new ProgressDialog(MatchActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<Products> call = service.getAllProducts();
        call.enqueue(new Callback<Products>() {
            @Override
            public void onResponse(Call<Products> call, Response<Products> response) {
                progressDialog.dismiss();

                List<Product> products = response.body().getProducts();

                // Set matchGrid
                RecyclerView recyclerView = findViewById(R.id.recycler_view);
                recyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MatchActivity.this, 4);
                recyclerView.setLayoutManager(layoutManager);

                RecyclerView.Adapter mAdapter = new MatchRecycleAdapter(products);
                recyclerView.setAdapter(mAdapter);

                // Set matchedGrid
                String imgURL = response.body().getProducts().get(0).getImage().getSrc();
                ImageView imageView1 = findViewById(R.id.matched_image);

                Picasso.get().load(imgURL).into(imageView1);

                // TODO: Set Title

                // Set progress bar
                numMax.set(products.size());
            }

            @Override
            public void onFailure(Call<Products> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MatchActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateMatch(View view) {
        numMatched.set(numMatched.get() + 1);
    }

    public void showResult(View view) {
        Intent intent = new Intent(this, ResultActivity.class);
        startActivity(intent);
    }

    public void flipAnimationLarge(View viewGroup) {
        ImageView imageFront = (ImageView) ((ViewGroup)viewGroup).getChildAt(0);
        ImageView imageBack = (ImageView) ((ViewGroup)viewGroup).getChildAt(1);

        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator flip = ObjectAnimator.ofFloat(imageFront, "rotationY", 0f, 90f);
        flip.setDuration(250);
        ObjectAnimator flip3 = ObjectAnimator.ofFloat(imageBack, "rotationY", -90f, 0f);
        flip3.setDuration(250);

        animatorSet.playSequentially(flip,flip3);
        animatorSet.start();
    }
}
