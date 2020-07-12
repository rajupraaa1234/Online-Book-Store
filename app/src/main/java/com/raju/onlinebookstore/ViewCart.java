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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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

public class ViewCart extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    Button next_btn;
    TextView total,txtMsg1;
    int totaolprice=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);
        recyclerView=findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        next_btn=findViewById(R.id.next_btn);
        total=findViewById(R.id.bill);
        txtMsg1=findViewById(R.id.msg1);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totaolprice==0){
                    StyleableToast.makeText(ViewCart.this,"First you add into cart",R.style.exampleToast).show();
                }
                else {
                    Intent intent = new Intent(ViewCart.this, ConfirmOrder.class);
                    intent.putExtra("total", String.valueOf(totaolprice));
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkorderstate();
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
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartlink.child("User view").child(temp).child("product"),Cart.class)
                .build();
        final String finalTemp = temp;
        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {
                holder.quantity.setText("Quantity : " + model.getQuantity());
                holder.bookprice.setText(model.getBookprice());
                holder.bookname.setText( model.getBookname());
                int temp=Integer.parseInt(model.getBookprice()) * Integer.parseInt(model.getQuantity());
                totaolprice=totaolprice+temp;
                total.setText("Total Price : " + String.valueOf(totaolprice));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Edit",
                                        "Remove"
                                };
                        AlertDialog.Builder builder =new AlertDialog.Builder(ViewCart.this);
                        builder.setTitle("Cart Options");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                 if(which==0){
                                     Intent intent = new Intent(ViewCart.this,ProductdetailActivity.class);
                                     intent.putExtra("bkid",model.getBookproductid());
                                     startActivity(intent);
                                 }
                                 if(which==1){
                                     cartlink.child("User view").child(finalTemp)
                                              .child("product")
                                              .child(model.getBookproductid())
                                              .removeValue()
                                              .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                  @Override
                                                  public void onComplete(@NonNull Task<Void> task) {
                                                       if(task.isSuccessful()){
                                                           StyleableToast.makeText(ViewCart.this,"Item remove successfully",R.style.exampleToast).show();
                                                           //Toast.makeText(ViewCart.this, "Item remove successfully", Toast.LENGTH_SHORT).show();
                                                           Intent nextactivity =new Intent(ViewCart.this,DashBoard.class);
                                                           startActivity(nextactivity);
                                                           finish();
                                                       }
                                                  }
                                              });
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
        recyclerView.setAdapter(adapter);
        adapter.startListening();

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
                             String state= dataSnapshot.child("status").getValue().toString();
                             String username = dataSnapshot.child("name").getValue().toString();
                             if(state.equals("shipped")){
                                   total.setText("Dear "  + username + "\n order is shipped successfully");
                                   recyclerView.setVisibility(View.GONE);
                                   txtMsg1.setVisibility(View.VISIBLE);
                                 StyleableToast.makeText(ViewCart.this,"you can purchase more book, once you received your first order",R.style.exampleToast).show();
                                // Toast.makeText(ViewCart.this, "you can purchase more book, once you received your first order", Toast.LENGTH_SHORT).show();
                             }
                             else if(state.equals("not shipped")){
                                 total.setText("Shipping state=Not shipped");
                                 recyclerView.setVisibility(View.GONE);
                                 txtMsg1.setVisibility(View.VISIBLE);
                                 StyleableToast.makeText(ViewCart.this,"you can purchase more book, once you received your first order",R.style.exampleToast).show();
                                // Toast.makeText(ViewCart.this, "you can purchase more book, once you received your first order", Toast.LENGTH_SHORT).show();

                             }
                         }
              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {

              }
          });
    }
    public void onBackPressed()
    {
        Intent stu = new Intent(ViewCart.this,DashBoard.class);
        startActivity(stu);
        finish();
    }
}
