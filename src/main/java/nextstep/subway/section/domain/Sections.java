package nextstep.subway.section.domain;

import nextstep.subway.section.exception.SectionNotLastStationException;
import nextstep.subway.section.exception.SectionNotMatchException;
import nextstep.subway.section.exception.SectionSingleException;
import nextstep.subway.section.exception.SectionWithInvalidStationException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    protected Sections() {}

    public Sections(Section section) {
        sections.add(section);
    }

    public void addSection(Section section) {
        Section lastSection = getLastSection();
        if (!lastSection.matchable(section)) {
            throw new SectionNotMatchException();
        }
        if (contains(section.getDownStation())) {
            throw new SectionWithInvalidStationException();
        }
        sections.add(section);
    }


    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        stations.add(sections.get(0).getUpStation());
        for (Section section : sections) {
            stations.add(section.getDownStation());
        }
        return stations;
    }

    public void deleteSection(Long stationId) {
        int multiple = 2;
        if (sections.size() < multiple) {
            throw new SectionSingleException();
        }
        Section lastSection = getLastSection();
        if (!lastSection.matchStationId(stationId)) {
            throw new SectionNotLastStationException();
        }
        removeLastSection();
    }


    private int getLastSectionIndex() {
        return sections.size() - 1;
    }

    private Section getLastSection() {
        return sections.get(getLastSectionIndex());
    }

    private void removeLastSection() {
        sections.remove(getLastSectionIndex());
    }

    private boolean contains(Station station) {
        return sections.stream()
                .anyMatch(section -> section.contains(station));
    }
}
