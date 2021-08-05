package com.example.usermanagement.service;

import com.example.usermanagement.service.bean.Age;
import com.example.usermanagement.service.bean.Countries;
import com.example.usermanagement.service.bean.Country;
import com.example.usermanagement.service.bean.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GeneratorTest {

    @Mock
    private RestTemplate restTemplateMock = new RestTemplate();

    @InjectMocks
    private Generator generatorMock;

    @BeforeEach
    public void setupMock() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAgeByName() {
        var age = new Age();
        age.setAge(45);
        when(restTemplateMock.getForObject("https://api.agify.io/?name=fistName", Age.class)).thenReturn(age);
        var actualAge = generatorMock.getAgeByName("fistName");
        assertEquals(age.getAge(),actualAge);
    }

    @Test
    void testGetGenderByName() {
        var gender = new Gender();
        gender.setGender("female");
        when(restTemplateMock.getForObject("https://api.genderize.io?name=fistName", Gender.class)).thenReturn(gender);
        var actualGender = generatorMock.getGenderByName("fistName");
        assertEquals(gender.getGender(),actualGender);
    }

    @Test
    void testGetNationalityByName() {
        var countries = new Countries();
        var country1 = new Country();
        var country2 = new Country();
        country1.setCountry_id("CN");
        country2.setCountry_id("TH");
        List<Country> countryList = new ArrayList<>(
                Arrays.asList(country1, country2));
        countries.setCountry(countryList);

        when(restTemplateMock.getForObject("https://api.nationalize.io/?name=firstName", Countries.class)).thenReturn(countries);
        var actualNationality = generatorMock.getNationalityByName("firstName");
        assertEquals(countries.getCountry().get(0).getCountry_id(), actualNationality);
    }
}