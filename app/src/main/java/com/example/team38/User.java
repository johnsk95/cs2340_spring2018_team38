package com.example.team38;

/**
 * Created by Nathaniel on 2/18/2018.
 * Information Holder - hold the information for a user of the app
 */

public class User {

    //the name of the user
    private static String name;
    //the loginID of the user
    private static String id;
    //what account type the user is
    private AccountType accountType;
    //the password for the user's account
    private String password;

    public User(String name, String id, String password, AccountType accountType) {
        this.name = name;
        this.id = id;
        this.accountType = accountType;
        this.password = password;
    }

    @Override
    public String toString() {
        return "name: " + name + "  id: " + id + "  account type: " + accountType.toString() + "  password: " + password;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public AccountType getAccountType() {
        return  accountType;
    }

    public String getPassword() {
        return password;
    }
}
