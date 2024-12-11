package com.fesi.mukitlist.domain.service.oauth;

import com.fesi.mukitlist.api.exception.AppException;
import com.fesi.mukitlist.api.repository.UserRepository;
import com.fesi.mukitlist.core.auth.PrincipalDetails;
import com.fesi.mukitlist.core.auth.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.fesi.mukitlist.api.exception.ExceptionCode.NOT_FOUND_USER;

@Service
@RequiredArgsConstructor
public class PrincipalService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new AppException(NOT_FOUND_USER));

        return new PrincipalDetails(user);
    }
}
