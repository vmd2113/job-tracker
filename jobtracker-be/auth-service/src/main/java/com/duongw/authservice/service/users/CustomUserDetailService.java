package com.duongw.authservice.service.users;

import com.duongw.authservice.client.CommonClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final CommonClient commonClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Load user by username: {}", username);
        UserDetails userDetails = commonClient.getUserDetailByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (userDetails == null) {
            throw new UsernameNotFoundException("User not found");
        }
        log.info("User found: {}, username: {}, password: {}", userDetails, userDetails.getUsername(), userDetails.getPassword());
        return userDetails;
    }
}
