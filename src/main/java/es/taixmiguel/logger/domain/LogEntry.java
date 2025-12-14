package es.taixmiguel.logger.domain;

import io.quarkus.mongodb.panache.PanacheMongoEntity;

import java.time.Instant;

public class LogEntry extends PanacheMongoEntity {
    public String application;
    public Instant timestamp;
    public LogLevel level;
    public String message;
    public String stackTrace;
    public Client client;

    public LogEntry() {}

    private LogEntry(Builder builder) {
        application = builder.application;
        stackTrace = builder.stackTrace;
        timestamp = builder.timestamp;
        message = builder.message;
        client = builder.client;
        level = builder.level;
    }

    public static Builder builder(String application, LogLevel level, String message) {
        return new Builder(application, level, message);
    }

    public static class Builder {
        private final String application;
        private final LogLevel level;
        private final String message;
        private Instant timestamp;
        private String stackTrace;
        private Client client;

        private Builder(String application, LogLevel level, String message) {
            this.application = application;
            timestamp = Instant.now();
            this.message = message;
            this.level = level;
        }
        public Builder timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        public Builder stackTrace(String stackTrace) {
            this.stackTrace = stackTrace;
            return this;
        }
        public Builder client(Client client) {
            this.client = client;
            return this;
        }
        public LogEntry build() { return new LogEntry(this); }
    }
}
