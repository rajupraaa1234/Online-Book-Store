package com.raju.onlinebookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.raju.onlinebookstore.Models.Cart;
import com.raju.onlinebookstore.view_book_product.CartViewHolder;

public class AdminnewOrderDetails extends AppCompatActivity {

    private RecyclerView productlist;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartlist;
    private String userId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminnew_order_details);
        userId=getIntent().getStringExtra("uid");
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
        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartlist,Cart.class)
                .build();
        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter =new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                holder.quantity.setText("Quantity : " + model.getQuantity());
                holder.bookprice.setText(model.getBookprice());
                holder.bookname.setText( model.getBookname());
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
}
