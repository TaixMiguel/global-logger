package es.taixmiguel.logger.application.dto;

import es.taixmiguel.logger.domain.Client;
import jakarta.validation.constraints.NotBlank;

public record ClientRequestDTO(
    @NotBlank String appVersionCode,
    @NotBlank String appVersionName,
    @NotBlank String os,
    @NotBlank String osVersion,
    String osVersionSdk,
    String deviceManufacturer,
    String deviceModel
) {
    public Client toClient() {
        return new Client(appVersionCode, appVersionName, os,
                osVersion, osVersionSdk, deviceManufacturer, deviceModel);
    }
}
