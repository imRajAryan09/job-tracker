package com.tracker.repository;

import com.tracker.entity.ColumnEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColumnRepository extends JpaRepository<ColumnEntity, Long> {
}