package subway.section.policy;

import subway.line.repository.Line;
import subway.station.repository.Station;

import java.util.Objects;

public class DeleteSectionPolicy {
    public static void validate(Line line, Station station) {
        if (line.getSections().size() == 1) {
            throw new RuntimeException("line's section is just one");
        }

        if (line.getDownEndStation() != station) {
            throw new RuntimeException("request's station is not line's downEndStation");
        }
    }
}
