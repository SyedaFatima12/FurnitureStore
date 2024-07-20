package com.example.furniture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Splash extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth=FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        //getting full window
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if (user==null){
                    Toast.makeText(Splash.this, "handler: something went wrong", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(Splash.this,Login_Activity.class);
                    startActivity(intent);
                    finish();
                }else {
                    CheckUserType();
                }


            }
        },2000);

    }

    private void CheckUserType() {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        if (user != null){
            String currentUser = user.getUid();
            databaseReference.orderByChild("uuid").equalTo(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds :snapshot.getChildren()){
                      //  Toast.makeText(Splash.this, "snapShot: .....", Toast.LENGTH_SHORT).show();
                        String accountType=""+ds.child("Account_Type").getValue();
                        if (accountType.equals("Seller")){
                          //  Toast.makeText(Splash.this, "seller Not found", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(Splash.this,MainSeller_Activity.class);
                            startActivity(intent);
                            finish();
                        }else if (accountType.equals("User")){
                           // Toast.makeText(Splash.this, "user not found", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(Splash.this,MainUser_Activity.class);
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
}