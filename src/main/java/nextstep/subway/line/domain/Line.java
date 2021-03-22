package nextstep.subway.line.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.section.domain.Section;
import nextstep.subway.section.exception.SectionNotLastStationException;
import nextstep.subway.section.exception.SectionNotMatchException;
import nextstep.subway.section.exception.SectionSingleException;
import nextstep.subway.section.exception.SectionWithInvalidStationException;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Line() {}

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        stations.add(sections.get(0).getUpStation());
        for (Section section : sections) {
            stations.add(section.getDownStation());
        }
        return stations;
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
