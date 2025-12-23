package com.ridehailing.backend.repository;

import com.ridehailing.backend.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TestEntityRepository extends JpaRepository<TestEntity, UUID> {
}

