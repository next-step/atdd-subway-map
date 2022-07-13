package nextstep.subway.domain;

import nextstep.subway.exception.ErrorCode;
import nextstep.subway.exception.unchecked.SectionException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public Sections(List<Section> sections) {
        if (isInvalidSectionsSize(sections)) {
            throw new SectionException(ErrorCode.INVALID_SIZE_SECTIONS);
        }
        this.sections = sections;
    }

    private boolean isInvalidSectionsSize(List<Section> sections) {
        return sections.isEmpty();
    }

    public void add(Section section) {
        this.sections.add(section);
    }

    public List<Station> getStations() {
        final List<Station> stations = new ArrayList<>();
        stations.add(getFirstSection().getUpStation());
        for (Section section : sections) {
            stations.add(section.getDownStation());
        }
        return new ArrayList<>(stations);
    }

    public boolean hasDownStation(Station station) {
        return sections.get(getLastIndex()).getDownStation().equals(station);
    }

    public boolean isOwnStation(Station station) {
        return sections.stream()
                .anyMatch(section -> section.isOwnStation(station));
    }

    public void deleteLastSection(Station station) {
        if (!isValidSize()) {
            throw new SectionException(ErrorCode.CAN_NOT_DELETE_SECTION_EXCEPTION);
        }
        Section lastSection = sections.get(getLastIndex());
        if (!lastSection.isOwnDownStation(station)) {
            throw new SectionException(ErrorCode.CAN_NOT_DELETE_SECTION_EXCEPTION);
        }
        sections.remove(lastSection);
    }

    private int getLastIndex() {
        return sections.size() - 1;
    }

    private Section getFirstSection() {
        return sections.get(0);
    }

    private boolean isValidSize() {
        return sections.size() > 1;
    }
}
