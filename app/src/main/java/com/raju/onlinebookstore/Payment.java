package com.raju.onlinebookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Payment extends AppCompatActivity {

    RadioGroup rdogrp;
    RadioButton radioButton;
    Button next;
    String select="";
    String total;
    String name;
    String add;
    TextView amount;
    String city;
    String number;
    String myupid="rajupraaaa1234@okicici";
    String owner_name="raju kumar";
    final int UPI_PAYMENT = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        rdogrp=findViewById(R.id.rdiogroup);
        next=findViewById(R.id.next);
        amount=findViewById(R.id.total_paid);
        Intent intent = getIntent();
        total= intent.getStringExtra("totalamount");
        name=intent.getStringExtra("name");
        add=intent.getStringExtra("Address");
        city=intent.getStringExtra("City");
        number=intent.getStringExtra("number");
        amount.setText("Total : " + total + "/-");
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   if(select.length()==0){
                       StyleableToast.makeText(Payment.this,"Please Select Method",R.style.exampleToast).show();
                   }
                   else if(select.equals("Google Pay")){
                       payUsingUpi(owner_name,myupid,"this is payment method",total);
                      // Toast.makeText(Payment.this, "Google Pay", Toast.LENGTH_SHORT).show();
                   }
                   else if(select.equals("By case")){
                        buybycase();
                   }
            }
        });
    }
    public void onselect(View view){
         int rdoid=rdogrp.getCheckedRadioButtonId();
         radioButton=findViewById(rdoid);
         select=radioButton.getText().toString();
    }
    public void buybycase(){
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
        String savecurrentdate,savecurrenttime;
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentdate=new SimpleDateFormat("MMMM , dd , YYYY");
        savecurrentdate = currentdate.format(calendar.getTime());

        SimpleDateFormat currenttime=new SimpleDateFormat("HH:mm:ss a");
        savecurrenttime = currenttime.format(calendar.getTime());
        final DatabaseReference cartlistref = FirebaseDatabase.getInstance().getReference()
                .child("Order")
                .child(temp);
        final HashMap<String,Object> ordermap =new HashMap<>();
        ordermap.put("totalamount",total);
        ordermap.put("name",name);
        ordermap.put("Address",add);
        ordermap.put("City",city);
        ordermap.put("number",number);
        ordermap.put("date",savecurrentdate);
        ordermap.put("time",savecurrenttime);
        ordermap.put("status","not shipped");
        ordermap.put("email",temp);

        final String finalTemp = temp;
        cartlistref.updateChildren(ordermap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                 if(task.isSuccessful()){
                     FirebaseDatabase.getInstance().getReference()
                             .child("cart list")
                             .child("User view")
                             .child(finalTemp)
                             .removeValue()
                             .addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {
                                     if(task.isSuccessful()){
                                         StyleableToast.makeText(Payment.this,"ordered successfully",R.style.exampleToast).show();
                                         //Toast.makeText(ConfirmOrder.this, "ordered successfully", Toast.LENGTH_SHORT).show();
                                         Intent intent = new Intent(Payment.this, DashBoard.class);
                                         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                         startActivity(intent);
                                         finish();
                                     }
                                 }
                             });
                 }
            }
        });
    }
//    public void onBackPressed()
//    {
//        Intent stu = new Intent(Payment.this,ConfirmOrder.class);
//        startActivity(stu);
//        finish();
//    }
void payUsingUpi(  String name,String upiId, String note, String amount) {
    Log.e("main ", "name "+name +"--up--"+upiId+"--"+ note+"--"+amount);
    Uri uri = Uri.parse("upi://pay").buildUpon()
            .appendQueryParameter("pa", upiId)
            .appendQueryParameter("pn", name)
            .appendQueryParameter("tn", note)
            .appendQueryParameter("am", amount)
            .appendQueryParameter("cu", "INR")
            .build();
    Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
    upiPayIntent.setData(uri);
    // will always show a dialog to user to choose an app
    Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
    // check if intent resolves
    if(null != chooser.resolveActivity(getPackageManager())) {
        startActivityForResult(chooser, UPI_PAYMENT);
    } else {
        StyleableToast.makeText(Payment.this,"No UPI app found, please install one to continue",R.style.exampleToast).show();
        //Toast.makeText(Payment.this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
    }
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("main ", "response "+resultCode );
        /*
       E/main: response -1
       E/UPI: onActivityResult: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPIPAY: upiPaymentDataOperation: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPI: payment successfull: 922118921612
         */
        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.e("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.e("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    //when user simply back without payment
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }
    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(Payment.this)) {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }
            if (status.equals("success")) {
                //Code to handle successful transaction here.
                StyleableToast.makeText(Payment.this,"Transaction successful.",R.style.exampleToast).show();
                buybycase();
                Log.e("UPI", "payment successfull: "+approvalRefNo);
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                StyleableToast.makeText(Payment.this,"Payment cancelled by user.",R.style.exampleToast).show();
                Log.e("UPI", "Cancelled by user: "+approvalRefNo);
            }
            else {
                StyleableToast.makeText(Payment.this,"Transaction failed.Please try again",R.style.exampleToast).show();
                Log.e("UPI", "failed payment: "+approvalRefNo);
            }
        } else {
            Log.e("UPI", "Internet issue: ");
            StyleableToast.makeText(Payment.this,"Internet connection is not available. Please check and try again",R.style.exampleToast).show();

        }
    }
    public static boolean isConnectionAvailable(Payment context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }
}
