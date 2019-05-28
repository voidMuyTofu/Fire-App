package com.google.codelabs.mdc.java.shrine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.codelabs.mdc.java.shrine.adapters.MessageAdapter;
import com.google.codelabs.mdc.java.shrine.java.Message;
import com.google.codelabs.mdc.java.shrine.network.ProductEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ChatFragment extends Fragment {

    private TextInputEditText etMessage;
    private TextInputLayout tiMessage;
    private MaterialButton btSend;
    private TextView tvProductName;
    DatabaseReference reference;
    DatabaseReference chatref;
    private FirebaseUser firebaseUser;

    private String userId;
    private String productName;
    private String imageUrl;

    MessageAdapter messageAdapter;
    List<Message> messages;

    RecyclerView recyclerView;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fir_chat_fragment, container, false);

        setUpToolbar(view);
        initComponents(view);

        Bundle bundle = getArguments();
        if(bundle != null) {
            userId = (String) bundle.get("userId");
            productName = (String) bundle.get("title");
            imageUrl = (String) bundle.get("url");
        }
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference("chat")
                .child(firebaseUser.getUid());

        recyclerView = view.findViewById(R.id.fir_list_messages);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        tvProductName.setText(productName);

        btSend.setOnClickListener(v -> {
            String message = etMessage.getText().toString();
            if(!message.equals("")){
                sendMessage(firebaseUser.getUid(), userId, message, productName);
                System.out.println(userId);
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                readMessage(firebaseUser.getUid(), userId, productName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void sendMessage(String sender, final String reciever, String message, String productName) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> map = new HashMap<>();

        map.put("sender", sender);
        map.put("reciever", reciever);
        map.put("message", message);
        map.put("productname", productName);
        map.put("isseen", false);

        reference.child("chat").push().setValue(map);

        //abrir una conversacion para el nuevo chat

        final DatabaseReference chatref = FirebaseDatabase.getInstance().getReference("chatlist")
                .child(firebaseUser.getUid());
        chatref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    chatref.child("fbuser").setValue(firebaseUser.getUid());
                    chatref.child("userid").setValue(userId);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        HashMap<String, Object> hashmap = new HashMap<>();
        hashmap.put("userId", userId);
        hashmap.put("productName", productName);
        hashmap.put("imageUrl", imageUrl);

        reference.child("chatlist").push().setValue(hashmap);

        etMessage.setText("");
    }

    private void readMessage(final String myid, final String userid, final String productName){
        messages = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("chat");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messages.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Message message = snapshot.getValue(Message.class);
                    if((message.getSender().equals(myid) && message.getReciever().equals(userid)
                        && message.getProductName().equals(productName))
                            ){
                        messages.add(message);
                    }

                    messageAdapter = new MessageAdapter(getContext(), messages);
                    recyclerView.setAdapter(messageAdapter);
                }
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fir_toolbar_chat,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.sign_out:
                return true;
            case R.id.search:
                break;
        }
        return false;
    }

    private void initComponents(View view){
        etMessage = view.findViewById(R.id.fir_message_edit_text);
        tiMessage = view.findViewById(R.id.fir_message_text_input);
        btSend = view.findViewById(R.id.fir_bt_send);
        tvProductName = view.findViewById(R.id.fir_product_name);
    }

}