package com.google.codelabs.mdc.java.shrine;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.codelabs.mdc.java.shrine.adapters.ChatAdapter;
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
    private List<User> users;
    private List<ChatList> conversations;
    private DatabaseReference reference;
    private ChatAdapter chatAdapter;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fir_messages_fragment, container, false);
        setUpToolbar(view);

        reference = FirebaseDatabase.getInstance().getReference("chatlist");


        chatList();
        recyclerView = view.findViewById(R.id.messages_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setOnClickListener(v -> {

        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        conversations = new ArrayList<>();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void chatList(){
        conversations = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("chatlist");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                conversations.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatList chat = snapshot.getValue(ChatList.class);
                    conversations.add(chat);
                }
                chatAdapter = new ChatAdapter(getContext(), conversations, item -> {

                });
                recyclerView.setAdapter(chatAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setUpToolbar(View view){
        android.support.v7.widget.Toolbar toolbar = view.findViewById(R.id.fir_toolbar_chat);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if(activity != null) {
            activity.setSupportActionBar(toolbar);
        }
    }
}