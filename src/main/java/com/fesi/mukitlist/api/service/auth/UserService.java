package com.fesi.mukitlist.api.service.auth;

import com.fesi.mukitlist.api.controller.auth.request.ChangePasswordRequest;
import com.fesi.mukitlist.api.service.auth.request.UserServiceCreateRequest;
import com.fesi.mukitlist.domain.auth.User;
import com.fesi.mukitlist.api.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(UserServiceCreateRequest request) {
        // 이메일 UK 걸려있으면
        // Optional<User> existed = userRepository.findByEmail(email);
        // if(existed.isPresent()){
        //     throw new EmailExistedException(email);
        // }

        String encodePassword = passwordEncoder.encode(request.password()); // 해싱하는 부분
        User user = User.of(request, encodePassword);
        return userRepository.save(user);
    }

    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

//        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
//
//        // check if the current password is correct
//        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
//            throw new IllegalStateException("Wrong password");
//        }
//        // check if the two new passwords are the same
//        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
//            throw new IllegalStateException("Password are not the same");
//        }
//
//        // update the password
//        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
//
//        // save the new password
//        repository.save(user);
//    }
    }
}