package com.raju.onlinebookstore;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.muddzdev.styleabletoastlibrary.StyleableToast;

public class Admin_dashboard extends AppCompatActivity {
    Button logout;
    Button addbook;
    Button Modify,delete;
    Button create_btn,checkneworder;
    private ProgressDialog loadingbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        logout=findViewById(R.id.logout);
        addbook=findViewById(R.id.addbook);
        create_btn=findViewById(R.id.createadmin);
        checkneworder=findViewById(R.id.checkneworder);
        delete=findViewById(R.id.removebook);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextactivity =new Intent(Admin_dashboard.this,ModifyBook.class);
                startActivity(nextactivity);
            }
        });
        loadingbar=new ProgressDialog(this);
        Modify=findViewById(R.id.Modifyboook);
        Modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextactivity =new Intent(Admin_dashboard.this,ModifyBook.class);
                startActivity(nextactivity);
            }
        });
        checkneworder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextactivity =new Intent(Admin_dashboard.this,AdminneworderActivity.class);
                startActivity(nextactivity);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingbar.setTitle("Admin Logout");
                loadingbar.setMessage("Please wait while we are in processing");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();
                Intent nextactivity =new Intent(Admin_dashboard.this,Login.class);
                nextactivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(nextactivity);
                finish();
            }
        });
        addbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextactivity =new Intent(Admin_dashboard.this,Addbook.class);
                startActivity(nextactivity);

            }
        });
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextactivity =new Intent(Admin_dashboard.this,Createadmin.class);
                startActivity(nextactivity);
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        StyleableToast.makeText(Admin_dashboard.this,"you can't go back without logout",R.style.exampleToast).show();
        //Toast.makeText(this, "you can't go back without logout", Toast.LENGTH_SHORT).show();
    }

}
