package com.example.team38;

public class EmployeeUser extends User {
    public EmployeeUser(String name, String id, String password) {
        super(name, id, password);
        this.setAccountType(AccountType.SHELTER_EMPLOYEE);
    }
}
