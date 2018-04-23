package com.example.team38;

import android.support.annotation.Nullable;

/**
 * Created by anish on 4/23/18.
 */

public class UserFactory {

    public static User createUser(String name, String id, String password, AccountType accountType) {
        if (accountType == AccountType.ADMIN) {
            return new AdminUser(name, id, password);
        } else if (accountType == AccountType.HOMELESS_USER) {
            return new HomelessUser(name, id, password);
        } else if (accountType == AccountType.SHELTER_EMPLOYEE) {
            return new EmployeeUser(name, id, password);
        } else {
            throw new RuntimeException("Invalid user type");
        }
    }
    public static User createUser(UserInfoStrings infoStrings,
                @Nullable HomelessShelter shelter, int numSpots) {
        String name = infoStrings.name;
        String id = infoStrings.id;
        String password = infoStrings.password;
        AccountType accountType;
        switch(infoStrings.accountType) {
            case ("Homeless User") :
                accountType = AccountType.HOMELESS_USER;
                break;
            case ("Admin") :
                accountType = AccountType.ADMIN;
                break;
            case ("Shelter Employee") :
                accountType = AccountType.SHELTER_EMPLOYEE;
                break;
            default:
                accountType = AccountType.HOMELESS_USER;
        }
        User toReturn = createUser(name, id, password, accountType);
        toReturn.setShelter(shelter);
        toReturn.setNumSpots(numSpots);

        return toReturn;
    }
}
