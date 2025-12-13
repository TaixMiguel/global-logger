package es.taixmiguel.logger.application.dto;

import es.taixmiguel.logger.domain.LogEntry;
import es.taixmiguel.logger.domain.LogLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record LogRequestDTO(
    @NotNull Instant timestamp,
    @NotNull LogLevel level,
    @NotBlank String message
) {
    public LogEntry toLogEntry(String application) {
        return new LogEntry(timestamp, level, message, application);
    }
}
