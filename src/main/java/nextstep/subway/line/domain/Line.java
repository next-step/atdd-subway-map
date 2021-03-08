package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.line.exception.AlreadyExistDownStationException;
import nextstep.subway.line.exception.NotSameUpStationException;
import nextstep.subway.station.domain.Station;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public static Line of(
        String name, String color, Station upStation,
        Station downStation, int distance) {
        Line line = new Line();
        line.name = name;
        line.color = color;
        line.sections.add(Section.of(line, upStation, downStation, distance));
        return line;
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void addStations(final Station upStation, final Station downStation, int distance) {
        final Station lastStation = findLastStation();
        validateStation(upStation, downStation, lastStation);
        sections.add(Section.of(this, upStation, downStation, distance));
    }

    public List<Station> getStations() {
        return sections.stream()
            .sorted()
            .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
            .distinct()
            .collect(Collectors.toList());
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

    private Station findLastStation() {
        return getSections().get(sections.size() - 1).getDownStation();
    }

    private void validateStation(Station upStation, Station downStation, Station lastStation) {
        if (!upStation.equals(lastStation)) {
            throw new NotSameUpStationException(upStation.getName(), lastStation.getName());
        }
        if (getStations().contains(downStation)) {
            throw new AlreadyExistDownStationException(downStation.getName());
        }
    }
}
