package com.tracker.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
@Table(name = "companies")
public class CompanyEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;

    @Column(nullable = false)
    private String companyName;

    @Column(length = 100)
    private String companyIndustry;

    @Column(length = 100)
    private String location;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<JobEntity> jobs = new ArrayList<>();
}
