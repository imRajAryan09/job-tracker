package com.tracker.repository;

import com.tracker.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, UUID> {
}