package com.example.furniture;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PlaceOrderActivity extends AppCompatActivity {
    private CheckBox checkbox_COD;
    private Button PlaceOrder;
    // private String COD;
    private TextView CreditCard, debitCard;
    private String SellerId, ShowImg_Price, di_quantity, Product_title;
    private String CustomerId;
    private FirebaseAuth firebaseAuth;
    private String timeStamp;
    private String Customer_name, Phone_no, Address;
    private TextView Customer_Name, Customer_Address, Customer_Phone_No;
    private String message, PaymentMethod, Cart, SingleOrder, listSize;
    private ActivityResultLauncher<Intent> launcher;
    private ArrayList<CartModel> cartModelList;
    private int size;
    private DatabaseReference databaseReference1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_place_order);
        checkbox_COD = findViewById(R.id.checkbox_COD);
        CreditCard = findViewById(R.id.CreditCard);
        debitCard = findViewById(R.id.debitCard);
        PlaceOrder = findViewById(R.id.PlaceOrder);
        Customer_Name = findViewById(R.id.Customer_name);
        Customer_Address = findViewById(R.id.Customer_Address);
        Customer_Phone_No = findViewById(R.id.Customer_Phone_No);


        //place order from cart

        cartModelList = new ArrayList<>();
        //Activity For Result

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == 78) {
                    Intent intent = result.getData();
                    // your operation....
                    if (intent != null) {
                        message = intent.getStringExtra("Message");
                        PaymentMethod = intent.getStringExtra("PayMethod");

//                        Toast.makeText(PlaceOrderActivity.this, "" + message, Toast.LENGTH_SHORT).show();
//                        Toast.makeText(PlaceOrderActivity.this, ""+PaymentMethod, Toast.LENGTH_SHORT).show();
//

                    }


                }
            }
        });
        Bundle bundleObject = getIntent().getExtras();
    cartModelList = (ArrayList<CartModel>) bundleObject.getSerializable("cartModelArrayList");

        SellerId = getIntent().getStringExtra("SellerId");
        CustomerId = getIntent().getStringExtra("CustomerId");
        ShowImg_Price = getIntent().getStringExtra("ShowImg_Price");
        di_quantity = getIntent().getStringExtra("di_quantity");
        Product_title = getIntent().getStringExtra("Product_title");
       PaymentMethod = getIntent().getStringExtra("PaymentMethod");
        Cart = getIntent().getStringExtra("CartOrder");
        SingleOrder = getIntent().getStringExtra("SingleOrder");
//        listSize = getIntent().getStringExtra("cartModelListSize");
//        size=Integer.parseInt(listSize);

        timeStamp = "" + System.currentTimeMillis();


