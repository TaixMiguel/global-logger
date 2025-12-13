package es.taixmiguel.logger.domain;

import io.quarkus.mongodb.panache.PanacheMongoEntity;

import java.time.Instant;

public class LogEntry extends PanacheMongoEntity {
    public Instant timestamp;
    public LogLevel level;
    public String message;
    public String application;

    public LogEntry() {}

    public LogEntry(LogLevel level, String message, String application) {
        this(Instant.now(), level, message, application);
    }

    public LogEntry(Instant timestamp, LogLevel level, String message, String application) {
        this.timestamp = timestamp;
        this.level = level;
        this.message = message;
        this.application = application;
    }
}
