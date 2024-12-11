package com.fesi.mukitlist.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.fesi.mukitlist.api.repository")
@EnableJpaAuditing
@Configuration
public class JpaAuditingConfig {
}
