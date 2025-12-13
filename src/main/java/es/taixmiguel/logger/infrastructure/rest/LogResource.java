package es.taixmiguel.logger.infrastructure.rest;

import es.taixmiguel.logger.application.LogService;
import es.taixmiguel.logger.application.dto.LogRequestDTO;
import es.taixmiguel.logger.application.query.LogSearchCriteria;
import es.taixmiguel.logger.domain.LogEntry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.util.List;

@ApplicationScoped
public class LogResource implements LogApi {
    @Inject
    LogService service;

    @Override
    public List<LogEntry> findLogs(LogSearchCriteria criteria) {
        return service.findLogs(criteria);
    }

    @Override
    public Response recordLog(String application, LogRequestDTO request) {
        service.recordLog(request.toLogEntry(application));
        return Response.status(Response.Status.CREATED).build();
    }
}
