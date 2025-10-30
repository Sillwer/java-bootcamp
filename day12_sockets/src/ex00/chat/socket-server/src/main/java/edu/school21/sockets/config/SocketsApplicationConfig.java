package edu.school21.sockets.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
@PropertySource("db.properties")
@ComponentScan("edu.school21.sockets.*")
public class SocketsApplicationConfig {
    @Autowired()
    Environment env;

    @Bean
    public DataSource getDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(env.getProperty("url"));
        dataSource.setDriverClassName(env.getProperty("driver_class_name"));
        dataSource.setUsername(env.getProperty("db_user"));
        dataSource.setPassword(env.getProperty("password"));
        return dataSource;
    }
}
