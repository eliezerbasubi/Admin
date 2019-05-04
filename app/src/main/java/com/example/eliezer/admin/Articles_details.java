package com.example.eliezer.admin;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Articles_details extends AppCompatActivity {

    private RecyclerView mRecyclerview;
    private DatabaseReference mArticle;
    String shop_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles_details);

        mRecyclerview = findViewById(R.id.articles_recyclerview);

        mArticle = FirebaseDatabase.getInstance().getReference().child("Articles");
        mArticle.keepSynced(true);

        shop_uid = getIntent().getStringExtra("shop_uid");

        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.setItemAnimator(new DefaultItemAnimator());
        mRecyclerview.setNestedScrollingEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final Query query = mArticle.orderByChild("shop_user").equalTo(shop_uid);

        FirebaseRecyclerAdapter<ArticleModel,myViewHolderArticle> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter <ArticleModel, myViewHolderArticle>(
                ArticleModel.class,
                R.layout.shop_layout_model,
                myViewHolderArticle.class,
                query

        ) {
            @Override
            protected void populateViewHolder(final myViewHolderArticle viewHolder,final ArticleModel model, int position) {
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dst : dataSnapshot.getChildren()){
                             viewHolder.setShopName(model.product_list);
                             viewHolder.setLocation(model.price_list);
                              viewHolder.setterLogoImage(model.image_list,getApplicationContext());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

           }
        };
        mRecyclerview.setAdapter(firebaseRecyclerAdapter);
    }

    public static class myViewHolderArticle extends RecyclerView.ViewHolder{

        ImageView prodImage;
        Button disable_btn, enable_btn;
        View mView;
        public myViewHolderArticle(View itemView) {
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
