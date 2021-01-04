package org.lordrose.vrms.domains.constants;

public enum ContractStatus {
    PENDING("PENDING"),
    CONFIRMED("CONFIRMED"),
    RESOLVED("RESOLVED");

    public String textValue;

    ContractStatus(String text) {
        textValue = text;
    }
}
