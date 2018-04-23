package com.example.team38;

/**
 * Created by anish on 4/23/18.
 */

public class HomelessUser extends User {
    public HomelessUser(String name, String id, String password) {
        this.name = name;
        this.id = id;
        this.accountType = AccountType.HOMELESS_USER;
        this.password = password;
    }
}
