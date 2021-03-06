package com.google.codelabs.mdc.java.shrine;

import android.os.Bundle;
import android.support.annotation.NonNull;
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

public class  ProductPageFragment extends Fragment {

    private TextView tvTitle;
    private TextView tvDescription;
    private TextView tvSize;
    private TextView tvPrice;
    private MaterialButton btMessage;
    private ImageView ivImage;
    private String [] images;
    public String iduser;
    private ImageRequester imageRequester;
    
    private FirebaseUser firebaseUser;


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
    
    private void initComponent(View view){
        tvTitle = view.findViewById(R.id.fir_product_title);
        tvDescription = view.findViewById(R.id.fir_product_descripction);
        tvPrice = view.findViewById(R.id.fir_product_price);
        tvSize = view.findViewById(R.id.fir_product_size);
        ivImage = view.findViewById(R.id.fir_product_image);
        btMessage = view.findViewById(R.id.fir_bt_message);
    }
}
