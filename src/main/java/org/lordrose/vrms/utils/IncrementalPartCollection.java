package org.lordrose.vrms.utils;

import org.lordrose.vrms.domains.ServiceRequestPart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IncrementalPartCollection {

    private final List<ServiceRequestPart> collection = new ArrayList<>();

    public void add(ServiceRequestPart requestPart) {
        if (collection.contains(requestPart)) {
            collection.get(collection.indexOf(requestPart)).addQuantity(requestPart.getQuantity());
        } else {
            collection.add(requestPart);
        }
    }

    public void addAll(Collection<ServiceRequestPart> requestParts) {
        requestParts.forEach(this::add);
    }

    public Collection<ServiceRequestPart> getCollection() {
        return collection;
    }
}
