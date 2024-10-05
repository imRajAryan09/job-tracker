package com.tracker.authentication.jwt;

import com.tracker.authentication.user.UserInfo;
import com.tracker.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Objects;

/**
 * @author by Raj Aryan,
 * created on 27/09/2024
 */
@Component
@RequiredArgsConstructor
public class JwtTokenUtils {

    private final UserInfoRepository useruserInfoRepo;

    public String getUserName(Jwt jwtToken) {
        return jwtToken.getSubject();
    }

    public boolean isTokenValid(Jwt jwtToken, UserDetails userDetails) {
        final String userName = getUserName(jwtToken);
        boolean isTokenExpired = getIfTokenIsExpired(jwtToken);
        boolean isTokenUserSameAsDatabase = userName.equals(userDetails.getUsername());
        return !isTokenExpired && isTokenUserSameAsDatabase;

    }

    private boolean getIfTokenIsExpired(Jwt jwtToken) {
        return Objects.requireNonNull(jwtToken.getExpiresAt()).isBefore(Instant.now());
    }

    public UserDetails userDetails(String emailId) {
        return useruserInfoRepo
                .findByEmailId(emailId)
                .map(UserInfo::new)
                .orElseThrow(() -> new UsernameNotFoundException("UserEmail: " + emailId + " does not exist"));
    }
}
