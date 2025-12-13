package es.taixmiguel.logger.infrastructure.db;

import es.taixmiguel.logger.application.query.LogSearchCriteria;
import es.taixmiguel.logger.application.query.LogSearchSortCriteria;
import es.taixmiguel.logger.domain.LogEntry;
import es.taixmiguel.logger.domain.LogLevel;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

@QuarkusTest
public class LogRepositoryPanacheMongoTest {
    @Inject
    LogRepositoryPanacheMongo repository;

    @BeforeEach
    public void setup() {
        repository.deleteAll();
    }

    @Test
    public void testSaveLog_ShouldPersistOneEntry() {
        LogEntry newLog = new LogEntry(
                LogLevel.info,
                "Service started successfully.",
                "auth-service"
        );

        repository.save(newLog);

        Assertions.assertEquals(1, repository.count());
        Assertions.assertNotNull(newLog.id);
    }

    @Test
    public void testFindLogs_ShouldFilterOnlyByMandatoryApplication() {
        repository.save(new LogEntry(
                LogLevel.info,
                "Service auth started successfully.",
                "auth-service"
        ));

        repository.save(new LogEntry(
                LogLevel.info,
                "Service billing started successfully.",
                "billing-service"
        ));
        repository.save(new LogEntry(
                LogLevel.info,
                "Service billing stopped successfully.",
                "billing-service"
        ));

        verifyFilterCount("auth-service", 1);
        verifyFilterCount("billing-service", 2);
        verifyFilterCount("unknow-service", 0);
    }

    @Test
    public void testFindLogs_FilterByApplicationAndLevel() {
        repository.save(new LogEntry(
                LogLevel.debug,
                "Service billing started successfully.",
                "billing-service"
        ));
        repository.save(new LogEntry(
                LogLevel.info,
                "Service billing stopped successfully.",
                "billing-service"
        ));

        verifyFilterCount("billing-service", 1, LogLevel.debug);
        verifyFilterCount("billing-service", 1, LogLevel.info);
        verifyFilterCount("billing-service", 0, LogLevel.warn);
    }

    @Test
    public void testFindLogs_FilterByDateRange() {
        repository.save(new LogEntry(
                Instant.parse("2025-01-01T00:00:00.00Z"),
                LogLevel.info,
                "Service billing started successfully.",
                "billing-service"
        ));
        repository.save(new LogEntry(
                Instant.parse("2025-01-01T00:05:00.00Z"),
                LogLevel.info,
                "Service billing stopped successfully.",
                "billing-service"
        ));

        verifyFilterCount("billing-service", 2, Instant.parse("2025-01-01T00:00:00.00Z"), Instant.parse("2025-01-01T01:00:00.00Z"));
        verifyFilterCount("billing-service", 1, Instant.parse("2025-01-01T00:00:00.00Z"), Instant.parse("2025-01-01T00:05:00.00Z"));
        verifyFilterCount("billing-service", 1, Instant.parse("2025-01-01T00:00:05.00Z"), Instant.parse("2025-01-01T00:10:00.00Z"));
        verifyFilterCount("billing-service", 0, Instant.parse("2025-01-01T01:00:00.00Z"), Instant.parse("2025-01-01T01:10:00.00Z"));
    }

    @Test
    public void testFindLogs_ShouldOrderCorrectly() {
        repository.save(new LogEntry(
                Instant.parse("2025-01-01T00:01:00.00Z"),
                LogLevel.info,
                "Service billing started successfully.",
                "billing-service"
        ));
        repository.save(new LogEntry(
                Instant.parse("2025-01-01T00:05:00.00Z"),
                LogLevel.info,
                "Service billing stopped successfully.",
                "billing-service"
        ));

        repository.save(new LogEntry(
                Instant.parse("2025-01-01T00:02:30.00Z"),
                LogLevel.info,
                "Service billing started successfully.",
                "billing-service"
        ));
        repository.save(new LogEntry(
                Instant.parse("2025-01-01T00:00:30.00Z"),
                LogLevel.info,
                "Service billing stopped successfully.",
                "billing-service"
        ));

        var criteria = new LogSearchCriteria("billing-service");
        var logs = repository.find(criteria);

        Assertions.assertEquals(4, logs.size());
        for (int i=0; i+1 < logs.size(); i++)
            Assertions.assertTrue(logs.get(i).timestamp.isAfter(logs.get(i+1).timestamp));

        criteria.sortOrder(LogSearchSortCriteria.timestamp_desc);
        logs = repository.find(criteria);

        Assertions.assertEquals(4, logs.size());
        for (int i=0; i+1 < logs.size(); i++)
            Assertions.assertTrue(logs.get(i).timestamp.isAfter(logs.get(i+1).timestamp));

        criteria.sortOrder(LogSearchSortCriteria.timestamp_asc);
        logs = repository.find(criteria);

        Assertions.assertEquals(4, logs.size());
        for (int i=0; i+1 < logs.size(); i++)
            Assertions.assertTrue(logs.get(i).timestamp.isBefore(logs.get(i+1).timestamp));
    }

    private void verifyFilterCount(String applicationSearch, int elementsCount) {
        verifyFilterCount(new LogSearchCriteria(applicationSearch), elementsCount);
    }

    private void verifyFilterCount(String applicationSearch, int elementsCount, LogLevel level) {
        var criteria = new LogSearchCriteria(applicationSearch);
        criteria.level(level);

        verifyFilterCount(criteria, elementsCount);
    }

    private void verifyFilterCount(String applicationSearch, int elementsCount, Instant dateFrom, Instant dateTo) {
        var criteria = new LogSearchCriteria(applicationSearch);
        criteria.dateFrom(dateFrom);
        criteria.dateTo(dateTo);

        verifyFilterCount(criteria, elementsCount);
    }

    private void verifyFilterCount(LogSearchCriteria criteria, int elementsCount) {
        var logs = repository.find(criteria);
        Assertions.assertEquals(elementsCount, logs.size());
        for (int i=0; i < elementsCount; i++)
            Assertions.assertNotNull(logs.get(i).id);
    }
}
