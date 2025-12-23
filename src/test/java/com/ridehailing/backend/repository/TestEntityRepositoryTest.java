package com.ridehailing.backend.repository;

import com.ridehailing.backend.entity.TestEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TestEntityRepositoryTest {

    @Autowired
    private TestEntityRepository repository;

    @Test
    void testSaveAndFind() {
        // Create entity
        TestEntity entity = new TestEntity("Test Name");
        
        // Save entity
        TestEntity saved = repository.save(entity);
        
        // Verify entity was saved with ID
        assertNotNull(saved.getId());
        assertEquals("Test Name", saved.getName());
        
        // Find by ID
        Optional<TestEntity> found = repository.findById(saved.getId());
        
        // Verify entity was found
        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals("Test Name", found.get().getName());
    }

    @Test
    void testFindById_NotFound() {
        // Try to find non-existent entity
        Optional<TestEntity> found = repository.findById(UUID.randomUUID());
        
        // Verify entity was not found
        assertFalse(found.isPresent());
    }
}

