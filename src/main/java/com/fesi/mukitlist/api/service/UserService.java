package com.fesi.mukitlist.api.service;

import com.fesi.mukitlist.api.domain.User;
import com.fesi.mukitlist.api.repository.UserRepository;
import com.fesi.mukitlist.exception.EmailExistedException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {

        this.userRepository =userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String email, String name, String password, String companyName) throws EmailExistedException {
        Optional<User> existed = userRepository.findByEmail(email);
        if(existed.isPresent()){
            throw new EmailExistedException(email);
        }
        String encodePassword = passwordEncoder.encode(password); // 해싱하는 부분
        User user = User.builder()
                .email(email)
                .password(encodePassword)
                .name(name)
                .companyName(companyName)
                .build();
        return userRepository.save(user);
    }
}