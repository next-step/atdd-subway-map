package subway.section.policy;

import subway.line.repository.Line;

import java.util.Objects;

public class DeleteSectionPolicy {
    public static void validate(Line line, Long stationId) {
        if (line.getSections().size() == 1) {
            throw new RuntimeException("line's section is just one");
        }

        if (!Objects.equals(line.getDownEndStation().getId(), stationId)) {
            throw new RuntimeException("request's stationId is not line's downEndStationId");
        }
    }
}
