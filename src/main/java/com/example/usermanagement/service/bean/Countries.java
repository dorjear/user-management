package com.example.usermanagement.service.bean;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class Countries {

    private String name;

    @Autowired
    private List<Country> country = new ArrayList<>();

    public List<Country> getCountry() {
        return country;
    }

    public void setCountry(List<Country> country) {
        this.country = country;
    }

}
