package com.example.rateme;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.ViewHolder> {
    private List<String> favouritesList;

    public FavouritesAdapter(List<String> favouritesList) {
        this.favouritesList = favouritesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String product = favouritesList.get(position);
        holder.productTextView.setText(product);
    }

    @Override
    public int getItemCount() {
        return favouritesList.size();
    }
    public void updateFavouritesList(List<String> newFavourites) {
        this.favouritesList = newFavourites;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView productTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            productTextView = itemView.findViewById(R.id.text_product);
        }
    }
}