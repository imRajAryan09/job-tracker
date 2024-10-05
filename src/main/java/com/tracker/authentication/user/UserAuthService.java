package com.tracker.authentication.user;

import com.tracker.authentication.jwt.JwtTokenGenerator;
import com.tracker.dto.request.UserRegistrationDto;
import com.tracker.dto.response.AuthResponseDto;
import com.tracker.entity.RefreshTokenEntity;
import com.tracker.entity.UserInfoEntity;
import com.tracker.enums.TokenType;
import com.tracker.repository.RefreshTokenRepository;
import com.tracker.repository.UserInfoRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author by Raj Aryan,
 * created on 27/09/2024
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserInfoRepository userInfoRepository;

    private final JwtTokenGenerator jwtTokenGenerator;

    private final PasswordEncoder passwordEncoder;


    public AuthResponseDto getJwtTokensAfterAuthentication(Authentication authentication, HttpServletResponse response) {
        try {
            var userInfoEntity = userInfoRepository.findByEmailId(authentication.getName())
                    .orElseThrow(() -> {
                        log.error("AuthService :: userSignInAuth User: {} not found", authentication.getName());
                        return new ResponseStatusException(HttpStatus.NOT_FOUND, "USER NOT FOUND ");
                    });


            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);

            saveUserRefreshToken(userInfoEntity, refreshToken);

            creatRefreshTokenCookie(response, refreshToken);
            log.info("AuthService :: userSignInAuth Access token for user:{}, has been generated", userInfoEntity.getUserName());
            return AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(15 * 60)
                    .userName(userInfoEntity.getUserName())
                    .tokenType(TokenType.BEARER.getName())
                    .build();


        } catch (Exception e) {
            log.error("AuthService :: userSignInAuth Exception while authenticating the user due to : {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Please Try Again");
        }
    }

    private void saveUserRefreshToken(UserInfoEntity userInfoEntity, String refreshToken) {
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .user(userInfoEntity)
                .refreshToken(refreshToken)
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshTokenEntity);
    }

    private void creatRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setMaxAge(15 * 24 * 60 * 60); // in seconds
        response.addCookie(refreshTokenCookie);
    }

    public AuthResponseDto getAccessTokenUsingRefreshToken(String authorizationHeader) {

        if (!authorizationHeader.startsWith(TokenType.BEARER.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token type");
        }

        final String refreshToken = authorizationHeader.substring(7);

        //Find refreshToken from database and should not be revoked : Same thing can be done through filter.
        var refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken)
                .filter(tokens -> !tokens.isRevoked())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Refresh token revoked"));

        UserInfoEntity userInfoEntity = refreshTokenEntity.getUser();

        //Now create the Authentication object
        Authentication authentication = createAuthenticationObject(userInfoEntity);

        //Use the authentication object to generate new accessToken as the Authentication object that we will have may not contain correct role.
        String accessToken = jwtTokenGenerator.generateAccessToken(authentication);

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .accessTokenExpiry(5 * 60)
                .userName(userInfoEntity.getUserName())
                .tokenType(TokenType.BEARER.getName())
                .build();
    }

    private static Authentication createAuthenticationObject(UserInfoEntity userInfoEntity) {
        // Extract user details from UserDetailsEntity
        String username = userInfoEntity.getEmailId();
        String password = userInfoEntity.getPassword();
        String roles = userInfoEntity.getRoles();

        // Extract authorities from roles (comma-separated)
        String[] roleArray = roles.split(",");
        GrantedAuthority[] authorities = Arrays.stream(roleArray)
                .map(role -> (GrantedAuthority) role::trim)
                .toArray(GrantedAuthority[]::new);

        return new UsernamePasswordAuthenticationToken(username, password, Arrays.asList(authorities));
    }

    public AuthResponseDto registerUser(UserRegistrationDto userRegistrationDto, HttpServletResponse httpServletResponse) {
        try {
            log.info(" AuthService :: registerUser User registration started for request {}", userRegistrationDto);

            Optional<UserInfoEntity> user = userInfoRepository.findByEmailId(userRegistrationDto.userEmail());
            if (user.isPresent()) {
                throw new DuplicateKeyException("User Already Exist");
            }

            UserInfoEntity userInfoEntity = convertToEntity(userRegistrationDto);
            Authentication authentication = createAuthenticationObject(userInfoEntity);


            // Generate a JWT token
            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);

            UserInfoEntity savedUserDetails = userInfoRepository.save(userInfoEntity);
            saveUserRefreshToken(userInfoEntity, refreshToken);

            creatRefreshTokenCookie(httpServletResponse, refreshToken);

            log.info("AuthService :: registerUser User:{} is successfully registered", savedUserDetails.getUserName());
            return AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(5 * 60)
                    .userName(savedUserDetails.getUserName())
                    .tokenType(TokenType.BEARER.getName())
                    .build();


        } catch (Exception e) {
            log.error("AuthService :: registerUser Exception while registering the user due to : ", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // TODO : use object mapper
    private UserInfoEntity convertToEntity(UserRegistrationDto userRegistrationDto) {
        UserInfoEntity userInfoEntity = new UserInfoEntity();
        userInfoEntity.setUserName(userRegistrationDto.userName());
        userInfoEntity.setEmailId(userRegistrationDto.userEmail());
        userInfoEntity.setRoles(userRegistrationDto.userRole());
        userInfoEntity.setPassword(passwordEncoder.encode(userRegistrationDto.userPassword()));
        return userInfoEntity;
    }
}
