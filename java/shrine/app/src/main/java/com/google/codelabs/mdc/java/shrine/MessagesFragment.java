package com.google.codelabs.mdc.java.shrine;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.codelabs.mdc.java.shrine.adapters.ChatAdapter;
import com.google.codelabs.mdc.java.shrine.java.Chat;
import com.google.codelabs.mdc.java.shrine.java.ChatList;
import com.google.codelabs.mdc.java.shrine.java.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class  MessagesFragment extends Fragment {

    public RecyclerView recyclerView;
    private FirebaseUser firebaseUser;
    private List<ChatList> chats;
    private List<Chat> conversations;
    private DatabaseReference reference;
    private ChatAdapter chatAdapter;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fir_messages_fragment, container, false);

        recyclerView = view.findViewById(R.id.messages_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        chats = new ArrayList<>();
        conversations = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("chatlist").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chats.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatList chatList = snapshot.getValue(ChatList.class);
                    chats.add(chatList);
                }

                chatList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return view;
    }

    private void chatList() {
        reference = FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                conversations.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    if(user.getId() != null){
                        for(ChatList chatList : chats){
                            if(user.getId().equals(chatList.getId())){
                                Chat chat = new Chat(chatList.getId(),
                                        chatList.getProductName(), chatList.getImageUrl());
                                conversations.add(chat);
                            }
                        }
                    }

                }
                chatAdapter = new ChatAdapter(getContext(), conversations);
                recyclerView.setAdapter(chatAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}