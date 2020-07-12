package com.raju.onlinebookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {

    TextView loginnow;
    TextView regnow;
    EditText email;
    Button reset;
    private ImageView emailicon;
    private FirebaseAuth firebaseAuth;
    private TextView emailtext;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        reset=findViewById(R.id.reset);
        loginnow=findViewById(R.id.login_now);
        regnow=findViewById(R.id.register_now);
        emailicon=findViewById(R.id.forget_email_icon);
        emailtext=findViewById(R.id.forget_pass_icon_text);
        progressBar=findViewById(R.id.forget_pass_progress);
        email=findViewById(R.id.email);
        firebaseAuth=FirebaseAuth.getInstance();
        reset.setEnabled(false);
        reset.setTextColor(Color.argb(50,255,255,255));
        loginnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextactivity = new Intent(ForgetPassword.this, Login.class);
                startActivity(nextactivity);
                finish();
            }
        });
        regnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextactivity = new Intent(ForgetPassword.this, Register.class);
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
                checkmail();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailtext.setVisibility(View.GONE);
                emailicon.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                reset.setEnabled(false);
                reset.setTextColor(Color.argb(50,255,255,255));
                firebaseAuth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            progressBar.setVisibility(View.GONE);
                            String succ= "Email has been Sent successfully!!";
                            emailtext.setText(succ);
                            emailtext.setTextColor(getResources().getColor(R.color.colorwhite));
                            emailtext.setVisibility(View.VISIBLE);
                            reset.setEnabled(false);
                            reset.setTextColor(Color.argb(50,255,255,255));
                        }
                        else
                        {
                            String error=task.getException().getMessage();
                            progressBar.setVisibility(View.GONE);
                            emailtext.setText(error);
                            emailtext.setTextColor(getResources().getColor(R.color.colorwhite));
                            emailtext.setVisibility(View.VISIBLE);
                        }
                        reset.setEnabled(true);
                        reset.setTextColor(Color.rgb(255,255,255));
                    }
                });
            }
        });
    }
    private void checkmail()
    {
        if(TextUtils.isEmpty(email.getText()))
        {
            reset.setEnabled(false);
            reset.setTextColor(Color.argb(50,255,255,255));
        }
        else
        {
            reset.setEnabled(true);
            reset.setTextColor(Color.rgb(255,255,255));
        }
    }
}
