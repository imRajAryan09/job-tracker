package com.tracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author by Raj Aryan,
 * created on 26/09/2024
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_info")
public class UserInfoEntity extends BaseEntity implements Serializable {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(nullable = false, name = "email_id", unique = true)
    private String emailId;

    @Column(nullable = false, name = "roles")
    private String roles;

    @Column(nullable = false, name = "password")
    private String password;
}
