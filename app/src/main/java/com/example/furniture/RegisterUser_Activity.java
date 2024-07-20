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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterUser_Activity extends AppCompatActivity {
private Button registerUser_btn;
    private TextView registerSellerTv;
    private EditText editText_registerUser,editTextPhone_registerUser,email_registerUser,password_registerUser,address_registerUser,ConfirmPassword_registerUser;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        getSupportActionBar().hide();
        //get views
        registerUser_btn=findViewById(R.id.registerUser_btn);
        registerSellerTv=findViewById(R.id.registerSellerTv);
        editText_registerUser=findViewById(R.id.editText_registerUser);
        editTextPhone_registerUser=findViewById(R.id.editTextPhone_registerUser);
        email_registerUser=findViewById(R.id.email_registerUser);
        password_registerUser=findViewById(R.id.password_registerUser);
        address_registerUser=findViewById(R.id.address_registerUser);
        ConfirmPassword_registerUser=findViewById(R.id.ConfirmPassword_registerUser);
        //initialize firebase instance
        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait....");
        progressDialog.setCanceledOnTouchOutside(false);








        registerUser_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InPutData();
                
            }
        });

        registerSellerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterUser_Activity.this,RegisterSeller_Activity.class);
                startActivity(intent);

            }
        });



    }
    String username,phone,email,password,confirmPassword,address;
    private void InPutData() {
        username=editText_registerUser.getText().toString().trim();
        phone=editTextPhone_registerUser.getText().toString().trim();
        email=email_registerUser.getText().toString().trim();
        password=password_registerUser.getText().toString().trim();
        address=address_registerUser.getText().toString().trim();
        confirmPassword=ConfirmPassword_registerUser.getText().toString().trim();

        if(TextUtils.isEmpty(username)){
            editText_registerUser.setError("Please Enter name");
            editText_registerUser.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone)){
            editTextPhone_registerUser.setError("Please Enter Phone Number");
            editTextPhone_registerUser.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)){
            email_registerUser.setError("Please Enter Email");
            email_registerUser.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

            email_registerUser.setError("Invalid Email Pattern");
            email_registerUser.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)){
            password_registerUser.setError("Please Enter password");
            password_registerUser.requestFocus();
            return;
        }

        if (password.length()<6){
            password_registerUser.setError("password must be 6 character long");
            password_registerUser.requestFocus();
            return;

        }

        if (TextUtils.isEmpty(address)) {
            address_registerUser.setError("please Enter the Address");
            address_registerUser.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)){
            ConfirmPassword_registerUser.setError("Please Enter the password again ");
            ConfirmPassword_registerUser.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)){
            ConfirmPassword_registerUser.setError("Password is not match");
            ConfirmPassword_registerUser.requestFocus();
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
                Toast.makeText(RegisterUser_Activity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void SaveUserInfo() {
        progressDialog.setMessage("Saving account info...");
        Map<String,Object> map=new HashMap<>();
        long timestamp=System.currentTimeMillis();
        map.put("uuid",""+firebaseAuth.getUid());
        map.put("name",""+username);
        map.put("email",""+email);
        map.put("Phone",""+phone);
        map.put("Password",""+password);
        map.put("Address",""+address);
        map.put("timestamp",""+timestamp);
        map.put("Account_Type",""+"User");


        //save to db

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid()).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Intent intent=new Intent(RegisterUser_Activity.this,MainUser_Activity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            Toast.makeText(RegisterUser_Activity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }
}