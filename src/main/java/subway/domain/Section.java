package subway.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "up_station_id", nullable = false)
    private Station upStation;


    @ManyToOne
    @JoinColumn(name = "down_station_id", nullable = false)
    private Station downStation;

    @Column(nullable = false)
    private Long distance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id")
    private SubwayLine subwayLine;

    private Section(Long distance, Station upStation, Station downStation) {
        this.distance = distance;
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public static Section of(Long distance, Station upStation, Station downStation) {
        return new Section(distance, upStation, downStation);
    }

    public void assignSubwayLine(SubwayLine subwayLine) {
        this.subwayLine = subwayLine;
    }

    public List<Station> getStations() {
        return Arrays.asList(upStation, downStation);
    }
}
