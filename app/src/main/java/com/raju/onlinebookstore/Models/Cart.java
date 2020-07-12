package com.raju.onlinebookstore.Models;

public class Cart {
    String bookauther,bookid,bookname,bookprice,bookproductid,date,quantity,subect,time;
    Cart(){

    }

    public String getBookauther() {
        return bookauther;
    }

    public String getBookid() {
        return bookid;
    }

    public String getBookname() {
        return bookname;
    }

    public String getBookprice() {
        return bookprice;
    }

    public String getBookproductid() {
        return bookproductid;
    }

    public String getDate() {
        return date;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getSubect() {
        return subect;
    }

    public String getTime() {
        return time;
    }

    public void setBookauther(String bookauther) {
        this.bookauther = bookauther;
    }

    public void setBookid(String bookid) {
        this.bookid = bookid;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public void setBookprice(String bookprice) {
        this.bookprice = bookprice;
    }

    public void setBookproductid(String bookproductid) {
        this.bookproductid = bookproductid;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setSubect(String subect) {
        this.subect = subect;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Cart(String bookauther, String bookid, String bookname, String bookprice, String bookproductid, String date, String quantity, String subect, String time) {
        this.bookauther = bookauther;
        this.bookid = bookid;
        this.bookname = bookname;
        this.bookprice = bookprice;
        this.bookproductid = bookproductid;
        this.date = date;
        this.quantity = quantity;
        this.subect = subect;
        this.time = time;
    }
}
