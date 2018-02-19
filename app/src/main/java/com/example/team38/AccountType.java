package com.example.team38;

/**
 * Created by Nathaniel on 2/18/2018.
 *
 * enum of user types
 */

public enum AccountType {
    HOMELESS_USER("Homeless User"),
    ADMIN("Admin"),
    SHELTER_EMPLOYEE("Shelter Employee")
    ;
    private final String userType;

    AccountType(final String userType) { this.userType = userType; }

    @Override
    public String toString() {
        return userType;
    }
}
