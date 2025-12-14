package es.taixmiguel.logger.domain;

public record Client(
    String appVersionCode,
    String appVersionName,
    String os,
    String osVersion,
    String osVersionSdk,
    String deviceManufacturer,
    String deviceModel) {}
