package com.example.furniture;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.ChildHolder> {

    private Context context;
    private List<ParentModel> parentModelList;

    public ParentAdapter(Context context, List<ParentModel> parentModelList) {
        this.context = context;
        this.parentModelList = parentModelList;
    }

    @NonNull
    @Override
    public ChildHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.parent_layout, parent, false);

        return new ChildHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ChildHolder holder, int position) {
        //get data
        ParentModel parentModel = parentModelList.get(position);
        String Category_title = parentModel.getParentItemTitle();
        holder.CategoryTitleTv.setText(Category_title);

        //set the child recycler view
        ChildAdapter childAdapter;
        childAdapter = new ChildAdapter(context, parentModelList.get(position).getChildModelList());
        // holder.Child_Rv.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        holder.Child_Rv.setAdapter(childAdapter);
        childAdapter.notifyDataSetChanged();

        holder.More_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,MoreActivity.class);
                intent.putExtra("Category_title",Category_title);
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return parentModelList.size();
    }

    public static class ChildHolder extends RecyclerView.ViewHolder {
        private RecyclerView Child_Rv;
        private TextView CategoryTitleTv;
        private Button More_Btn;

        public ChildHolder(@NonNull View itemView) {
            super(itemView);
            Child_Rv = itemView.findViewById(R.id.Child_Rv);
            CategoryTitleTv = itemView.findViewById(R.id.CategoryTitleTv);
            More_Btn = itemView.findViewById(R.id.More_Btn);


        }
    }
}
