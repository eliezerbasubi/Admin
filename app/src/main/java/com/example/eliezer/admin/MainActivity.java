package com.example.eliezer.admin;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private FirebaseAuth mAuth;
    String current_user;
    private RecyclerView recyclerView;
    private DatabaseReference mShopDatabase;
    private RelativeLayout relativeLayout;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         Toolbar toolbar = findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);
         ActionBar actionbar = getSupportActionBar();
         actionbar.setDisplayHomeAsUpEnabled(true);
         actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        recyclerView = findViewById(R.id.recyclerview_shop);

        relativeLayout = findViewById(R.id.content_frame);

        textView = findViewById(R.id.test);

        mAuth = FirebaseAuth.getInstance();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        mShopDatabase = FirebaseDatabase.getInstance().getReference().child("Shops");
        mShopDatabase.keepSynced(true);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        int id =menuItem.getItemId();

                        if(id==R.id.about){
                            //activity to be opened
                        }

                        if(id==R.id.setting){
                            //activity to be opened
                        }

                        if(id==R.id.logout){
                            //activity to be opened
                            FirebaseAuth.getInstance().signOut();
                            finish();
                        }


                        menuItem.setChecked(true);

                        mDrawerLayout.closeDrawers();



                        return true;
                    }
                });

    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        if (mUser != null){
            current_user = mAuth.getCurrentUser().getUid().toString();
        }

        FirebaseRecyclerAdapter<ShopsModelClass,myViewHolder> firebaseAdapter = new FirebaseRecyclerAdapter <ShopsModelClass, myViewHolder>(
                ShopsModelClass.class,
                R.layout.shop_layout_model,
                myViewHolder.class,
                mShopDatabase
        ) {
            @Override
            protected void populateViewHolder(final myViewHolder viewHolder,final ShopsModelClass model, int position) {
                final String shop_list_id = getRef(position).getKey().toString();

                mShopDatabase.child(shop_list_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       /* String shop_main_name = dataSnapshot.child("Shop_Name").getValue().toString();
                        String shop_logo = dataSnapshot.child("Shop_Logo").getValue().toString();
                        String shop_main_physical_address = dataSnapshot.child("Shop_Address").getValue().toString();*/

                        final String shop_user_id =model.getShop_User();

                        String str =model.getShop_Name();
                        String[] strArray = str.split(" ");
                        StringBuilder builder = new StringBuilder();
                        for (String s : strArray) {
                            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
                            builder.append(cap + " ");
                        }
                        viewHolder.setShopName(builder.toString());

                        //viewHolder.setShopName(shop_main_name);
                        viewHolder.setterLogoImage(model.getShop_Logo(),getApplicationContext());
                        viewHolder.setLocation(model.getShop_Address());



                        //HANDLE ENABLE BUTTON
                        viewHolder.enable_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mShopDatabase.child(shop_list_id).child("status").setValue("paid").addOnCompleteListener(new OnCompleteListener <Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task <Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(MainActivity.this, model.getShop_Name() +" Successfully added", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Snackbar.make(relativeLayout,"Try again",Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    return;
                                                }
                                            }).show();
                                        }
                                    }
                                });
                            }
                        });



                        //HANDLE DISABLE BUTTON
                        viewHolder.disable_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mShopDatabase.child(shop_list_id).child("status").setValue("unpaid").addOnCompleteListener(new OnCompleteListener <Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task <Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(MainActivity.this, model.getShop_Name() +" Successfully removed", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Snackbar.make(relativeLayout,"Try again",Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    return;
                                                }
                                            }).show();
                                        }
                                    }
                                });
                            }
                        });



                        if (model.getStatus().equals("paid")){
                            viewHolder.enable_btn.setBackgroundResource(R.drawable.ic_check_black_24dp);
                            viewHolder.enable_btn.setText("");
                        }

                        if(model.getStatus().equals("unpaid")){
                            viewHolder.disable_btn.setBackgroundResource(R.drawable.ic_delete_black_24dp);
                            viewHolder.disable_btn.setText("");
                        }



                        //SEND DATA TO NEXT ACTIVITY

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent details = new Intent(MainActivity.this,Articles_details.class);
                                details.putExtra("shop_uid",shop_user_id);
                                startActivity(details);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            };
        recyclerView.setAdapter(firebaseAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.online :
                //mDrawerLayout.openDrawer(GravityCompat.START);
                Intent launch = getPackageManager().getLaunchIntentForPackage("com.example.eliezer.onlineshop");
                if (launch != null){
                    startActivity(launch);
                }

        }
        return super.onOptionsItemSelected(item);
    }





    public static class myViewHolder extends RecyclerView.ViewHolder{

        ImageView prodImage;
        Button disable_btn, enable_btn;
        View mView;
        public myViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            disable_btn = mView.findViewById(R.id.disable);
            enable_btn = mView.findViewById(R.id.permit_btn);
        }

        public void setShopName(String product_name) {
            TextView product = (TextView)mView.findViewById(R.id.shop_name);
            product.setText(product_name);
        }

        public void setLocation(String shopCity) {
            TextView price = (TextView)mView.findViewById(R.id.shop_location);
            price.setText(shopCity);
        }

        public void setterLogoImage(String logo_thumb,Context context){
            prodImage = (ImageView)mView.findViewById(R.id.shop_logo);
            //Picasso.get().load(image_thumb).placeholder(R.drawable.lo).into(prodImage);

            Glide
                    .with(context)
                    .load(logo_thumb).apply(new RequestOptions()
                    .placeholder(R.drawable.lo)
                    .dontAnimate()
                    .dontTransform())
                    .into(prodImage);
        }
    }
}
