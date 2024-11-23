package com.tracker.authentication.user;

import com.tracker.entity.UserInfoEntity;
import com.tracker.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author by Raj Aryan,
 * created on 26/09/2024
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserInfoService implements UserDetailsService {

    private final UserInfoRepository userInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
        return userInfoRepository
                .findByEmailId(emailId)
                .map(UserInfo::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void saveUser(UserInfoEntity userInfoEntity) {
        userInfoRepository.save(userInfoEntity);
    }

    public Optional<UserInfoEntity> getUserByEmailId(String emailId) {
        return userInfoRepository.findByEmailId(emailId);
    }
}
