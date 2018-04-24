package com.example.team38;

public class EmployeeUser extends User {
    public EmployeeUser(String name, String id, String password, String email) {
        super(name, id, password, email);
        this.setAccountType(AccountType.SHELTER_EMPLOYEE);
    }
}
