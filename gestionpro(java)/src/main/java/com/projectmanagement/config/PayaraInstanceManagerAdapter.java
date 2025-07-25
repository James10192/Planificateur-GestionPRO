package com.projectmanagement.config;

import jakarta.servlet.ServletContext;
import org.springframework.web.SpringServletContainerInitializer;
import org.springframework.web.WebApplicationInitializer;

import java.util.Set;

/**
 * Classe utilitaire pour aider à l'initialisation de l'application dans Payara.
 * Cette classe fournit un point d'entrée supplémentaire pour Spring dans l'environnement Payara.
 */
public class PayaraInstanceManagerAdapter extends SpringServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> webApplicationInitializers, ServletContext servletContext) {
        try {
            // Appel de la méthode parent pour initialiser Spring correctement
            super.onStartup(webApplicationInitializers, servletContext);
            
            // Ajouter des attributs spécifiques à Payara si nécessaire
            servletContext.setAttribute("com.sun.appserv.jsp.classpath", 
                    servletContext.getRealPath("/WEB-INF/classes"));
            
        } catch (Exception e) {
            // Capturer et logger les exceptions qui pourraient survenir
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'initialisation de l'application dans Payara", e);
        }
    }
} 