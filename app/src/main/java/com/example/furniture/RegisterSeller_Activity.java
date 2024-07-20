package com.example.furniture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterSeller_Activity extends AppCompatActivity {
    private Button registerSeller_btn;
    private EditText editText_registerSeller,editText_ShopName,editTextPhone_registerSeller,email_registerSeller,password_registerSeller,address_registerSeller,ConfirmPassword_registerSeller;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_seller);
        getSupportActionBar().hide();
        //initialize firebase instance
        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        //initialize views
        editText_registerSeller=findViewById(R.id.editText_registerSeller);
        editText_ShopName=findViewById(R.id.editText_ShopName);
        editTextPhone_registerSeller=findViewById(R.id.editTextPhone_registerSeller);
        email_registerSeller=findViewById(R.id.email_registerSeller);
        password_registerSeller=findViewById(R.id.password_registerSeller);
        address_registerSeller=findViewById(R.id.address_registerSeller);
        ConfirmPassword_registerSeller=findViewById(R.id.ConfirmPassword_registerSeller);
        registerSeller_btn=findViewById(R.id.registerSeller_btn);

        registerSeller_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InPutData();
                Intent intent=new Intent(RegisterSeller_Activity.this,MainSeller_Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    String username,phone,email,password,confirmPassword,address,ShopName;
    private void InPutData() {
        username=editText_registerSeller.getText().toString().trim();
        ShopName=editText_ShopName.getText().toString().trim();
        phone=editTextPhone_registerSeller.getText().toString().trim();
        email=email_registerSeller.getText().toString().trim();
        password=password_registerSeller.getText().toString().trim();
        address=address_registerSeller.getText().toString().trim();
        confirmPassword=ConfirmPassword_registerSeller.getText().toString().trim();

        if(TextUtils.isEmpty(username)){
            editText_registerSeller.setError("Please Enter name");
            editText_registerSeller.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(ShopName)){
            editText_ShopName.setError("Please Enter Shop Name");
            editText_ShopName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(phone)){
            editTextPhone_registerSeller.setError("Please Enter Phone Number");
            editTextPhone_registerSeller.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)){
            email_registerSeller.setError("Please Enter Email");
            email_registerSeller.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

            email_registerSeller.setError("Invalid Email Pattern");
            email_registerSeller.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)){
            password_registerSeller.setError("Please Enter password");
            password_registerSeller.requestFocus();
            return;
        }

        if (password.length()<6){
            password_registerSeller.setError("password must be 6 character long");
            password_registerSeller.requestFocus();
            return;

        }

        if (TextUtils.isEmpty(address)) {
            address_registerSeller.setError("please Enter the Address");
            address_registerSeller.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)){
            ConfirmPassword_registerSeller.setError("Please Enter the password again ");
            ConfirmPassword_registerSeller.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)){
            ConfirmPassword_registerSeller.setError("Password is not match");
            ConfirmPassword_registerSeller.requestFocus();
            return;
        }



        CreateAccount();



    }


    private void CreateAccount() {
        progressDialog.setMessage("Creating account");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                SaveUserInfo();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterSeller_Activity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void SaveUserInfo() {
        progressDialog.setMessage("Saving account info...");
        Map<String,Object> map=new HashMap<>();
        long timestamp=System.currentTimeMillis();
        map.put("uuid",""+firebaseAuth.getUid());
        map.put("name",""+username);
        map.put("shop_Name",""+ShopName);
        map.put("email",""+email);
        map.put("Phone",""+phone);
        map.put("Password",""+password);
        map.put("Address",""+address);
        map.put("timestamp",""+timestamp);
        map.put("Account_Type",""+"Seller");


        //save to db

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid()).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Intent intent=new Intent(RegisterSeller_Activity.this,MainSeller_Activity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterSeller_Activity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }
}