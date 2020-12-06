package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.MaintenanceLevelDetail;
import org.lordrose.vrms.domains.Request;
import org.lordrose.vrms.models.responses.RequestHistoryResponse;

import java.util.Collections;
import java.util.Optional;

import static org.lordrose.vrms.converters.ExpenseConverter.toExpenseResponses;
import static org.lordrose.vrms.converters.MaintenanceLevelDetailConverter.toLevelDetailResponse;
import static org.lordrose.vrms.converters.PartConverter.toPartResponses;
import static org.lordrose.vrms.converters.ProviderConverter.toProviderResponse;
import static org.lordrose.vrms.converters.ServiceConverter.toRequestServiceResponses;
import static org.lordrose.vrms.converters.VehicleModelConverter.toModelResponse;
import static org.lordrose.vrms.utils.DateTimeUtils.toSeconds;

public class RequestConverter {

    public static RequestHistoryResponse toRequestCheckoutResponse(Request request) {
        if (request == null)
            return null;
        if (request.getExpenses() == null)
            request.setExpenses(Collections.emptySet());
        Optional<MaintenanceLevelDetail> detail = Optional.ofNullable(request.getLevelDetail());
        RequestHistoryResponse response = RequestHistoryResponse.builder()
                .id(request.getId())
                .bookingTime(toSeconds(request.getBookingTime()))
                .note(request.getNote())
                .status(request.getStatus())
                .technicianName(request.getTechnician().getFullName())
                .model(toModelResponse(request.getVehicle().getModel()))
                .services(toRequestServiceResponses(request.getServices()))
                .parts(toPartResponses(request.getPartDetails()))
                .expenses(toExpenseResponses(request.getExpenses()))
                .provider(toProviderResponse(request.getProvider()))
                .build();
        detail.ifPresent(value -> {
            response.setLevelDetail(toLevelDetailResponse(value));
        });
        return response;
    }
}
