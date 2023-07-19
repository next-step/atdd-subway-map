package subway.section.policy;

import subway.line.repository.Line;
import subway.section.repository.Sections;
import subway.station.repository.Station;

public class DeleteSectionPolicy {
    public static void validate(Sections sections, Station station) {
        if (sections.size() == 1) {
            throw new RuntimeException("line's section is just one");
        }

        if (sections.getDownEndStation() != station) {
            throw new RuntimeException("request's station is not line's downEndStation");
        }
    }
}
