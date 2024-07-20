package com.example.furniture;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddProduct_Activity extends AppCompatActivity {
    private ImageView profileIv_Seller;
    private EditText titleEt, descriptionEt, PriceEt;
    private Button add;
    private TextView cateogryTv;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    //permission constant
    public static final int CAMERA_REQUEST_CODE = 200;
    public static final int STORAGE_REQUEST_CODE = 300;
    //image pick constant
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;
    //private Uri img_uri = null;

//permission array

    private String[] camerapermission;
    private String[] storagepermission;

    private ActivityResultLauncher<String> launcher;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        getSupportActionBar().hide();

        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                imageUri = result;
                profileIv_Seller.setImageURI(result);
            }
        });

        profileIv_Seller = findViewById(R.id.profileIv_Seller);
        titleEt = findViewById(R.id.titleEt);
        descriptionEt = findViewById(R.id.descriptionEt);
        cateogryTv = findViewById(R.id.cateogryTv);
//        quantityEt = findViewById(R.id.quantityEt);
        PriceEt = findViewById(R.id.PriceEt);
        add = findViewById(R.id.add);
        //initializing firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");

        //taking permission from the user
        camerapermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagepermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        cateogryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddProduct_Activity.this);
                builder.setTitle("Category");
                builder.setItems(Constants.Product_Category, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String Category = Constants.Product_Category[which];
                        cateogryTv.setText(Category);

                    }
                }).show();
            }
        });


        profileIv_Seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher.launch("image/*");

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputData();

            }
        });


    }

    private String Product_title, Product_Description, Product_Category,
            price;

    private void InputData() {
        Product_title = titleEt.getText().toString().trim();
        Product_Description = descriptionEt.getText().toString().trim();
        Product_Category = cateogryTv.getText().toString();
        price = PriceEt.getText().toString().trim();


        if (TextUtils.isEmpty(Product_title)) {
            titleEt.setError("enter the title");
            titleEt.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(Product_Description)) {
            descriptionEt.setError("enter the description");
            descriptionEt.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(Product_Category)) {
            cateogryTv.setError("choose the category");
            cateogryTv.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(price)) {
            PriceEt.setError("enter the price");
            PriceEt.requestFocus();
            return;
        }

        AddProduct();


    }

    private void AddProduct() {
        progressDialog.setMessage("Adding Product");
        progressDialog.show();

        final String timeStamp = "" + System.currentTimeMillis();


        String FilePathAndName = "Product_images/" + "" + timeStamp;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(FilePathAndName);
        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //get url of uploaded image
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful()) ;
                Uri downloadImageUri = uriTask.getResult();
                Toast.makeText(AddProduct_Activity.this, "saving Product Info", Toast.LENGTH_SHORT).show();
                if (uriTask.isSuccessful()) {
                    //Set up data to save
                    Map<String, Object> map = new HashMap<>();
                    map.put("uuid", firebaseAuth.getUid());
                    map.put("Product_Id", timeStamp);
                    map.put("Product_Description", Product_Description);
                    map.put("Product_title", Product_title);
                    map.put("Product_Category", Product_Category);
                    map.put("price", price);
                    map.put("Product_img", downloadImageUri.toString());

                    //save to db
                    try {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products");
                        databaseReference.child(timeStamp).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.dismiss();
                                Toast.makeText(AddProduct_Activity.this, "Products Added", Toast.LENGTH_SHORT).show();
                                ClearData();


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(AddProduct_Activity.this, "Products Not Added", Toast.LENGTH_SHORT).show();

                            }
                        });

                    } catch (NullPointerException ignored) {
                        Toast.makeText(AddProduct_Activity.this, "Null Pinter" + ignored.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddProduct_Activity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void ClearData() {
        titleEt.setText("");
        descriptionEt.setText("");
        cateogryTv.setText("");
        PriceEt.setText("");
        profileIv_Seller.setImageResource(R.drawable.ic_baseline_image);
        imageUri = null;
    }


//    private void ShowImagePickDiaLog() {
//        String options[] = {"Camera", "Gallery"};
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Pick img");
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (which == 0) {
//                    //camera clicked
//                    if (CheckCameraPermission()) {
//                        //permission granted
//                        PickFromCamera();
//                    } else {
//                        requestCameraPermission();
//                    }
//                } else {
//                    if (CheckStoragePermission()) {
//                        //permission Granted
//                        PickFromGallery();
//                    } else {
//                        requestGalleryPermission();
//                    }
//                }
//
//            }
//        });
//    }
//
//    private void PickFromGallery() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
//    }
//
//    private void PickFromCamera() {
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(MediaStore.Images.Media.TITLE, "Temp image Title");
//        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp image description");
//        img_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, img_uri);
//        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
//
//
//
//    }
//
//    private void requestGalleryPermission() {
//        ActivityCompat.requestPermissions(this, storagepermission, STORAGE_REQUEST_CODE);
//    }
//
//    private boolean CheckStoragePermission() {
//        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                == (PackageManager.PERMISSION_GRANTED);
//        return result1;
//    }
//
//    private void requestCameraPermission() {
//
//        ActivityCompat.requestPermissions(this, camerapermission, CAMERA_REQUEST_CODE);
//
//    }
//
//    private boolean CheckCameraPermission() {
//        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//                == (PackageManager.PERMISSION_GRANTED);
//        boolean result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                == (PackageManager.PERMISSION_GRANTED);
//        return result1 && result2;
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        switch (requestCode) {
//            case CAMERA_REQUEST_CODE: {
//                if (grantResults.length > 0) {
//                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                    boolean StorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
//
//                    if (cameraAccepted && StorageAccepted) {
//                        //permission allowed
//                        PickFromCamera();
//                    } else {
//                        //permission denied
//
//                        Toast.makeText(this, "Camera permission is necessary", Toast.LENGTH_SHORT).show();
//
//                    }
//
//                }
//            }
//            break;
//            case STORAGE_REQUEST_CODE: {
//                if (grantResults.length > 0) {
//                    boolean StorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
//                    if (StorageAccepted) {
//                        //permission allowed
//                        PickFromGallery();
//                    } else {
//
//                        //permission denied
//
//                        Toast.makeText(this, "Camera permission is necessary", Toast.LENGTH_SHORT).show();
//
//
//                    }
//
//                }
//            }break;
//            default:
//                break;
//
//        }
//
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode==RESULT_OK){
//            if (requestCode==IMAGE_PICK_GALLERY_CODE){
//                img_uri=data.getData();
//                Picasso.get().load(img_uri).into(profileIv_Seller);
//            }else if (requestCode==IMAGE_PICK_CAMERA_CODE){
//                img_uri=data.getData();
//                Picasso.get().load(img_uri).into(profileIv_Seller);
//            }
//
//        }
//
//    }
}
