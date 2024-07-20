package com.example.furniture;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class AddToCartActivity extends AppCompatActivity {
    private RecyclerView cartRV;
    private ArrayList<CartModel> cartModelArrayList;
    private CartAdapter cartAdapter;
    private FirebaseAuth firebaseAuth;
    private String uuid, ShowImg_Price, quantity, Product_title;
    public int DeliveryFee = 230;
    private int Total_cost;
    public TextView sTotalTv, dFeeTv, TotalTv;
    public int allTotalPrice = 0;
    private Button Checkoutbtn;
    private int listSize;
    // private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);

        firebaseAuth = FirebaseAuth.getInstance();

        cartRV = findViewById(R.id.cartRV);
        sTotalTv = findViewById(R.id.sTotalTv);
        dFeeTv = findViewById(R.id.dFeeTv);
        TotalTv = findViewById(R.id.TotalTv);
        Checkoutbtn = findViewById(R.id.Checkoutbtn);


        cartModelArrayList = new ArrayList<>();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(Objects.requireNonNull(firebaseAuth.getUid())).child("Cart").addValueEventListener(new ValueEventListener() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartModelArrayList.clear();


                for (DataSnapshot ds : snapshot.getChildren()) {

                    CartModel cartModel = ds.getValue(CartModel.class);


                    try {
                        String Price = "" + ds.child("Price").getValue().toString().trim();
                        String eachPrice = "" + ds.child("eachPrice").getValue().toString().trim();
                        quantity = "" + ds.child("quantity").getValue().toString().trim();
                        uuid = "" + ds.child("OrderTo").getValue().toString().trim();
                        ShowImg_Price = "" + ds.child("Price").getValue().toString().trim();
                        Product_title = "" + ds.child("Product_title").getValue().toString().trim();

                        Log.d(TAG, "Price: " + Price);

                        if (Price.equals("") && eachPrice.equals("")) {
                            allTotalPrice = 0;
                            Total_cost = 0;
                        } else {
                            allTotalPrice = allTotalPrice + Integer.parseInt(eachPrice) * Integer.parseInt(quantity);
                            Total_cost = allTotalPrice + DeliveryFee;

                            sTotalTv.setText("" + allTotalPrice);
                            TotalTv.setText("" + Total_cost);
                            dFeeTv.setText("" + DeliveryFee);

                        }


                    } catch (NullPointerException ignored) {

                    }

                    cartModelArrayList.add(cartModel);


                }
                //setup adapter
                cartAdapter = new CartAdapter(AddToCartActivity.this, cartModelArrayList);
                cartRV.setAdapter(cartAdapter);
                cartAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Checkoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  listSize = cartModelArrayList.size();
                Intent intent = new Intent(AddToCartActivity.this, PlaceOrderActivity.class);
                // intent.putExtra("SellerId",uuid);
                intent.putExtra("CustomerId", Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid());
//                intent.putExtra("ShowImg_Price",ShowImg_Price);
//                intent.putExtra("di_quantity",quantity);
//                intent.putExtra("Product_title",Product_title);
                Bundle bundle = new Bundle();
                bundle.putSerializable("cartModelArrayList", cartModelArrayList);
                intent.putExtras(bundle);
//                intent.putExtra("cartModelListSize",Integer.toString(listSize));
//                String s=Integer.toString(listSize);
//                Toast.makeText(AddToCartActivity.this, ""+s, Toast.LENGTH_SHORT).show();
                intent.putExtra("CartOrder", "True");
                intent.putExtra("SingleOrder", "false");

                startActivity(intent);

            }
        });




    }

    //Update Ui
    private final ValueEventListener cartValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            int totalValue = allTotalPrice;
            updateUI(totalValue);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e(TAG, "onCancelled", databaseError.toException());
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Users").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Cart");
        databaseReference1.addValueEventListener(cartValueEventListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Users").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Cart");

        databaseReference2.removeEventListener(cartValueEventListener);
    }

    private void updateUI(int totalValue) {
//        TextView totalValueTextView = findViewById(R.id.TotalTv);
        int TotalPriceAfterProductRemove = 0;
        for (CartModel cartModel : cartModelArrayList) {
            TotalPriceAfterProductRemove = TotalPriceAfterProductRemove + Integer.parseInt(cartModel.getEachPrice()) * Integer.parseInt(cartModel.getQuantity());

        }
        int TotalWithDC = TotalPriceAfterProductRemove + DeliveryFee;
        TotalTv.setText(String.valueOf(TotalWithDC));
        sTotalTv.setText(String.valueOf(TotalPriceAfterProductRemove));
        dFeeTv.setText(String.valueOf(DeliveryFee));

        if (cartModelArrayList.size() == 0) {
            TotalTv.setText("0");
            sTotalTv.setText("0");
            dFeeTv.setText("0");

        }

    }


}