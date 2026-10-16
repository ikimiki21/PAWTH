package com.pawth.service;

import com.pawth.dao.UserDAO;
import com.pawth.model.User;

public class AuthService {

    private final UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public User login(String email, String rawPassword, String workspaceRole) {
        return userDAO.authenticate(email, rawPassword, workspaceRole);
    }
}