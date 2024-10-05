package com.tracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author by Raj Aryan,
 * created on 02/10/2024
 */

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contacts")
public class ContactEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contactId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserInfoEntity user;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private JobEntity job;

    @Column(nullable = false)
    private String contactName;

    @Column
    private String company;

    @Column(length = 100)
    private String position;

    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String phoneNumber;

    @Column
    private String linkedinUrl;
}