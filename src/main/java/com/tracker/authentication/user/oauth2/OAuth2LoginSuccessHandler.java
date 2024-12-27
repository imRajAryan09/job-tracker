package com.tracker.authentication.user.oauth2;

import com.tracker.authentication.jwt.JwtTokenGenerator;
import com.tracker.authentication.user.UserAuthService;
import com.tracker.authentication.user.UserInfoService;
import com.tracker.entity.RoleEntity;
import com.tracker.entity.UserInfoEntity;
import com.tracker.enums.Role;
import com.tracker.exception.OAuth2AuthenticationProcessingException;
import com.tracker.repository.RoleRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author by Raj Aryan,
 * created on 20/10/2024
 */
@Slf4j
@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final RoleRepository roleRepository;

    @Lazy
    private final JwtTokenGenerator jwtTokenGenerator;

    @Value("${frontend.url}")
    private String frontendUrl;

    private final UserInfoService userService;

    public OAuth2LoginSuccessHandler(RoleRepository roleRepository,
                                     @Lazy JwtTokenGenerator jwtTokenGenerator,
                                     UserInfoService userService) {
        this.roleRepository = roleRepository;
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        // Check the provider
        String registrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
        DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = principal.getAttributes();

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, attributes);

        String email = userInfo.getEmail();
        String username = userInfo.getName();
        log.info("HELLO OAUTH: {} : {} : {}", email, username, userInfo.getId());

        userService.getUserByEmailId(email)
                .ifPresentOrElse(user -> {
                    if (!user.getPictureUrl().equals(userInfo.getImageUrl())) {
                        user.setPictureUrl(userInfo.getImageUrl());
                        userService.saveUser(user);
                    }
                    Authentication securityAuth = getAuthenticationToken(user, attributes, oAuth2AuthenticationToken);
                    SecurityContextHolder.getContext().setAuthentication(securityAuth);
                }, () -> processNewUser(email, userInfo, oAuth2AuthenticationToken));

        // JWT Token Logic
        String jwtToken = generateJwtToken(email);
        String targetUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/oauth2/redirect")
                .queryParam("token", jwtToken)
                .build()
                .toUriString();

        this.setAlwaysUseDefaultTargetUrl(true);
        this.setDefaultTargetUrl(targetUrl);
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private void processNewUser(String email, OAuth2UserInfo userInfo, OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        UserInfoEntity newUser = new UserInfoEntity();
        Optional<RoleEntity> userRole = roleRepository.findByRoleName(Role.USER); // Fetch existing role
        if (userRole.isPresent()) {
            newUser.setRole(userRole.get());
        } else {
            throw new OAuth2AuthenticationProcessingException("Default role not found");
        }
        newUser.setEmailId(email);
        newUser.setUserName(userInfo.getName());
        newUser.setProvider(userInfo.getProvider());
        userService.saveUser(newUser);

        Authentication authenticationToken = getAuthenticationToken(newUser, userInfo.getAttributes(), oAuth2AuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private Authentication getAuthenticationToken(UserInfoEntity newUser, Map<String, Object> attributes, OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        DefaultOAuth2User oauthUser = new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority(newUser.getRole().getRoleName().name())),
                attributes,
                newUser.getUserName()
        );
        return new OAuth2AuthenticationToken(
                oauthUser,
                List.of(new SimpleGrantedAuthority(newUser.getRole().getRoleName().name())),
                oAuth2AuthenticationToken.getAuthorizedClientRegistrationId()
        );
    }

    private String generateJwtToken(String email) {
        UserInfoEntity user = userService.getUserByEmailId(email).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        Authentication authenticationToken = UserAuthService.createAuthenticationObject(user);
        return jwtTokenGenerator.generateAccessToken(authenticationToken);
    }
}

