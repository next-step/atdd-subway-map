package subway.section.policy;

import subway.line.repository.Line;
import subway.station.repository.Station;

public class AddSectionPolicy {
    public static void validate(Line line, Station upStation, Station downStation) {
        if (line.getDownEndStation() != upStation) {
            throw new RuntimeException("section's upStation is not line's downEndStation");
        }

        if (line.getAllStation().contains(downStation)) {
            throw new RuntimeException("section's downStation is already included in line");
        }
    }
}
