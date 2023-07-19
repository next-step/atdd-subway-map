package subway.section.policy;

import subway.line.repository.Line;
import subway.section.repository.Section;

public class AddSectionPolicy {
    public static void validate(Line line, Section section) {
        if (line.getDownEndStation() != section.getUpStation()) {
            throw new RuntimeException("section's upStation is not line's downEndStation");
        }

        if (line.getAllStation().contains(section.getDownStation())) {
            throw new RuntimeException("section's downStation is already included in line");
        }
    }
}
