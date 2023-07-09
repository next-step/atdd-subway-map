package subway.section.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.station.entity.Station;
import subway.subwayline.entity.SubwayLine;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subway_line_id", nullable = false)
    private SubwayLine subwayLine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "up_station_id", nullable = false)
    private Station upStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "down_station_id", nullable = false)
    private Station downStation;

    private Integer distance;

    @Builder
    public Section(SubwayLine subwayLine, Station upStation, Station downStation, Integer distance) {
        this.subwayLine = subwayLine;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public void addSection(SubwayLine subwayLine) {
        this.subwayLine = subwayLine;
    }

    public boolean isDownStation(Station station) {
        return downStation.equals(station);
    }
}
