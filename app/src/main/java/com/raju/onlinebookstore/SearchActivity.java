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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.raju.onlinebookstore.Models.Bookclass;
import com.raju.onlinebookstore.view_book_product.Bookviewholder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    List<String> list;
    Spinner spinner;
    EditText searchinput;
    int choose=0;
    Button search_btn;
    String search_input;
    RecyclerView searchlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchinput = findViewById(R.id.searchtext);
        spinner=findViewById(R.id.searchby);
        search_btn=findViewById(R.id.search_btn);
        searchlist = findViewById(R.id.searchrec);
        list=new ArrayList<String>();
        list.add("Title");
        list.add("Author");
        list.add("subject");
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choose=position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        searchlist.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_input=searchinput.getText().toString();
                onStart();
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Books");
        FirebaseRecyclerOptions<Bookclass> options=null;
        if(choose==0) {
             options = new FirebaseRecyclerOptions.Builder<Bookclass>()
                    .setQuery(databaseReference.orderByChild("bookname").startAt(search_input), Bookclass.class).build();
        }
        else if(choose==1){
             options = new FirebaseRecyclerOptions.Builder<Bookclass>()
                    .setQuery(databaseReference.orderByChild("bookauthor").startAt(search_input), Bookclass.class).build();
        }
        else if(choose==2){
             options = new FirebaseRecyclerOptions.Builder<Bookclass>()
                    .setQuery(databaseReference.orderByChild("booksubject").startAt(search_input), Bookclass.class).build();
        }

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
                                Intent intent=new Intent(SearchActivity.this,ProductdetailActivity.class);
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
