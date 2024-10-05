package com.tracker.repository;

import com.tracker.entity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoEntity, UUID> {
    Optional<UserInfoEntity> findByEmailId(String emailId);
}