//        Toast.makeText(this, "SellerId" + SellerId, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "CustomerId" + CustomerId, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "ShowImg_Price" + ShowImg_Price, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "di_quantity" + di_quantity, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "timeStamp" + timeStamp, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "Product_title" + Product_title, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "CartOrder" + Cart, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "SingleOrder" + SingleOrder, Toast.LENGTH_SHORT).show();
//         Toast.makeText(this, "cartModelArrayList" + cartModelList, Toast.LENGTH_SHORT).show();

        //getting Customer data
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(CustomerId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Customer_name = snapshot.child("name").getValue().toString();
                Phone_no = snapshot.child("Phone").getValue().toString();
                Address = snapshot.child("Address").getValue().toString();
                Customer_Name.setText("Name: " + Customer_name);
                Customer_Phone_No.setText("Phone no: " + Phone_no);
                Customer_Address.setText("Address: " + Address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        PlaceOrder.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if (Cart.contains("True") && message.contains("Done")) {

                    //OrderFromCart(PaymentMethod);

                    OrderInfo(PaymentMethod);


                } else if (SingleOrder.contains("True") && message.contains("Done")) {
                    doneOrder(PaymentMethod);
                } else {
                    Toast.makeText(PlaceOrderActivity.this, "Please Select a PaymentMethod", Toast.LENGTH_SHORT).show();


                }

            }


        });


        debitCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String PaymentMethod = "debitCard";
                Intent intent = new Intent(PlaceOrderActivity.this, CardFormActivity.class);
                intent.putExtra("SellerId", SellerId);
                intent.putExtra("CustomerId", firebaseAuth.getCurrentUser().getUid());
                intent.putExtra("ShowImg_Price", ShowImg_Price);
                intent.putExtra("di_quantity", di_quantity);
                intent.putExtra("Product_title", Product_title);
                intent.putExtra("Customer_name", Customer_name);
                intent.putExtra("Phone_no", Phone_no);
                intent.putExtra("Address", Address);
                intent.putExtra("Payment_Method", PaymentMethod);
                launcher.launch(intent);
            }
        });

        CreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String PaymentMethod = "CreditCard";
                Intent intent = new Intent(PlaceOrderActivity.this, CardFormActivity.class);
                intent.putExtra("SellerId", SellerId);
                intent.putExtra("CustomerId", firebaseAuth.getCurrentUser().getUid());
                intent.putExtra("ShowImg_Price", ShowImg_Price);
                intent.putExtra("di_quantity", di_quantity);
                intent.putExtra("Product_title", Product_title);
                intent.putExtra("Customer_name", Customer_name);
                intent.putExtra("Phone_no", Phone_no);
                intent.putExtra("Address", Address);
                intent.putExtra("Payment_Method", PaymentMethod);
                launcher.launch(intent);
                // startActivityForResult(intent,2);
            }
        });
    }

    //
    private void OrderInfo(String paymentMethod) {
        //getting cart data
        submitOrder(paymentMethod);


//        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Users");
//        databaseReference2.child(Objects.requireNonNull(firebaseAuth.getUid())).child("Cart").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                cartModelList.clear();
//                for (DataSnapshot ds : snapshot.getChildren()) {
//
//                    CartModel cartModel = ds.getValue(CartModel.class);
//                    cartModelList.add(cartModel);
//
////                    String di_quantity = "" + Objects.requireNonNull(ds.child("quantity").getValue()).toString().trim();
////                    String SellerId = "" + Objects.requireNonNull(ds.child("OrderTo").getValue()).toString().trim();
////                    String ShowImg_Price = "" + Objects.requireNonNull(ds.child("Price").getValue()).toString().trim();
////                    String Product_title = "" + Objects.requireNonNull(ds.child("Product_title").getValue()).toString().trim();
//
////                    submitOrder(di_quantity, SellerId, ShowImg_Price, Product_title, paymentMethod);
//
////
////
////
////                    OrderFromCart(ShowImg_Price, SellerId, di_quantity, paymentMethod, Product_title);
//
//                }
//                submitOrder(paymentMethod);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


//        Toast.makeText(this, "Size" + cartModelList.size(), Toast.LENGTH_SHORT).show();
//        for (int i = 0; i < cartModelList.size(); i++) {
//
//            ShowImg_Price = cartModelList.get(i).getPrice();
//            SellerId = cartModelList.get(i).getUuid();
//            di_quantity = cartModelList.get(i).getQuantity();
//            Product_title = cartModelList.get(i).getProduct_title();
//
//
//
//
//            //OrderFromCart(ShowImg_Price, SellerId, di_quantity, paymentMethod, Product_title);
//
//
//        }
//        for (int i = 0; i <cartModelList.size(); i++) {
//            String   OrderCost = cartModelList.get(i).getPrice();
//            String   Seller_Id = cartModelList.get(i).getUuid();
//            String   quantity = cartModelList.get(i).getQuantity();
//            String   ProductTitle = cartModelList.get(i).getProduct_title();
//            Toast.makeText(this, "modelSize"+cartModelList.size(), Toast.LENGTH_SHORT).show();
//            Log.d(TAG, "OrderInfo: "+OrderCost+Seller_Id+quantity+ProductTitle);
//
//            Map<String, Object> map = new HashMap<>();
//            map.put("OrderId", timeStamp);
//            map.put("OrderTime", timeStamp);
//            map.put("OrderStatus", "In Progress");
//            map.put("OrderCost", "" + OrderCost);
//            map.put("OrderBy", "" + firebaseAuth.getCurrentUser().getUid());
//            map.put("orderTo", "" + Seller_Id);
//            //after for loop
//
//            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Users").child(Seller_Id).child("Orders");
//            databaseReference1.child(timeStamp).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void unused) {
//
//
//                    //order info added now add order items
//
//
//                    Map<String, Object> map2 = new HashMap<>();
//                    map2.put("quantity", quantity);
//                    map2.put("Payment_Method", paymentMethod);
//                    map2.put("Product_title", ProductTitle);
//                    map2.put("Customer_name", Customer_name);
//                    //map1.put("OrderId",timeStamp);
//                    map2.put("Phone_no", Phone_no);
//                    map2.put("Address", Address);
//                    databaseReference1.child(timeStamp).child("OrderedItems").setValue(map2);
//                    Toast.makeText(PlaceOrderActivity.this, "Order Place Successfully", Toast.LENGTH_SHORT).show();
//
//
//
//
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//
//                }
//            });
//            //calling order from cart method
////            OrderFromCart(OrderCost,Seller_Id,quantity,paymentMethod,ProductTitle);
//            Toast.makeText(this, "count"+j++, Toast.LENGTH_SHORT).show();
//
//        }

    }

    private void submitOrder(String paymentMethod) {

        for (int i = 0; i < cartModelList.size(); i++) {

            String ShowImg_Price = cartModelList.get(i).getPrice();
            String SellerId = cartModelList.get(i).getUuid();
            String di_quantity = cartModelList.get(i).getQuantity();
            String Product_title = cartModelList.get(i).getProduct_title();
            String TimeStamp = "" + System.currentTimeMillis();
            Map<String, Object> map = new HashMap<>();
            map.put("OrderId", TimeStamp);
            map.put("OrderTime", TimeStamp);
            map.put("OrderStatus", "In Progress");
            map.put("OrderCost", "" + ShowImg_Price);
            map.put("OrderBy", "" + firebaseAuth.getCurrentUser().getUid());
            map.put("orderTo", "" + SellerId);
            map.put("quantity", di_quantity);
            map.put("PaymentMethod", paymentMethod);
            map.put("Product_title", Product_title);
            map.put("Customer_name", Customer_name);
            map.put("Phone_no", Phone_no);
            map.put("Address", Address);
            databaseReference1 = FirebaseDatabase.getInstance().getReference("Users").child(SellerId).child("Orders");
            Log.d(TAG, "onDataChange: " + SellerId + "\n");

            databaseReference1.child(TimeStamp).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(PlaceOrderActivity.this, "Order Place successfully", Toast.LENGTH_SHORT).show();
                    cartModelList.clear();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }

    }

    private void doneOrder(String PaymentMethod) {


        Map<String, Object> map = new HashMap<>();
        map.put("OrderId", timeStamp);
        map.put("OrderTime", timeStamp);
        map.put("OrderStatus", "In Progress");
        map.put("OrderCost", "" + ShowImg_Price);
        map.put("OrderBy", "" + CustomerId);
        map.put("orderTo", "" + SellerId);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(SellerId).child("Orders");
        databaseReference.child(timeStamp).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Map<String, Object> map1 = new HashMap<>();
                map1.put("quantity", di_quantity);
                map1.put("Payment_Method", PaymentMethod);
                map1.put("Product_title", Product_title);
                map1.put("Customer_name", Customer_name);
                //map1.put("OrderId",timeStamp);
                map1.put("Phone_no", Phone_no);
                map1.put("Address", Address);

                databaseReference.child(timeStamp).child("OrderedItems").setValue(map1);
                Toast.makeText(PlaceOrderActivity.this, "Order Place Successfully", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PlaceOrderActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

//    private void OrderFromCart(String showImg_Price, String sellerId, String di_quantity, String paymentMethod, String product_title) {
//
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("OrderId", timeStamp);
//        map.put("OrderTime", timeStamp);
//        map.put("OrderStatus", "In Progress");
//        map.put("OrderCost", "" +showImg_Price );
//        map.put("OrderBy", "" + firebaseAuth.getCurrentUser().getUid());
//        map.put("orderTo", "" + sellerId);
//        //after for loop
//
//        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Users").child(SellerId).child("Orders");
//        databaseReference1.child(timeStamp).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//
//
//                //order info added now add order items
//
////                    String OrderCost = cartModelList.get(i).getPrice();
////                    String Seller_Id = cartModelList.get(i).getUuid();
////                    String quantity = cartModelList.get(i).getQuantity();
////                    String ProductTitle = cartModelList.get(i).getProduct_title();
//                    Map<String, Object> map2 = new HashMap<>();
//                    map2.put("quantity", di_quantity);
//                    map2.put("Payment_Method", paymentMethod);
//                    map2.put("Product_title", product_title);
//                    map2.put("Customer_name", Customer_name);
//                    map2.put("OrderId",timeStamp);
//                    map2.put("Phone_no", Phone_no);
//                    map2.put("Address", Address);
//                    databaseReference1.child(timeStamp).child("OrderedItems").setValue(map2);
//                    Toast.makeText(PlaceOrderActivity.this, "Order Place Successfully", Toast.LENGTH_SHORT).show();
//
//
//
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });
//
//    }
//

    public void onCheckboxClicked(View view) {

        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {

            case R.id.checkbox_COD:
                if (checked) {
                    PaymentMethod = checkbox_COD.getText().toString();
                    message = "Done";
                    Toast.makeText(this, "COD" + PaymentMethod, Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, "COD" + message, Toast.LENGTH_SHORT).show();
                }
                // Put some meat on the sandwich

        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult(requestCode, resultCode, data);
//        // check if the request code is same as what is passed  here it is 2
//        if (requestCode == 2) {
//            if (resultCode == Activity.RESULT_OK) {
//                message = data.getStringExtra("MESSAGE");
//                Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
}