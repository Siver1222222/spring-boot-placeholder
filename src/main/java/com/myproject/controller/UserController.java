package com.example.socialhub.controller;

import com.example.socialhub.dto.UserRequest;
import com.example.socialhub.dto.UserResponse;
import com.example.socialhub.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get all users")
    @GetMapping
    public List<UserResponse> getAll() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @Operation(summary = "Create new user")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Validated @RequestBody UserRequest request) {
        return userService.createUser(request);
    }

    @Operation(summary = "Update existing user")
    @PutMapping("/{id}")
    public UserResponse update(@PathVariable Long id, @Validated @RequestBody UserRequest request) {
        return userService.updateUser(id, request);
    }

    @Operation(summary = "Delete user by ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}