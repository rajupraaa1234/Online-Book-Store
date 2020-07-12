package com.raju.onlinebookstore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.basgeekball.awesomevalidation.ValidationStyle.TEXT_INPUT_LAYOUT;

public class Myaccount extends AppCompatActivity {

     private CircleImageView circleImageView;
     private TextView close;
     private TextView update;
     private Button choose_btn;
     private TextInputLayout name;
     private Uri imageuri;
     private String imagenameindatabase;
     private String myuri;
     private StorageTask uploadtask;
     private StorageReference storageProfilePrictureRef;
     private ProgressDialog loadingbar;
     private StorageReference storageReference;
     private String cheker="";
     private String uname,add,ucity,unumber,ugender,umail;
     private TextInputLayout address;
     private TextInputLayout number;
     private AwesomeValidation awesomeValidation;
     private TextInputLayout city;
     private TextInputLayout gen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaccount);
        loadingbar=new ProgressDialog(this);
        circleImageView = (CircleImageView)findViewById(R.id.pic);
        close=findViewById(R.id.close_settings_btn);
        update=findViewById(R.id.update_account_settings_btn);
        choose_btn=findViewById(R.id.choosepic);
        storageProfilePrictureRef = FirebaseStorage.getInstance().getReference().child("Profile pictures");
        name=findViewById(R.id.name);
        address=findViewById(R.id.address);
        number=findViewById(R.id.number);
        city=findViewById(R.id.city);
        gen=findViewById(R.id.gen_der);
        awesomeValidation = new AwesomeValidation(TEXT_INPUT_LAYOUT);
        awesomeValidation.addValidation(this,R.id.name, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this,R.id.city, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.cityerror);
        awesomeValidation.addValidation(this,R.id.number, "^[2-9]{2}[0-9]{8}$", R.string.numbererror);
        userinfo(circleImageView,name,address,number,city,gen);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cheker.equals("cicked")){
                    userinfosaved();
                }
                else{
                    if(check()){
                    updateonlyuserinfo();
                    }
                }
            }
        });
        choose_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cheker="cicked";
                CropImage.activity(imageuri)
                        .setAspectRatio(1,1)
                        .start(Myaccount.this);
            }
        });
    }
    public boolean check(){
        if(TextUtils.isEmpty(name.getEditText().getText().toString())){
            StyleableToast.makeText(Myaccount.this,"Username is mandatory",R.style.exampleToast).show();
        }
        else if(TextUtils.isEmpty(address.getEditText().getText().toString())){
            StyleableToast.makeText(Myaccount.this,"Please Enter address",R.style.exampleToast).show();
        }
        else if(TextUtils.isEmpty(number.getEditText().getText().toString())){
            StyleableToast.makeText(Myaccount.this,"Please Enter phone number",R.style.exampleToast).show();
        }
        else if(TextUtils.isEmpty(city.getEditText().getText().toString())){
            StyleableToast.makeText(Myaccount.this,"Please Enter city",R.style.exampleToast).show();
        }
        else if(TextUtils.isEmpty(gen.getEditText().getText().toString())){
            StyleableToast.makeText(Myaccount.this,"Please Enter your gender",R.style.exampleToast).show();
        }
        else if(!awesomeValidation.validate()){
        }
        else if(!isvalidgender(gen.getEditText().getText().toString())){
            StyleableToast.makeText(Myaccount.this,"Invalid Gender",R.style.exampleToast).show();
        }
        else if(isNumeric(address.getEditText().getText().toString())){
            StyleableToast.makeText(Myaccount.this,"Invalid Address",R.style.exampleToast).show();
        }
        else
            return true;
        return false;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageuri=result.getUri();
            circleImageView.setImageURI(imageuri);
        }
        else{
            StyleableToast.makeText(Myaccount.this,"Error Try again",R.style.exampleToast).show();
            //Toast.makeText(this, "Error Try again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Myaccount.this,Myaccount.class));
            finish();
        }
    }

    public void userinfosaved(){
            if(TextUtils.isEmpty(name.getEditText().getText().toString())){
                StyleableToast.makeText(Myaccount.this,"Username is mandatory",R.style.exampleToast).show();
               // Toast.makeText(this, "Username is mandatory", Toast.LENGTH_SHORT).show();
            }
            else if(TextUtils.isEmpty(address.getEditText().getText().toString())){
                StyleableToast.makeText(Myaccount.this,"Please Enter address",R.style.exampleToast).show();
                //Toast.makeText(this, "Please Enter address", Toast.LENGTH_SHORT).show();
            }
            else if(TextUtils.isEmpty(number.getEditText().getText().toString())){
                StyleableToast.makeText(Myaccount.this,"Please Enter phone number",R.style.exampleToast).show();
               // Toast.makeText(this, "Please Enter phone number", Toast.LENGTH_SHORT).show();
            }
            else if(TextUtils.isEmpty(city.getEditText().getText().toString())){
                StyleableToast.makeText(Myaccount.this,"Please Enter city",R.style.exampleToast).show();
                //Toast.makeText(this, "Please Enter city ", Toast.LENGTH_SHORT).show();
            }
            else if(TextUtils.isEmpty(gen.getEditText().getText().toString())) {
                StyleableToast.makeText(Myaccount.this, "Please Enter your gender", R.style.exampleToast).show();
            }
            else if(!awesomeValidation.validate()){
            }
            else if(!isvalidgender(gen.getEditText().getText().toString())){
                StyleableToast.makeText(Myaccount.this,"Invalid Gender",R.style.exampleToast).show();
            }
            else if(cheker.equals("cicked")){
                uploadimage();
            }
    }
    public void uploadimage(){
        loadingbar.setTitle("Update profile");
        loadingbar.setMessage("Please wait,while we are updating your changes");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();
        if(imageuri != null ){
            final StorageReference storageReference=storageProfilePrictureRef
                    .child(imagenameindatabase + ".jpg");
            uploadtask = storageReference.putFile(imageuri);
            uploadtask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                     if(!task.isSuccessful()) {
                         throw task.getException();
                     }
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                      if(task.isSuccessful()){
                          Uri downloaduri=task.getResult();
                          myuri=downloaduri.toString();
                          DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("User");
                          FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                          HashMap<String,Object> usermap=new HashMap<>();
                          usermap.put("Username",name.getEditText().getText().toString());
                          usermap.put("Moblienumber",number.getEditText().getText().toString());
                          usermap.put("gender",gen.getEditText().getText().toString());
                          usermap.put("Address",address.getEditText().getText().toString());
                          usermap.put("city",city.getEditText().getText().toString());
                          usermap.put("email",user.getEmail());
                          usermap.put("image",myuri);
                          ref.child(imagenameindatabase).updateChildren(usermap);
                          loadingbar.dismiss();
                          StyleableToast.makeText(Myaccount.this,"Profile update successfully",R.style.exampleToast).show();
                          startActivity(new Intent(Myaccount.this,DashBoard.class));
                          finish();
                      }else{
                          loadingbar.dismiss();
                          StyleableToast.makeText(Myaccount.this,"Error...",R.style.exampleToast).show();
                          ///Toast.makeText(Myaccount.this, "Error...", Toast.LENGTH_SHORT).show();
                      }
                }
            });
        }
        else{
            StyleableToast.makeText(Myaccount.this,"Image is Not Selected",R.style.exampleToast).show();
        }
    }
    public void updateonlyuserinfo(){
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("User");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        HashMap<String,Object> usermap=new HashMap<>();
        usermap.put("Username",name.getEditText().getText().toString());
        usermap.put("Moblienumber",number.getEditText().getText().toString());
        usermap.put("gender",gen.getEditText().getText().toString());
        usermap.put("Address",address.getEditText().getText().toString());
        usermap.put("city",city.getEditText().getText().toString());
        usermap.put("email",user.getEmail());
        ref.child(imagenameindatabase).updateChildren(usermap);
        StyleableToast.makeText(Myaccount.this,"Profile update successfully",R.style.exampleToast).show();
        startActivity(new Intent(Myaccount.this,DashBoard.class));
        finish();

    }
    private void userinfo(final CircleImageView circleImageView, final TextInputLayout name, final TextInputLayout address, final TextInputLayout number,final TextInputLayout city,final TextInputLayout gen) {
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
        imagenameindatabase=temp;
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("User").child(temp);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("image").exists()){
                        String image=dataSnapshot.child("image").getValue().toString();
                         uname=dataSnapshot.child("Username").getValue().toString();
                         add=dataSnapshot.child("Address").getValue().toString();
                         ucity=dataSnapshot.child("city").getValue().toString();
                         unumber=dataSnapshot.child("Moblienumber").getValue().toString();
                         ugender=dataSnapshot.child("gender").getValue().toString();
                         umail=dataSnapshot.child("email").getValue().toString();
                        Picasso.get().load(image).into(circleImageView);
                        name.getEditText().setText(uname);
                        address.getEditText().setText(add);
                        city.getEditText().setText(ucity);
                        number.getEditText().setText(unumber);
                        gen.getEditText().setText(ugender);
                    }
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
    public static boolean isvalidgender(String str){
        if(str.equals("Male") || str.equals("MALE") || str.equals("male") || str.equals("Female") || str.equals("FEMALE") || str.equals("female")){
            return true;
        }
        return false;
    }

}
