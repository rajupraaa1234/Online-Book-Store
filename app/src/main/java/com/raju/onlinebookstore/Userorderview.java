package com.raju.onlinebookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.raju.onlinebookstore.Models.Cart;
import com.raju.onlinebookstore.view_book_product.CartViewHolder;

import java.util.HashMap;

public class Userorderview extends AppCompatActivity {

    private RecyclerView productlist;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartlist;
    private String userId="";
    private String str1="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userorderview);
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
        userId=temp;
        productlist = findViewById(R.id.product_list);
        productlist.setHasFixedSize(true);
        layoutManager =new LinearLayoutManager(this);
        productlist.setLayoutManager(layoutManager);
        cartlist = FirebaseDatabase.getInstance().getReference()
                .child("cart list")
                .child("Admin view").child(userId).child("product");

    }


    @Override
    protected void onStart() {
        super.onStart();
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
        final DatabaseReference orderlist = FirebaseDatabase.getInstance().getReference().child("Order").child(temp);
        orderlist.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  if(dataSnapshot.exists()){
                      haveaorder();
                  }else{
                      StyleableToast.makeText(Userorderview.this,"You don't have any new order",R.style.exampleToast).show();
                  }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    void haveaorder(){
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
        final DatabaseReference cartlink = FirebaseDatabase.getInstance().getReference().child("cart list");
        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartlist,Cart.class)
                        .build();
        final String finalTemp = temp;
        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter =new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {
                holder.quantity.setText("Quantity : " + model.getQuantity());
                holder.bookprice.setText(model.getBookprice() + "/-");
                holder.bookname.setText( model.getBookname());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Cancel Order",
                                        "Back"
                                };
                        AlertDialog.Builder builder =new AlertDialog.Builder(Userorderview.this);
                        builder.setTitle("Order Options");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0){
                                    updateorder(model.getBookprice().toString());
                                    cartlink.child("Admin view").child(finalTemp)
                                            .child("product")
                                            .child(model.getBookproductid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        StyleableToast.makeText(Userorderview.this,"Order Cancel successfully",R.style.exampleToast).show();
                                                        Intent nextactivity =new Intent(Userorderview.this,DashBoard.class);
                                                        startActivity(nextactivity);
                                                        finish();
                                                    }
                                                }
                                            });
                                }
                                if(which==1){

                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,parent,false);
                CartViewHolder cartViewHolder = new CartViewHolder(view);
                return cartViewHolder;
            }

        };
        productlist.setAdapter(adapter);
        adapter.startListening();
    }
    void updateorder(final String amt){
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

        final DatabaseReference orderlist = FirebaseDatabase.getInstance().getReference().child("Order").child(temp);
        orderlist.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    str1=dataSnapshot.child("totalamount").getValue().toString();
                    if(Integer.parseInt(str1)-Integer.parseInt(amt)==0){
                        deletefromorder();
                    }else{
                        int nowa=Integer.parseInt(str1)-Integer.parseInt(amt);
                        String name=dataSnapshot.child("name").getValue().toString();
                        String number=dataSnapshot.child("number").getValue().toString();
                        String time=dataSnapshot.child("time").getValue().toString();
                        String date=dataSnapshot.child("date").getValue().toString();
                        String City=dataSnapshot.child("City").getValue().toString();
                        String Address=dataSnapshot.child("Address").getValue().toString();
                        update(String.valueOf(nowa),name,number,time,date,City,Address);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    void deletefromorder(){
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
        final DatabaseReference orderlist = FirebaseDatabase.getInstance().getReference().child("Order").child(temp);
        orderlist.removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //StyleableToast.makeText(Userorderview.this,"deeeeeeeeeeeeeeeeeeeee successfully",R.style.exampleToast).show();
                    }
                });

    }
    void update(String amt,String name1,String number1,String time1,String date1,String city1,String address1){
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
        final DatabaseReference orderlist = FirebaseDatabase.getInstance().getReference().child("Order").child(temp);
        final HashMap<String,Object> ordermap =new HashMap<>();
        ordermap.put("totalamount",amt);
        ordermap.put("name",name1);
        ordermap.put("Address",address1);
        ordermap.put("City",city1);
        ordermap.put("number",number1);
        ordermap.put("date",date1);
        ordermap.put("time",time1);
        ordermap.put("status","not shipped");
        ordermap.put("email",temp);
        orderlist.updateChildren(ordermap);
    }
}
