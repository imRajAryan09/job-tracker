package com.tracker.authentication.jwt;

import com.tracker.constants.AppConstant;
import com.tracker.enums.Permission;
import com.tracker.enums.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author by Raj Aryan,
 * created on 27/09/2024
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenGenerator {

    @Value("${jwt.access.token.expiry.minutes}")
    private int accessTokenExpiryMinutes;

    @Value("${jwt.refresh.token.expiry.days}")
    private int refreshTokenExpiryDays;

    private final JwtEncoder jwtEncoder;

    public String generateAccessToken(Authentication authentication) {

        log.info("JwtTokenGenerator :: Access Token Creation Started for:{}", authentication.getName());

        String roles = getUserRole(authentication);

        String permissions = getPermissionsFromRoles(roles);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(AppConstant.Jwt.ISSUER)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(accessTokenExpiryMinutes, ChronoUnit.MINUTES))
                .subject(authentication.getName())
                .claim(AppConstant.Jwt.SCOPE, permissions)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }


    public String generateRefreshToken(Authentication authentication) {

        log.info("JwtTokenGenerator :: Refresh Token Creation Started for:{}", authentication.getName());

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(AppConstant.Jwt.ISSUER)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(refreshTokenExpiryDays, ChronoUnit.DAYS))
                .subject(authentication.getName())
                .claim(AppConstant.Jwt.SCOPE, AppConstant.Jwt.REFRESH_TOKEN)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private static String getUserRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(StringUtils.EMPTY));
    }

    private String getPermissionsFromRoles(String roles) {
        Set<String> permissions = new HashSet<>();

        if (roles.contains(Role.ADMIN.name())) {
            permissions.addAll(List.of(Permission.READ.name(), Permission.WRITE.name(), Permission.DELETE.name()));
        }
        if (roles.contains(Role.USER.name())) {
            permissions.add(Permission.READ.name());
        }
        if (roles.contains(Role.GUEST.name())) {
            permissions.add(Permission.READ.name());
        }

        return String.join(StringUtils.EMPTY, permissions);
    }
}
