package es.taixmiguel.logger.infrastructure.db;

import es.taixmiguel.logger.application.port.LogRepository;
import es.taixmiguel.logger.application.port.qualifier.JpaRepository;
import es.taixmiguel.logger.application.port.qualifier.MongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class RepositoryProducer {
    @ConfigProperty(name = "log.persistence.type", defaultValue = "mongodb")
    String persistenceType;

    @Inject
    @MongoRepository
    LogRepositoryPanacheMongo mongoRepository;

    @Inject
    @JpaRepository
    LogRepositoryPanacheJPA jpaRepository;

    @Produces
    @ApplicationScoped
    public LogRepository getLogRepository() {
        if ("postgres".equalsIgnoreCase(persistenceType))
            return jpaRepository;
        return mongoRepository;
    }
}
