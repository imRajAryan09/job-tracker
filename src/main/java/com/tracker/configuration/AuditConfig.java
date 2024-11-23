package com.tracker.configuration;

import com.tracker.entity.UserInfoEntity;
import lombok.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

/**
 * @author by Raj Aryan,
 * created on 06/10/2024
 */
@Configuration
@EnableJpaAuditing
public class AuditConfig implements AuditorAware<UUID> {

    @Override
    public @NonNull Optional<UUID> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        UserInfoEntity userPrincipal = (UserInfoEntity) authentication.getPrincipal();
        return Optional.ofNullable(userPrincipal.getUserId());
    }
}
