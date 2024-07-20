package com.example.furniture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class CardFormActivity extends AppCompatActivity {
    private CardForm cardForm;
    private Button payment_button;
    private AlertDialog.Builder alertBuilder;
    private String SellerId,ShowImg_Price,di_quantity,Product_title,CustomerId,timeStamp,Customer_name,Phone_no,Address,PaymentMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_form);
        cardForm = findViewById(R.id.card_form);
        payment_button = findViewById(R.id.payment);


        SellerId=getIntent().getStringExtra("SellerId");
        CustomerId=getIntent().getStringExtra("CustomerId");
        ShowImg_Price=getIntent().getStringExtra("ShowImg_Price");
        di_quantity=getIntent().getStringExtra("di_quantity");
        Product_title=getIntent().getStringExtra("Product_title");
        Customer_name=getIntent().getStringExtra("Customer_name");
        Phone_no=getIntent().getStringExtra("Phone_no");
        Address=getIntent().getStringExtra("Address");
        PaymentMethod=getIntent().getStringExtra("Payment_Method");
        Toast.makeText(this, ""+PaymentMethod, Toast.LENGTH_SHORT).show();
        timeStamp=""+System.currentTimeMillis();
        CardForm cardForm = (CardForm) findViewById(R.id.card_form);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("SMS is required on this number")
                .actionLabel("Purchase")
                .setup(CardFormActivity.this);
        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        payment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardForm.isValid()) {
                    alertBuilder = new AlertDialog.Builder(CardFormActivity.this);
                    alertBuilder.setTitle("Confirm before purchase");
                    alertBuilder.setMessage("Card number: " + cardForm.getCardNumber() + "\n" +
                            "Card expiry date: " + cardForm.getExpirationDateEditText().getText().toString() + "\n" +
                            "Card CVV: " + cardForm.getCvv() + "\n" +
                            "Postal code: " + cardForm.getPostalCode() + "\n" +
                            "Phone number: " + cardForm.getMobileNumber());

                    alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                            Intent intent=new Intent();
                            intent.putExtra("Card_number",cardForm.getCardNumber());
                            intent.putExtra("CVC_expDate",cardForm.getExpirationDateEditText().getText().toString());
                            intent.putExtra("PayMethod",PaymentMethod);
                            intent.putExtra("Message","Done");
                            setResult(78,intent);
                            CardFormActivity.super.onBackPressed();

                            Toast.makeText(CardFormActivity.this, "Thank you for purchase", Toast.LENGTH_LONG).show();

                        }
                    });
                    alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();

                } else {
                    Toast.makeText(CardFormActivity.this, "Please complete the form", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}