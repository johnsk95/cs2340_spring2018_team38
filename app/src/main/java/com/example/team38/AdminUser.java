package com.example.team38;

public class AdminUser extends User {
    public AdminUser(String name, String id, String password, String email) {
        super(name, id, password, email);
        this.setAccountType(AccountType.ADMIN);
    }
}
