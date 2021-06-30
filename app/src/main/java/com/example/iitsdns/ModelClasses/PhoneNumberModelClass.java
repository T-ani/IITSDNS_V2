package com.example.iitsdns.ModelClasses;

public class PhoneNumberModelClass {
    private String phoneNumber1;
    private String phoneNumber2;

    public PhoneNumberModelClass(String phoneNumber1, String phoneNumber2) {
        this.phoneNumber1 = phoneNumber1;
        this.phoneNumber2 = phoneNumber2;
    }

    @Override
    public String toString() {
        return "PhoneNumberModelClass{" +
                "phoneNumber1='" + phoneNumber1 + '\'' +
                ", phoneNumber2='" + phoneNumber2 + '\'' +
                '}';
    }

    public String getPhoneNumber1() {
        return phoneNumber1;
    }

    public void setPhoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
    }

    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
    }
}