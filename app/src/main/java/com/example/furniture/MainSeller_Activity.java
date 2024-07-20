package com.example.furniture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MainSeller_Activity extends AppCompatActivity {
//Ui views
private  TextView tabProductsSeller,tabOrdersSeller,viewProductsSeller;
private ImageView addProductSeller;
private RelativeLayout ProductRLSeller,OrderRLSeller;
private RecyclerView OrderRVSeller;
private ArrayList<ModelOrderSeller> modelOrderSellerArrayList;
private  AdapterOrderSeller adapterOrderSeller;
private ImageView Seller_logout;
private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       try {
           getSupportActionBar().hide();

       }
       catch (NullPointerException e){

       }
        setContentView(R.layout.activity_main_seller);
       //init firebase auth
        firebaseAuth=FirebaseAuth.getInstance();
       //init Ui Views
        tabProductsSeller=findViewById(R.id.tabProductsSeller);
        tabOrdersSeller=findViewById(R.id.tabOrdersSeller);
        ProductRLSeller=findViewById(R.id.ProductRLSeller);
        OrderRLSeller=findViewById(R.id.OrderRLSeller);
        addProductSeller=findViewById(R.id.addProductSeller);
        OrderRVSeller=findViewById(R.id.OrderRVSeller);
        viewProductsSeller=findViewById(R.id.viewProductsSeller);
        Seller_logout=findViewById(R.id.Seller_logout);
        ShowProductUI();

        modelOrderSellerArrayList=new ArrayList<>();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).child("Orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelOrderSellerArrayList.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
//
//                    String OrderStatus= Objects.requireNonNull(ds.child("OrderStatus").getValue()).toString();
//                    Toast.makeText(MainSeller_Activity.this, ""+OrderStatus, Toast.LENGTH_SHORT).show();

                    ModelOrderSeller modelOrderSeller = ds.getValue(ModelOrderSeller.class);
                    modelOrderSellerArrayList.add(modelOrderSeller);


                }
                adapterOrderSeller=new AdapterOrderSeller(MainSeller_Activity.this,modelOrderSellerArrayList);
                //set Adapter to list
                OrderRVSeller.setAdapter(adapterOrderSeller);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        modelOrderSellerArrayList.add(new ModelOrderSeller("1","02/07/2022","syedafatima7666@gmail.com","2000","inProgress"));
//        modelOrderSellerArrayList.add(new ModelOrderSeller("2","02/07/2022","syedafatima7666@gmail.com","4000","inProgress"));
//        modelOrderSellerArrayList.add(new ModelOrderSeller("3","02/07/2022","syedafatima7666@gmail.com","5000","inProgress"));
//        modelOrderSellerArrayList.add(new ModelOrderSeller("4","02/07/2022","syedafatima7666@gmail.com","6000","inProgress"));
//        modelOrderSellerArrayList.add(new ModelOrderSeller("5","02/07/2022","syedafatima7666@gmail.com","8000","inProgress"));





        tabProductsSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProductUI();
            }
        });

        tabOrdersSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowOrderUI();
            }
        });

        addProductSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainSeller_Activity.this,AddProduct_Activity.class);
                startActivity(intent);
            }
        });

        viewProductsSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainSeller_Activity.this,ViewProduct_Activity.class);
                startActivity(intent);
            }
        });

        Seller_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMenu(v);
            }
        });

    }

    private void ShowMenu(View v) {
        PopupMenu popupMenu=new PopupMenu(MainSeller_Activity.this,v);
        popupMenu.getMenuInflater().inflate(R.menu.popmenu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId()==R.id.Logout){

                    firebaseAuth.signOut();
                    Toast.makeText(MainSeller_Activity.this,"Logout",Toast.LENGTH_SHORT).show();

                    // this listener will be called when there is change in firebase user session
                    FirebaseAuth.AuthStateListener authStateListener=new FirebaseAuth.AuthStateListener() {
                        @Override
                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                            FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                            if (firebaseUser==null){
                                //auth state change user is null
                                //lunch login Activity

                                Intent intent=new Intent(MainSeller_Activity.this,Login_Activity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    };
                    authStateListener.onAuthStateChanged(firebaseAuth);


                }
                return true;
            }
        });

        popupMenu.show();
    }

    private void ShowOrderUI() {
        OrderRLSeller.setVisibility(View.VISIBLE);
        ProductRLSeller.setVisibility(View.GONE);
        tabOrdersSeller.setTextColor(getResources().getColor(R.color.mustard));
        tabOrdersSeller.setBackgroundResource(R.drawable.shape_rect04);

        tabProductsSeller.setTextColor(getResources().getColor(R.color.grey_black));
        tabProductsSeller.setBackgroundColor(getResources().getColor(android.R.color.transparent));

    }

    private void ShowProductUI() {
        ProductRLSeller.setVisibility(View.VISIBLE);
        OrderRLSeller.setVisibility(View.GONE);
        tabProductsSeller.setTextColor(getResources().getColor(R.color.mustard));
        tabProductsSeller.setBackgroundResource(R.drawable.shape_rect04);

        tabOrdersSeller.setTextColor(getResources().getColor(R.color.grey_black));
        tabOrdersSeller.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }
}