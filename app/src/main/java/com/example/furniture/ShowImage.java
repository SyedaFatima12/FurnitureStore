package com.example.furniture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Printer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ShowImage extends AppCompatActivity {
    private String img, Price, Product_Category, Product_title, Product_Description;
    private ImageView show_Img;
    private Button Cart_Btn, Order_Btn;
    private FirebaseAuth firebaseAuth;
    private TextView di_quantity;
    private TextView ShowImg_Price;
    private TextView Child_Title;
    private TextView Child_description;
    private String Product_Id;
    private ImageButton decrementButton;
    private ImageButton incrementButton;
    private TextView quantityTv;
    private String uuid;
    private int cost = 0;
    private int finalCost = 0;
    private int quantity = 1;
    private String Address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        firebaseAuth = FirebaseAuth.getInstance();

        img = getIntent().getStringExtra("img");


        Price = getIntent().getStringExtra("Price");
        Product_Category = getIntent().getStringExtra("Product_Category");
        Product_title = getIntent().getStringExtra("Product_title");
        Product_Description = getIntent().getStringExtra("Product_Description");
        Product_Id = getIntent().getStringExtra("Product_Id");
        uuid = getIntent().getStringExtra("uuid");
      //  Toast.makeText(this, "UUid"+uuid, Toast.LENGTH_SHORT).show();


        cost = Integer.parseInt(Price.replaceAll("Rs", ""));
        finalCost = Integer.parseInt(Price.replaceAll("Rs", ""));


        show_Img = findViewById(R.id.show_Img);
        ShowImg_Price = findViewById(R.id.ShowImg_Price);
        Order_Btn = findViewById(R.id.Order_Btn);
        Cart_Btn = findViewById(R.id.Cart_Btn);
        Child_Title = findViewById(R.id.Child_Title);
        Child_description = findViewById(R.id.Child_description);
        di_quantity = findViewById(R.id.di_quantity);

        decrementButton = findViewById(R.id.decrementButton);
        incrementButton = findViewById(R.id.incrementButton);
        quantityTv = findViewById(R.id.quantityTv);


        Order_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(ShowImage.this,PlaceOrderActivity.class);
                intent.putExtra("SellerId",uuid);
                intent.putExtra("CustomerId",firebaseAuth.getCurrentUser().getUid());
                intent.putExtra("ShowImg_Price",""+finalCost);
                Toast.makeText(ShowImage.this, "ShowImg_Price"+finalCost, Toast.LENGTH_SHORT).show();
                intent.putExtra("di_quantity",di_quantity.getText().toString());
                intent.putExtra("Product_title",Product_title);
                intent.putExtra("SingleOrder","True");
                intent.putExtra("CartOrder","false");

                startActivity(intent);

            }
        });
        decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1) {
                    quantity--;

                    finalCost = finalCost - cost;
                    di_quantity.setText("" + quantity);
                    ShowImg_Price.setText("Price :" + finalCost + " Rs");

                }
            }
        });

        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                finalCost = finalCost + cost;
                di_quantity.setText("" + quantity);

                ShowImg_Price.setText("Price :" + finalCost + " Rs");

            }
        });
        //get user Address
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Address= (String) snapshot.child("Address").getValue();
                //Toast.makeText(ShowImage.this, "Address"+Address, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //String s = Double.toString(quantity);

        Cart_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String timestamp=""+System.currentTimeMillis();
                Map<String, String> map = new HashMap<>();
                map.put("img", img);
                map.put("Price",Integer.toString(finalCost));
                map.put("Product_Category", Product_Category);
                map.put("Product_title", Product_title);
                map.put("Product_Description", Product_Description);
                map.put("Product_Id", Product_Id);
                map.put("uuid", uuid);
                map.put("quantity", di_quantity.getText().toString());
                map.put("id",timestamp);
                map.put("OrderBy", Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid());
                map.put("OrderTo",uuid);
                map.put("Address",Address);
                map.put("eachPrice",Integer.toString(cost));




                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                databaseReference.child(firebaseAuth.getUid()).child("Cart").child(timestamp).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ShowImage.this, "Product added to the  cart Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ShowImage.this, AddToCartActivity.class);
                        startActivity(intent);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(ShowImage.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        try {
            Picasso.get().load(Uri.parse(img)).placeholder(R.drawable.ic_baseline_image).into(show_Img);
        } catch (NullPointerException ignored) {

        }
        ShowImg_Price.setText("Price :" + finalCost + " Rs");
        Child_Title.setText("Title :" + Product_title);
        Child_description.setText("Description :" + Product_Description);

    }
}