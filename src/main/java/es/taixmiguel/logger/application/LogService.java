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
        validateLog(entry);
        repository.save(entry);
    }

    private void validateLog(LogEntry entry) {
        if (entry.application == null || entry.application.isBlank())
            throw new IllegalArgumentException("Log entry validation failed: 'application' cannot be null or blank.");

        if (entry.timestamp == null)
            throw new IllegalArgumentException("Log entry validation failed: 'timestamp' cannot be null.");

        if (entry.level == null)
            throw new IllegalArgumentException("Log entry validation failed: 'level' cannot be null.");

        if (entry.message == null || entry.message.isBlank())
            throw new IllegalArgumentException("Log entry validation failed: 'message' cannot be null or blank.");
    }

    public List<LogEntry> findLogs(LogSearchCriteria criteria) {
        return repository.find(criteria);
    }
}
