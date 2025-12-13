package es.taixmiguel.logger.infrastructure.rest;

import es.taixmiguel.logger.application.dto.LogRequestDTO;
import es.taixmiguel.logger.application.query.LogSearchCriteria;
import es.taixmiguel.logger.domain.LogEntry;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/logs")
public interface LogApi {
    @GET
    @Path("/{application}")
    @Produces(MediaType.APPLICATION_JSON)
    List<LogEntry> findLogs(@BeanParam LogSearchCriteria criteria);

    @POST
    @Path("/{application}")
    @Consumes(MediaType.APPLICATION_JSON)
    Response recordLog(@PathParam("application") @NotBlank String application, @Valid LogRequestDTO request);
}
