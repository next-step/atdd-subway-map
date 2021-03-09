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
            throw new NotMatchingStationException("기존 노선의 하행선과 신규 노선의 상행선이 같지 않습니다.");
        }
    }

    private void validateExistDownStation(List<Section> sections, Station downStation) {
        if (sections.contains(downStation)) {
            throw new StationAlreadyExistException("하행선이 이미 존재합니다.");
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
            throw new IllegalArgumentException("하행 종점역만 제거할 수 있습니다.");
        }
        if (sections.size() == 1) {
            throw new IllegalArgumentException("구간을 더이상 삭제할 수 없습니다.");
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
