package com.raju.onlinebookstore.Models;

public class User_data {
    String Address,Moblienumber,Username,city,email,gender,image;
    User_data(){

    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setMoblienumber(String moblienumber) {
        Moblienumber = moblienumber;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMoblienumber() {
        return Moblienumber;
    }

    public String getUsername() {
        return Username;
    }

    public String getCity() {
        return city;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getImage() {
        return image;
    }

    public User_data(String address, String moblienumber, String username, String city, String email, String gender, String image) {
        Address = address;
        Moblienumber = moblienumber;
        Username = username;
        this.city = city;
        this.email = email;
        this.gender = gender;
        this.image = image;
    }
}
