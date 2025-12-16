package es.taixmiguel.logger.infrastructure.db.entity;

import es.taixmiguel.logger.domain.LogEntry;
import es.taixmiguel.logger.domain.LogLevel;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "logs")
public class LogEntryJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String application;
    public Instant timestamp;
    public LogLevel level;
    public String message;
    public String stackTrace;
    @Embedded private ClientEntryJpa client;

    public static LogEntryJpa fromDomain(LogEntry domain) {
        var entry = new LogEntryJpa();
        entry.application = domain.application;
        entry.timestamp = domain.timestamp;
        entry.level = domain.level;
        entry.message = domain.message;
        entry.stackTrace = domain.stackTrace;
        entry.client = ClientEntryJpa.fromDomain(domain.client);

        return entry;
    }
    public LogEntry toDomain() {
        var builder = LogEntry.builder(application, level, message)
                .timestamp(timestamp)
                .stackTrace(stackTrace);
        if (client != null) builder.client(client.toDomain());
        return builder.build();
    }
}
