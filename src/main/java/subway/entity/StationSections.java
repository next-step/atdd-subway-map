package subway.entity;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

public class StationSections {

    public static final int MIN_DELETE_REQUIRED_SECTIONS_SIZE = 1;

    @OneToMany(mappedBy = "stationLine", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<StationSection> sections = new ArrayList<>();

    protected StationSections() {
    }

    public void addSection(StationSection section) {
        sections.add(section);
    }

    public Long findFirstUpStation() {
        return sections.get(0).getUpStationId();
    }

    public boolean areAllUpStationsDifferentFrom(StationSection newSection) {
        return sections.stream()
                .noneMatch(existSection -> existSection.isUpStationSame(newSection.getDownStationId()));
    }

    public boolean isDeletionAllowed() {
        return sections.size() > MIN_DELETE_REQUIRED_SECTIONS_SIZE;
    }

    public void deleteSection(Long stationIdToDelete) {
        StationSection stationSectionToDelete = sections.stream()
                .filter(section -> section.getDownStationId().equals(stationIdToDelete))
                .findFirst()
                .orElseThrow(RuntimeException::new);

        sections.remove(stationSectionToDelete);
    }

    public Long findLastStationId() {
        if (sections.isEmpty()) {
            throw new RuntimeException();
        }
        return sections.get(sections.size() - 1).getDownStationId();
    }

    public List<StationSection> getSections() {
        return sections;
    }
}
