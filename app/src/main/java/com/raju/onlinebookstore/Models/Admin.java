package com.raju.onlinebookstore.Models;

public class Admin {
    private String adminuser,name;
    private String  password;
    Admin(){

    }

    public Admin(String adminuser, String name, String password) {
        this.adminuser = adminuser;
        this.name = name;
        this.password = password;
    }

    public void setAdminuser(String adminuser) {
        this.adminuser = adminuser;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdminuser() {
        return adminuser;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
