package nextstep.subway.line.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.line.exception.LineIllegalStationException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
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

    public Line(String name, String color, Station upStaion, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        sections.add(new Section(this, upStaion, downStation, distance));
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

    public void addSections(Section section) {
        sections.add(section);
    }

    public List<StationResponse> getSortedStations() {
        List<StationResponse> stations = sections
                .stream()
                .map(s -> StationResponse.of(s.getUpStation()))
                .sorted(Comparator.comparing(s -> s.getId()))
                .collect(Collectors.toList());
        stations.add(StationResponse.of(sections.get(getLastSectionIndex()).getDownStation()));
        return stations;
    }

    public void validateStationAdd(Station upStation, Station downStation) {
        validateNewUpStation(upStation);
        validateNewDownStation(downStation);
    }

    public void validateStationDelete(Long stationId) {
        checkMinimumSection();
        checkLastDownStation(stationId);
    }

    public void removeLastSections() {
        sections.remove(getLastSectionIndex());
    }

    private int getLastSectionIndex() {
        return sections.size() - 1;
    }

    private void validateNewUpStation(Station upStation) {
        Section lastSection = sections.get(getLastSectionIndex());

        if (lastSection.getDownStation() != upStation) {
            throw new LineIllegalStationException("새 구간의 상행역은 기존 하행 종점역이어야 합니다.");
        }
    }

    private void validateNewDownStation(Station downStation) {
        sections.stream()
                .filter(s -> s.getUpStation() == downStation || s.getDownStation() == downStation)
                .findAny()
                .ifPresent(s -> {
                    throw new LineIllegalStationException("새 구간의 하행역은 기존에 존재할 수 없습니다.");
                });
    }

    private void checkMinimumSection() {
        if (sections.size() <= 1) {
            throw new LineIllegalStationException("구간이 1개 이하여서 삭제 불가합니다.");
        }
    }

    private void checkLastDownStation(Long stationId) {
        Section lastSection = sections.get(getLastSectionIndex());
        if (lastSection.getDownStation().getId() != stationId) {
            throw new LineIllegalStationException("마지막 역만 제거 가능합니다.");
        }
    }
}
