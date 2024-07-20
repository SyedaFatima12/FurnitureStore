package com.example.furniture;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ChildHolder> {

    private Context context;
    private List<ChildModel> childModelList;

    public ChildAdapter(Context context, List<ChildModel> childModelList) {
        this.context = context;
        this.childModelList = childModelList;
    }

    @NonNull
    @Override
    public ChildHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.child_layout, parent, false);
        //view.getLayoutParams().width = (int) (getScreenWidth() /5); /// THIS LINE WILL DIVIDE OUR VIEW INTO NUMBERS OF PARTS


        return new ChildHolder(view);
    }

//    private int getScreenWidth() {
//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//
//        return size.x;
//    }

    @Override
    public void onBindViewHolder(@NonNull ChildHolder holder, int position) {
        //get data

        ChildModel childModel = childModelList.get(position);
        String ChildModel_Img = childModel.getProduct_img();
        String Price = childModel.getPrice();
        String Product_title = childModel.getProduct_title();
        String Product_Description = childModel.getProduct_Description();
        String Product_Category = childModel.getProduct_Category();
        String uuid=childModel.getUuid();
        String Product_Id=childModel.getProduct_Id();


        Picasso.get().load(Uri.parse(ChildModel_Img)).placeholder(R.drawable.ic_baseline_image).into(holder.Child_Img);

        holder.Child_Price.setText(Price);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowImage.class);
                intent.putExtra("img", childModelList.get(holder.getAdapterPosition()).getProduct_img());
                intent.putExtra("Price", childModelList.get(holder.getAdapterPosition()).getPrice());
                intent.putExtra("Product_title", childModelList.get(holder.getAdapterPosition()).getProduct_title());
                intent.putExtra("Product_Description", childModelList.get(holder.getAdapterPosition()).getProduct_Description());
                intent.putExtra("Product_Category", childModelList.get(holder.getAdapterPosition()).getProduct_Category());
                intent.putExtra("uuid", childModelList.get(holder.getAdapterPosition()).getUuid());
                intent.putExtra("Product_Id", childModelList.get(holder.getAdapterPosition()).getProduct_Id());
                //Toast.makeText(context, "child"+uuid, Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        });

    }
    private final  int limit=5;


    @Override
    public int getItemCount() {

        if (childModelList.size()>limit){
            return limit;

        }else {
            return childModelList.size();
        }
        //int limit = 25 Math.min(childModelList.size(), limit);
       // return childModelList.size();
    }

    public static class ChildHolder extends RecyclerView.ViewHolder {
        private ImageView Child_Img;
        private TextView Child_Price;

        public ChildHolder(@NonNull View itemView) {
            super(itemView);
           // handleShowView(itemView);
            Child_Img = itemView.findViewById(R.id.Child_Img);
            Child_Price = itemView.findViewById(R.id.Child_Price);

        }

//        private void handleShowView(View itemView) {
//            if (getAdapterPosition() > 5 + 1) {
//                itemView.setVisibility(View.GONE);
//                return;
//            }
//            itemView.setVisibility(View.VISIBLE);
//        }

    }



}
