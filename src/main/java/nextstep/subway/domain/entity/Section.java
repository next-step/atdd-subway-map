package nextstep.subway.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.exception.BadRequestException;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "line_id", nullable = false)
    private Line line;


    @OneToOne
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @OneToOne
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    @Builder(toBuilder = true)
    public Section(Line line, Station upStation, Station downStation) {
        if (upStation.equals(downStation)) {
            throw new BadRequestException("upStation and downStation can not be same");
        }

        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public boolean existAnyStation(Station station) {
        if (this.upStation.equals(station)) {
            return true;
        }

        return this.downStation.equals(station);
    }

    public boolean equalsUpStation(Station station) {
        return this.upStation.equals(station);
    }

    public boolean equalsDownStation(Station station) {
        return this.downStation.equals(station);
    }
}
