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
        var builder = LogEntry.builder(application, level, message);
        if (client != null) builder.client(client.toClient());
        builder.stackTrace(stackTrace);
        builder.timestamp(timestamp);
        return builder.build();
    }
}
