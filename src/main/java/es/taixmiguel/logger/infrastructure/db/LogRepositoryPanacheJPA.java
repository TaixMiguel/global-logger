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

import java.util.ArrayList;
import java.util.List;

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
        List<Object> params = new ArrayList<>();

        query.append("application = ?1");
        params.add(criteria.application());

        criteria.level().ifPresent(logLevel -> {
            query.append(" and level = ?").append(params.size() + 1);
            params.add(logLevel);
        });

        criteria.dateFrom().ifPresent(dateFrom -> {
            query.append(" and timestamp >= ?").append(params.size() + 1);
            params.add(dateFrom);
        });

        criteria.dateTo().ifPresent(dateTo -> {
            query.append(" and timestamp < ?").append(params.size() + 1);
            params.add(dateTo);
        });

        Sort sort = criteria.sortOrder()
                .map(sortOrder ->
                        switch (sortOrder) {
                            case LogSearchSortCriteria.timestamp_asc -> Sort.ascending("timestamp");
                            default -> Sort.descending("timestamp");
                        }
                )
                .orElse(Sort.descending("timestamp"));

        return find(query.toString(), sort, params.toArray())
                .stream()
                .map(LogEntryJpa::toDomain)
                .toList();
    }
}
