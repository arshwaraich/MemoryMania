package com.example.memorymania.util;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorymania.R;
import com.example.memorymania.data.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MatchRecycleAdapter extends RecyclerView.Adapter<MatchRecycleAdapter.MatchViewHolder> {
    private List<Product> dataset;
    private List<Product> currMatch;

    class MatchViewHolder extends RecyclerView.ViewHolder {
        ViewGroup view;
        MatchViewHolder(ViewGroup view) {
            super(view);
            this.view = view;
        }
    }

    public MatchRecycleAdapter(List<Product> dataset) {
        this.dataset = dataset;
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup v = (ViewGroup) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.match_card, parent, false);
        return new MatchViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MatchViewHolder holder, final int position) {
        Product.MatchState matchState = dataset.get(position).getMatchState();
        if(matchState == Product.MatchState.SHOWN) {
            ImageView imageView = (ImageView) holder.view.getChildAt(1);
            imageView.setVisibility(View.VISIBLE);
        } else if (matchState == Product.MatchState.MATCHED) {
            ImageView imageView = (ImageView) holder.view.getChildAt(2);
            imageView.setVisibility(View.VISIBLE);
            Picasso.get().load(dataset.get(position).getImage().getSrc()).into(imageView);
        } else {
            ImageView imageView = (ImageView) holder.view.getChildAt(0);
            imageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
