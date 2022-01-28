package nextstep.subway.domain;

import nextstep.subway.applicaion.Section;
import nextstep.subway.applicaion.dto.StationResponse;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        if (!validateUpStation(section.getUpStation())) {
            throw new IllegalArgumentException("새로운 구간의 상행역은 현재 등록되어있는 하행 종점역이어야 합니다.");
        }

        if (!validateDownStation(section.getDownStation())) {
            throw new IllegalArgumentException("하행역은 현재 등록되어있는 역일 수 없습니다.");
        }

        sections.add(section);

        if (section.getLine() != this) {
            section.changeLine(this);
        }
    }

    public boolean validateUpStation(Station upStation) {
        if (this.sections.isEmpty()) {
            return true;
        }

        return getLastDownStation() == upStation;
    }

    public boolean validateDownStation(Station downStation) {
        Function<Section, Station> getDownStation = Section::getDownStation;
        Function<Section, Station> getUpStation = Section::getUpStation;

        if (getSectionStations(getUpStation).contains(downStation)) {
            return false;
        }

        return !getSectionStations(getDownStation).contains(downStation);
    }

    private List<Station> getSectionStations(Function<Section, Station> func) {
        return sections
            .stream()
            .map(func)
            .collect(Collectors.toList());
    }

    private Station getLastDownStation() {
        return sections
            .stream()
            .map(Section::getDownStation)
            .reduce((a, b) -> b)
            .orElseThrow(() -> new IllegalArgumentException("하행 종점역이 없습니다."));
    }

    public void deleteSection(Long lastDownStationId) {
        if (sections.size() <= 1) {
            throw new IllegalArgumentException("구간이 1개 이하인 경우 역을 삭제할 수 없습니다.");
        }

        Station lastDownStation = getLastDownStation();

        Section delete = sections.stream()
            .filter(section -> section.getDownStation() == lastDownStation)
            .filter(section -> section.getDownStation().getId().equals(lastDownStationId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("마지막 역(하행 종점역)만 제거할 수 있습니다."));

        sections.remove(delete);
    }

    public List<StationResponse> getAllStations() {
        if (sections.isEmpty()) {
            return new ArrayList<>();
        }

        List<Station> allStation = sections.
            stream()
            .map(Section::getUpStation)
            .collect(Collectors.toList());

        allStation.add(getLastDownStation());

        return allStation
            .stream()
            .map(StationResponse::ofStation)
            .collect(Collectors.toList());
    }
}
