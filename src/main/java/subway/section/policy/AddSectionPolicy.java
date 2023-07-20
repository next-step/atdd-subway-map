package subway.section.policy;

import subway.section.repository.Section;
import subway.section.repository.Sections;

public class AddSectionPolicy {
    public static void validate(Sections sections, Section section) {
        if (sections.getDownEndStation() != section.getUpStation()) {
            throw new RuntimeException("section's upStation is not line's downEndStation");
        }

        if (sections.getAllStation().contains(section.getDownStation())) {
            throw new RuntimeException("section's downStation is already included in line");
        }
    }
}
