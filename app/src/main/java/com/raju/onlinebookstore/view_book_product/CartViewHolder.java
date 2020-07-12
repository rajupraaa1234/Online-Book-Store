package com.raju.onlinebookstore.view_book_product;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raju.onlinebookstore.Interface.ItemClickListner;
import com.raju.onlinebookstore.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView bookname,bookprice,quantity;
    private ItemClickListner itemClickListner;
    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        bookname=itemView.findViewById(R.id.cartbookname);
        bookprice=itemView.findViewById(R.id.cartbookprice);
        quantity=itemView.findViewById(R.id.cartbookqautity);
    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v,getAdapterPosition(),false);
    }
    public void setItemClickListner(ItemClickListner  itemClickListner){
        this.itemClickListner=itemClickListner;
    }
}
