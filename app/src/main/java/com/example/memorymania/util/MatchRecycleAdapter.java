package com.example.memorymania.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
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
import java.util.List;
import java.util.stream.Collectors;

public class MatchRecycleAdapter extends RecyclerView.Adapter<MatchRecycleAdapter.MatchViewHolder> {

    private List<Product> dataset;
    private Context context;
    private Integer NUM_MATCHES = 2;

    class MatchViewHolder extends RecyclerView.ViewHolder {
        final MatchCardBinding item;

        public void bind(final int position) {
            item.setProduct(dataset.get(position));
            item.getRoot().setOnClickListener(
                    v -> showLogic(position)
            );
            item.executePendingBindings();
        }

        private void showLogic(Integer index) {
            if(dataset.get(index).getMatchState() == Product.MatchState.HIDDEN) {
                dataset.get(index).setMatchState(Product.MatchState.SHOWN);
                notifyItemChanged(index);

                if(!isMatch(index)) {
                    ((MatchActivity)context).enableGridClick(false);
                    VibrateFunc();

                    // Delayed flip
                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        hideLogic();
                        ((MatchActivity)context).enableGridClick(true);
                    }, 500);
                } else if(
                    (dataset.stream()
                                    .filter(item -> (item.getMatchState() == Product.MatchState.SHOWN))
                            .collect(Collectors.toList()).size()) >= NUM_MATCHES) {
                    ((MatchActivity)context).enableGridClick(false);

                    // Delayed flip
                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        matchLogic();
                        ((MatchActivity)context).enableGridClick(true);
                    }, 500);
                }
            } else if(dataset.get(index).getMatchState() == Product.MatchState.SHOWN) {
                dataset.get(index).setMatchState(Product.MatchState.HIDDEN);
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
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void shuffleProducts() {
        Collections.shuffle(dataset);
        notifyDataSetChanged();
    }

    public void initDataset() {
        for(Product p: dataset) {
            p.setMatchState(Product.MatchState.HIDDEN);
        }
        notifyDataSetChanged();
    }

    // Util
    private void VibrateFunc() {
        Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vib.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            vib.vibrate(200);
        }
    }

    // Animations
    @BindingAdapter("matchState")
    public static void spinAnimation(ViewGroup viewGroup, Product oldProduct, Product newProduct) {
        ImageView imageHidden = viewGroup.findViewById(R.id.image_hidden);
        ImageView imageMatched = viewGroup.findViewById(R.id.image_matched);
        ImageView imageShown = viewGroup.findViewById(R.id.image_shown);

        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator flipFront, flipBack;

        if(newProduct.getMatchState() == Product.MatchState.HIDDEN) {
            flipFront = ObjectAnimator.ofFloat(imageShown, "rotationY", 0f, -90f);
            flipBack = ObjectAnimator.ofFloat(imageHidden, "rotationY", 90f, 0f);
        } else if(newProduct.getMatchState() == Product.MatchState.SHOWN) {
            Picasso.get()
                    .load(newProduct.getImage().getSrc())
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .into(imageShown);

            flipFront = ObjectAnimator.ofFloat(imageHidden, "rotationY", 0f, 90f);
            flipBack = ObjectAnimator.ofFloat(imageShown, "rotationY", -90f, 0f);
        } else {
            flipFront = ObjectAnimator.ofFloat(imageShown, "rotationY", 0f, -90f);
            flipBack = ObjectAnimator.ofFloat(imageMatched, "rotationY", -90f, 0f);
        }

        flipFront.setDuration(200);
        flipBack.setDuration(400);

        animatorSet.playSequentially(flipFront, flipBack);
        animatorSet.start();
    }
}
