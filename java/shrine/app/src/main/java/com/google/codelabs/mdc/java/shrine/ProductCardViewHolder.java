package com.google.codelabs.mdc.java.shrine;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.codelabs.mdc.java.shrine.network.ProductEntry;

public class ProductCardViewHolder extends RecyclerView.ViewHolder {

    public ImageView productImage;
    public TextView productTitle;
    public TextView productPrice;

    public ProductCardViewHolder(@NonNull View itemView) {
        super(itemView);

        productImage = itemView.findViewById(R.id.product_image);
        productTitle = itemView.findViewById(R.id.product_title);
        productPrice = itemView.findViewById(R.id.product_price);

    }
    public void bind(final ProductEntry item, final OnItemClickListener listener){
        itemView.setOnClickListener(v ->
                listener.onItemClick(item));
    }

}
