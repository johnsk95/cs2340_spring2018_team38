package com.example.team38;

public class HomelessUser extends User {
    public HomelessUser(String name, String id, String password, String email) {
        super(name, id, password, email);
        this.setAccountType(AccountType.HOMELESS_USER);
    }
}
