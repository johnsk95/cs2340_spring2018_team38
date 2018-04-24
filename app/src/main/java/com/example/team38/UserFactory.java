package com.example.team38;

public class UserFactory {

    public static User createUser(String name, String id, String password, String email,
                                  AccountType accountType) {
        if (accountType == AccountType.ADMIN) {
            return new AdminUser(name, id, password, email);
        } else if (accountType == AccountType.HOMELESS_USER) {
            return new HomelessUser(name, id, password, email);
        } else if (accountType == AccountType.SHELTER_EMPLOYEE) {
            return new EmployeeUser(name, id, password, email);
        } else {
            throw new RuntimeException("Invalid user type");
        }
    }
}
