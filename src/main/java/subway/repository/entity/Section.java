package subway.repository.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.exception.SubwayRuntimeException;
import subway.exception.message.SubwayErrorCode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Section {
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

    private Integer distance;

    @Builder
    private Section(Station upStation,
                    Station downStation,
                    Integer distance) {
        validate(upStation, downStation);

        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    private void validate(Station upStation, Station downStation) {
        if (upStation.equals(downStation)) {
            throw new SubwayRuntimeException(SubwayErrorCode.SECTION_SAME_STATION);
        }
    }

    public static Section of (Station upStation,
                              Station downStation,
                              Integer distance) {

        return Section.builder()
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .build();
    }

    public void updateLine(Line line) {
        this.line = line;
    }

    public boolean isDownStationId(long stationId) {
        return downStation.getId() == stationId;
    }

    public boolean addable(Section lastSection) {
        return upStation.equals(lastSection.downStation);
    }

    public boolean addable(List<Station> stations) {
        return !stations.contains(this.downStation);
    }
}
