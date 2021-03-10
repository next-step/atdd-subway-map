package nextstep.subway.line.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Section addSection(Station upStation, Station downStation, int distance) {

        Section section = new Section(this, upStation, downStation, distance);

        if (sections.isEmpty()) {
            sections.add(section);
            return section;
        }

        Station downEndStation = getDownEndStation();
        if (upStation != downEndStation) {
            throw new RuntimeException();
        }

        if (isAlreadyAddedStation(downStation)) {
            throw new RuntimeException();
        }

        sections.add(section);
        return section;
    }

    public Station getUpEndStation() {
        List<Station> upStations = getUpStations();
        List<Station> downStations = getDownStations();
        upStations.removeAll(downStations);
        return upStations.get(0);
    }

    public Station getDownEndStation() {
        List<Station> upStations = getUpStations();
        List<Station> downStations = getDownStations();
        downStations.removeAll(upStations);
        return downStations.get(0);
    }

    public List<Station> getUpStations() {
        return sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
    }

    public List<Station> getDownStations() {
        return sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());
    }

    public boolean isAlreadyAddedStation(Station station) {
        return getAllStations().stream().anyMatch(station::equals);
    }

    // 모든 상행역 + 하행 종점 = 모든 역
    public List<Station> getAllStations() {
        List<Station> upStations = getUpStations();
        upStations.add(getDownEndStation());
        return upStations;
    }

    public Section getLastSection() {
        Station downEndStation = getDownEndStation();
        return sections.stream()
                .filter(it -> it.getDownStation().equals(downEndStation))
                .findFirst().orElse(null);
    }

}
