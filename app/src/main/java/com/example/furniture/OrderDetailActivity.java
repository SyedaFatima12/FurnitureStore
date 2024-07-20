package com.example.furniture;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Objects;

public class OrderDetailActivity extends AppCompatActivity {
    private String orderId,OrderStatus,OrderTime,OrderCost,quantity,ShopName,Address,Product_title,Payment_Method;
private ImageButton backbtn;
private TextView orderIdTV,orderDateTv,orderStatusTV,ShopNameIdTV,TotalItemsIdTV,amountIdTV,DeliveryAddressTv,OrderUserProductTitle,PaymentMethodTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        Objects.requireNonNull(getSupportActionBar()).hide();



      try {
         orderId = getIntent().getStringExtra("orderId").trim();
           OrderStatus = getIntent().getStringExtra("OrderStatus").trim();
           OrderTime = getIntent().getStringExtra("OrderTime").trim();

//           quantity = getIntent().getStringExtra("quantity").trim();
//
//
//           Address = getIntent().getStringExtra("Address").trim();
//
//            Product_title = getIntent().getStringExtra("Product_title").trim();
//          Payment_Method = getIntent().getStringExtra("Payment_Method").trim();

          //setData(orderId,OrderStatus,OrderTime,OrderCost,quantity,OrderTo)
         // getShopName(OrderTo);
         // Log.d(TAG, "onCreate: "+OrderTo);

          //convert timestamp to date format

      }catch (NullPointerException ignored){

      }
//getting orderedItem detail:
try {
    String SellerId = getIntent().getStringExtra("OrderTo").trim();
   // Toast.makeText(this, "SellerId"+SellerId, Toast.LENGTH_SHORT).show();

    DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference("Users");
    databaseReference1.child(SellerId).child("Orders").child(orderId).child("OrderedItems").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()){
                quantity= Objects.requireNonNull(snapshot.child("quantity").getValue()).toString().trim();
                Address= Objects.requireNonNull(snapshot.child("Address").getValue()).toString().trim();
                Product_title= Objects.requireNonNull(snapshot.child("Product_title").getValue()).toString().trim();
                Payment_Method= Objects.requireNonNull(snapshot.child("Payment_Method").getValue()).toString().trim();

                OrderUserProductTitle.setText(Product_title);
                PaymentMethodTV.setText(Payment_Method);
                DeliveryAddressTv.setText(Address);
                TotalItemsIdTV.setText(quantity);

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

        //get shop name
        try {
           String OrderTo = getIntent().getStringExtra("OrderTo").trim();
            //Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");
            databaseReference.child(OrderTo).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String ShopName= Objects.requireNonNull(snapshot.child("shop_Name").getValue()).toString().trim();
                    ShopNameIdTV.setText(ShopName);
                    Toast.makeText(OrderDetailActivity.this, ""+ShopName, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (NullPointerException ignored){

        }


        //init views

        backbtn=findViewById(R.id.backbtn);
        orderIdTV=findViewById(R.id.orderIdTV);
        orderDateTv=findViewById(R.id.orderDateTv);
        orderStatusTV=findViewById(R.id.orderStatusTV);
        ShopNameIdTV=findViewById(R.id.ShopNameIdTV);
        TotalItemsIdTV=findViewById(R.id.TotalItemsIdTV);
        amountIdTV=findViewById(R.id.amountIdTV);
        DeliveryAddressTv=findViewById(R.id.DeliveryAddressTv);
        OrderUserProductTitle=findViewById(R.id.OrderUserProductTitle);
        PaymentMethodTV=findViewById(R.id.PaymentMethodTV);




        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });





       try {
           Calendar calendar=Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(OrderTime));
           String dateformat= DateFormat.format("dd/MM/yyy",calendar).toString();

           orderIdTV.setText(orderId);
           orderDateTv.setText(dateformat);
           orderStatusTV.setText(OrderStatus);
           orderIdTV.setText(orderId);

           OrderCost = getIntent().getStringExtra("OrderCost").trim();
           amountIdTV.setText(OrderCost+"RS");

       }catch (NullPointerException ignored){

       }





    }




}