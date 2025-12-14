package es.taixmiguel.logger.application.dto;

import es.taixmiguel.logger.domain.LogEntry;
import es.taixmiguel.logger.domain.LogLevel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record LogRequestDTO(
    @NotNull Instant timestamp,
    @NotNull LogLevel level,
    @NotBlank String message,
    String stackTrace,
    @Valid ClientRequestDTO client
) {
    public LogEntry toLogEntry(String application) {
        var entry = new LogEntry(application, level, message, timestamp);
        entry.client(client.toClient());
        entry.stackTrace(stackTrace);
        return entry;
    }
}
