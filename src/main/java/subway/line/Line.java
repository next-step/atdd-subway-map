package subway.line;

import subway.section.Section;
import subway.section.Sections;
import subway.station.Station;

import javax.persistence.*;
import java.util.Objects;
import java.util.Optional;

@Entity
public class Line {

    private static final Long MIN_DISTANCE = 0L;
    private static final long MIN_SECTION_COUNT = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "up_station_id", nullable = false)
    private Station upStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "down_station_id", nullable = false)
    private Station downStation;

    @Column(nullable = false)
    private Long distance;

    @Embedded
    private Sections sections;

    public Line(String name, String color, Station upStation, Station downStation, Long distance) {
        validate(name, color, upStation.getId(), downStation.getId(), distance);

        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Line() {
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

    public Station getUpStation() {
        return upStation;
    }

    public Long getDownStationId() {
        return downStation.getId();
    }

    public Station getDownStation() {
        return downStation;
    }

    public Long getDistance() {
        return distance;
    }

    public void update(String name, String color, Station upStation, Station downStation, Long distance) {
        validate(name, color, upStation.getId(), downStation.getId(), distance);

        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    private void validate(String name, String color, Long upStationId, Long downStationId, Long distance) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(String.format("지하철 노선 이름이 없습니다. (name: %s)", name));
        }

        if (color == null || color.isBlank()) {
            throw new IllegalArgumentException(String.format("지하철 노선 색상이 없습니다. (color: %s)", color));
        }

        if (Objects.equals(upStationId, downStationId)) {
            throw new IllegalArgumentException(String.format("상행종점역과 하행종점역이 같을 수 업습니다. (stationId: %d)", upStationId));
        }

        validateDistance(distance);
    }

    public void setDownStation(Station downStation) {
        this.downStation = downStation;
    }

    public void plusDistance(Long distance) {
        validateDistance(distance);

        this.distance += distance;
    }

    private void validateDistance(Long distance) {
        if (distance == null || distance <= MIN_DISTANCE) {
            throw new IllegalArgumentException(String.format("지하철 거리는 0 이상의 숫자여야 합니다. (distance: %d)", distance));
        }
    }

    public boolean contains(Station station) {
        return sections.contains(station);
    }

    public Section deleteSectionByDownStationId(Long downStationId) {
        if (!downStation.getId().equals(downStationId)) {
            throw new IllegalArgumentException(String.format("하행 종점역이 아니면 구간을 제거할 수 없습니다. (stationId: %d)", downStationId));
        }

        if (sections.count() <= MIN_SECTION_COUNT) {
            throw new IllegalArgumentException(String.format("구간은 최소 %d개 이상이야 합니다.", MIN_SECTION_COUNT));
        }

        Optional<Section> optionalSection = sections.findByDownStationId(downStationId);
        if (optionalSection.isEmpty()) {
            throw new IllegalArgumentException(String.format("구간을 찾을 수 없습니다. (downStationId: %d)", downStationId));
        }

        Section section = optionalSection.get();
        sections.delete(section);
        downStation = section.getUpStation();
        distance -= section.getDistance();

        return section;
    }
}
