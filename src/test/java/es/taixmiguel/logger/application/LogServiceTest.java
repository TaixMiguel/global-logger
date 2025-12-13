package es.taixmiguel.logger.application;

import es.taixmiguel.logger.application.port.LogRepository;
import es.taixmiguel.logger.domain.LogEntry;
import es.taixmiguel.logger.domain.LogLevel;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

@ExtendWith(MockitoExtension.class)
public class LogServiceTest {
    @Mock
    LogRepository repository;

    @InjectMocks
    LogService service;

    @Test
    public void shouldSaveLogWhenEntryIsValid() {
        var entry = new LogEntry(Instant.now(), LogLevel.debug, "test invocation", "test-application");
        service.recordLog(entry);
        Mockito.verify(repository).save(entry);
    }
}
