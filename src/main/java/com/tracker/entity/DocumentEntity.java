package com.tracker.entity;

import com.tracker.enums.DocumentType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @author by Raj Aryan,
 * created on 02/10/2024
 */
@Entity
@Getter
@Setter
@Builder
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "documents")
public class DocumentEntity extends TimeStampedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long documentId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserInfoEntity user;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private JobEntity job;

    @Column(nullable = false)
    private String documentName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private DocumentType documentType;

    @Column(nullable = false)
    private String documentPath;
}
