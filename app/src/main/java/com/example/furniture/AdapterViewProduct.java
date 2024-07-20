package com.example.furniture;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterViewProduct extends RecyclerView.Adapter<AdapterViewProduct.HolderViewProduct> {
    private Context context;
    private ArrayList<ViewProductModel>  viewProductModelArrayList;

    public AdapterViewProduct(Context context, ArrayList<ViewProductModel> viewProductModelArrayList) {
        this.context = context;
        this.viewProductModelArrayList = viewProductModelArrayList;
    }

    @NonNull
    @Override
    public HolderViewProduct onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate Layout
       View view= LayoutInflater.from(context).inflate(R.layout.row_product_seller,parent,false);
        return new HolderViewProduct(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderViewProduct holder, int position) {
        //get data
        ViewProductModel viewProductModel=viewProductModelArrayList.get(position);
        String product_icon=viewProductModel.getProduct_img();
         String tile=viewProductModel.getProduct_title();
         String description=viewProductModel.getProduct_Description();
         String Price=viewProductModel.getPrice();

        //set data

        try {
            Picasso.get().load(product_icon).placeholder(R.drawable.ic_shopping_cart).into(holder.product_icon);
        }catch (Exception e){
            holder.product_icon.setImageResource(R.drawable.ic_shopping_cart);
        }

        holder.quantity.setText("Description: "+description);
        holder.tile.setText("Title: "+tile);
        holder.OrignalPrice.setText("Price: "+Price+" RS");


    }

    @Override
    public int getItemCount() {
        return viewProductModelArrayList.size();
    }


   class HolderViewProduct extends RecyclerView.ViewHolder{
       private TextView  tile,quantity,OrignalPrice;
       private ImageView product_icon;
       public HolderViewProduct(@NonNull View itemView) {
           super(itemView);
           product_icon=itemView.findViewById(R.id.product_icon);
           tile=itemView.findViewById(R.id.tile);
           quantity=itemView.findViewById(R.id.quantity);
           OrignalPrice=itemView.findViewById(R.id.OrignalPrice);
       }
   }
}

