package com.example.usermanagement.controller;

import com.example.usermanagement.service.CustomPage;
import com.example.usermanagement.service.User;
import com.example.usermanagement.service.UserManagementService;
import com.example.usermanagement.service.bean.UserOut;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = UserManagementController.class)
class UserManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserManagementService userServiceMock;

    private UserOut userOut;

    @BeforeEach
    public void setupMock() {
        MockitoAnnotations.openMocks(this);
        this.userOut = new UserOut(
                "1224214",
                "fist name",
                "last name",
                "123@gmail.com",
                "1234",
                0,
                null,
                null,
                "a:b:c");
    }


    @Test
    void testAddNewUser() throws Exception {
        doNothing().when(userServiceMock).addNewUser(isA(User.class));
        this.mockMvc.perform(post("/api/user-management/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userOut)))
                .andExpect(status().isCreated());
    }

    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(userServiceMock).deleteUserByEmail(userOut.getEmail());
        this.mockMvc.perform(delete("/api/user-management/user/{email}",userOut.getEmail())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteNonExistingUser() throws Exception {
        doThrow(new EntityNotFoundException()).when(userServiceMock).deleteUserByEmail(userOut.getEmail());
        this.mockMvc.perform(delete("/api/user-management/user/{email}",userOut.getEmail())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testUpdateUser() throws Exception {
        doNothing().when(userServiceMock).updateUser(isA(User.class));
        this.mockMvc.perform(put("/api/user-management/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userOut)))
                .andExpect(status().isOk());
    }

    @Test
    void testPaginateUserList() throws Exception {

        var userOutList = new ArrayList<UserOut>();
        var user1 = new UserOut(
                "239923",
                "first name",
                "last name",
                "123@gmail.com",
                "238237",
                45,
                "female",
                "CN",
                "a:b:c"
        );

        var user2 = new UserOut(
                "2324323",
                "first2 name",
                "last2 name",
                "222@gmail.com",
                "23823237",
                65,
                "male",
                "CN",
                "a:b:c");

        userOutList.add(user1);
        userOutList.add(user2);

        int pageNo = 1;
        int pageSize = 2;

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        PageImpl<UserOut> pageList = new PageImpl<>(userOutList, pageable, userOutList.size());
        when(userServiceMock.paginateUserList(pageNo, pageSize)).thenReturn(new CustomPage<>(pageList));

        this.mockMvc.perform(get("/api/user-management/user/list")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", String.valueOf(pageNo))
                .param("pageSize", String.valueOf(pageSize)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(pageNo))
                .andExpect(jsonPath("$.pageSize").value(pageSize))
                .andExpect(jsonPath("$.users[0].firstName").value(user1.getFirstName()))
                .andExpect(jsonPath("$.users[1].password").value(user2.getPassword()));
    }

    @Test
    void testGetUser() throws Exception {
        when(userServiceMock.getUser(userOut.getEmail())).thenReturn(userOut);

        this.mockMvc.perform(get("/api/user-management/user/{email}",userOut.getEmail())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email").value(userOut.getEmail()));

    }
}