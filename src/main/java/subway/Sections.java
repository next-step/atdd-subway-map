package subway;

import org.springframework.util.CollectionUtils;

import java.util.List;

public class Sections {

    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public boolean hasSection(long sectionId) {
        return this.sections.stream()
                .anyMatch(x -> x.getId() == sectionId);
    }

    public boolean equalsLastSectionDownStationId(long stationId) {
        int lastSectionIndex = sections.size();

        if (lastSectionIndex <= 0) {
            return true;
        }

        Section lastSection = sections.get(lastSectionIndex - 1);
        if (lastSection.getDownStationId() == stationId) {
            return true;
        }
        return false;
    }

    public int getSectionCount() {
        if (CollectionUtils.isEmpty(this.sections)) {
            return 0;
        }

        return this.sections.size();
    }

    public boolean equalsLastSection(long sectionId) {
        int lastSectionIndex = sections.size();

        if (lastSectionIndex <= 0) {
            return false;
        }

        Section lastSection = sections.get(lastSectionIndex - 1);
        return lastSection.getId() == sectionId;
    }
}
