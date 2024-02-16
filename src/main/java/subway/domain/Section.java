package subway.domain;

import javax.persistence.*;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "up_station_id")
    private Station upStationId;

    @ManyToOne
    @JoinColumn(name = "down_station_id")
    private Station downStationId;
    private int distance;

    @ManyToOne
    @JoinColumn(name = "line_id")
    private Line line;

    public Section() {
    }

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStationId = upStation;
        this.downStationId = downStation;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Station getUpStationId() {
        return upStationId;
    }

    public Station getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public Line getLine() {
        return line;
    }
}
