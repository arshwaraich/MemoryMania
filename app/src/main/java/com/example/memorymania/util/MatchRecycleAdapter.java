package com.example.memorymania.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorymania.R;
import com.example.memorymania.data.Product;
import com.example.memorymania.databinding.MatchCardBinding;
import com.example.memorymania.ui.MatchActivity;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class MatchRecycleAdapter extends RecyclerView.Adapter<MatchRecycleAdapter.MatchViewHolder> {

    private List<Product> dataset;
    private Context context;
    private Integer NUM_MATCHES = 2;

    class MatchViewHolder extends RecyclerView.ViewHolder {
        final MatchCardBinding item;

        public void bind(final Product product, final int position) {
            item.setMatchState(product.getMatchState());
            item.getRoot().setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showLogic(position, product);
                        }
                    }
            );
            item.executePendingBindings();
        }

        private void showLogic(Integer index, Product product) {
            if(item.getMatchState() == Product.MatchState.HIDDEN) {
                product.setMatchState(Product.MatchState.SHOWN);
                notifyItemChanged(index);

                if(!isMatch(index)) {
                    hideLogic();
                } else if(
                        (dataset.stream()
                                .filter(item -> (item.getMatchState() == Product.MatchState.SHOWN))
                        .collect(Collectors.toList()).size()) >= NUM_MATCHES) {
                    matchLogic();
                }
            } else if(item.getMatchState() == Product.MatchState.SHOWN) {
                product.setMatchState(Product.MatchState.HIDDEN);
                notifyItemChanged(index);
                hideLogic();
            }
        }

        // TODO: Improve looping
        private Boolean isMatch(Integer index) {
            boolean ret = true;
            long currId = dataset.get(index).getId();
            final List<Product> tmpDataset = dataset.stream()
                    .filter(item -> (item.getMatchState() == Product.MatchState.SHOWN))
                    .collect(Collectors.toList());
            for(Product p: tmpDataset) {
                ret = ret && (p.getId() == currId);
            }
            return ret;
        }

        // TODO: Improve looping
        private void hideLogic() {
            for(int i = 0; i < dataset.size(); i++) {
                if(dataset.get(i).getMatchState() == Product.MatchState.SHOWN) {
                    dataset.get(i).setMatchState(Product.MatchState.HIDDEN);
                    notifyItemChanged(i);
                }
            }
        }

        // TODO: Improve looping
        private void matchLogic() {
            for(int i = 0; i < dataset.size(); i++) {
                if(dataset.get(i).getMatchState() == Product.MatchState.SHOWN) {
                    dataset.get(i).setMatchState(Product.MatchState.MATCHED);
                    notifyItemChanged(i);
                }
            }
            ((MatchActivity) context).incrementMatches(NUM_MATCHES);
        }

        MatchViewHolder(MatchCardBinding itemView) {
            super(itemView.getRoot());
            item = itemView;
        }
    }

    public MatchRecycleAdapter(List<Product> dataset, Context context) {
        this.dataset = dataset;
        this.context = context;
        this.NUM_MATCHES = context.getResources().getInteger(R.integer.match_size);
        this.shuffleProducts();
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MatchCardBinding matchCardBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.match_card, parent, false);
        return new MatchViewHolder(matchCardBinding);
    }

    @Override
    public void onBindViewHolder(MatchViewHolder holder, final int position) {
        ViewGroup viewGroup = (ViewGroup) holder.item.getRoot();

        ImageView imageView = (ImageView) viewGroup.getChildAt(1);
        Picasso.get()
                .load(dataset.get(position).getImage().getSrc())
                .error(R.drawable.ic_broken_image_black_24dp)
                .into(imageView);

        holder.bind(dataset.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void shuffleProducts() {
        Collections.shuffle(dataset);
        notifyDataSetChanged();
    }

    // Animations
    @BindingAdapter("matchState")
    public static void spinAnimation(ViewGroup viewGroup, Product.MatchState matchState) {
        ImageView imageHidden = viewGroup.findViewById(R.id.image_hidden);
        ImageView imageMatched = viewGroup.findViewById(R.id.image_matched);
        ImageView imageShown = viewGroup.findViewById(R.id.image_shown);

        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator flipFront, flipBack;

        if(matchState == Product.MatchState.HIDDEN) {
            flipFront = ObjectAnimator.ofFloat(imageShown, "rotationY", 0f, -90f);
            flipBack = ObjectAnimator.ofFloat(imageHidden, "rotationY", 90f, 0f);
        } else if(matchState == Product.MatchState.SHOWN) {
            flipFront = ObjectAnimator.ofFloat(imageHidden, "rotationY", 0f, 90f);
            flipBack = ObjectAnimator.ofFloat(imageShown, "rotationY", -90f, 0f);
        } else {
            flipFront = ObjectAnimator.ofFloat(imageShown, "rotationY", 0f, -90f);
            flipBack = ObjectAnimator.ofFloat(imageMatched, "rotationY", -90f, 0f);
        }

        flipFront.setDuration(200);
        flipBack.setDuration(200);

        animatorSet.playSequentially(flipFront, flipBack);
        animatorSet.start();
    }
}
