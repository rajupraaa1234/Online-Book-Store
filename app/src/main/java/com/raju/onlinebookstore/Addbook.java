package com.raju.onlinebookstore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.raju.onlinebookstore.Models.Bookclass;
import java.io.IOException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static com.basgeekball.awesomevalidation.ValidationStyle.TEXT_INPUT_LAYOUT;

public class Addbook extends AppCompatActivity {

    ImageView bookimage;
    EditText bookid;
    EditText bookauthor;
    EditText subject;
    EditText bookname;
    EditText price;
    Button upload;
    ProgressDialog progressDialog ;
    private ProgressDialog loadingBar;
    Uri ImageUri;
    String bkid,bkauthore,bksub,bkname,imageurl;
    String rs,saveCurrentDate, saveCurrentTime;
    private String productRandomKey, downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private AwesomeValidation awesomeValidation;
    int GalleryPick = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbook);
        bookimage=findViewById(R.id.bookimage);
        loadingBar=new ProgressDialog(this);
        bookid=findViewById(R.id.bookid);
        bookauthor=findViewById(R.id.bookauthore);
        subject=findViewById(R.id.subject);
        bookname=findViewById(R.id.bookname);
        price=findViewById(R.id.price);
        upload=findViewById(R.id.addbook_btn);
        progressDialog = new ProgressDialog(Addbook.this);
        ProductImagesRef = FirebaseStorage.getInstance().getReference("Books");
        ProductsRef = FirebaseDatabase.getInstance().getReference("Books");
        bookimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.bookauthore, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this,R.id.subject, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.subname);
        awesomeValidation.addValidation(this,R.id.bookname, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.bkname);
        awesomeValidation.addValidation(this,R.id.phone, "^[2-9]{2}[0-9]{8}$", R.string.numbererror);
    }
  
    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            ImageUri = data.getData();
            bookimage.setImageURI(ImageUri);
        }
    }


    private void ValidateProductData()
    {
          bkid= bookid.getText().toString();
          bkauthore=bookauthor.getText().toString();
          bksub=subject.getText().toString();
          bkname=bookname.getText().toString();
          rs=price.getText().toString();
          if(ImageUri==null){
              StyleableToast.makeText(Addbook.this,"book image is mandatory",R.style.exampleToast).show();
             //Toast.makeText(this, "book image is mandatory", Toast.LENGTH_SHORT).show();
          }
         else if(TextUtils.isEmpty(bkid)){
              StyleableToast.makeText(Addbook.this,"please type book id",R.style.exampleToast).show();
            // Toast.makeText(this, "please type book id", Toast.LENGTH_SHORT).show();
         }
         else if(TextUtils.isEmpty(bkauthore)){
              StyleableToast.makeText(Addbook.this,"please type author name",R.style.exampleToast).show();
            //Toast.makeText(this, "please type author name", Toast.LENGTH_SHORT).show();
        }
         else if(TextUtils.isEmpty(bksub)){
              StyleableToast.makeText(Addbook.this,"please type book subject",R.style.exampleToast).show();
               //Toast.makeText(this, "please type book subject", Toast.LENGTH_SHORT).show();
          }
          else if(TextUtils.isEmpty(bkname)){
              StyleableToast.makeText(Addbook.this,"please type book name or title",R.style.exampleToast).show();
             // Toast.makeText(this, "please type book name or title", Toast.LENGTH_SHORT).show();
         }
         else if(TextUtils.isEmpty(rs)){
              StyleableToast.makeText(Addbook.this,"please type book price",R.style.exampleToast).show();
              //Toast.makeText(this, "please type book price", Toast.LENGTH_SHORT).show();
         }
         else if(!awesomeValidation.validate()){

          }
         else if(!isNumeric(rs)){
              StyleableToast.makeText(Addbook.this,"Invalid book price",R.style.exampleToast).show();
          }
          else{
              StoreProductInformation();
          }
    }



    private void StoreProductInformation()
    {
        loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Dear Admin, please wait while we are adding the new product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;


        final StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                StyleableToast.makeText(Addbook.this,message,R.style.exampleToast).show();
                //Toast.makeText(Addbook.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                StyleableToast.makeText(Addbook.this,"Product Image uploaded Successfully...",R.style.exampleToast).show();
                //Toast.makeText(Addbook.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();
                            StyleableToast.makeText(Addbook.this,"got the Product image Url Successfully...",R.style.exampleToast).show();
                          //  Toast.makeText(Addbook.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();
                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }



    private void SaveProductInfoToDatabase()
    {
        HashMap<String, Object> productMap = new HashMap<>();
         productMap.put("bookproductid", productRandomKey);
         productMap.put("bookid", bkid);
         productMap.put("imageURL", downloadImageUrl);
         productMap.put("bookauthor", bkauthore);
         productMap.put("bookprice", rs);
         productMap.put("booksubject",bksub);
         productMap.put("bookname", bkname);

        ProductsRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(Addbook.this, Admin_dashboard.class);
                            startActivity(intent);
                            finish();
                            loadingBar.dismiss();
                            StyleableToast.makeText(Addbook.this,"Product is added successfully..",R.style.exampleToast).show();
                            //Toast.makeText(Addbook.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            StyleableToast.makeText(Addbook.this,"Error...",R.style.exampleToast).show();
                            //Toast.makeText(Addbook.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
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

