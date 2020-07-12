package com.raju.onlinebookstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth =FirebaseAuth.getInstance();
        SystemClock.sleep(3000);
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentuser= firebaseAuth.getCurrentUser();
        if(currentuser==null)
        {
            Intent nextactivity =new Intent(MainActivity.this,Login.class);
            startActivity(nextactivity);
            finish();
        }
        else
        {
            Intent nextactivity =new Intent(MainActivity.this,DashBoard.class);
            startActivity(nextactivity);
            finish();
        }

    }
}
