package com.example.memorymania.util;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorymania.R;
import com.example.memorymania.data.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MatchRecycleAdapter extends RecyclerView.Adapter<MatchRecycleAdapter.MatchViewHolder> {
    private List<Product> dataset;

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
        // create a new view
        ViewGroup v = (ViewGroup) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.match_card, parent, false);
        MatchViewHolder vh = new MatchViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MatchViewHolder holder, int position) {
        ImageView imageView = (ImageView) holder.view.getChildAt(1);
        Picasso.get().load(dataset.get(position).getImage().getSrc()).into(imageView);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
