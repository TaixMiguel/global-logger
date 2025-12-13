package es.taixmiguel.logger.application;

import es.taixmiguel.logger.application.port.LogRepository;
import es.taixmiguel.logger.application.query.LogSearchCriteria;
import es.taixmiguel.logger.domain.LogEntry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class LogService {
    private final LogRepository repository;

    @Inject
    public LogService(LogRepository repository) {
        this.repository = repository;
    }

    public void recordLog(LogEntry entry) {
        repository.save(entry);
    }

    public List<LogEntry> findLogs(LogSearchCriteria criteria) {
        return repository.find(criteria);
    }
}
