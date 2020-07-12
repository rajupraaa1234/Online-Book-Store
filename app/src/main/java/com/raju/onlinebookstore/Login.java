package com.raju.onlinebookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import com.muddzdev.styleabletoastlibrary.StyleableToast;

public class Login extends AppCompatActivity {

    TextView reg;
    TextView forget;
    TextView admin;
    EditText email;
    EditText pass;
    private ProgressDialog loadingBar;
    String usertype="user";
    ProgressBar progressBar;
    Button login;
    FirebaseAuth firebaseAuth;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        reg=findViewById(R.id.register_now);
        email=findViewById(R.id.email);
        pass=findViewById(R.id.password);
        loadingBar=new ProgressDialog(this);
        admin=findViewById(R.id.admin_login);
        progressBar=findViewById(R.id.progressBar);
        login=findViewById(R.id.login_button);
        forget=findViewById(R.id.forgot_pass);
        firebaseAuth=FirebaseAuth.getInstance();
        login.setEnabled(false);
        login.setTextColor(Color.argb(50,255,255,255));
        login.setEnabled(false);
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
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextactivity = new Intent(Login.this, Register.class);
                startActivity(nextactivity);
                finish();
            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextactivity = new Intent(Login.this, ForgetPassword.class);
                startActivity(nextactivity);
                finish();
            }
        });
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stu = new Intent(Login.this,Adminlogin.class);
                startActivity(stu);
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkemailpattern();
            }
        });
    }
    public void checkinput(){
        if(!TextUtils.isEmpty(email.getText()))
        {
            if(!TextUtils.isEmpty((pass.getText())) && pass.length()>=8)
            {
                login.setEnabled(true);
                login.setTextColor(Color.rgb(255,255,255));
            }
            else
            {
                login.setEnabled(false);
                login.setTextColor(Color.argb(50,255,255,255));
            }
        }
        else
        {
            login.setEnabled(false);
            login.setTextColor(Color.argb(50,255,255,255));
        }
    }
    public void checkemailpattern(){
        if(email.getText().toString().matches(emailPattern))
        {
            loadingBar.setTitle("User Logging");
            loadingBar.setMessage("Dear User, please wait while we are checking your account");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            if(pass.length()>=8)
            {
//                progressBar.setVisibility(View.VISIBLE);
                login.setEnabled(false);
                login.setTextColor(Color.argb(50,255,255,255));
                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            loadingBar.dismiss();
                            Intent stu = new Intent(Login.this,DashBoard.class);
                            startActivity(stu);
                            finish();
                        }
                        else
                        {
                            loadingBar.dismiss();
                           // progressBar.setVisibility(View.INVISIBLE);
                            login.setEnabled(true);
                            login.setTextColor(Color.rgb(255,255,255));
                            String error=task.getException().getMessage();
                            StyleableToast.makeText(Login.this,error,R.style.exampleToast).show();
                            //Toast.makeText(Login.this,error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else
            {
                loadingBar.dismiss();
                StyleableToast.makeText(Login.this,"Incorrect email id or Password",R.style.exampleToast).show();
               // Toast.makeText(Login.this, "Incorrect email id or Password", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            loadingBar.dismiss();
            StyleableToast.makeText(Login.this,"Incorrect email id or Password",R.style.exampleToast).show();
           // Toast.makeText(Login.this, "Incorrect email id or Password", Toast.LENGTH_SHORT).show();
        }
    }
    public void onBackPressed()
    {
        finish();
        moveTaskToBack(true);
    }
}
