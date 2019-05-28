package com.google.codelabs.mdc.java.shrine;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.google.codelabs.mdc.java.shrine.R;
import com.google.codelabs.mdc.java.shrine.java.Image;
import com.google.codelabs.mdc.java.shrine.network.ProductEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ProductGridUserFragment extends Fragment {

    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private RecyclerView recyclerView;

    private List<ProductEntry> products;
    private List<ProductEntry> testProducts;
    private List<Image> productImages;

    private String productUserId;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fir_product_grid_user_fragment, container, false);
        setUpToolbar(view);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("productentry");

        products = new ArrayList<>();
        productImages = new ArrayList<>();


        //inicializar componentes
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),
                2,GridLayoutManager.VERTICAL,false));

        //Listener boton Nuevo producto

        //Listener boton Mensajes
        testProducts = ProductEntry.initProductEntryList(getResources());
        products.addAll(testProducts);


        ProductCardRecyclerViewAdapter adapter = new ProductCardRecyclerViewAdapter(
                products,getContext(), item -> {
            Bundle bundle = createBundle(item);
            ((NavigationHost)getContext()).navigateTo(new ProductPageFragment(), true, bundle);

        });


        recyclerView.setAdapter(adapter);
        int largePadding = getResources().getDimensionPixelSize(R.dimen.fir_product_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.fir_staggered_product_grid_spacing_small);
        recyclerView.addItemDecoration(new ProductGridItemDecoration(largePadding,smallPadding));

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.findViewById(R.id.product_grid).setBackground(getContext().getDrawable(
                    R.drawable.fir_product_grid_background_shape));
        }


        return (view);
    }

    private void setUpToolbar(View view){
        Toolbar toolbar = view.findViewById(R.id.app_bar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if(activity != null) {
            activity.setSupportActionBar(toolbar);
        }
    }

    private Bundle createBundle(ProductEntry item){
        Bundle bundle = new Bundle();
        bundle.putString("title",item.getTitle());
        bundle.putString("description", item.getDescription());
        bundle.putString("price", item.getPrice());
        bundle.putString("size", item.getSize());
        bundle.putString("url", item.getUrl());
        bundle.putString("userId", item.getUserId());
        bundle.putStringArray("imagesurl", item.getImages());
        return bundle;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fir_toolbar_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.sign_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), LoginActivty.class));
                return true;
            case R.id.search:
                break;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ProductEntry product = snapshot.getValue(ProductEntry.class);
                    String userId = (String) snapshot.child("userid").getValue();
                    String url = (String) snapshot.child("url").getValue();
                    product.setUserId(userId);
                    product.setUrl(url);
                    if(!firebaseUser.getUid().equals(product.getUserId()))
                        continue;

                    products.add(product);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        reference.addValueEventListener(valueEventListener);
    }
}
