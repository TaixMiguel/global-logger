package es.taixmiguel.logger.infrastructure.db.entity;

import es.taixmiguel.logger.domain.Client;
import jakarta.persistence.*;

@Embeddable
public class ClientEntryJpa {
    public String appVersionCode;
    public String appVersionName;
    public String os;
    public String osVersion;
    public String osVersionSdk;
    public String deviceManufacturer;
    public String deviceModel;

    public static ClientEntryJpa fromDomain(Client domain) {
        var entry = new ClientEntryJpa();
        entry.appVersionCode = domain.appVersionCode();
        entry.appVersionName = domain.appVersionName();
        entry.os = domain.os();
        entry.osVersion = domain.osVersion();
        entry.osVersionSdk = domain.osVersionSdk();
        entry.deviceManufacturer = domain.deviceManufacturer();
        entry.deviceModel = domain.deviceModel();

        return entry;
    }
    public Client toDomain() {
        return new Client(appVersionCode, appVersionName, os, osVersion, osVersionSdk, deviceManufacturer, deviceModel);
    }
}
