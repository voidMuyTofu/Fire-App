package com.google.codelabs.mdc.java.shrine;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.google.codelabs.mdc.java.shrine.network.ImageRequester;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private TextView tvTitle;
    private TextView tvDescription;
    private TextView tvSize;
    private TextView tvPrice;
    private MaterialButton btMessage;
    private ImageView ivImage;
    private CircleImageView profileImage;
    private String [] images;
    public String iduser;
    private ImageRequester imageRequester;
    private FirebaseUser firebaseUser;
    Uri imageUri;

    private final int RESULTADO_CARGA_FINAL = 1;


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fir_product_page_fragment, container, false);

        initComponent(view);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        displayInfo();

        btMessage.setOnClickListener(v -> {
            Bundle bundle1 = new Bundle();
            bundle1.putString("title", getArguments().get("title").toString());
            bundle1.putString("url", getArguments().get("url").toString());
            bundle1.putString("userId", getArguments().get("userId").toString());
            ((NavigationHost)getContext()).navigateTo(new ChatFragment(), true, bundle1);
        });

        profileImage.setOnClickListener(v -> setImage());
        return view;
    }

    private void displayInfo(){
        Bundle bundle;
        bundle = this.getArguments();
        if(bundle != null){
            tvTitle.setText((CharSequence) bundle.get("title"));
            //tvSize.setText((CharSequence) bundle.get("size"));
            tvDescription.setText((CharSequence) bundle.get("description"));
            tvPrice.setText((CharSequence) bundle.get("price"));
            Glide.with(getContext()).load(bundle.get("url")).into(ivImage);
            if(bundle.get("userId").equals(firebaseUser.getUid()))
                btMessage.setVisibility(View.GONE);
            images = (String[]) bundle.get("imagesurl");
        }
    }

    private void setImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULTADO_CARGA_FINAL);
    }

    private void initComponent(View view){
        tvTitle = view.findViewById(R.id.fir_product_title);
        tvDescription = view.findViewById(R.id.fir_product_descripction);
        tvPrice = view.findViewById(R.id.fir_product_price);
        tvSize = view.findViewById(R.id.fir_product_size);
        ivImage = view.findViewById(R.id.fir_product_image);
        btMessage = view.findViewById(R.id.fir_bt_message);
        profileImage = view.findViewById(R.id.profile_image);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULTADO_CARGA_FINAL && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            profileImage.setImageURI(imageUri);
        }
    }
}
