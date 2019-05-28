package com.google.codelabs.mdc.
        java.shrine;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Fragment representing the login screen for Shrine.
 */
public class FormFragment extends Fragment {

    private Uri imageUri;
    private Uri imageToUpload;
    public int i;
    public int position;

    String goodUrl;

    private ImageView ivProduct1;
    private ImageView ivProduct2;
    private ImageView ivProduct3;
    private ImageView ivProduct4;
    private ImageView ivProduct5;
    private List<ImageView> images;
    private List<String> imagesString;
    private List<String> imagesUrl;

    private MaterialButton btUpload;

    private TextInputEditText etName;
    private TextInputLayout tiName;
    private TextInputEditText etPrice;
    private TextInputLayout tiPrice;
    private TextInputEditText etSize;
    private TextInputLayout tiSize;
    private TextInputEditText etDescription;
    private TextInputLayout tiDescription;

    private FirebaseUser firebaseUser;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    private final int RESULTADO_CARGA_FINAL = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fir_form_fragment, container, false);
        setUpToolbar(view);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("products");

        imagesString = new ArrayList<>();
        imagesUrl = new ArrayList<>();

        initComponents(view);


        ivProduct1.setOnClickListener(v ->{
            setImage(0);

        });

        ivProduct2.setOnClickListener(v ->{
            setImage(1);

        });

        ivProduct3.setOnClickListener(v ->{
            setImage(2);
        });

        ivProduct4.setOnClickListener(v ->{
            setImage(3);
        });

        ivProduct5.setOnClickListener(v ->{
            setImage(4);
        });



        btUpload.setOnClickListener(v -> {
            uploadProduct();
            System.out.println(goodUrl);
            ((NavigationHost)getContext()).navigateTo(new ProductGridFragment(), false);
        });
            
        return view;
    }

    private void setImage(int position){
        this.position = position;
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULTADO_CARGA_FINAL);
    }


    private void setUpToolbar(View view){
        Toolbar toolbar = view.findViewById(R.id.app_bar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if(activity != null) {
            activity.setSupportActionBar(toolbar);
        }
    }
    private void uploadProduct() {
        List<String> imagesToUpload = new ArrayList<>();
        String url;

        for(String image : imagesString){
            if(image != null){
                imagesToUpload.add(image);
            }
        }

        for(String image: imagesToUpload){
            imageToUpload = Uri.parse(image);
            long imageName = System.currentTimeMillis();
            StorageReference fileReference = storageReference.child(imageName
                    + "." + getFileExtension(imageToUpload));
            fileReference.putFile(imageToUpload).addOnSuccessListener(command ->{
                Task<Uri> task = fileReference.getDownloadUrl();
                goodUrl = task.toString();
            });

            System.out.println(String.valueOf(fileReference.getStorage().getReference("uploads")
                    .child(String.valueOf(imageName)).getDownloadUrl()));
        }


        if(etName.getText().toString().equals("")){
            etName.setError(getResources().getString(R.string.fir_error_title));
            return;
        }
        if(etDescription.getText().toString().equals("")){
            etDescription.setError(getResources().getString(R.string.fir_error_description));
            return;       
        }
        if(etSize.getText().toString().equals("")){
            etSize.setError(getResources().getString(R.string.fir_error_size));
            return;       
        }
        if(etPrice.getText().toString().equals("")){
            etPrice.setError(getResources().getString(R.string.fir_error_price));
            return;
        }

        if(imagesToUpload.size() == 0){
            Toast.makeText(getContext(), getResources().getString(R.string.fir_error_images), Toast.LENGTH_LONG).show();
            return;
        }
           
        etName.setError(null);
        etDescription.setError(null);
        etSize.setError(null);
        etPrice.setError(null);

        HashMap<String, Object> map = new HashMap<>();
        map.put("title", etName.getText().toString());
        map.put("description", etDescription.getText().toString());
        map.put("url", imagesToUpload.get(0));
        map.put("price", etPrice.getText().toString());
        map.put("size", etSize.getText().toString());
        map.put("imagesurl", imagesToUpload);
        map.put("userid", firebaseUser.getUid());

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("productentry").push().setValue(map);

    }
    private String getFileExtension(Uri uri){
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap= MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fir_toolbar,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULTADO_CARGA_FINAL && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            switch (position){
                case 0 :
                    ivProduct1.setImageURI(imageUri);
                    imagesString.add(0, imageUri.toString());
                    break;
                case 1 :
                    ivProduct2.setImageURI(imageUri);
                    imagesString.add(imageUri.toString());
                    break;
                case 2 :
                    ivProduct3.setImageURI(imageUri);
                    imagesString.add(imageUri.toString());
                    break;
                case 3 :
                    ivProduct4.setImageURI(imageUri);
                    imagesString.add(imageUri.toString());
                    break;
                case 4 :
                    ivProduct5.setImageURI(imageUri);
                    imagesString.add(imageUri.toString());
                    break;
            }
        }
    }

    public void initComponents(View view){
        images = new ArrayList<>();
        btUpload = view.findViewById(R.id.upload_button);
        etName = view.findViewById(R.id.product_name_edit_text);
        tiName = view.findViewById(R.id.product_name_text_input);
        etDescription = view.findViewById(R.id.product_description_edit_text);
        tiDescription = view.findViewById(R.id.product_description_text_input);
        etPrice = view.findViewById(R.id.product_price_edit_text);
        tiPrice = view.findViewById(R.id.product_price_text_input);
        etSize = view.findViewById(R.id.product_size_edit_text);
        tiSize = view.findViewById(R.id.product_size_text_input);
        ivProduct1 = view.findViewById(R.id.image_1);
        images.add(ivProduct1);
        ivProduct2 = view.findViewById(R.id.image_2);
        images.add(ivProduct2);
        ivProduct3 = view.findViewById(R.id.image_3);
        images.add(ivProduct3);
        ivProduct4 = view.findViewById(R.id.image_4);
        images.add(ivProduct4);
        ivProduct5 = view.findViewById(R.id.image_5);
        images.add(ivProduct5);
    }
}
