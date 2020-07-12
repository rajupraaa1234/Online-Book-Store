package com.raju.onlinebookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.raju.onlinebookstore.Models.UserOrders;

public class AdminneworderActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference orderref;
    private DatabaseReference adminview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminneworder);
        orderref= FirebaseDatabase.getInstance().getReference().child("Order");
        adminview=FirebaseDatabase.getInstance().getReference().child("cart list").child("Admin view");
        recyclerView= findViewById(R.id.recycler_menu);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<UserOrders> options =
                new FirebaseRecyclerOptions.Builder<UserOrders>()
                .setQuery(orderref,UserOrders.class)
                .build();
        FirebaseRecyclerAdapter<UserOrders,AdminOrderViewHolder> adapter =
                new FirebaseRecyclerAdapter<UserOrders, AdminOrderViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrderViewHolder holder, int position, @NonNull final UserOrders model) {
                             holder.username.setText("Name :" + model.getName());
                             holder.userphn.setText("Phone No :"+model.getNumber());
                             holder.useradd.setText("Address :" + model.getAddress() + ", " +model.getCity());
                             holder.userdatetime.setText("Order At :" + model.getDate() + " " + model.getTime());
                             holder.userprice.setText("Total Amount :" +model.getTotalamount() + " /-" );
                             holder.showbtn.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     Intent intent =new Intent(AdminneworderActivity.this,AdminnewOrderDetails.class);
                                     intent.putExtra("uid",model.getEmail());
                                     startActivity(intent);
                                 }
                             });
                             holder.itemView.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     CharSequence options[] = new CharSequence[]{
                                             "Yes",
                                             "No"
                                     };
                                     AlertDialog.Builder builder = new AlertDialog.Builder(AdminneworderActivity.this);
                                     builder.setTitle("Hove you shipped this order product ?");
                                     builder.setItems(options, new DialogInterface.OnClickListener() {
                                         @Override
                                         public void onClick(DialogInterface dialog, int which) {
                                               if(which==0){
                                                       String usermail=model.getEmail();
                                                       removeproduct(usermail);
                                               }else{
                                                   finish();
                                               }
                                         }
                                     });
                                     builder.show();
                                 }
                             });

                    }

                    @NonNull
                    @Override
                    public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
                        return new AdminOrderViewHolder(view);
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    public static class AdminOrderViewHolder extends RecyclerView.ViewHolder {
        public TextView username,userphn,useradd,userprice,userdatetime;
        public Button showbtn;
           public AdminOrderViewHolder(View itemView){
                super(itemView);
                username = itemView.findViewById(R.id.username);
                userphn = itemView.findViewById(R.id.phone);
                useradd = itemView.findViewById(R.id.order_address);
                userprice = itemView.findViewById(R.id.price);
                userdatetime = itemView.findViewById(R.id.date_time);
                showbtn = itemView.findViewById(R.id.order_btn);

        }
    }
    public void removeproduct(String uid){
        orderref.child(uid).removeValue();
        adminview.child(uid).removeValue();
    }
}
