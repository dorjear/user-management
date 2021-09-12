package com.example.usermanagement.controller;

import com.example.usermanagement.domain.UserAccount;
import com.example.usermanagement.domain.ManagementService;
import com.example.usermanagement.service.CustomPage;
import com.example.usermanagement.service.UserManagementService;
import com.example.usermanagement.service.bean.UserOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserManagementController {

    @Autowired
    private UserManagementService userService;

    @Autowired
    private ManagementService managementService;

    @PostMapping("/api/user-management/user")
    @ResponseStatus(HttpStatus.CREATED)
    public void addNewUser(@RequestBody UserAccount userAccount) {
        managementService.addNewUser(userAccount);
    }

    @DeleteMapping("/api/user-management/user/{email}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable String email){
        managementService.deleteUserByEmail(email);
    }

    @PutMapping("/api/user-management/user")
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@RequestBody UserAccount userAccount){
        managementService.updateUser(userAccount);
    }

    @GetMapping("/api/user-management/user/list")
    @ResponseStatus(HttpStatus.OK)
    public CustomPage<UserOut> paginateUserList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int pageSize){
        return userService.paginateUserList(page, pageSize);
    }

    @GetMapping("/api/user-management/user/{email}")
    @ResponseStatus(HttpStatus.OK)
    public UserAccount getUser(@PathVariable String email){
        return managementService.getUser(email);
    }

}
