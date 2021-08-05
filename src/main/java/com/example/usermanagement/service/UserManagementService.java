package com.example.usermanagement.service;

import com.example.usermanagement.repository.UserRepository;
import com.example.usermanagement.service.bean.UserOut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class UserManagementService {

    Logger logger = LoggerFactory.getLogger(UserManagementService.class);

    final static String STATUS_ACTIVE = "Active";

    private final UserRepository userRepository;

    private final Generator generator;

    public UserManagementService(UserRepository userRepository, Generator generator) {
        this.userRepository = userRepository;
        this.generator = generator;
    }

    public void addNewUser(User user) {
        user.setUsername(user.getEmail());
        user.setAge(generator.getAgeByName(user.getFirstName()));
        user.setGender(generator.getGenderByName(user.getFirstName()));
        user.setNationality(generator.getNationalityByName(user.getFirstName()));
        user.setStatus(STATUS_ACTIVE);

        LocalDateTime now = LocalDateTime.now();
        user.setCreated(LocalDateTime.now());
        user.setUpdated(LocalDateTime.now());
        userRepository.save(user);
    }

    public void deleteUserByEmail(String email) {
        if (userRepository.findById(email).isPresent())
            userRepository.deleteById(email);
    }

    public void updateUser(User user) {
        if (userRepository.findById(user.getEmail()).isPresent()) {
            var dbUser = userRepository.findById(user.getEmail()).get();
            user.setUsername(user.getEmail());
            user.setAge(generator.getAgeByName(user.getFirstName()));
            user.setGender(generator.getGenderByName(user.getFirstName()));
            user.setNationality(generator.getNationalityByName(user.getFirstName()));
            user.setStatus(dbUser.getStatus());
            user.setCreated(dbUser.getCreated());
            user.setUpdated(LocalDateTime.now());
            userRepository.save(user);
        } else
            throw new EntityNotFoundException("User email: " + user.getEmail() + " cannot be found in the database");
    }

    public UserOut getUser(String email) {
        if(userRepository.findById(email).isPresent()) {
            var dbUser = userRepository.findById(email).get();
            return new UserOut(
                    dbUser.getPassword(),
                    dbUser.getFirstName(),
                    dbUser.getLastName(),
                    dbUser.getEmail(),
                    dbUser.getContactNumber(),
                    dbUser.getAge(),
                    dbUser.getGender(),
                    dbUser.getNationality(),
                    dbUser.getTags());
        } else
            throw new EntityNotFoundException("User email: " + email + " cannot be found in the database");
    }

    public CustomPage<UserOut> paginateUserList(int pageNo, int pageSize){
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        var userOutList = userRepository.findAll().stream().map(
                user -> new UserOut(
                        user.getPassword(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getContactNumber(),
                        user.getAge(),
                        user.getGender(),
                        user.getNationality(),
                        user.getTags()))
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), userOutList.size());
        PageImpl<UserOut> page;
        if(start < end)
            page = new PageImpl<>(userOutList.subList(start, end), pageable, userOutList.size());
        else
            throw new EntityNotFoundException("Page Not Existing");

        return new CustomPage<>(page);
    }


}
