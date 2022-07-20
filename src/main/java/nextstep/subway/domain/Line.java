package nextstep.subway.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Line {
    private static final String NOT_ALLOWED_EQUAL_STATIONS = "상행종점역과 하행종점역은 같을 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    private int distance;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    public Line(Long id, String name, String color, int distance, Station upStation, Station downStation) {
        this(name, color, distance, upStation, downStation);
        this.id = id;
    }

    public Line(String name, String color, int distance, Station upStation, Station downStation) {
        if (upStation.equals(downStation)) {
            throw new IllegalArgumentException(NOT_ALLOWED_EQUAL_STATIONS);
        }

        this.name = name;
        this.color = color;
        this.distance = distance;
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
