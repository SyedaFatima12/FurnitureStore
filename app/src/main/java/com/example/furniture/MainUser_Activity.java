package com.example.furniture;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainUser_Activity extends AppCompatActivity {
    private static final String TAG = "MainUser_Activity";
    private FloatingActionButton addToCart;
    private ImageView user_logout;
    private LinearLayout ProductsLT;
    private RelativeLayout ordersRL;
    private TextView tabProducts;
    private TextView tabOrders;
    private RecyclerView orderRv, Parent_Rv;
    private ArrayList<ModelOrderUser> modelOrderUserList;
    private AdapterOrder_User adapterOrder_user;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser seller;
    private ArrayList<ParentModel> parentModelArrayList;
    private ArrayList<ChildModel> childModelArrayList;
    private ArrayList<ChildModel> chairArraylist;
    private ArrayList<ChildModel> TableArraylist;
    private ArrayList<ChildModel> CupboardsArraylist;
    private ArrayList<ChildModel> BedsArraylist;
    private ParentAdapter parentAdapter;
    private List<Map<String, Object>> productList;
    private ChildModel[] model = new ChildModel[5];
    private ArrayList[] arrayLists = new ArrayList[5];


    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);
        getSupportActionBar().hide();
        productList = new ArrayList<>();
        tabProducts = findViewById(R.id.tabProducts);
        tabOrders = findViewById(R.id.tabOrders);
        user_logout = findViewById(R.id.user_logout);
        ProductsLT = findViewById(R.id.ProductsLT);
        addToCart = findViewById(R.id.addToCart);
        ordersRL = findViewById(R.id.ordersLT);
        orderRv = findViewById(R.id.orderRv);
        Parent_Rv = findViewById(R.id.Parent_Rv);
        //initialize arraylist
        chairArraylist = new ArrayList<>();
        childModelArrayList = new ArrayList<>();
        TableArraylist = new ArrayList<>();
        CupboardsArraylist = new ArrayList<>();
        BedsArraylist = new ArrayList<>();
        parentModelArrayList = new ArrayList<>();
        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();


        ShowProductUI();
        checkUser();
        LoadOrders();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
//                    Map<String, Object> resultMap = (Map<String, Object>) ds.getValue();
//                    productList.add(resultMap);

                    String Product_Category = "" + ds.child("Product_Category").getValue();

                    Log.d(TAG, "onDataChange: " + Product_Category);
                    // Toast.makeText(MainUser_Activity.this, ""+Product_Category, Toast.LENGTH_SHORT).show();

                    if (Product_Category.equals("Sofa")) {
                        String Product_img = "" + ds.child("Product_img").getValue();
                        Log.d(TAG, "Product_img: " + Product_img);
                        ChildModel childModel = ds.getValue(ChildModel.class);

                        childModelArrayList.add(childModel);

                        Log.d(TAG, "Images: " + childModelArrayList);

                    } else if (Product_Category.equals("Chair")) {
                        ChildModel childModel = ds.getValue(ChildModel.class);
                        chairArraylist.add(childModel);

                    } else if (Product_Category.equals("Beds")) {
                        ChildModel childModel = ds.getValue(ChildModel.class);
                        BedsArraylist.add(childModel);
                    } else if (Product_Category.equals("Cupboards")) {
                        ChildModel childModel = ds.getValue(ChildModel.class);
                        CupboardsArraylist.add(childModel);
                    } else if (Product_Category.equals("Tables")) {
                        ChildModel childModel = ds.getValue(ChildModel.class);
                        TableArraylist.add(childModel);

                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //add data to parent list
        parentModelArrayList.add(new ParentModel("Sofa", childModelArrayList));
        parentModelArrayList.add(new ParentModel("Chair", chairArraylist));
        parentModelArrayList.add(new ParentModel("Beds", BedsArraylist));
        parentModelArrayList.add(new ParentModel("Tables", TableArraylist));
        parentModelArrayList.add(new ParentModel("Cupboards", CupboardsArraylist));

        parentAdapter = new ParentAdapter(this, parentModelArrayList);
        // Parent_Rv.setLayoutManager(new LinearLayoutManager(this));
        Parent_Rv.setAdapter(parentAdapter);
        parentAdapter.notifyDataSetChanged();


        tabProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProductUI();
            }
        });

        tabOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowOrderUI();
            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainUser_Activity.this, AddToCartActivity.class);
                startActivity(intent);
            }
        });

        //populate horizontal scroll view


