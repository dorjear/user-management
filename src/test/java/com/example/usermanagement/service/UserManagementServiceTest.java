package com.example.usermanagement.service;

import com.example.usermanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserManagementServiceTest {

    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private Generator generatorMock;
    @InjectMocks
    private UserManagementService userManagementService;

    private User user;

    @BeforeEach
    public void setupMock() {
        MockitoAnnotations.openMocks(this);
        userManagementService = new UserManagementService(userRepositoryMock, generatorMock);
        this.user = new User();
        this.user.setEmail("123@gmail.com");
        this.user.setFirstName("first name");
        this.user.setLastName("last name");
        this.user.setContactNumber("238237");
        this.user.setTags(new String[]{"a","b"});
    }

    @Test
    void testAddNewUser() {
        when(generatorMock.getAgeByName(user.getFirstName())).thenReturn(34);
        when(generatorMock.getGenderByName(user.getFirstName())).thenReturn("male");
        when(generatorMock.getNationalityByName(user.getFirstName())).thenReturn("TH");
        when(userRepositoryMock.save(user)).thenReturn(user);

        userManagementService.addNewUser(user);

        assertEquals(user.getUsername(),user.getEmail());
        assertEquals(user.getStatus(),"Active");
    }

    @Test
    void testDeleteExistingUserByEmail() {
        when(userRepositoryMock.findById(user.getEmail())).thenReturn(Optional.of(user));
        doNothing().when(userRepositoryMock).deleteById(isA(String.class));
        userManagementService.deleteUserByEmail(user.getEmail());

        verify(userRepositoryMock, times(1)).deleteById(user.getEmail());
    }

    @Test
    void testDeleteNonExistingUserByEmail() {
        when(userRepositoryMock.findById(user.getEmail())).thenReturn(Optional.empty());
        userManagementService.deleteUserByEmail(user.getEmail());

        verify(userRepositoryMock, times(0)).deleteById(user.getEmail());
    }

    @Test
    void testUpdateExistingUser() {
        when(userRepositoryMock.findById(user.getEmail())).thenReturn(Optional.of(user));
        when(generatorMock.getAgeByName(user.getFirstName())).thenReturn(38);
        when(generatorMock.getGenderByName(user.getFirstName())).thenReturn("male");
        when(generatorMock.getNationalityByName(user.getFirstName())).thenReturn("GE");
        when(userRepositoryMock.save(user)).thenReturn(user);
        userManagementService.updateUser(user);

        assertEquals(user.getUsername(),user.getEmail());
        verify(userRepositoryMock, times(1)).save(user);
    }

    @Test
    void testUpdateNonExistingUser() {
        when(userRepositoryMock.findById(user.getEmail())).thenReturn(Optional.empty());
        Exception exception = assertThrows(EntityNotFoundException.class, () -> userManagementService.updateUser(user));

        String expectedMessage = "User email: " + user.getEmail() + " cannot be found in the database";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
    @Test
    void testGetExistingUser() {
        var dbUser = new User();
        LocalDateTime now = LocalDateTime.now();
        dbUser.setEmail("123@gmail.com");
        dbUser.setFirstName("first name");
        dbUser.setLastName("last name");
        dbUser.setContactNumber("238237");
        dbUser.setTags(new String[]{"a","b","c"});
        dbUser.setUpdated(now);
        dbUser.setCreated(now);
        dbUser.setAge(45);
        dbUser.setGender("female");
        dbUser.setNationality("CN");

        when(userRepositoryMock.findById(user.getEmail())).thenReturn(Optional.of(dbUser));
        var actualUserOut = userManagementService.getUser(user.getEmail());

        assertEquals(dbUser.getAge(), actualUserOut.getAge());
        assertEquals(dbUser.getGender(), actualUserOut.getGender());
        assertEquals(dbUser.getEmail(),actualUserOut.getEmail());
        assertEquals(dbUser.getContactNumber(), actualUserOut.getContactNumber());
        assertEquals(dbUser.getNationality(), actualUserOut.getNationality());
        assertEquals(dbUser.getFirstName(), actualUserOut.getFirstName());
        assertEquals(dbUser.getLastName(), actualUserOut.getLastName());
        assertEquals(dbUser.getTags(),String.join(":", actualUserOut.getTags()));
        assertEquals(dbUser.getPassword(),actualUserOut.getPassword());
        assertEquals(dbUser.getUpdated(),now);
    }

    @Test
    void testGetNonExistingUser() {
        when(userRepositoryMock.findById(user.getEmail())).thenReturn(Optional.empty());
        Exception exception = assertThrows(EntityNotFoundException.class, () ->
            userManagementService.getUser(user.getEmail())
        );

        String expectedMessage = "User email: " + user.getEmail() + " cannot be found in the database";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    void testPaginateUserList() {
        var dbUserList = new ArrayList<User>();
        var user1 = new User();
        user1.setEmail("123@gmail.com");
        user1.setUsername(user1.getEmail());
        user1.setPassword("239923");
        user1.setFirstName("first name");
        user1.setLastName("last name");
        user1.setContactNumber("238237");
        user1.setTags(new String[]{"a","b","c"});
        user1.setUpdated(LocalDateTime.now());
        user1.setCreated(LocalDateTime.now());
        user1.setAge(45);
        user1.setGender("female");
        user1.setNationality("CN");
        user1.setStatus("Active");

        var user2 = new User();

        user2.setEmail("222@gmail.com");
        user2.setUsername(user1.getEmail());
        user2.setPassword("2324323");
        user2.setFirstName("first2 name");
        user2.setLastName("last2 name");
        user2.setContactNumber("23823237");
        user2.setTags(new String[]{"a","b"});
        user2.setUpdated(LocalDateTime.now());
        user2.setCreated(LocalDateTime.now());
        user2.setAge(65);
        user2.setGender("male");
        user2.setNationality("CN");
        user2.setStatus("Active");

        dbUserList.add(user1);
        dbUserList.add(user2);

        when(userRepositoryMock.findAll()).thenReturn(dbUserList);
        var pageList = userManagementService.paginateUserList(1,5);

        assertEquals(1, pageList.getPage());
        assertEquals(5, pageList.getPageSize());
        assertEquals(1,pageList.getTotalPage());
        assertEquals(user1.getEmail(), pageList.getUsers().get(0).getEmail());
        assertEquals(user2.getEmail(), pageList.getUsers().get(1).getEmail());
    }


}