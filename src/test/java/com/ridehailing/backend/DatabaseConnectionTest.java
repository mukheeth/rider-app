package com.ridehailing.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class to verify PostgreSQL database connection.
 * 
 * Note: This test uses the actual PostgreSQL database configuration from
 * src/main/resources/application.properties, not the H2 test database.
 * 
 * To run this test, ensure:
 * 1. PostgreSQL is running on localhost:5432
 * 2. Database 'ridehailing' exists
 * 3. Username 'postgres' with password 'postgres' has access
 */
@SpringBootTest
class DatabaseConnectionTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void testDataSourceExists() {
        assertNotNull(dataSource, "DataSource should be configured");
    }

    @Test
    void testDatabaseConnection() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            assertNotNull(connection, "Database connection should not be null");
            assertFalse(connection.isClosed(), "Database connection should be open");
            
            // Get database metadata
            DatabaseMetaData metaData = connection.getMetaData();
            assertNotNull(metaData, "Database metadata should not be null");
            
            // Verify it's PostgreSQL
            String databaseProductName = metaData.getDatabaseProductName();
            assertEquals("PostgreSQL", databaseProductName, 
                "Database should be PostgreSQL, but found: " + databaseProductName);
            
            System.out.println("✓ Database Product: " + databaseProductName);
            System.out.println("✓ Database Version: " + metaData.getDatabaseProductVersion());
            System.out.println("✓ Driver Name: " + metaData.getDriverName());
            System.out.println("✓ Driver Version: " + metaData.getDriverVersion());
            System.out.println("✓ URL: " + metaData.getURL());
            System.out.println("✓ Username: " + metaData.getUserName());
        }
    }

    @Test
    void testDatabaseQuery() {
        // Test a simple query
        String result = jdbcTemplate.queryForObject("SELECT current_database()", String.class);
        assertNotNull(result, "Query result should not be null");
        assertEquals("ridehailing", result, 
            "Database name should be 'ridehailing', but found: " + result);
        
        System.out.println("✓ Current Database: " + result);
    }

    @Test
    void testDatabaseVersion() {
        String version = jdbcTemplate.queryForObject("SELECT version()", String.class);
        assertNotNull(version, "PostgreSQL version should not be null");
        assertTrue(version.contains("PostgreSQL"), 
            "Version string should contain 'PostgreSQL'");
        
        System.out.println("✓ PostgreSQL Version: " + version);
    }

    @Test
    void testTablesExist() {
        // Check if any tables exist in the public schema
        Integer tableCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public'",
            Integer.class
        );
        
        assertNotNull(tableCount, "Table count should not be null");
        System.out.println("✓ Tables in database: " + tableCount);
        
        if (tableCount > 0) {
            System.out.println("  Tables found:");
            jdbcTemplate.query(
                "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' ORDER BY table_name",
                (rs, rowNum) -> rs.getString("table_name")
            ).forEach(tableName -> System.out.println("    - " + tableName));
        } else {
            System.out.println("  ⚠ No tables found. You may need to run migration scripts.");
        }
    }
}

