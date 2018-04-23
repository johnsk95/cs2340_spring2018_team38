package com.example.team38;

public class HomelessUser extends User {
    public HomelessUser(String name, String id, String password) {
        super(name, id, password);
        this.setAccountType(AccountType.HOMELESS_USER);
    }
}
