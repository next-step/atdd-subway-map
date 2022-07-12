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
        if (isInvalidSize(sections)) {
            throw new SectionException(ErrorCode.INVALID_SIZE_SECTIONS);
        }
        this.sections = sections;
    }

    private boolean isInvalidSize(List<Section> sections) {
        return sections.size() < 1;
    }

    public void add(Section section) {
        this.sections.add(section);
    }

    public void changeUpStation(Station upStation) {
        Section firstSection = sections.get(0);
        firstSection.changeUpStation(upStation);
    }

    public void changeDownStation(Station downStation) {
        Section lastSection = sections.get(sections.size() - 1);
        lastSection.changeDownStation(downStation);
    }

    public List<Station> getStations() {
        final List<Station> stations = new ArrayList<>();
        stations.add(sections.get(0).getUpStation());
        for (Section section : sections) {
            stations.add(section.getDownStation());
        }

        return stations;
    }

    public boolean isDownStation(Station station) {
        return sections.get(sections.size() - 1).getDownStation().equals(station);
    }

    public boolean isOwnStation(Station station) {
        return sections.stream()
                .filter(section -> section.isOwnStation(station))
                .findAny()
                .isPresent();
    }

    public void deleteLastSection(Station station) {
        Section lastSection = sections.get(sections.size() - 1);
        if (!lastSection.isOwnDownStation(station)) {
            throw new SectionException(ErrorCode.CAN_NOT_DELETE_SECTION_EXCEPTION);
        }
        sections.remove(lastSection);
    }
}
