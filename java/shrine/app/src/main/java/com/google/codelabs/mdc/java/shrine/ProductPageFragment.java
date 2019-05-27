package com.google.codelabs.mdc.java.shrine;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.codelabs.mdc.java.shrine.network.ImageRequester;

public class  ProductPageFragment extends Fragment {

    private TextView tvTitle;
    private TextView tvDescription;
    private TextView tvSize;
    private TextView tvPrice;
    private MaterialButton btMessage;
    private NetworkImageView ivImage;
    private ImageRequester imageRequester;
    private String [] images;


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fir_product_page_fragment, container, false);

        tvTitle = view.findViewById(R.id.fir_product_title);
        tvDescription = view.findViewById(R.id.fir_product_descripction);
        tvPrice = view.findViewById(R.id.fir_product_price);
        tvSize = view.findViewById(R.id.fir_product_size);
        ivImage = view.findViewById(R.id.fir_product_image);
        btMessage = view.findViewById(R.id.fir_bt_message);
        imageRequester = ImageRequester.getInstance();

        Bundle bundle;
        bundle = this.getArguments();

        if(bundle != null){
            tvTitle.setText((CharSequence) bundle.get("title"));
            //tvSize.setText((CharSequence) bundle.get("size"));
            tvDescription.setText((CharSequence) bundle.get("description"));
            tvPrice.setText((CharSequence) bundle.get("price"));
            imageRequester.setImageFromUrl(ivImage, String.valueOf(bundle.get("url")));
            images = (String[]) bundle.get("imagesurl");
        }

        btMessage.setOnClickListener(v -> {
            Bundle bundle1 = new Bundle();
            bundle1.putString("title", getArguments().get("title").toString());
            bundle1.putString("url", getArguments().get("url").toString());
            bundle1.putString("userId", getArguments().get("userId").toString());
            ((NavigationHost)getContext()).navigateTo(new ChatFragment(), true, bundle1);
        });
        return view;
    }


}