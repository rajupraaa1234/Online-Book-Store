package com.raju.onlinebookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.raju.onlinebookstore.Models.Admin;

public class Adminlogin extends AppCompatActivity {
    Button userlogin;
    Button adminlogin;
    EditText email;
    EditText pass;
    private ProgressDialog loadingbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlogin);
        userlogin=findViewById(R.id.userlogin);
        adminlogin=findViewById(R.id.adminlogin);
        email=findViewById(R.id.email);
        pass=findViewById(R.id.password);
        loadingbar=new ProgressDialog(this);
        //databaseReference= FirebaseDatabase.getInstance().getReference();
        adminlogin.setEnabled(false);
        adminlogin.setTextColor(Color.argb(50,255,255,255));
        userlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextactivity = new Intent(Adminlogin.this, Login.class);
                startActivity(nextactivity);
                finish();
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkinput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkinput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        adminlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkemailpattern();
            }
        });
    }


    public void checkinput(){
        if(!TextUtils.isEmpty(email.getText()))
        {
            if(!TextUtils.isEmpty((pass.getText())) && pass.length()>=8) {
                String str = email.getText().toString();
                boolean flag = true;
                for (int i = 0; i < str.length(); i++) {
                    if (str.charAt(i) == '.' || str.charAt(i) == '/' || str.charAt(i) == '#' || str.charAt(i) == '[' || str.charAt(i) == ']' || str.charAt(i) == '$') {
                        flag = false;
                    }
                }
                if (flag) {
                    adminlogin.setEnabled(true);
                    adminlogin.setTextColor(Color.rgb(255, 255, 255));
                }
                else{
                    StyleableToast.makeText(Adminlogin.this,"Userid contain only alphabet or number",R.style.exampleToast).show();
                    //Toast.makeText(this, "Userid contain only alphabet or number", Toast.LENGTH_SHORT).show();
                }

            }
            else
            {
                adminlogin.setEnabled(false);
                adminlogin.setTextColor(Color.argb(50,255,255,255));
            }
        }
        else
        {
            adminlogin.setEnabled(false);
            adminlogin.setTextColor(Color.argb(50,255,255,255));
        }
    }
    public void checkemailpattern(){
        loadingbar.setTitle("Admin Loging");
        loadingbar.setMessage("Please wait while we are checking the credential");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();
        final DatabaseReference databaseReference;
        databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Admin").child(email.getText().toString()).exists()){
                    Admin admindata=dataSnapshot.child("Admin").child(email.getText().toString()).getValue(Admin.class);
                    if(admindata.getPassword().equals(pass.getText().toString())){
                        StyleableToast.makeText(Adminlogin.this,"Admin Logged in successfully",R.style.exampleToast).show();
                        //Toast.makeText(Adminlogin.this, "Admin Logged in successfully", Toast.LENGTH_SHORT).show();
                        loadingbar.dismiss();
                         Intent nextactivity =new Intent(Adminlogin.this,Admin_dashboard.class);
                         startActivity(nextactivity);
                         finish();
                    }
                    else{
                        StyleableToast.makeText(Adminlogin.this,"Password incorrect",R.style.exampleToast).show();
                       // Toast.makeText(Adminlogin.this, "Password incorrect", Toast.LENGTH_SHORT).show();
                        loadingbar.dismiss();
                    }

                }
                else{
                    StyleableToast.makeText(Adminlogin.this,"Invalid userId or password",R.style.exampleToast).show();
                    //Toast.makeText(Adminlogin.this, "Invalid userId or password", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
