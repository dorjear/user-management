package com.example.usermanagement.domain;

public class ManagementService {

    public void addNewUser(UserAccount userAccount){
        UserAccount.registerAccount(userAccount);
    }
    public void deleteUserByEmail(String email){
        UserAccount.unregisterAccount(email);
    }

    public void updateUser(UserAccount userAccount){
       UserAccount
               .searchByEmail(userAccount.getEmail())
               .modifyAccount(userAccount);
    }

    public UserAccount getUser(String email){
        UserAccount userAccount = UserAccount.searchByEmail(email);
        return userAccount;
    }
}
