package com.raju.onlinebookstore.Models;

public class UserOrders {
    String Address,City,date,name,number,status,time,totalamount,email;

    UserOrders(){

    }

    public UserOrders(String address, String city, String date, String name, String number, String status, String time, String totalamount, String email) {
        Address = address;
        City = city;
        this.date = date;
        this.name = name;
        this.number = number;
        this.status = status;
        this.time = time;
        this.totalamount = totalamount;
        this.email = email;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setCity(String city) {
        City = city;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return Address;
    }

    public String getCity() {
        return City;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getStatus() {
        return status;
    }

    public String getTime() {
        return time;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public String getEmail() {
        return email;
    }
}
