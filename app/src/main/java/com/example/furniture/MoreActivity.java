package com.example.furniture;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MoreActivity extends AppCompatActivity {
    private String Category_title,filter_by;
    private Chip nameChip, PriceChip, ascending_Chip, descendingChip;
    private ChipGroup chip_group, chip_group2;
    private RecyclerView moreItem_Rv;
    private FirebaseAuth firebaseAuth;
    private ArrayList<Model_moreItem> modelMoreItemArrayList;
    private MoreItemAdapter moreItemAdapter;
    List<String> selectedChipData=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        //nameChip = findViewById(R.id.nameChip);
        //PriceChip = findViewById(R.id.PriceChip);
        moreItem_Rv = findViewById(R.id.moreItem_Rv);
        chip_group2 = findViewById(R.id.Chip_group2);
        chip_group = findViewById(R.id.Chip_group);
        //moreItem_Rv = findViewById(R.id.moreItem_Rv);
        firebaseAuth = FirebaseAuth.getInstance();

//        ChildModel childModel = ds.getValue(ChildModel.class);
//        chairArraylist.add(childModel);

        modelMoreItemArrayList=new ArrayList<>();


            Category_title = getIntent().getStringExtra("Category_title");
            Toast.makeText(this, "Category_title" + Category_title, Toast.LENGTH_SHORT).show();



        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        databaseReference.addValueEventListener(new ValueEventListener() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelMoreItemArrayList.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Model_moreItem item = ds.getValue(Model_moreItem.class);
                    if (item != null){

                        String item_category = item.getProduct_Category();
                        if (Category_title.equals("Sofa") && item_category.equals("Sofa")) {

                            String Product_img = "" + ds.child("Product_img").getValue();
                            String Price=""+ds.child("price").getValue();


                            Log.d("Product_img", "" + Product_img);

                            Model_moreItem model_moreItem = ds.getValue(Model_moreItem.class);
                            modelMoreItemArrayList.add(model_moreItem);

                        } else if (Category_title.equals("Chair") && item_category.equals("Chair")){
                            Model_moreItem model_moreItem = ds.getValue(Model_moreItem.class);
                            modelMoreItemArrayList.add(model_moreItem);
                        }else if (Category_title.equals("Beds") && item_category.equals("Beds")){
                            Model_moreItem model_moreItem = ds.getValue(Model_moreItem.class);
                            modelMoreItemArrayList.add(model_moreItem);
                        }else if (Category_title.equals("Cupboards") && item_category.equals("Cupboards")){
                            Model_moreItem model_moreItem = ds.getValue(Model_moreItem.class);
                            modelMoreItemArrayList.add(model_moreItem);
                        }else if(Category_title.equals("Tables") && item_category.equals("Tables")){
                            Model_moreItem model_moreItem = ds.getValue(Model_moreItem.class);
                            modelMoreItemArrayList.add(model_moreItem);

                        }

                    }

                }
                //set up Adapter
                moreItemAdapter=new MoreItemAdapter(MoreActivity.this,modelMoreItemArrayList);
                //set Adapter to list
                moreItem_Rv.setAdapter(moreItemAdapter);
               moreItemAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



//        CompoundButton.OnCheckedChangeListener checkedChangeListener=new CompoundButton.OnCheckedChangeListener(){
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                if (isChecked){
//                    selectedChipData.add(buttonView.getText().toString());
//                   // FilterProducts();
//                }else{
//                    selectedChipData.remove(buttonView.getText().toString());
//                }
//
//            }
//        };
//
//        nameChip.setOnCheckedChangeListener(checkedChangeListener);
//        PriceChip.setOnCheckedChangeListener(checkedChangeListener);

//        nameChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (buttonView.isChecked()) {
//                    name_Chip = buttonView.getText().toString();
//                    Toast.makeText(MoreActivity.this, "" + name_Chip, Toast.LENGTH_SHORT).show();
//                }
//
//
//            }
//        });
//
//        PriceChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                if (buttonView.isChecked()) {
//                    Price_Chip = buttonView.getText().toString();
//                    Toast.makeText(MoreActivity.this, "" + Price_Chip, Toast.LENGTH_SHORT).show();
////                    FilterProducts();
//                }
//
//
//            }
//        });
        //singleSelection(chip_group2);




        chip_group.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, int checkedId) {
                Chip chip=group.findViewById(checkedId);
                if (chip.isChecked()){

                   filter_by=chip.getText().toString();
                    Toast.makeText(MoreActivity.this, ""+filter_by, Toast.LENGTH_SHORT).show();

                }

            }
        });
        chip_group2.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, int checkedId) {
                Chip chip = group.findViewById(checkedId);
                if (chip.isChecked()) {


                    String  Order_by=chip.getText().toString();
                    Toast.makeText(MoreActivity.this, "Order_By"+Order_by, Toast.LENGTH_SHORT).show();

                    FilterProducts(filter_by,Order_by);
                }


            }
        });


    }

    public void FilterProducts(String Filter_by,String  Order_by){
        if (Filter_by.contains("Price") && Order_by.contains("ascending") ){
            Toast.makeText(this, "list"+Filter_by+Order_by, Toast.LENGTH_SHORT).show();

            Collections.sort(modelMoreItemArrayList, new Comparator<Model_moreItem>() {
                @Override
                public int compare(Model_moreItem o1, Model_moreItem o2) {
                    return o1.getPrice().compareTo(o2.getPrice());
                }
            });

        }else if (Filter_by.contains("Price") && Order_by.contains("descending") ){
            Collections.sort(modelMoreItemArrayList, new Comparator<Model_moreItem>() {
                @Override
                public int compare(Model_moreItem o1, Model_moreItem o2) {
                    return o2.getPrice().compareTo(o1.getPrice());
                }
            });
        }else if (Filter_by.contains("name") && Order_by.contains("ascending")){
            Collections.sort(modelMoreItemArrayList, new Comparator<Model_moreItem>() {
                @Override
                public int compare(Model_moreItem o1, Model_moreItem o2) {
                    return o1.getProduct_title().compareToIgnoreCase(o2.getProduct_title());
                }
            });

        }else if(Filter_by.contains("name") && Order_by.contains("descending")){
            Collections.sort(modelMoreItemArrayList, new Comparator<Model_moreItem>() {
                @Override
                public int compare(Model_moreItem o1, Model_moreItem o2) {
                    return o2.getProduct_title().compareToIgnoreCase(o1.getProduct_title());
                }
            });
        }

        moreItemAdapter.notifyDataSetChanged();

    }




