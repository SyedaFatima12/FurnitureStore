package com.example.furniture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login_Activity extends AppCompatActivity {
    private TextView noAccountTv;
    private Button loginBtn;
    private EditText Et_email ,Et_password;
    private String email,password;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        //init firebase auth
        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait....");
        user=firebaseAuth.getCurrentUser();
        progressDialog.setCanceledOnTouchOutside(true);

        Et_email=findViewById(R.id.Et_email);
        Et_password=findViewById(R.id.Et_password);
        noAccountTv=findViewById(R.id.noAccountTvText);
        loginBtn =findViewById(R.id.Login_button);

        noAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Login_Activity.this,RegisterUser_Activity.class);
                startActivity(intent);
            }
        });




        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Login();
            }
        });
    }

    private void Login() {
        email =Et_email.getText().toString().trim();
        password =Et_password.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            Et_email.setError("Enter the Email");
            Et_email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Et_email.setError("Invalid Email pattlern");
            Et_email.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)){
            Et_password.setError("Enter password");
            Et_password.requestFocus();
            return;
        }

        if (password.length()<6){
            Et_password.setError("Password must be 6 character long");
            Et_password.requestFocus();
            return;
        }



        progressDialog.setMessage("Login...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if (user!=null){
                    CheckUserType();
                    Toast.makeText(Login_Activity.this,"Login Successfully",Toast.LENGTH_SHORT).show();

                }

                progressDialog.dismiss();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login_Activity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void CheckUserType() {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");

            Toast.makeText(this, "user is present", Toast.LENGTH_SHORT).show();
            databaseReference.orderByChild("uuid").equalTo(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot ds :snapshot.getChildren()){
                        String accountType=""+ds.child("Account_Type").getValue();
                        Log.d("onDataChange" ,accountType);
                        if (accountType.equals("Seller")){
                            Intent intent=new Intent(Login_Activity.this,MainSeller_Activity.class);
                            startActivity(intent);
                            finish();
                        }else if (accountType.equals("User")){
                            Intent intent=new Intent(Login_Activity.this,MainUser_Activity.class);
                            startActivity(intent);
                            finish();

                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }
