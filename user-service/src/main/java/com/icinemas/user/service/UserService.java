package com.icinemas.user.service;

import com.icinemas.enums.UserRole;
import com.icinemas.model.AuthResponse;
import com.icinemas.model.User;
import com.icinemas.user.exception.InvalidFieldException;
import com.icinemas.user.exception.UnauthorizedException;
import com.icinemas.user.repository.UserRepository;
import com.icinemas.user.util.PasswordUtil;
import com.icinemas.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public AuthResponse authenticateUser(final User user) {
        User dbUser = userRepository.findById(user.getUserId()).
                orElseThrow(() -> new UnauthorizedException("user not found"));
        boolean isValidPassword = PasswordUtil.checkPassword(user.getPassword(), dbUser.getPassword());
        if (!isValidPassword) {
            throw new UnauthorizedException("invalid password");
        }
         String token = JwtUtil.generateToken(dbUser.getUserId(),dbUser.getRole());
        return new AuthResponse(token);
    }

    public ResponseEntity<String> registerUser(final User user){
        user.setRole(UserRole.USER);
        if(userRepository.existsById(user.getUserId())){
            throw new InvalidFieldException("user already exists");

        }
        encodePassword(user);
        User registeredUser = userRepository.save(user);
        return ResponseEntity.ok(registeredUser.getUserId());
    }

    private void encodePassword(User user) {
        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
    }
}

