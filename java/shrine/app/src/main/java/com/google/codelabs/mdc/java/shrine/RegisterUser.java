package com.google.codelabs.mdc.java.shrine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class  RegisterUser extends Fragment {

    TextInputLayout tiUsername;
    TextInputLayout tiEmail;
    TextInputLayout tiPass;
    TextInputEditText etUsername;
    TextInputEditText etEmail;
    TextInputEditText etPass;
    MaterialButton btNext;
    MaterialButton btCancel;
    private FirebaseAuth auth;
    private DatabaseReference reference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fir_register_user, container, false);

        auth = FirebaseAuth.getInstance();

        //inicializar componentes
        initComponents(view);


        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPass.getText().toString();
                String email = etEmail.getText().toString();

                if(!isUserValid(username)){
                    tiUsername.setError(getString(R.string.fir_error_username));
                    return;
                }
                if(!isPasswordValid(password)){
                    tiPass.setError(getString(R.string.fir_error_password));
                    return;
                }
                if(!isValidEmailAddress(email)){
                    tiEmail.setError(getString(R.string.fir_error_email));
                    return;
                }
                
                tiUsername.setError(null);
                tiPass.setError(null);
                tiEmail.setError(null);

                registerUser(username, email, password);
            }
        });

        return view;
    }

    private void registerUser(final String username, String email, String pass) {
        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("iduser", userid);
                            hashMap.put("username", username);
                            hashMap.put("imageurl", "default");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Intent intent = new Intent(getContext(), MainActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    }
                });
    }
    
    private boolean isPasswordValid(@Nullable String pass){
        return pass != null && pass.length() >= 8;
    }
    
    private boolean isUserValid(@Nullable String user){
        return user != null && user.length() >= 6;
    }
    
    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    private void initComponents(View view){
        tiUsername = view.findViewById(R.id.username_text_input);
        tiPass = view.findViewById(R.id.password_text_input);
        tiEmail = view.findViewById(R.id.email_text_input);
        etUsername = view.findViewById(R.id.username_edit_text);
        etPass = view.findViewById(R.id.password_edit_text);
        etEmail = view.findViewById(R.id.email_edit_text);
        btNext = view.findViewById(R.id.next_button);
        btCancel = view.findViewById(R.id.cancel_button);
    }
}
