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

@ExtendWith(MockitoExtension.class)
public class LogServiceTest {
    @Mock
    LogRepository repository;

    @InjectMocks
    LogService service;

    @Test
    public void shouldSaveLogWhenEntryIsValid() {
        var entry = LogEntry.builder("test-application", LogLevel.debug, "test invocation").build();
        service.recordLog(entry);
        Mockito.verify(repository).save(entry);
    }
}
