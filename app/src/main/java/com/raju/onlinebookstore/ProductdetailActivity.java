package com.raju.onlinebookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.raju.onlinebookstore.Models.Bookclass;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductdetailActivity extends AppCompatActivity {

    ImageView imagepic;
    TextView bookname,bookauthor,bookprice,subject,id;
    ElegantNumberButton elegantNumberButton;
    FloatingActionButton cart;
    private ProgressDialog loadingbar;
    String bookid = "";
    String state="noraml";
    Button cartbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetail);
        bookid= getIntent().getStringExtra("bkid");
        imagepic=findViewById(R.id.bookphoto);
        bookname=findViewById(R.id.booknamedetail);
        bookauthor=findViewById(R.id.authornamedetail);
        cartbtn=findViewById(R.id.cart_btn);
        bookprice=findViewById(R.id.pricedetail);
        id=findViewById(R.id.iddetail);
        loadingbar=new ProgressDialog(this);
        subject=findViewById(R.id.subjectdetail);
        elegantNumberButton=(ElegantNumberButton)findViewById(R.id.number_btn);
        cart=findViewById(R.id.cart_detail);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextactivity2 = new Intent(ProductdetailActivity.this, ViewCart.class);
                startActivity(nextactivity2);
            }
        });
        getproductdetails(bookid);
        cartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state.equals("order shipped") || state.equals("order placed")){
                    StyleableToast.makeText(ProductdetailActivity.this,"You can purchase new book once your orders are shipped or confirm ",R.style.exampleToast).show();
                    //Toast.makeText(ProductdetailActivity.this, "You can purchase new book once your orders are shipped or confirm ", Toast.LENGTH_SHORT).show();
                }else{
                    addtocart();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkorderstate();
    }

    public void addtocart(){
        loadingbar.setTitle("Add to cart");
        loadingbar.setMessage("Please wait,while we are adding this book in your cart");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();
        String savecurrentdate,savecurrenttime;
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentdate=new SimpleDateFormat("MMMM , dd , YYYY");
        savecurrentdate = currentdate.format(calendar.getTime());

        SimpleDateFormat currenttime=new SimpleDateFormat("HH:mm:ss a");
        savecurrenttime = currenttime.format(calendar.getTime());

       final DatabaseReference cartlistref = FirebaseDatabase.getInstance().getReference().child("cart list");
        final HashMap<String,Object> cartmap =new HashMap<>();
        cartmap.put("bookproductid",bookid);
        cartmap.put("bookname",bookname.getText().toString());
        cartmap.put("bookauther",bookauthor.getText().toString());
        cartmap.put("bookprice",bookprice.getText().toString());
        cartmap.put("subect",subject.getText().toString());
        cartmap.put("quantity",elegantNumberButton.getNumber());
        cartmap.put("date",savecurrentdate);
        cartmap.put("time",savecurrenttime);
        cartmap.put("bookid",id.getText().toString());
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
        final String finalTemp = temp;
        cartlistref.child("User view").child(temp).child("product").child(bookid).updateChildren(cartmap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                 if(task.isSuccessful()){
                     cartlistref.child("Admin view").child(finalTemp).child("product").child(bookid)
                             .updateChildren(cartmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                             if(task.isSuccessful()){
                                   loadingbar.dismiss();
                                 StyleableToast.makeText(ProductdetailActivity.this,"Book added into cart successfully",R.style.exampleToast).show();
                                 //Toast.makeText(ProductdetailActivity.this, "Book added into cart successfully", Toast.LENGTH_SHORT).show();
                                 Intent nextactivity =new Intent(ProductdetailActivity.this,DashBoard.class);
                                 startActivity(nextactivity);
                                 finish();
                             }
                         }
                     });

                 }
            }
        });

    }

    private void getproductdetails(String bookid) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Books");
        databaseReference.child(bookid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  if(dataSnapshot.exists()){
                      Bookclass bookclass=dataSnapshot.getValue(Bookclass.class);
                      bookname.setText("Book name:" + bookclass.getBookname());
                      bookauthor.setText("Author name:" + bookclass.getBookauthor());
                      bookprice.setText(bookclass.getBookprice());
                      subject.setText("subject : " +bookclass.getBooksubject());
                      id.setText("Book id : " + bookclass.getBookid());
                      Picasso.get().load(bookclass.getImageURL()).into(imagepic);
                  }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void checkorderstate(){
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
        DatabaseReference orderref = FirebaseDatabase.getInstance().getReference().child("Order").child(temp);
        orderref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String state1= dataSnapshot.child("status").getValue().toString();
                    String username = dataSnapshot.child("name").getValue().toString();
                    if(state1.equals("shipped")){
                        state = "order shipped";
                    }
                    else if(state1.equals("not shipped")){
                        state = "order placed";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}



