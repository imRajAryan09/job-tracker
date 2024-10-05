package com.tracker.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
 
import java.time.LocalDate;

/**
 * @author by Raj Aryan,
 * created on 02/10/2024
 */
@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {

    @CreatedDate
    private LocalDate createdDate;

    @LastModifiedDate
    private LocalDate lastModifiedDate;

}
