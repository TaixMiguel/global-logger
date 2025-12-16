package es.taixmiguel.logger.infrastructure.db;

import es.taixmiguel.logger.application.port.LogRepository;
import es.taixmiguel.logger.application.port.qualifier.MongoRepository;
import es.taixmiguel.logger.application.query.LogSearchCriteria;
import es.taixmiguel.logger.application.query.LogSearchSortCriteria;
import es.taixmiguel.logger.domain.LogEntry;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@MongoRepository
@ApplicationScoped
public class LogRepositoryPanacheMongo implements LogRepository, PanacheMongoRepository<LogEntry> {

    @Override
    public void save(LogEntry log) {
        persist(log);
    }

    public List<LogEntry> find(LogSearchCriteria criteria) {
        StringBuilder query = new StringBuilder("{");
        List<Object> params = new ArrayList<>();

        query.append("\"application\": ?1");
        params.add(criteria.application());

        criteria.level().ifPresent(logLevel -> {
            query.append(", \"level\": ?").append(params.size() + 1);
            params.add(logLevel);
        });

        if (criteria.dateFrom().isPresent() || criteria.dateTo().isPresent()) {
            StringBuilder queryTimestamp = new StringBuilder();

            criteria.dateFrom().ifPresent(dateFrom -> {
                queryTimestamp.append("\"$gte\": ?").append(params.size() + 1);
                params.add(dateFrom);
            });

            criteria.dateTo().ifPresent(dateTo -> {
                if (!queryTimestamp.isEmpty()) queryTimestamp.append(", ");
                queryTimestamp.append("\"$lt\": ?").append(params.size() + 1);
                params.add(dateTo);
            });

            query.append(", \"timestamp\": {").append(queryTimestamp).append("}");
        }
        query.append("}");

        Sort sort = criteria.sortOrder()
            .map(sortOrder ->
                switch (sortOrder) {
                    case LogSearchSortCriteria.timestamp_asc -> Sort.ascending("timestamp");
                    default -> Sort.descending("timestamp");
                }
            )
            .orElse(Sort.descending("timestamp"));

        return find(query.toString(), sort, params.toArray()).list();
    }
}