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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    TextView login;
    EditText name;
    EditText gender;
    EditText phno;
    EditText email;
    EditText address;
    EditText city;
    private ProgressDialog loadingbar;
    EditText pass;
    EditText cpass;
    Button submit;
    private FirebaseFirestore firebaseFirestore;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        login=findViewById(R.id.register_now);
        name =findViewById(R.id.fullname);
        loadingbar=new ProgressDialog(this);
        gender=findViewById(R.id.genderinput);
        phno=findViewById(R.id.mobileNumber);
        email=findViewById(R.id.email);
        address=findViewById(R.id.address);
        city=findViewById(R.id.city);
        submit=findViewById(R.id.submit);
        pass=findViewById(R.id.password);
        cpass=findViewById(R.id.cpassword);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        submit.setEnabled(false);
        submit.setTextColor(Color.argb(50, 255, 255, 255));
        submit.setTextColor(Color.argb(50, 255, 255, 255));
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextactivity = new Intent(Register.this, Login.class);
                startActivity(nextactivity);
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               checkemailandpassword();
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
        name.addTextChangedListener(new TextWatcher() {
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
        gender.addTextChangedListener(new TextWatcher() {
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
        phno.addTextChangedListener(new TextWatcher() {
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
        address.addTextChangedListener(new TextWatcher() {
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
        city.addTextChangedListener(new TextWatcher() {
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
        cpass.addTextChangedListener(new TextWatcher() {
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

    }
    public void checkinput(){
        if(!TextUtils.isEmpty(name.getText())){
            if(!TextUtils.isEmpty(gender.getText())){
                if(!TextUtils.isEmpty(phno.getText())){
                    if(!TextUtils.isEmpty(email.getText())){
                        if(!TextUtils.isEmpty(address.getText())){
                            if(!TextUtils.isEmpty(city.getText())){
                                if(!TextUtils.isEmpty(pass.getText()) && pass.length()>=8){
                                    if(!TextUtils.isEmpty(cpass.getText()) && cpass.length()>=8){
                                        submit.setEnabled(true);
                                        submit.setTextColor(Color.rgb(255, 255, 255));
                                    }
                                    else{
                                        submit.setEnabled(false);
                                        submit.setTextColor(Color.argb(50, 255, 255, 255));
                                    }

                                }
                                else{
                                    submit.setEnabled(false);
                                    submit.setTextColor(Color.argb(50, 255, 255, 255));
                                }
                            }
                            else{
                                submit.setEnabled(false);
                                submit.setTextColor(Color.argb(50, 255, 255, 255));
                            }
                        }
                        else{
                            submit.setEnabled(false);
                            submit.setTextColor(Color.argb(50, 255, 255, 255));
                        }

                    }
                    else{
                        submit.setEnabled(false);
                        submit.setTextColor(Color.argb(50, 255, 255, 255));
                    }
                }
                else{
                    submit.setEnabled(false);
                    submit.setTextColor(Color.argb(50, 255, 255, 255));
                }

            }
            else{
                submit.setEnabled(false);
                submit.setTextColor(Color.argb(50, 255, 255, 255));
            }

        }
        else{
            submit.setEnabled(false);
            submit.setTextColor(Color.argb(50, 255, 255, 255));
        }
    }
    public void checkemailandpassword(){
        if (email.getText().toString().matches(emailPattern)) {
            if (pass.getText().toString().equals(cpass.getText().toString())) {
                loadingbar.setTitle("New User");
                loadingbar.setMessage("Please wait,while we are in registered as new user");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();
                submit.setEnabled(false);
                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            final DatabaseReference RootRef;
                            RootRef = FirebaseDatabase.getInstance().getReference();
                            RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Map<String,Object> userdata=new HashMap<>();
                                    userdata.put("Username",name.getText().toString());
                                    userdata.put("Moblienumber",phno.getText().toString());
                                    userdata.put("gender",gender.getText().toString());
                                    userdata.put("Address",address.getText().toString());
                                    userdata.put("city",city.getText().toString());
                                    userdata.put("email",email.getText().toString());
                                    String email1=email.getText().toString();
                                    String temp="";
                                    for(int i=0;i<email1.length();i++){
                                        if(email1.charAt(i)=='.')
                                            break;
                                        else{
                                          temp = temp + String.valueOf(email1.charAt(i));
                                        }
                                    }

                                    if(!(dataSnapshot.child("User").child(temp).exists())){
                                        RootRef.child("User").child(temp).updateChildren(userdata)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            StyleableToast.makeText(Register.this,"Congratulations, your account has been created.",R.style.exampleToast).show();
                                                            //Toast.makeText(Register.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                                                            loadingbar.dismiss();
                                                            Intent intent = new Intent(Register.this, Login.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                        else{
                                                            loadingbar.dismiss();
                                                            StyleableToast.makeText(Register.this,"Network Error: Please try again after some time...",R.style.exampleToast).show();
                                                            //Toast.makeText(Register.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                    else{
                                        loadingbar.dismiss();
                                        StyleableToast.makeText(Register.this,"This email id already registered...",R.style.exampleToast).show();
                                        //Toast.makeText(Register.this, "This email id already registered...", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        } else {
                            loadingbar.dismiss();
                            submit.setEnabled(false);
                            submit.setTextColor(Color.argb(50, 255, 255, 255));
                            String error = task.getException().getMessage();
                            StyleableToast.makeText(Register.this,error,R.style.exampleToast).show();
                            //Toast.makeText(Register.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                cpass.setError("Password doesn't matched");
            }

        }
        else
        {
            email.setError("Invalid Email!!!");
        }
    }

}
