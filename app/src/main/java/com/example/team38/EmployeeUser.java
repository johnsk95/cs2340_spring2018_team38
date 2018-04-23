package com.example.team38;

/**
 * Created by anish on 4/23/18.
 */

public class EmployeeUser extends User {
    public EmployeeUser(String name, String id, String password) {
        this.name = name;
        this.id = id;
        this.accountType = AccountType.SHELTER_EMPLOYEE;
        this.password = password;
    }
}
