package com.projectmanagement.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * Producer CDI pour l'EntityManager
 * Permet d'injecter l'EntityManager dans les beans CDI
 */
@ApplicationScoped
public class EntityManagerProducer {

    @PersistenceContext(unitName = "projectManagementPU")
    private EntityManager entityManager;

    @Produces
    public EntityManager getEntityManager() {
        return entityManager;
    }
} 