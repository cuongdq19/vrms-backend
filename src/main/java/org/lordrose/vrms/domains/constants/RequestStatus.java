package org.lordrose.vrms.domains.constants;

public enum RequestStatus {
    ACCEPTED("ACCEPTED"),
    ARRIVED("ARRIVED"),
    CONFIRMED("CONFIRMED"),
    WORK_COMPLETED("WORK COMPLETED"),
    FINISHED("FINISHED"),
    CANCELED("CANCELED");
    
    public String textValue;
    
    RequestStatus(String text) {
        textValue = text;
    }
}
