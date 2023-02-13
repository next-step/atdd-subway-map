package subway.section;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.exception.ErrorCode;
import subway.exception.SubwayException;
import subway.line.Line;
import subway.station.Station;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id")
    private Line line;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    private Long distance;

    public Section(Line line, Station upStation, Station downStation, Long distance) {
        validateEndStation(line, upStation);
        validateUniqueDownStation(line, downStation);

        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.line.addSection(this);
    }

    private void validateEndStation(Line line, Station upStation) {
        boolean endStationMismatch = !(line.getSections().isEmpty() || upStation.getId()
            .equals(line.getDownStation().getId()));

        if (endStationMismatch) {
            throw new SubwayException(ErrorCode.END_STATION_MISMATCH);
        }
    }

    private void validateUniqueDownStation(Line line, Station downStation) {
        boolean duplicatedStation = line.getSections().stream()
            .anyMatch(section -> section.getUpStation().getId().equals(downStation.getId()));
        if (duplicatedStation) {
            throw new SubwayException(ErrorCode.DUPLICATED_STATION);
        }
    }
}
