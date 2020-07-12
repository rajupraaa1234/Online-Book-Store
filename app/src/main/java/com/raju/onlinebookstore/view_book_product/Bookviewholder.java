package com.raju.onlinebookstore.view_book_product;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raju.onlinebookstore.Interface.ItemClickListner;
import com.raju.onlinebookstore.R;

public class Bookviewholder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView bookname, bookauthor, bookprice,subject;
    public ImageView imageView;
    public ItemClickListner listner;
    public Bookviewholder(@NonNull View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.book_image);
        bookname = (TextView) itemView.findViewById(R.id.book_name);
        bookauthor = (TextView) itemView.findViewById(R.id.book_author);
        bookprice = (TextView) itemView.findViewById(R.id.product_price);
        subject = (TextView) itemView.findViewById(R.id.subject);

    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view)
    {
        listner.onClick(view, getAdapterPosition(), false);
    }

}
