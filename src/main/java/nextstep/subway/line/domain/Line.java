package nextstep.subway.line.domain;

import nextstep.subway.common.domain.BaseEntity;
import nextstep.subway.line.exception.CreateSectionWithWrongUpStationException;
import nextstep.subway.line.exception.DeleteSectionWithNotLastException;
import nextstep.subway.line.exception.DeleteSectionWithOnlyOneException;
import nextstep.subway.line.exception.DownStationDuplicatedException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Line extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String color;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
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

    public List<Section> getSections() {
        return sections;
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        Section section = new Section(upStation, downStation, distance);
        validateAddSection(section);
        this.sections.add(section);
        section.setLine(this);
    }

    public void removeByStationId(Long stationId) {
        validateRemoveSection(stationId);
        getSections().removeIf(it -> it.getDownStation().getId().equals(stationId));
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
        stations.add(sections.get(sections.size() - 1).getDownStation());

        return stations;
    }

    public Long getLastStationId() {
        if (sections.isEmpty()) {
            return 0L;
        }
        return sections.get(sections.size() - 1).getDownStation().getId();
    }

    public boolean isWrongUpStation(Long upStationId) {
        return !sections.isEmpty()
                && !getLastStationId().equals(upStationId);
    }

    public boolean idDownStationDuplicated(Long downStationId) {
        List<Long> stationIds = getStations()
                .stream()
                .map(Station::getId)
                .collect(Collectors.toList());
        return stationIds.contains(downStationId);
    }

    public boolean isNotLastStation(Long stationId) {
        if (sections.isEmpty()) {
            return false;
        }
        return !getLastStationId().equals(stationId);
    }

    public boolean isHasOnlyOneSection() {
        return sections.size() == 1;
    }

    private void validateAddSection(Section section) {
        if (isWrongUpStation(section.getUpStation().getId())) {
            throw new CreateSectionWithWrongUpStationException();
        }
        if (idDownStationDuplicated(section.getDownStation().getId())) {
            throw new DownStationDuplicatedException();
        }
    }

    private void validateRemoveSection(Long stationId) {
        if (isNotLastStation(stationId)) {
            throw new DeleteSectionWithNotLastException();
        }
        if (isHasOnlyOneSection()) {
            throw new DeleteSectionWithOnlyOneException();
        }
    }
}