//        //pop menu
//        final  PopupMenu popupMenu=new PopupMenu(MainUser_Activity.this,user_logout);
//        popupMenu.getMenu().add("Logout");
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//
//                if (item.equals("Logout")){
//
//
//
//                }
//                return true;
//            }
//        });

        user_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // popupMenu.show();
                ShowMenu(v);
            }
        });


    }


    private void LoadOrders() {
        //show user Order list
        // add items to list
//        modelOrderUserList.clear();


        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            String uid;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    uid = ds.getRef().getKey();
                    assert uid != null;


                }
                showOrder(uid);

            }

            private void showOrder(String uid) {

                modelOrderUserList = new ArrayList<>();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

                databaseReference.child(uid).child("Orders").orderByChild("OrderBy").equalTo(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        modelOrderUserList.clear();
                        if (snapshot.exists()) {
                            for (DataSnapshot ds1 : snapshot.getChildren()) {
                                ModelOrderUser modelOrderUser = ds1.getValue(ModelOrderUser.class);
                                modelOrderUserList.add(modelOrderUser);
                                // Toast.makeText(MainUser_Activity.this, "modelOrderUserList"+modelOrderUserList.size(), Toast.LENGTH_SHORT).show();

                            }
                        }
                        //setup Adapter
                        adapterOrder_user = new AdapterOrder_User(MainUser_Activity.this, modelOrderUserList);
                        //set Adapter to list
                        orderRv.setAdapter(adapterOrder_user);
                        // adapterOrder_user.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        modelOrderUserList.add(new ModelOrderUser("1", "28/06/2022", "inProgress", "6000", "user", "Seller", "Super Market"));
//        modelOrderUserList.add(new ModelOrderUser("2", "28/06/2022", "inProgress", "7000", "user", "Seller", "Super Market"));
//        modelOrderUserList.add(new ModelOrderUser("3", "28/06/2022", "inProgress", "8000", "user", "Seller", "Super Market"));
//        modelOrderUserList.add(new ModelOrderUser("4", "28/06/2022", "inProgress", "9000", "user", "Seller", "Super Market"));
//        //setup Adapter
//        adapterOrder_user = new AdapterOrder_User(MainUser_Activity.this, modelOrderUserList);
//        //set Adapter to list
//        orderRv.setAdapter(adapterOrder_user);
    }


    private void ShowMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(MainUser_Activity.this, v);
        popupMenu.getMenuInflater().inflate(R.menu.popmenu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.Logout) {

                    firebaseAuth.signOut();
                    Toast.makeText(MainUser_Activity.this, "Logout", Toast.LENGTH_SHORT).show();

                    // this listener will be called when there is change in firebase user session
                    FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
                        @Override
                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            if (firebaseUser == null) {
                                //auth state change user is null
                                //lunch login Activity

                                Intent intent = new Intent(MainUser_Activity.this, Login_Activity.class);
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

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            Intent intent = new Intent(MainUser_Activity.this, Login_Activity.class);
            startActivity(intent);
            finish();
        }

    }

    private void ShowOrderUI() {
        //show order Ui hide Product Ui

        ProductsLT.setVisibility(View.GONE);
        ordersRL.setVisibility(View.VISIBLE);
        tabOrders.setTextColor(getResources().getColor(R.color.mustard));
        tabOrders.setBackgroundResource(R.drawable.shape_rect04);

        tabProducts.setTextColor(getResources().getColor(R.color.grey_black));
        tabProducts.setBackgroundColor(getResources().getColor(android.R.color.transparent));

    }

    private void ShowProductUI() {
        //Show product uI hide Order Ui
        ProductsLT.setVisibility(View.VISIBLE);
        ordersRL.setVisibility(View.GONE);
        tabProducts.setTextColor(getResources().getColor(R.color.mustard));
        tabProducts.setBackgroundResource(R.drawable.shape_rect04);

        tabOrders.setTextColor(getResources().getColor(R.color.grey_black));
        tabOrders.setBackgroundColor(getResources().getColor(android.R.color.transparent));


    }
}