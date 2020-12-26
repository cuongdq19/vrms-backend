package org.lordrose.vrms.constants;

import org.lordrose.vrms.exceptions.InvalidArgumentException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Configuration
public class MaintenanceConstants {

    @Value("#{'${maintenance.milstones}'.split(';')}")
    private List<String> milestones;

    @Bean
    public MaintenanceMilestone getMilestone() {
        return new MaintenanceMilestone(milestones);
    }

    public class MaintenanceMilestone {

        private final List<Double> milestones;

        public MaintenanceMilestone(List<String> values) {
            milestones = values.stream()
                    .map(Double::valueOf)
                    .collect(Collectors.toList());
        }

        public Double getMilestoneAt(int index) {
            if (index < 0 || index >= milestones.size()) {
                throw new InvalidArgumentException("Invalid milestone index!");
            }
            return milestones.get(index);
        }

        public Map<Integer, Double> getMilesAsMap() {
            return IntStream.range(0, milestones.size())
                    .boxed()
                    .collect(Collectors.toMap(Function.identity(), milestones::get));
        }
    }
}
