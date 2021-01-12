package org.lordrose.vrms.domains.constants;

public enum ContractStatus {
    PENDING("PENDING"),
    CONFIRMED("CONFIRMED"),
    RESOLVED("RESOLVED"),
    DENIED("DENIED");

    public String textValue;

    ContractStatus(String text) {
        textValue = text;
    }
}
