package com.example.rohanspc.healthcare.Models;

public class User {
    String name;
    String email;
    String gender;
    String contact;

    public User(){

    }

    public User(String name, String email, String gender, String contact) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
