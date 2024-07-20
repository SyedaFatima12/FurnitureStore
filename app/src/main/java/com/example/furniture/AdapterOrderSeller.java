package com.example.furniture;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class AdapterOrderSeller extends  RecyclerView.Adapter<AdapterOrderSeller.HolderOrderSeller>{
    private Context context;
   private final ArrayList<ModelOrderSeller> modelOrderSellerArrayList;
   private FirebaseAuth firebaseAuth;
   private String ShopName;



    public AdapterOrderSeller(Context context, ArrayList<ModelOrderSeller> modelOrderSellerArrayList) {
        this.context = context;
        this.modelOrderSellerArrayList = modelOrderSellerArrayList;
    }

    @NonNull
    @Override
    public HolderOrderSeller onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       // firebaseAuth=FirebaseAuth.getInstance();

        View view= LayoutInflater.from(context).inflate(R.layout.row_order_seller,parent,false);
        return new HolderOrderSeller(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderOrderSeller holder, int position) {

        //get data
        ModelOrderSeller modelOrderSeller=modelOrderSellerArrayList.get(position);
        final  String orderId=modelOrderSeller.getOrderId();
         String OrderBy=modelOrderSeller.getOrderBy();
        String OrderStatus=modelOrderSeller.getOrderStatus();
        String OrderTime=modelOrderSeller.getOrderTime();
        String OrderCost=modelOrderSeller.getOrderCost();
        String OrderTo=modelOrderSeller.getOrderTo();
        String quantity=modelOrderSeller.getQuantity();
        String Address=modelOrderSeller.getAddress();
        String Product_title=modelOrderSeller.getProduct_title();

        //convert timestamp to date format
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(OrderTime));
        String dateformat= DateFormat.format("dd/MM/yyy",calendar).toString();

       //set data
        holder.orderIdTV_Seller.setText("OrderId: "+orderId);
        holder.amountTv_Seller.setText("Cost: "+OrderCost);
        holder.orderDateTv_Seller.setText(dateformat);
        holder.orderStatusTV_Seller.setText(OrderStatus);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get shop name
//                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");
//                databaseReference.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        ShopName= Objects.requireNonNull(snapshot.child("shop_Name").getValue()).toString().trim();
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

                Intent intent=new Intent(context,OrderDetailSellerActivity.class);
                intent.putExtra("orderId",orderId);
                intent.putExtra("OrderStatus",OrderStatus);
                intent.putExtra("OrderTime",dateformat);
                intent.putExtra("OrderCost",OrderCost);
               // intent.putExtra("quantity",quantity);
               // Toast.makeText(context, "Seller quantity"+quantity, Toast.LENGTH_SHORT).show();
                intent.putExtra("OrderTo",OrderTo);
                //intent.putExtra("Address",Address);
                intent.putExtra("ShopName",ShopName);
                //intent.putExtra("Product_title",Product_title);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return modelOrderSellerArrayList.size();
    }

    class HolderOrderSeller extends RecyclerView.ViewHolder{
  private TextView orderIdTV_Seller,orderDateTv_Seller,amountTv_Seller,orderStatusTV_Seller;

        public HolderOrderSeller(@NonNull View itemView) {
            super(itemView);
            orderIdTV_Seller=itemView.findViewById(R.id.orderIdSellerTv);
            orderDateTv_Seller=itemView.findViewById(R.id.dateIdSellerTv);
            amountTv_Seller=itemView.findViewById(R.id.amountSellerTv);
            orderStatusTV_Seller=itemView.findViewById(R.id.orderSellerStatus);







        }
    }
}