//    public void FilterProducts(){
//
////        List<Integer> ids = chip_group2.getCheckedChipIds();
////        List<Integer> a=chip_group.getCheckedChipIds();
////        for (Integer id:ids){
////            Chip chip = chip_group2.findViewById(id);
////            if (chip.isChecked()){
////                Toast.makeText(MoreActivity.this, "" + chip.getText().toString(), Toast.LENGTH_SHORT).show();
////
////               // Order_by=chip.getText().toString();
////                selectedChipData.add(chip.getText().toString());
////            }
////        }
////        for (Integer b:a){
////            Chip chip = chip_group.findViewById(b);
////            if (chip.isChecked()){
////                Toast.makeText(MoreActivity.this, "" + chip.getText().toString(), Toast.LENGTH_SHORT).show();
////
////                // Order_by=chip.getText().toString();
////                selectedChipData.add(chip.getText().toString());
////            }
////        }
//
//
//
//        for (String str: selectedChipData){
//            Toast.makeText(this, "Filter"+str, Toast.LENGTH_SHORT).show();
//
//
//            if (str.contains("Price") && str.contains("ascending")){
//                Toast.makeText(this, "Filter2"+str, Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "FilterProducts: "+str);
//                Collections.sort(modelMoreItemArrayList, new Comparator<Model_moreItem>() {
//                @Override
//                public int compare(Model_moreItem o1, Model_moreItem o2) {
//                    return o1.getProduct_title().compareToIgnoreCase(o2.getProduct_title());
//                }
//            });
//            }else{
//                Toast.makeText(this, "Error else"+str, Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "FilterProduct"+selectedChipData.toString());
//
//            }
//        }
//        //Price_Chip.contains()descendingChip
////        if ( Price_Chip.equals("Price") && Order_by.equals("ascending")){
////            Collections.sort(modelMoreItemArrayList, new Comparator<Model_moreItem>() {
////                @Override
////                public int compare(Model_moreItem o1, Model_moreItem o2) {
////                    return o1.getProduct_title().compareToIgnoreCase(o2.getProduct_title());
////                }
////            });
////
////        }else if(Price_Chip.equals("Price") && Order_by.equals("descendingChip")){
////            Collections.reverse(modelMoreItemArrayList);
////
////        }
//
//       moreItemAdapter.notifyDataSetChanged();
//
//    }
//    public void singleSelection(View v){
//        int id=chip_group2.getCheckedChipId();
//        Chip chip=findViewById(id);
//        if (chip.isChecked()) {
//            Toast.makeText(MoreActivity.this, "" + chip.getText().toString(), Toast.LENGTH_SHORT).show();
//
//            Order_by=chip.getText().toString();
//            selectedChipData.add(chip.getText().toString());
//            FilterProducts();
//        }else{
//            selectedChipData.remove(chip.getText().toString());
//        }
//
//
//
//
//
//
//    }
}