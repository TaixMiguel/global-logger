package es.taixmiguel.logger.infrastructure.db;

import es.taixmiguel.logger.application.port.LogRepository;
import es.taixmiguel.logger.application.port.qualifier.JpaRepository;
import es.taixmiguel.logger.application.query.LogSearchCriteria;
import es.taixmiguel.logger.application.query.LogSearchSortCriteria;
import es.taixmiguel.logger.domain.LogEntry;
import es.taixmiguel.logger.infrastructure.db.entity.LogEntryJpa;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JpaRepository
@ApplicationScoped
public class LogRepositoryPanacheJPA implements LogRepository, PanacheRepository<LogEntryJpa> {

    @Override
    @Transactional
    public void save(LogEntry entry) {
        LogEntryJpa jpaEntity = LogEntryJpa.fromDomain(entry);
        persist(jpaEntity);
    }

    @Override
    public List<LogEntry> find(LogSearchCriteria criteria) {
        StringBuilder query = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        query.append("application = :application");
        params.put("application", criteria.application());

        criteria.level().ifPresent(logLevel -> {
            query.append(" and level = :level");
            params.put("level", logLevel);
        });

        criteria.dateFrom().ifPresent(dateFrom -> {
            query.append(" and timestamp >= :dateFrom");
            params.put("dateFrom", dateFrom);
        });

        criteria.dateTo().ifPresent(dateTo -> {
            query.append(" and timestamp < :dateTo");
            params.put("dateTo", dateTo);
        });

        Sort sort = criteria.sortOrder()
                .map(sortOrder ->
                        switch (sortOrder) {
                            case LogSearchSortCriteria.timestamp_asc -> Sort.ascending("timestamp");
                            default -> Sort.descending("timestamp");
                        }
                )
                .orElse(Sort.descending("timestamp"));

        return find(query.toString(), sort, params)
                .stream()
                .map(LogEntryJpa::toDomain)
                .toList();
    }
}
