package org.lordrose.vrms.controllers;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.services.impl.AccessoryServiceImpl;
import org.lordrose.vrms.services.impl.ReminderServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/accessories")
public class AccessoryController {

    private final AccessoryServiceImpl accessoryService;
    private final ReminderServiceImpl reminderService;

    @GetMapping("/vehicles/{vehicleId}")
    public Object getAllByVehicleId(@PathVariable Long vehicleId) {
        return accessoryService.findAllByVehicle(vehicleId);
    }

    @GetMapping("/reminders/{reminderId}/time/{remindDate}")
    public Object changeReminderDate(@PathVariable Long reminderId,
                                     @PathVariable Long remindDate) {
        return reminderService.updateReminderDate(reminderId, remindDate);
    }
}
