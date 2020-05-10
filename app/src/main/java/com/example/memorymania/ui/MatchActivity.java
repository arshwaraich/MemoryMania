package com.example.memorymania.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableInt;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.memorymania.R;
import com.example.memorymania.data.*;
import com.example.memorymania.util.*;
import com.example.memorymania.databinding.ActivityMatchBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchActivity extends AppCompatActivity {
    public static final String EXTRA_SCORE = "com.example.memorymania.SCORE";

    ActivityMatchBinding activityMatchBinding;
    ProgressDialog progressDialog;
    final ObservableInt numMatched = new ObservableInt();
    final ObservableInt numMax = new ObservableInt();

    @Override
    public void onBackPressed() {
        Button button = findViewById(R.id.start_match_button);
        if(button.getVisibility() == View.GONE) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.match_alert_title)
                    .setMessage(R.string.match_alert_message)
                    .setPositiveButton(R.string.match_alert_p, (dialog, which) -> finish())
                    .setNegativeButton(R.string.match_alert_n, null)
                    .show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMatchBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_match
        );

        // Binding variables
        activityMatchBinding.setNumMatched(numMatched);
        activityMatchBinding.setNumMax(numMax);


        // Set progress dialog
        // TODO: Use non-ui blocking progress bar
        progressDialog = new ProgressDialog(MatchActivity.this);
        progressDialog.setMessage(getString(R.string.match_prgdlg_msg));
        progressDialog.show();

        // Set title text
        String matchSize = PreferenceManager
                        .getDefaultSharedPreferences(this)
                        .getString("match_size", "2");
        TextView textView = findViewById(R.id.title_text);
        textView.setText(String.format(getString(R.string.title_activity_match), matchSize));

        // Set recycler view
        int gridSize = Integer.parseInt(
                PreferenceManager
                        .getDefaultSharedPreferences(this)
                        .getString("grid_size", "4"));

        final RecyclerView recyclerView = findViewById(R.id.match_grid);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MatchActivity.this, gridSize);
        recyclerView.setLayoutManager(layoutManager);

        // Handle for the RetrofitInstance interface
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<Products> call = service.getAllProducts();
        call.enqueue(new Callback<Products>() {
            @Override
            public void onResponse(Call<Products> call, Response<Products> response) {
                progressDialog.dismiss();

                final List<Product> products = response.body().getProducts(MatchActivity.this);

                // Set matchGrid
                final RecyclerView.Adapter mAdapter = new MatchRecycleAdapter(products, MatchActivity.this);
                recyclerView.setAdapter(mAdapter);

                // Set max products
                numMax.set(products.size());
            }

            @Override
            public void onFailure(Call<Products> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MatchActivity.this, R.string.match_prgdlg_err, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void shuffleMatchGird(View view) {
        RecyclerView recyclerView = findViewById(R.id.match_grid);

        MatchRecycleAdapter matchRecycleAdapter = (MatchRecycleAdapter) recyclerView.getAdapter();
        if (matchRecycleAdapter != null) {
            matchRecycleAdapter.shuffleProducts();
        }
    }

    public void showResult() {
        Chronometer chronometer = findViewById(R.id.chronometer);
        chronometer.stop();
        long score = SystemClock.elapsedRealtime() - chronometer.getBase();

        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra(EXTRA_SCORE, score);
        startActivity(intent);
    }

    public void incrementMatches(int increment) {
        numMatched.set(numMatched.get() + increment);

        if(numMatched.get() >= numMax.get()) {
            this.showResult();
        }
    }

    public void startMatch(View view) {
        // Hide button
        view.setVisibility(View.GONE);

        RecyclerView recyclerView = findViewById(R.id.match_grid);
        ((MatchRecycleAdapter) recyclerView.getAdapter()).initDataset();

        enableGridClick(true);

        View floatingBar = findViewById(R.id.floating_bar);
        floatingBar.setVisibility(View.VISIBLE);

        // Set chronometer
        final Chronometer chronometer = findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    public void enableGridClick(Boolean status) {
        View overlayView = findViewById(R.id.overlay_view);
        overlayView.setVisibility(status ? View.GONE : View.VISIBLE);
    }
}
