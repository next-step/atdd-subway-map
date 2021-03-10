package nextstep.subway.line.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.line.application.SectionValidationException;
import nextstep.subway.section.domain.Section;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public List<Section> getSections() {
        return sections;
    }

    public void deleteStation(Long stationId) {
        checkStationDeleteValidity(stationId);
        getSections().stream()
                .filter(it -> it.getDownStation().getId() == stationId)
                .findFirst()
                .ifPresent(it -> getSections().remove(it));
    }

    public void addSectionToLine(Station upStation, Station downStation, int distance) {
        checkSectionAddValidity(upStation, downStation);
        getSections().add(new Section(this, upStation, downStation, distance));
    }

    private void checkStationDeleteValidity(Long stationId) {
        if (getSections().size() <= 1) {
            throw new SectionValidationException("구간이 1개인 경우 역을 삭제할 수 없습니다.");
        }

        List<Station> stationsOfLine = getStations();
        int lastIndex = stationsOfLine.size() - 1;
        if (stationsOfLine.get(lastIndex).getId() != stationId) {
            throw new SectionValidationException("하행 종점역만 삭제가 가능합니다.");
        }
    }

    private void checkSectionAddValidity(Station newUpStation, Station newDownStation) {
        List<Station> stations = getStations();
        if (stations.isEmpty()) {
            return;
        }
        if (isNotValidUpStation(newUpStation, stations)) {
            throw new SectionValidationException("새로운 구간의 상행역은 기존 하행 종점역이어야 합니다.");
        }
        if (isNotValidDownStation(newDownStation, stations)) {
            throw new SectionValidationException("하행역이 이미 등록되어 있습니다.");
        }
    }

    private boolean isNotValidUpStation(Station newUpStation, List<Station> stations) {
        return stations.get(stations.size() - 1).getId() != newUpStation.getId();
    }

    private boolean isNotValidDownStation(Station newDownStation, List<Station> stations) {
        return stations.stream().anyMatch(it -> it.getId() == newDownStation.getId());
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();

        if (getSections().isEmpty()) {
            return stations;
        }

        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = getSections().stream()
                    .filter(it -> it.getUpStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }
        return stations;
    }

    private Station findUpStation() {
        Station downStation = getSections().get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = getSections().stream()
                    .filter(it -> it.getDownStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }
        return downStation;
    }

}
