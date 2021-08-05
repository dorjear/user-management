package com.example.usermanagement.service;

import com.example.usermanagement.service.bean.Age;
import com.example.usermanagement.service.bean.Countries;
import com.example.usermanagement.service.bean.Country;
import com.example.usermanagement.service.bean.Gender;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class Generator {

    private RestTemplate restTemplate = new RestTemplate();

    public int getAgeByName(String name) {
        var uriAge = "https://api.agify.io/?name=" + name;
        var resultForAge = Optional.ofNullable(restTemplate.getForObject(uriAge, Age.class)).get();
        return resultForAge.getAge();
    }

    public String getGenderByName(String name) {
        var uriGender = "https://api.genderize.io?name=" + name;
        var resultForGender = Optional.ofNullable(restTemplate.getForObject(uriGender, Gender.class)).get();
        return resultForGender.getGender();
    }

    public String getNationalityByName(String name) {
        var uriNationality = "https://api.nationalize.io/?name=" + name;
        var resultForNationality = Optional.ofNullable(restTemplate.getForObject(uriNationality, Countries.class)).get();
        return resultForNationality.getCountry().stream().map(Country::getCountry_id).findFirst().get();
    }
}
