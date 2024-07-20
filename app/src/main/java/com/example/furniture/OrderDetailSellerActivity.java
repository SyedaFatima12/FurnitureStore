package com.example.furniture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class OrderDetailSellerActivity extends AppCompatActivity {
    private ImageButton edit_btm;
    private FirebaseAuth firebaseAuth;
    private String orderId,OrderStatus,OrderTime,OrderCost,quantity,OrderTo,Shop_Name,Address,Product_title,Payment_Method;
    private TextView orderSellerIdTV,orderSellerDateTv,orderSellerStatusTV,ShopNameSellerIdTV,SellerTotalItemsIdTV,SelleramountIdTV,SellerDeliveryAddressTv,OrderSellerProductTitle,PaymentMethodSellerTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_seller);
        firebaseAuth=FirebaseAuth.getInstance();
        getSupportActionBar().hide();
        edit_btm=findViewById(R.id.edit_btm);
        edit_btm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String options[]={"In Progress","Cancelled","Completed"};
                AlertDialog.Builder builder=new AlertDialog.Builder(OrderDetailSellerActivity.this);
                builder.setTitle("Edit Order Status")
                        .setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // handel item clicks
                                String SelectOption=options[which];
                                editOrderStatus(SelectOption);
                            }
                        }).show();
            }
        });
       

        //get shop name
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(Objects.requireNonNull(firebaseAuth.getCurrentUser().getUid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Shop_Name= Objects.requireNonNull(snapshot.child("shop_Name").getValue()).toString().trim();
                ShopNameSellerIdTV.setText(Shop_Name);
                Toast.makeText(OrderDetailSellerActivity.this, ""+Shop_Name, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





       try {
           orderId= getIntent().getStringExtra("orderId").trim();
           OrderStatus= getIntent().getStringExtra("OrderStatus").trim();
           OrderTime= getIntent().getStringExtra("OrderTime").trim();
           OrderCost= getIntent().getStringExtra("OrderCost").trim();
           quantity= getIntent().getStringExtra("quantity").trim();
          // Toast.makeText(this, "Quantity"+quantity, Toast.LENGTH_SHORT).show();
           OrderTo= getIntent().getStringExtra("OrderTo").trim();
          // Toast.makeText(this, ""+OrderTo, Toast.LENGTH_SHORT).show();
           Address= getIntent().getStringExtra("Address").trim();
           Product_title = getIntent().getStringExtra("Product_title").trim();
       }catch (NullPointerException ignored){

       }


        //getting orderedItem detail:
        try {
//            String SellerId = getIntent().getStringExtra("OrderTo").trim();
//            Toast.makeText(this, "SellerId"+SellerId, Toast.LENGTH_SHORT).show();

            DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference("Users");
            databaseReference1.child(firebaseAuth.getCurrentUser().getUid()).child("Orders").child(orderId).child("OrderedItems").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        quantity= Objects.requireNonNull(snapshot.child("quantity").getValue()).toString().trim();
                        Address= Objects.requireNonNull(snapshot.child("Address").getValue()).toString().trim();
                        Product_title= Objects.requireNonNull(snapshot.child("Product_title").getValue()).toString().trim();
                        Payment_Method= Objects.requireNonNull(snapshot.child("Payment_Method").getValue()).toString().trim();

                        OrderSellerProductTitle.setText(Product_title);

                        SellerTotalItemsIdTV.setText(quantity);
                        SellerDeliveryAddressTv.setText(Address);
                        PaymentMethodSellerTV.setText(Payment_Method);


//                Toast.makeText(OrderDetailActivity.this, "quantity"+quantity, Toast.LENGTH_SHORT).show();
//                Toast.makeText(OrderDetailActivity.this, "Address"+Address, Toast.LENGTH_SHORT).show();
//                Toast.makeText(OrderDetailActivity.this, "Product_title"+Product_title, Toast.LENGTH_SHORT).show();
//                Toast.makeText(OrderDetailActivity.this, "Payment_Method"+Payment_Method, Toast.LENGTH_SHORT).show();
//

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }catch (NullPointerException ignored){

        }


                // ShopName= getIntent().getStringExtra("ShopName").trim();

        orderSellerIdTV=findViewById(R.id.orderSellerIdTV);
        orderSellerDateTv=findViewById(R.id.orderSellerDateTv);
        orderSellerStatusTV=findViewById(R.id.orderSellerStatusTV);
        ShopNameSellerIdTV=findViewById(R.id.ShopNameSellerIdTV);
        SellerTotalItemsIdTV=findViewById(R.id.SellerTotalItemsIdTV);
        SelleramountIdTV=findViewById(R.id.SelleramountIdTV);
        SellerDeliveryAddressTv=findViewById(R.id.SellerDeliveryAddressTv);
        OrderSellerProductTitle=findViewById(R.id.OrderSellerProductTitle);
        PaymentMethodSellerTV=findViewById(R.id.PaymentMethodSellerTV);

        orderSellerIdTV.setText(orderId);
        orderSellerDateTv.setText(OrderTime);
        orderSellerStatusTV.setText(OrderStatus);

        SelleramountIdTV.setText(OrderCost);




    }

    private void editOrderStatus(String selectOption) {

        HashMap<String,Object> map=new HashMap<>();
        map.put("OrderStatus",""+selectOption);

        final String message="Order is now"+selectOption;

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).child("Orders").child(orderId).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(OrderDetailSellerActivity.this, message, Toast.LENGTH_SHORT).show();




            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OrderDetailSellerActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}