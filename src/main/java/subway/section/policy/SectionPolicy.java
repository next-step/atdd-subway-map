package subway.section.policy;

import subway.line.repository.Line;
import subway.station.repository.Station;

import java.util.Objects;
import java.util.stream.Collectors;

public class SectionPolicy {
    public static void validate(Line line, Long upStationId, Long downStationId) {
        if (!Objects.equals(line.getDownEndStation().getId(), upStationId)) {
            throw new RuntimeException("section's upStation is not line's downEndStation");
        }

        if (line.getAllStation()
                .stream()
                .map(Station::getId)
                .collect(Collectors.toList())
                .contains(downStationId)) {
            throw new RuntimeException("section's downStation is already included in line");
        }
    }
}
