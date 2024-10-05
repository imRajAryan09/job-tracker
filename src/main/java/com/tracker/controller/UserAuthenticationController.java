package com.tracker.controller;

import com.tracker.authentication.user.UserAuthService;
import com.tracker.constants.ApiEndPoint;
import com.tracker.dto.request.UserRegistrationDto;
import com.tracker.dto.response.AuthResponseDto;
import com.tracker.dto.response.SuccessResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author by Raj Aryan,
 * created on 27/09/2024
 */
@Slf4j
@RestController
@RequestMapping(ApiEndPoint.Authentication.BASE)
public class UserAuthenticationController {

    private final UserAuthService userAuthService;

    @Autowired
    public UserAuthenticationController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @PostMapping(ApiEndPoint.Authentication.SIGN_IN)
    public SuccessResponse<AuthResponseDto> authenticateUser(Authentication authentication, HttpServletResponse response) {
        return new SuccessResponse<>(userAuthService.getJwtTokensAfterAuthentication(authentication, response));
    }

    @PreAuthorize("hasAuthority('SCOPE_REFRESH_TOKEN')")
    @PostMapping(ApiEndPoint.Authentication.REFRESH_TOKEN)
    public SuccessResponse<AuthResponseDto> getAccessToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return new SuccessResponse<>(userAuthService.getAccessTokenUsingRefreshToken(authorizationHeader));
    }

    @PostMapping(ApiEndPoint.Authentication.SIGN_UP)
    public SuccessResponse<?> registerUser(@Valid @RequestBody UserRegistrationDto userRegistrationDto,
                                           BindingResult bindingResult,
                                           HttpServletResponse httpServletResponse) {

        log.info("AuthController :: registerUser Signup Process Started for user:{}", userRegistrationDto.userName());
        if (bindingResult.hasErrors()) {
            List<String> errorMessage = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            log.error("AuthController:: registerUser Errors in user registration request :{}", errorMessage);
            return new SuccessResponse<>(HttpStatus.BAD_REQUEST, errorMessage);
        }
        return new SuccessResponse<>(userAuthService.registerUser(userRegistrationDto, httpServletResponse));
    }
}
