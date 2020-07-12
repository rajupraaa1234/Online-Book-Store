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
import com.basgeekball.awesomevalidation.AwesomeValidation;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.HashMap;

import static com.basgeekball.awesomevalidation.ValidationStyle.TEXT_INPUT_LAYOUT;

public class Createadmin extends AppCompatActivity {

    EditText userid;
    EditText password;
    EditText username;
    Button create_btn;
    private AwesomeValidation awesomeValidation;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createadmin);
        userid=findViewById(R.id.userid);
        password=findViewById(R.id.password);
        username=findViewById(R.id.username);
        create_btn=findViewById(R.id.login_button);
        loadingbar=new ProgressDialog(this);
        create_btn.setEnabled(false);
        create_btn.setTextColor(Color.argb(50,255,255,255));
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.username, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        userid.addTextChangedListener(new TextWatcher() {
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
        username.addTextChangedListener(new TextWatcher() {
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
        password.addTextChangedListener(new TextWatcher() {
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
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createadmin();
            }
        });
    }
    public void checkinput(){

        if(!TextUtils.isEmpty(userid.getText()))
        {
            if(!TextUtils.isEmpty(userid.getText())){
            if(!TextUtils.isEmpty((password.getText())) && password.length()>=8)
            {
                if(!awesomeValidation.validate()) {
                }else {
                    create_btn.setEnabled(true);
                    create_btn.setTextColor(Color.rgb(255, 255, 255));
                }
            }
            else
              {
                create_btn.setEnabled(false);
                create_btn.setTextColor(Color.argb(50,255,255,255));
              }
            }
            else{
                create_btn.setEnabled(false);
                create_btn.setTextColor(Color.argb(50,255,255,255));
            }
        }
        else
        {
            create_btn.setEnabled(false);
            create_btn.setTextColor(Color.argb(50,255,255,255));
        }
    }
    public void createadmin(){
        loadingbar.setTitle("Create new Admin");
        loadingbar.setMessage("Please wait while we are in processing");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();
        final String user_id=userid.getText().toString();
        final String user_pass=password.getText().toString();
        final String user_name=username.getText().toString();
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Admin").child(user_id).exists())){
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("adminuser", user_id);
                    userdataMap.put("name", user_name);
                    userdataMap.put("password", user_pass);
                    RootRef.child("Admin").child(user_id).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        StyleableToast.makeText(Createadmin.this,"Congratulations, your account has been created.",R.style.exampleToast).show();
                                       // Toast.makeText(Createadmin.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                                        loadingbar.dismiss();
                                        Intent intent = new Intent(Createadmin.this, Admin_dashboard.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        loadingbar.dismiss();
                                        StyleableToast.makeText(Createadmin.this,"Network Error: Please try again after some time...",R.style.exampleToast).show();
                                        //Toast.makeText(Createadmin.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    StyleableToast.makeText(Createadmin.this,"This " + user_id + " already exists.",R.style.exampleToast).show();
                    //Toast.makeText(Createadmin.this, "This " + user_id + " already exists.", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();

                    //Toast.makeText(Createadmin.this, "Please try again using another admin userid.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Createadmin.this, Admin_dashboard.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
