package com.raju.onlinebookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import android.widget.ImageView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import static com.basgeekball.awesomevalidation.ValidationStyle.TEXT_INPUT_LAYOUT;

public class AfterClickOnBookInModify extends AppCompatActivity {

    ImageView bookkimage;
    private TextInputLayout bookkname,bookauthor,bookkid,booksubject,bookprice;
    private Button bookupdate,delete_btn;
    private ProgressDialog loadingbar;
    private String productId="";
    private DatabaseReference productref;
    private AwesomeValidation awesomeValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_click_on_book_in_modify);
        bookkname = findViewById(R.id.bkname);
        bookauthor =findViewById(R.id.bkauthor);
        bookkid =findViewById(R.id.bkid);
        loadingbar=new ProgressDialog(this);
        booksubject = findViewById(R.id.bksubject);
        bookprice = findViewById(R.id.bkprice);
        bookupdate =findViewById(R.id.bkupdate);
        productId=getIntent().getStringExtra("bkid");
        bookkimage=findViewById(R.id.bkimage);
        delete_btn=findViewById(R.id.deletebtn);
        awesomeValidation = new AwesomeValidation(TEXT_INPUT_LAYOUT);
        awesomeValidation.addValidation(this,R.id.bkauthor, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this,R.id.bksubject, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.subname);
        awesomeValidation.addValidation(this,R.id.bkname, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.bkname);
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingbar.setTitle("Book Delete");
                loadingbar.setMessage("Please wait,while we are deleteing this Book");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();
                productref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        loadingbar.dismiss();
                        StyleableToast.makeText(AfterClickOnBookInModify.this,"Book Deleted successfully",R.style.exampleToast).show();
                        //Toast.makeText(AfterClickOnBookInModify.this, "Book Deleted successfully", Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(AfterClickOnBookInModify.this,Admin_dashboard.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
        productref= FirebaseDatabase.getInstance().getReference().child("Books").child(productId);
        displayinfo();
        bookupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingbar.setTitle("Book Update");
                loadingbar.setMessage("Please wait,while we are updating your changes");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();
                infochanges();
            }
        });
    }
    private void infochanges(){
        String newname=bookkname.getEditText().getText().toString();
        String newauthor=bookauthor.getEditText().getText().toString();
        String newid=bookkid.getEditText().getText().toString();
        String newsubject=booksubject.getEditText().getText().toString();
        String newprice=bookprice.getEditText().getText().toString();
        if(TextUtils.isEmpty(newname)){
            StyleableToast.makeText(AfterClickOnBookInModify.this,"Please Enter Book name",R.style.exampleToast).show();
            loadingbar.dismiss();
        }
        else if(TextUtils.isEmpty(newauthor)){
            StyleableToast.makeText(AfterClickOnBookInModify.this,"Please Enter author name",R.style.exampleToast).show();
            loadingbar.dismiss();
        }
        else if(TextUtils.isEmpty(newid)){
            StyleableToast.makeText(AfterClickOnBookInModify.this,"Please Enter Book id",R.style.exampleToast).show();
            loadingbar.dismiss();
        }
        else if(TextUtils.isEmpty(newsubject)){
            StyleableToast.makeText(AfterClickOnBookInModify.this,"Please Enter subject",R.style.exampleToast).show();
            loadingbar.dismiss();
        }
        else if(TextUtils.isEmpty(newprice)){
            StyleableToast.makeText(AfterClickOnBookInModify.this,"Please Enter Price of Book",R.style.exampleToast).show();
            loadingbar.dismiss();
        }
        else if(!awesomeValidation.validate()){
            loadingbar.dismiss();
        }
        else if(!isNumeric(bookprice.getEditText().getText().toString())){
            loadingbar.dismiss();
            StyleableToast.makeText(AfterClickOnBookInModify.this,"Invalid Book price",R.style.exampleToast).show();
        }
        else{
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("bookproductid", productId);
            productMap.put("bookid", newid);
            productMap.put("bookauthor", newauthor);
            productMap.put("bookprice", newprice);
            productMap.put("booksubject",newsubject);
            productMap.put("bookname", newname);
            productref.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        loadingbar.dismiss();
                        StyleableToast.makeText(AfterClickOnBookInModify.this,"Product details update successfully",R.style.exampleToast).show();
                        Intent intent =new Intent(AfterClickOnBookInModify.this,Admin_dashboard.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }

    }
    public void displayinfo(){
        productref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String bname =  dataSnapshot.child("bookname").getValue().toString();
                    String bauthor =  dataSnapshot.child("bookauthor").getValue().toString();
                    String bid =  dataSnapshot.child("bookid").getValue().toString();
                    String bsub =  dataSnapshot.child("booksubject").getValue().toString();
                    String bprice =  dataSnapshot.child("bookprice").getValue().toString();
                    String bimage =  dataSnapshot.child("imageURL").getValue().toString();
                    bookkname.getEditText().setText(bname);
                    bookauthor.getEditText().setText(bauthor);
                    bookkid.getEditText().setText(bid);
                    booksubject.getEditText().setText(bsub);
                    bookprice.getEditText().setText(bprice);
                    Picasso.get().load(bimage).into(bookkimage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
