package com.example.usermanagement.domain;

public class AgePredictionStrategy extends UserAgeStrategy{

    @Override
    int getAge(UserAccount userAccount) {
        //TODO Call Agify.io with accoutn name
        return 0;
    }
}
