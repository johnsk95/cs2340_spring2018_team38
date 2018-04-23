package com.example.team38;

public class AdminUser extends User {
    public AdminUser(String name, String id, String password) {
        super(name, id, password);
        this.setAccountType(AccountType.ADMIN);
    }
}
