package com.raju.onlinebookstore.Models;

public class Bookclass {
     String bookid,bookauthor,bookname,booksubject,bookprice,bookproductid;
     public String imageURL;
     Bookclass(){

     }

    public Bookclass(String bookid, String bookauthor, String bookname, String booksubject, String bookprice, String bookproductid, String imageURL) {
        this.bookid = bookid;
        this.bookauthor = bookauthor;
        this.bookname = bookname;
        this.booksubject = booksubject;
        this.bookprice = bookprice;
        this.bookproductid = bookproductid;
        this.imageURL = imageURL;
    }

    public void setBookid(String bookid) {
        this.bookid = bookid;
    }

    public void setBookauthor(String bookauthor) {
        this.bookauthor = bookauthor;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public void setBooksubject(String booksubject) {
        this.booksubject = booksubject;
    }

    public void setBookprice(String bookprice) {
        this.bookprice = bookprice;
    }

    public void setBookproductid(String bookproductid) {
        this.bookproductid = bookproductid;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getBookid() {
        return bookid;
    }

    public String getBookauthor() {
        return bookauthor;
    }

    public String getBookname() {
        return bookname;
    }

    public String getBooksubject() {
        return booksubject;
    }

    public String getBookprice() {
        return bookprice;
    }

    public String getBookproductid() {
        return bookproductid;
    }

    public String getImageURL() {
        return imageURL;
    }
}
