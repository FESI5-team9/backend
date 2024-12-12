package com.fesi.mukitlist.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaRepositories(basePackages = "com.fesi.mukitlist.api.repository")
@EntityScan(basePackages = "com.fesi.mukitlist.core")
@EnableJpaAuditing
@Configuration
@EnableTransactionManagement
public class JpaConfig {
}
