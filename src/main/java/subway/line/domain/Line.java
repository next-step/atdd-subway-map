package subway.line.domain;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import subway.station.domain.Station;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "up_sation_id")
    private Station upStation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "down_sation_id")
    private Station downStation;

    private Integer distance;

    protected Line() {
    }

    public Line(String name, String color, Station upStation, Station downStation, Integer distance) {
        validateStation(upStation, downStation);

        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    private void validateStation(Station upStation, Station downStation) {
        if (upStation.equals(downStation)) {
            throw new IllegalArgumentException("upStation과 downStation은 같을 수 없습니다.");
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

    public List<Station> getStations() {
        return List.of(upStation, downStation);
    }

    public Integer getDistance() {
        return distance;
    }

    //TODO null이 들어오면 어떻게 할지 고민 -> null일 경우 예외를 던질까?
    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
