package com.example.usermanagement.domain;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Optional;

public class UserAccount {

    private static UserAccountRepository userAccountRepository = null;

    @Id
    private String email; //from request
    private String username; //from request - email
    private String password; //from request
    private String firstName; //from request
    private String lastName; //from request
    private String contactNumber; //from request
    private int age;
    private String gender;
    private String nationality;
    private String tags;
    private String status; // active/inactive/deletedØØ
    private LocalDateTime created;
    private LocalDateTime updated;

    private UserAgeStrategy userAgeStrategy;

    public UserAccount(UserAccountRepository accountRepo, UserAgeStrategy ageStrategy) {
        userAccountRepository = accountRepo;
        userAgeStrategy = ageStrategy;
    }

    public static UserAccount searchByEmail(String email) {
        Optional<UserAccount> userAccount = userAccountRepository.findById(email);
        return userAccount.get();
    }

    public static void registerAccount(UserAccount userAccount) {
        userAccountRepository.save(userAccount);
    }

    public static void unregisterAccount(String email) {
        userAccountRepository.deleteById(email);
    }

    public void modifyAccount(UserAccount userAccount) {
        //TODO ..map when exists
        this.username = userAccount.username;
        userAccountRepository.save(userAccount);
    }

    public int getAge() {
        return userAgeStrategy.getAge(this);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
