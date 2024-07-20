package com.example.furniture;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MoreItemAdapter extends RecyclerView.Adapter<MoreItemAdapter.MoreItemHolder> {
    Context context;
    ArrayList<Model_moreItem> modelMoreItemArrayList;

    public MoreItemAdapter(Context context, ArrayList<Model_moreItem> modelMoreItemArrayList) {
        this.context = context;
        this.modelMoreItemArrayList = modelMoreItemArrayList;
    }


    @NonNull
    @Override
    public MoreItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.more_item_layout,parent,false);

        return new MoreItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoreItemHolder holder, int position) {
        Model_moreItem model_moreItem=modelMoreItemArrayList.get(position);
        String Product_img=model_moreItem.getProduct_img();
        String price=model_moreItem.getPrice();
        String uuid=model_moreItem.getUuid();
        String Product_Id=model_moreItem.getProduct_Id();
        String Product_Description=model_moreItem.getProduct_Description();
        String Product_title=model_moreItem.getProduct_title();
        String Product_Category=model_moreItem.getProduct_Category();

        Picasso.get().load(Uri.parse(Product_img)).placeholder(R.drawable.ic_image).into(holder.more_item_Img);
        holder.more_item_Price.setText(price+" Rs");
        holder.more_item_title.setText("Title :"+" "+Product_title);
        holder.more_item_description.setText("description :"+" "+Product_Description);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ShowImage.class);
                intent.putExtra("img",Product_img);
                intent.putExtra("Price",price);
                intent.putExtra("Product_title",Product_title);
                intent.putExtra("Product_Description",Product_Description);
                intent.putExtra("Product_Id",Product_Id);
                intent.putExtra("uuid",uuid);
                //Toast.makeText(context, "More"+uuid, Toast.LENGTH_SHORT).show();

                context.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {

        return modelMoreItemArrayList.size();
    }

    public class MoreItemHolder extends RecyclerView.ViewHolder{
       private ImageView more_item_Img ;
       private TextView  more_item_Price,more_item_title,more_item_description;


        public MoreItemHolder(@NonNull View itemView) {
            super(itemView);
            more_item_Img=itemView.findViewById(R.id.more_item_Img);
            more_item_Price=itemView.findViewById(R.id.more_item_Price);
            more_item_title=itemView.findViewById(R.id.more_item_title);
            more_item_description=itemView.findViewById(R.id.more_item_description);


        }
    }
}
