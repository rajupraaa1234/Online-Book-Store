package com.raju.onlinebookstore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.raju.onlinebookstore.Models.Admin;
import com.raju.onlinebookstore.Models.Bookclass;
import com.raju.onlinebookstore.Models.User_data;
import com.raju.onlinebookstore.view_book_product.Bookviewholder;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashBoard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private AppBarConfiguration mAppBarConfiguration;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private CircleImageView circleImageView;
    TextView notsinged;
    private ProgressDialog loadingBar;
    private TextView emailname;
    String temp="";
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Books");
        Toolbar toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        //emailname=findViewById(R.layout.nav_header_dash_board);
        loadingBar=new ProgressDialog(this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.openDrawer, R.string.closeDrawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        View hView =  navigationView.getHeaderView(0);
        TextView setemail = (TextView)hView.findViewById(R.id.emailid);
         notsinged = (TextView)hView.findViewById(R.id.notsigned);
         circleImageView = (CircleImageView)hView.findViewById(R.id.pic);
        setemail.setTextColor(getResources().getColor(R.color.colorwhite));
        setemail.setText(user.getEmail());
        String email1=user.getEmail();
        for(int i=0;i<email1.length();i++){
            if(email1.charAt(i)=='.')
                break;
            else{
                temp = temp + String.valueOf(email1.charAt(i));
            }
        }
        final DatabaseReference databaseReference;
        databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("User").child(temp).exists()){
                    User_data user_data=dataSnapshot.child("User").child(temp).getValue(User_data.class);
                    notsinged.setText(user_data.getUsername());
                    notsinged.setTextColor(getResources().getColor(R.color.colorwhite));
                    Picasso.get().load(user_data.getImage()).placeholder(R.mipmap.profile_placeholder).into(circleImageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void onBackPressed()
    {
        finish();
        moveTaskToBack(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Bookclass> options=new FirebaseRecyclerOptions.Builder<Bookclass>()
                .setQuery(databaseReference,Bookclass.class)
                .build();

        FirebaseRecyclerAdapter<Bookclass, Bookviewholder> adapter=
                new FirebaseRecyclerAdapter<Bookclass, Bookviewholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull Bookviewholder holder, int position, @NonNull final  Bookclass model) {
                        holder.bookname.setText(model.getBookname());
                        holder.subject.setText("Subject: "+model.getBooksubject());
                        holder.bookauthor.setText("Book Author: "+model.getBookauthor());
                        holder.bookprice.setText("Rs : "+model.getBookprice() + "/-");
                        Picasso.get().load(model.getImageURL()).into(holder.imageView);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(DashBoard.this,ProductdetailActivity.class);
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
                  recyclerView.setAdapter(adapter);
                  adapter.startListening();
     }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId())
        {
            case R.id.nav_home:
                Intent nextactivity1 = new Intent(DashBoard.this, DashBoard.class);
                startActivity(nextactivity1);
                break;
            case R.id.nav_cart:
                Intent nextactivity2 = new Intent(DashBoard.this, ViewCart.class);
                startActivity(nextactivity2);
                //Toast.makeText(this, "Hii bell", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                loadingBar.setTitle("User Logout");
                loadingBar.setMessage("Thanks for Shopping");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this,Login.class));
                break;
            case R.id.order:
                Intent nextactivity3 = new Intent(DashBoard.this, Userorderview.class);
                startActivity(nextactivity3);
                break;
            case R.id.nav_account:
                Intent nextactivity = new Intent(DashBoard.this, Myaccount.class);
                startActivity(nextactivity);
                break;
            default:
               // Toast.makeText(this, "kuchho nahi", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.search){
            Intent nextactivity2 = new Intent(DashBoard.this, ViewCart.class);
            startActivity(nextactivity2);
        }
        else if(id==R.id.addtocart){
            Intent nextactivity2 = new Intent(DashBoard.this, SearchActivity.class);
            startActivity(nextactivity2);
        }
        return super.onOptionsItemSelected(item);
    }

}