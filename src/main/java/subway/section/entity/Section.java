package subway.section.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.exception.ErrorCode;
import subway.exception.SubwayException;
import subway.station.entity.Station;
import subway.subwayline.entity.SubwayLine;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subway_line_id")
    private SubwayLine subwayLine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "down_station_id")
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
        this.upStation = subwayLine.getUpStationId();
        this.downStation = subwayLine.getDownStationId();
    }

    public boolean isDownStation(Station station) {
        if (!downStation.equals(station)) throw new SubwayException(ErrorCode.NOT_DOWN_STATION);
        return true;
    }
}
