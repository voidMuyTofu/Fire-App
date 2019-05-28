package com.google.codelabs.mdc.java.shrine.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.codelabs.mdc.java.shrine.ChatFragment;
import com.google.codelabs.mdc.java.shrine.NavigationHost;
import com.google.codelabs.mdc.java.shrine.OnItemClickListener;
import com.google.codelabs.mdc.java.shrine.R;
import com.google.codelabs.mdc.java.shrine.java.ChatList;
import com.google.codelabs.mdc.java.shrine.java.OnRecyclerClickListener;
import com.google.codelabs.mdc.java.shrine.network.ProductEntry;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private Context context;
    private List<ChatList> chats;

    public ChatAdapter(Context context, List<ChatList> chats){
        this.context = context;
        this.chats = chats;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.fir_chat_item, viewGroup, false);
            return new ChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder viewHolder, int i) {
        ChatList chat = chats.get(i);

        viewHolder.tvChatName.setText(chat.getProductName());
        Glide.with(context).load(chat.getImageUrl()).into(viewHolder.productImage);
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvChatName;
        public CircleImageView productImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvChatName = itemView.findViewById(R.id.chat__name);
            productImage = itemView.findViewById(R.id.product_image);
        }
        public void bind(final ChatList item, final OnRecyclerClickListener listener){
            itemView.setOnClickListener(v ->
                    listener.onItemClick(item));
        }
    }
}
