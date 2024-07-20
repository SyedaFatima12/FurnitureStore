package com.example.furniture;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ViewProduct_Activity extends AppCompatActivity {
    private RecyclerView ViewProductsRv;
    private ArrayList<ViewProductModel> viewProductModelArrayList;
    private  AdapterViewProduct adapterViewProduct;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
        getSupportActionBar().hide();

        ViewProductsRv=findViewById(R.id.ViewProductsRv);
        firebaseAuth=FirebaseAuth.getInstance();

        viewProductModelArrayList=new ArrayList<>();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Products");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                viewProductModelArrayList.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
                    String uid= Objects.requireNonNull(ds.child("uuid").getValue()).toString().trim();
                    //Toast.makeText(ViewProduct_Activity.this, "VID"+uid, Toast.LENGTH_SHORT).show();
                    if (uid.equals(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())){

                        //Toast.makeText(ViewProduct_Activity.this, "Data added to list", Toast.LENGTH_SHORT).show();

                        ViewProductModel viewProductModel=ds.getValue(ViewProductModel.class);
                        viewProductModelArrayList.add(viewProductModel);
                      //  Log.d(TAG, "onDataChange vProducts: "+viewProductModelArrayList);
                    }

                }

                adapterViewProduct=new AdapterViewProduct(ViewProduct_Activity.this,viewProductModelArrayList);
                //set Adapter to list
                try {
                    ViewProductsRv.setAdapter(adapterViewProduct);
                }catch (NullPointerException ignored){

                }

                adapterViewProduct.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//         viewProductModelArrayList=new ArrayList<>();
//        viewProductModelArrayList.add(new ViewProductModel("https://cdn1.vectorstock.com/i/1000x1000/10/45/velvet-sofa-in-living-room-modern-trendy-vector-31571045.jpg","chair","1","5000"));
//        viewProductModelArrayList.add(new ViewProductModel("https://cdn1.vectorstock.com/i/1000x1000/10/45/velvet-sofa-in-living-room-modern-trendy-vector-31571045.jpg","sofa","1","5000"));
//        viewProductModelArrayList.add(new ViewProductModel("","beds","1","5000"));
//        viewProductModelArrayList.add(new ViewProductModel("","cupboards","1","5000"));


    }
}