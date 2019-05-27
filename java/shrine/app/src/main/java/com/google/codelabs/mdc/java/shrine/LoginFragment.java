package com.google.codelabs.mdc.java.shrine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Fragment representing the login screen for Shrine.
 */
public class LoginFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fir_login_fragment, container, false);


        final TextInputLayout emailTextInput = view.findViewById(R.id.email_text_input);
        final TextInputEditText emailEditText = view.findViewById(R.id.email_edit_text);
        final TextInputLayout passwordTextInput = view.findViewById(R.id.password_text_input);
        final TextInputEditText passwordEditText = view.findViewById(R.id.password_edit_text);
        final TextView tvNewUser = view.findViewById(R.id.fir_new_user);
        MaterialButton btNext = view.findViewById(R.id.next_button);

        auth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null){
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        }



        //Error para el campo contraseña
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String pass = passwordEditText.getText().toString();

                if(!(isPasswordValid(pass))){
                    passwordTextInput.setError(getString(R.string.fir_error_password));
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    emailTextInput.setError(getString(R.string.fir_error_email));
                    return;
                }

                passwordTextInput.setError(null);
                auth.signInWithEmailAndPassword(email,pass)
                        .addOnCompleteListener((Activity) v.getContext(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Intent intent = new Intent(getContext(), MainActivity.class);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(getContext(),"Usuario incorrecto",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        tvNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavigationHost)v.getContext()).navigateTo(new RegisterUser(), true);
            }
        });



        return view;
    }

    private boolean isPasswordValid(@Nullable String pass){
        return pass != null && pass.length() >= 8;
    }
}
