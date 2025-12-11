package es.taixmiguel.logger.application.port;

import es.taixmiguel.logger.application.query.LogSearchCriteria;
import es.taixmiguel.logger.domain.LogEntry;

import java.util.List;

public interface LogRepository {
    void save(LogEntry log);
    List<LogEntry> find(LogSearchCriteria criteria);
}
