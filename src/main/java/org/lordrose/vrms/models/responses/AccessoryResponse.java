package org.lordrose.vrms.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessoryResponse {

    private Double quantity;
    private List<ReminderResponse> reminders;
    private PartResponse part;

    public void addQuantity(Double value) {
        quantity += value;
    }
}
