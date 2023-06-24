package pl.springwebmvc.integration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

@TestConfiguration
public class PersistenceContainerTestConfiguration {

    public static final String POSTGRES_USERNAME = "username";
    public static final String POSTGRES_PASSWORD = "password";
    public static final String POSTGRES_CONTAINER = "postgres:15.0";

    @Bean
    PostgreSQLContainer<?> postgreSQLContainer(){
        PostgreSQLContainer<?> container = new PostgreSQLContainer<>(POSTGRES_CONTAINER)
                .withUsername(POSTGRES_USERNAME)
                .withPassword(POSTGRES_PASSWORD);
        container.start();
        return container;
    }

    @Bean
    DataSource dataSource(final PostgreSQLContainer<?> postgreSQLContainer){
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .driverClassName(postgreSQLContainer.getDriverClassName())
                .url(postgreSQLContainer.getJdbcUrl())
                .username(postgreSQLContainer.getUsername())
                .password(postgreSQLContainer.getPassword())
                .build();
    }
}
