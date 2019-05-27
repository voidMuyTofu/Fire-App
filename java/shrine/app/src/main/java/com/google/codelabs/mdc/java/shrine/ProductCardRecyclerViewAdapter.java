package com.google.codelabs.mdc.java.shrine;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.codelabs.mdc.java.shrine.java.Product;
import com.google.codelabs.mdc.java.shrine.network.ImageRequester;
import com.google.codelabs.mdc.java.shrine.network.ProductEntry;

import java.util.List;

public class ProductCardRecyclerViewAdapter extends RecyclerView.Adapter<ProductCardViewHolder> {

    private List<ProductEntry> productList;
    private final OnItemClickListener listener;
    private ImageRequester imageRequester;
    private Context context;

    ProductCardRecyclerViewAdapter(List<ProductEntry> productList,Context context, OnItemClickListener listener) {
        this.productList = productList;
        imageRequester = ImageRequester.getInstance();
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fir_product_card, parent, false);
        return new ProductCardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCardViewHolder holder, int position) {
        if(productList != null && position < productList.size()){
            ProductEntry product = productList.get(position);
            holder.productTitle.setText(product.getTitle());
            holder.productPrice.setText(product.getPrice());
            Glide.with(context).load(product.getUrl()).into(holder.productImage);
            holder.bind(productList.get(position),listener);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
