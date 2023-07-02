package subway.line;

import subway.station.Station;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Line {

    private static final Long MIN_DISTANCE = 0L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    @OneToOne
    @JoinColumn(name = "up_station_id", nullable = false)
    private Station upStation;

    @OneToOne
    @JoinColumn(name = "down_station_id", nullable = false)
    private Station downStation;

    @Column(nullable = false)
    private Long distance;

    public Line(String name, String color, Station upStation, Station downStation, Long distance) {
        validate(name, color, upStation.getId(), downStation.getId(), distance);

        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Line() {}

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

    public Station getDownStation() {
        return downStation;
    }

    public Long getDistance() {
        return distance;
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

        if (distance == null || distance <= MIN_DISTANCE) {
            throw new IllegalArgumentException(String.format("지하철 거리는 0 이상의 숫자여야 합니다. (distance: %d)", distance));
        }
    }
}
