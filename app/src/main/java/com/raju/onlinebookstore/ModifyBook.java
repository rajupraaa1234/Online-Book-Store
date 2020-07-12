package com.raju.onlinebookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.raju.onlinebookstore.Models.Bookclass;
import com.raju.onlinebookstore.view_book_product.Bookviewholder;
import com.squareup.picasso.Picasso;

public class ModifyBook extends AppCompatActivity {

    RecyclerView searchlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_book);
        searchlist = findViewById(R.id.allbookresult);
        searchlist.setLayoutManager(new LinearLayoutManager(ModifyBook.this));
    }
    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Books");

        FirebaseRecyclerOptions<Bookclass> options = new FirebaseRecyclerOptions.Builder<Bookclass>()
                    .setQuery(databaseReference,Bookclass.class).build();
        FirebaseRecyclerAdapter<Bookclass, Bookviewholder> adapter =
                new FirebaseRecyclerAdapter<Bookclass, Bookviewholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull Bookviewholder holder, int position, @NonNull final Bookclass model) {
                        holder.bookname.setText(model.getBookname());
                        holder.subject.setText("Subject: "+model.getBooksubject());
                        holder.bookauthor.setText("Book Author: "+model.getBookauthor());
                        holder.bookprice.setText("Rs : "+model.getBookprice() + "/-");
                        Picasso.get().load(model.getImageURL()).into(holder.imageView);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(ModifyBook.this,AfterClickOnBookInModify.class);
                                intent.putExtra("bkid",model.getBookproductid());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public Bookviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_product_layout, parent, false);
                        Bookviewholder holder = new Bookviewholder(view);
                        return holder;
                    }
                };
        searchlist.setAdapter(adapter);
        adapter.startListening();
    }
}
