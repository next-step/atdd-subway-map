package nextstep.subway.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Section extends BaseEntity{
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

    public Section(Line line, Station upStation, Station downStation) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public Long getUpStationId() {
        return this.upStation.getId();
    }

    public Long getDownStationId() {
        return this.downStation.getId();
    }
}