package nextstep.subway.line.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    private String color;

    @Embedded
    private final Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        sections.add(new Section(distance, this, upStation, downStation));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void addStation(int distance, Station upStation, Station downStation) {
        sections.addStation(distance, this, upStation, downStation);
    }

    public void removeStation(Long stationId) {
        sections.removeStation(stationId);
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

    public Sections getSections() {
        return sections;
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
