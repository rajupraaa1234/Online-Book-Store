package com.raju.onlinebookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static com.basgeekball.awesomevalidation.ValidationStyle.TEXT_INPUT_LAYOUT;

public class ConfirmOrder extends AppCompatActivity {

    private TextInputLayout name;
    private TextInputLayout address;
    private TextInputLayout city;
    private TextInputLayout phn;
    private AwesomeValidation awesomeValidation;
    Button same;
    Button confirm;
    String totalamount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        name = (TextInputLayout)findViewById(R.id.name);
        phn=(TextInputLayout)findViewById(R.id.phone);
        address=(TextInputLayout)findViewById(R.id.address);
        city = (TextInputLayout)findViewById(R.id.city);
        same = findViewById(R.id.same);
        confirm=findViewById(R.id.confirm);
        totalamount= getIntent().getStringExtra("total");
        same.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillalldata();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               onconfirm();
           }
       });
        awesomeValidation = new AwesomeValidation(TEXT_INPUT_LAYOUT);
        awesomeValidation.addValidation(this,R.id.name, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this,R.id.phone, "^[2-9]{2}[0-9]{8}$", R.string.numbererror);
    }
    public void fillalldata(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email1=user.getEmail();
        String temp="";
        for(int i=0;i<email1.length();i++){
            if(email1.charAt(i)=='.')
                break;
            else{
                temp = temp + String.valueOf(email1.charAt(i));
            }
        }

        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("User").child(temp);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                       String uname=dataSnapshot.child("Username").getValue().toString();
                       String add=dataSnapshot.child("Address").getValue().toString();
                       String ucity=dataSnapshot.child("city").getValue().toString();
                       String unumber=dataSnapshot.child("Moblienumber").getValue().toString();
                       name.getEditText().setText(uname);
                       address.getEditText().setText(add);
                       city.getEditText().setText(ucity);
                       phn.getEditText().setText(unumber);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void onconfirm(){
        if(TextUtils.isEmpty(name.getEditText().getText().toString())){
            StyleableToast.makeText(ConfirmOrder.this,"Please enter your name",R.style.exampleToast).show();
            //Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(address.getEditText().getText().toString())){
            StyleableToast.makeText(ConfirmOrder.this,"Please Enter shipping Address",R.style.exampleToast).show();
           // Toast.makeText(this, "Please Enter shipping Address", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(city.getEditText().getText().toString())){
            StyleableToast.makeText(ConfirmOrder.this,"Please Enter city",R.style.exampleToast).show();
            //Toast.makeText(this, "Please Enter city", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phn.getEditText().getText().toString())){
            StyleableToast.makeText(ConfirmOrder.this,"Please Enter yout phone number",R.style.exampleToast).show();
           // Toast.makeText(this, "Please Enter yout phone number", Toast.LENGTH_SHORT).show();
        }
        else if(!awesomeValidation.validate()){
            //StyleableToast.makeText(ConfirmOrder.this,"Invalid name",R.style.exampleToast).show();
        }
        else if(isNumeric(city.getEditText().getText().toString())){
            StyleableToast.makeText(ConfirmOrder.this,"Invalid city",R.style.exampleToast).show();
        }
        else if(!awesomeValidation.validate()){
            StyleableToast.makeText(ConfirmOrder.this,"Invalid phone number",R.style.exampleToast).show();
        }
        else if(isNumeric(address.getEditText().getText().toString())){
            StyleableToast.makeText(ConfirmOrder.this,"Invalid Address",R.style.exampleToast).show();
        }
        else{
              saveorderdatatodatabase();
        }
    }
    public void saveorderdatatodatabase(){

        Intent stu = new Intent(ConfirmOrder.this,Payment.class);
        stu.putExtra("totalamount",totalamount);
        stu.putExtra("name",name.getEditText().getText().toString());
        stu.putExtra("Address",address.getEditText().getText().toString());
        stu.putExtra("City",city.getEditText().getText().toString());
        stu.putExtra("number",phn.getEditText().getText().toString());
        startActivity(stu);


    }
    public void onBackPressed()
    {
        Intent stu = new Intent(ConfirmOrder.this,ViewCart.class);
        startActivity(stu);
        finish();
    }
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
