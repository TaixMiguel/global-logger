package es.taixmiguel.logger.application.dto;

import es.taixmiguel.logger.domain.LogEntry;
import es.taixmiguel.logger.domain.LogLevel;

import java.time.Instant;

public record LogRequestDTO(
    Instant timestamp,
    LogLevel level,
    String message
) {
    public LogEntry toLogEntry(String application) {
        return new LogEntry(timestamp, level, message, application);
    }
}
