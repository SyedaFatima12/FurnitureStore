package com.example.furniture;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {
    private final Context context;
    private final ArrayList<CartModel> cartModelArrayList;
    private FirebaseAuth firebaseAuth;



    public CartAdapter(Context context, ArrayList<CartModel> cartModelArrayList) {
        this.context = context;
        this.cartModelArrayList = cartModelArrayList;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        firebaseAuth = FirebaseAuth.getInstance();
        View view = LayoutInflater.from(context).inflate(R.layout.cart_layout, parent, false);
        return new CartHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, int position) {
        CartModel cartModel = cartModelArrayList.get(position);
        String Price = cartModel.getPrice();
        String img = cartModel.getImg();
        String Product_Category = cartModel.getProduct_Category();
        String Product_title = cartModel.getProduct_title();
        String Product_Description = cartModel.getProduct_Description();
        String Product_Id = cartModel.getProduct_Id();
        String uuid = cartModel.getUuid();
        String quantity = cartModel.getQuantity();
        String id = cartModel.getId();
        String priceEach = cartModel.getEachPrice();


        holder.Product_Price.setText("Price: " + Price + "Rs");
        Picasso.get().load(Uri.parse(img)).placeholder(R.drawable.ic_baseline_image).into(holder.Product_img);
        holder.Product_title.setText("Title: " + Product_title);
        holder.Product_quantity.setText("quantity: " + quantity);

        holder.Cancel_Product.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                databaseReference.child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).child("Cart").child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(context, "Product delete Successfully", Toast.LENGTH_SHORT).show();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
                notifyItemRemoved(holder.getAbsoluteAdapterPosition());
                notifyItemChanged(holder.getAbsoluteAdapterPosition());
                notifyItemRangeChanged(holder.getAbsoluteAdapterPosition(),cartModelArrayList.size());
                notifyDataSetChanged();


            }
        });





    }

    @Override
    public int getItemCount() {
        return cartModelArrayList.size();
    }

    public static class CartHolder extends RecyclerView.ViewHolder {
        private TextView Product_title, Product_quantity, Product_Price;
        private ImageView Product_img, Cancel_Product;

        public CartHolder(@NonNull View itemView) {
            super(itemView);
            Product_title = itemView.findViewById(R.id.Product_title);
            Product_quantity = itemView.findViewById(R.id.Product_quantity);
            Product_Price = itemView.findViewById(R.id.Product_Price);
            Product_img = itemView.findViewById(R.id.Product_img);
            Cancel_Product = itemView.findViewById(R.id.Cancel_Product);


        }
    }

}
