package com.tracker.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
@Table(name = "boards")
public class BoardEntity extends TimeStampedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID boardId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserInfoEntity user;

    @Column(nullable = false)
    private String boardName;

    @Column(nullable = false)
    private boolean isDefault;

    @Column(nullable = false)
    private boolean isStarred;

    @Column
    private String color;

    @Column
    private String icon;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<ColumnEntity> columns = new ArrayList<>();
}
