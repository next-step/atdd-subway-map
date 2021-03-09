package nextstep.subway.line.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.exception.NotMatchingStationException;
import nextstep.subway.station.exception.StationAlreadyExistException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static nextstep.subway.line.exception.LineExceptionMessage.*;
import static nextstep.subway.station.exception.StationExceptionMessage.*;

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

    public void addSection(Station upStation, Station downStation, int distance) {
        if (sections.isEmpty()) {
            sections.add(createSection(upStation, downStation, distance));
            return;
        }

        validateUpStationMatching(upStation);
        validateExistDownStation(sections, downStation);

        sections.add(createSection(upStation, downStation, distance));
    }

    private Section createSection(Station upStation, Station downStation, int distance) {
        return new Section(this, upStation, downStation, distance);
    }

    private void validateUpStationMatching(Station upStation) {
        if (!getLastDownStation().equals(upStation)) {
            throw new NotMatchingStationException(EXCEPTION_MESSAGE_NOT_MATCHING_EXISTING_AND_NEW_STATION);
        }
    }

    private void validateExistDownStation(List<Section> sections, Station downStation) {
        if (sections.contains(downStation)) {
            throw new StationAlreadyExistException(EXCEPTION_MESSAGE_EXIST_DOWN_STATION);
        }
    }

    public void deleteLastDownStation(Long downStation) {
        Station lastDownStation = getLastDownStation();
        validateDeletableStation(downStation, lastDownStation.getId());
        sections.remove(lastDownStation);
    }

    private void validateDeletableStation(Long downStation, Long lastDownStationId) {
        boolean isEqualStation = lastDownStationId.equals(downStation);

        if (!isEqualStation) {
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_NOT_DELETABLE_STATION);
        }
        if (sections.size() == 1) {
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_NOT_DELETABLE_SECTION);
        }
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

    public Station getLastDownStation() {
        Section section = this.sections.get(sections.size() - 1);
        return section.getDownStation();
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getStations() {
        return sections.stream()
                .sorted()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .distinct()
                .collect(Collectors.toList());
    }
}
