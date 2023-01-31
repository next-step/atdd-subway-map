package subway.section;

import lombok.Getter;
import subway.station.Station;

import javax.persistence.*;

@Getter
@Table(name = "sections")
@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer distance;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "begin_station_id")
    private Station beginStation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_station_id")
    private Station endStation;

    public Section() {
    }

    public Section(Integer distance, Station beginStation, Station endStation) {
        this.distance = distance;
        this.beginStation = beginStation;
        this.endStation = endStation;
    }
}
