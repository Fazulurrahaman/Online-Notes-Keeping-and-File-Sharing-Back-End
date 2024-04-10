package com.notes.service;

import com.notes.models.LoginDto;
import com.notes.models.User;

public interface AuthService {
    String login(LoginDto loginDto);

	User saveUser(User userRequest);
}