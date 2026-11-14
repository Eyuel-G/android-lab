package com.example.lab1;

public class Contact {
    int id;
    String fname;
    String phone_number;

    public Contact() {
    }

    public Contact(int id, String name, String phone) {
        this.id = id;
        this.fname = name;
        this.phone_number = phone;
    }

    public Contact(String name, String phone) {
        this.fname = name;
        this.phone_number = phone;
    }

    public Contact(String name, String phone, int id) {
        this.fname = name;
        this.phone_number = phone;
        this.id = id;
    }

    public int getID() {
        return this.id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getName() {
        return this.fname;
    }

    public void setName(String name) {
        this.fname = name;
    }

    public String getPhoneNumber() {
        return this.phone_number;
    }

    public void setPhoneNumber(String phone) {
        this.phone_number = phone;
    }

    public String getPhone_number() {
        return this.phone_number;
    }

    public String get_fname() {
        return this.fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void set_phonenumber(String phone) {
        this.phone_number = phone;
    }
}
