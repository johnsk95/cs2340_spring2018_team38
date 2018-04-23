package com.example.team38;

/**
 * Created by anish on 4/23/18.
 */

public class AdminUser extends User {
    public AdminUser(String name, String id, String password) {
        this.name = name;
        this.id = id;
        this.accountType = AccountType.ADMIN;
        this.password = password;
    }
}
