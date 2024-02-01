package subway.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "up_station_id", nullable = false)
    private Section upStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "down_station_id", nullable = false)
    private Section downStation;

    @Column(nullable = false)
    private Long distance;

    protected Section() {
    }

    public Section(Section upStation, Section downStation, Long distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }
    public List<Long> stationIds(){
        return List.of(upStation.id, downStation.id);
    }

}
