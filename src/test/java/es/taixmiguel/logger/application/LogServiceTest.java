package es.taixmiguel.logger.application;

import es.taixmiguel.logger.application.port.LogRepository;
import es.taixmiguel.logger.domain.LogEntry;
import es.taixmiguel.logger.domain.LogLevel;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class LogServiceTest {
    @Mock
    LogRepository repository;

    @InjectMocks
    LogService service;

    private static Stream<LogEntry> invalidLogEntries() {
        return Stream.of(
                new LogEntry(null, LogLevel.info, "Valid message", "app-service"),
                new LogEntry(Instant.now(), null, "Valid message", "app-service"),
                new LogEntry(Instant.now(), LogLevel.info, null, "app-service"),
                new LogEntry(Instant.now(), LogLevel.info, "  ", "app-service")
        );
    }

    @MethodSource("invalidLogEntries")
    @ParameterizedTest(name = "Should throw illegal argument exception when required fields are null: case {index}")
    public void shouldThrowExceptionForInvalidEntries(LogEntry invalidEntry) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.recordLog(invalidEntry));
        Mockito.verify(repository, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void shouldSaveLogWhenEntryIsValid() {
        var entry = new LogEntry(Instant.now(), LogLevel.debug, "test invocation", "test-application");
        service.recordLog(entry);
        Mockito.verify(repository).save(entry);
    }
}
