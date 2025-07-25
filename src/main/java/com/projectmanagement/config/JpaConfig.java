package com.projectmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import jakarta.persistence.EntityManagerFactory;

/**
 * Configuration JPA pour l'intégration de Spring Data JPA avec Payara/GlassFish
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.projectmanagement.repository")
@EnableTransactionManagement
public class JpaConfig {

    /**
     * Récupère la source de données JNDI
     */
    @Bean
    public DataSource dataSource() throws NamingException {
        InitialContext initialContext = new InitialContext();
        return (DataSource) initialContext.lookup("java:comp/env/jdbc/NsiaProjectDS");
    }

    /**
     * Configure la factory d'EntityManager
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.projectmanagement.entity");
        em.setPersistenceUnitName("projectManagementPU");
        
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        em.setJpaVendorAdapter(vendorAdapter);
        
        return em;
    }

    /**
     * Configure le gestionnaire de transactions
     */
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }
} 