package com.example.auth_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource; // <--- Importera denna

@SpringBootTest
// Lägg till raden nedan. Den tvingar testet att använda en H2-databas i minnet.
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.datasource.username=sa",
        "spring.datasource.password=password"
})
class SmokeTest {

    @Test
    void contextLoads() {
    }
}