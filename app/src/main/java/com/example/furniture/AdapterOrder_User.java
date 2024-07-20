package com.example.furniture;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class AdapterOrder_User extends RecyclerView.Adapter<AdapterOrder_User.HolderOrderUser> {
    private Context context;
    private ArrayList<ModelOrderUser> orderUserList;

    public AdapterOrder_User(Context context, ArrayList<ModelOrderUser> orderUserList) {
        this.context = context;
        this.orderUserList = orderUserList;
    }

    @NonNull
    @Override
    public HolderOrderUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate Layout
       View view= LayoutInflater.from(context).inflate(R.layout.row_order_user,parent,false);
        return new HolderOrderUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderOrderUser holder, int position) {
        //get data
        ModelOrderUser modelOrderUser=orderUserList.get(position);
        final  String orderId=modelOrderUser.getOrderId();
       // String OrderBy=modelOrderUser.getOrderBy();
         String OrderStatus=modelOrderUser.getOrderStatus();
         String OrderTime=modelOrderUser.getOrderTime();
         String OrderCost=modelOrderUser.getOrderCost();
         String ShopName=modelOrderUser.getShopName();
         String OrderTo=modelOrderUser.getOrderTo();
         String quantity=modelOrderUser.getQuantity();
         String Product_title=modelOrderUser.getProduct_title();
         String Address=modelOrderUser.getAddress();
         String Payment_Method=modelOrderUser.getPayment_Method();
        //convert timestamp to date format

        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(OrderTime));
        String dateformat= DateFormat.format("dd/MM/yyy",calendar).toString();

        //set data
        holder.orderTv.setText("OrderId: "+orderId);
        holder.amountTv.setText("RS: "+OrderCost);
        holder.orderStatus.setText(OrderStatus);
        holder.dateTv.setText(dateformat);
        holder.Shop_nameTv.setText(ShopName);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,OrderDetailActivity.class);
                intent.putExtra("orderId",orderId);
                intent.putExtra("OrderStatus",OrderStatus);
                intent.putExtra("OrderTime",OrderTime);
                intent.putExtra("OrderCost",OrderCost);
               // intent.putExtra("quantity",quantity);
                intent.putExtra("OrderTo",OrderTo);
//                intent.putExtra("Address",Address);
//                intent.putExtra("Product_title",Product_title);
//                intent.putExtra("Payment_Method",Payment_Method);
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return orderUserList.size();
    }


    static class HolderOrderUser extends RecyclerView.ViewHolder{

        private TextView orderTv,dateTv,Shop_nameTv,amountTv,orderStatus;

        public HolderOrderUser(@NonNull View itemView) {
            super(itemView);
            orderTv=itemView.findViewById(R.id.orderTv);
            dateTv=itemView.findViewById(R.id.dateTv);
            Shop_nameTv=itemView.findViewById(R.id.Shop_nameTv);
            amountTv=itemView.findViewById(R.id.amountTv);
            orderStatus=itemView.findViewById(R.id.orderStatus);


        }
    }
}

