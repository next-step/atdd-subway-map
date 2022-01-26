package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.exception.ValidationException;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    private int distance;

    public Section() {
    }

    public static Section of(Line line, Station upStation, Station downStation, SectionRequest request) {
        return new Section(line, upStation, downStation, request.getDistance());
    }

    public static void validateForSave(final List<Section> sections, final Long upStationId, final Long downStationId) {
        final List<Station> registeredDownStations = getRegisteredDownStation(sections);
        final List<Station> registeredUpStations = getRegisteredUpStation(sections);

        final Station lastRegisteredDownStation = registeredDownStations.get(registeredDownStations.size() - 1);
        if(!lastRegisteredDownStation.getId().equals(upStationId)) {
            throw new ValidationException("새로운 구간의 상행역은 현재 등록되어있는 하행 종점역이어야 합니다.");
        }

        if(getIds(registeredDownStations).contains(downStationId) || getIds(registeredUpStations).contains(downStationId)) {
            throw new ValidationException("새로운 구간의 하행역은 현재 등록되어있는 역일 수 없습니다.");
        }
    }

    public static void validateForDelete(final List<Section> sections, final Long downStationId) {
        if(sections.isEmpty() || sections.size() == 1) {
            throw new ValidationException("구간을 삭제할 수 없습니다.");
        }

        final List<Station> registeredDownStations = getRegisteredDownStation(sections);
        final Station lastRegisteredDownStation = registeredDownStations.get(registeredDownStations.size() - 1);
        if(!lastRegisteredDownStation.getId().equals(downStationId)) {
            throw new ValidationException("마지막 구간만 삭제할 수 있습니다.");
        }
    }

    private Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    private static List<Station> getRegisteredDownStation(final List<Section> sections) {
        return sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());
    }

    private static List<Station> getRegisteredUpStation(final List<Section> sections) {
        return sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
    }

    private static List<Long> getIds(List<Station> registeredStations) {
        return registeredStations.stream()
                .map(Station::getId)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public Line getLine() {
        return line;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance;
    }
}
