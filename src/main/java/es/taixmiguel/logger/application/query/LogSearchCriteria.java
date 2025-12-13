package es.taixmiguel.logger.application.query;

import es.taixmiguel.logger.domain.LogLevel;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;

import java.time.Instant;
import java.util.Optional;

public class LogSearchCriteria {
    @PathParam("application")
    private String application;
    @QueryParam("level")
    private LogLevel level;
    @QueryParam("dateFrom")
    private Instant dateFrom;
    @QueryParam("dateTo")
    private Instant dateTo;
    @QueryParam("sortOrder")
    private LogSearchSortCriteria sortOrder;

    public LogSearchCriteria() {
        this("unknown");
    }

    public LogSearchCriteria(String application) {
        this.application = application;
    }

    public String application() {
        return application;
    }

    public Optional<LogLevel> level() {
        return Optional.ofNullable(level);
    }

    public void level(LogLevel level) {
        this.level = level;
    }

    public Optional<Instant> dateFrom() {
        return Optional.ofNullable(dateFrom);
    }

    public void dateFrom(Instant dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Optional<Instant> dateTo() {
        return Optional.ofNullable(dateTo);
    }

    public void dateTo(Instant dateTo) {
        this.dateTo = dateTo;
    }

    public Optional<LogSearchSortCriteria> sortOrder() {
        return Optional.ofNullable(sortOrder);
    }

    public void sortOrder(LogSearchSortCriteria sortOrder) {
        this.sortOrder = sortOrder;
    }
}