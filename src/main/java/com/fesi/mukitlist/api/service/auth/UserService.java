package com.fesi.mukitlist.api.service.auth;

import com.fesi.mukitlist.api.controller.auth.request.UserCreateRequest;
import com.fesi.mukitlist.api.service.auth.request.UserServiceCreateRequest;
import com.fesi.mukitlist.api.service.gathering.response.GatheringResponse;
import com.fesi.mukitlist.domain.auth.User;
import com.fesi.mukitlist.api.repository.UserRepository;
import com.fesi.mukitlist.exception.EmailExistedException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(UserServiceCreateRequest request) {
        String encodePassword = passwordEncoder.encode(request.password()); // 해싱하는 부분
        User user = User.of(request, encodePassword);
        return userRepository.save(user);
    }

}