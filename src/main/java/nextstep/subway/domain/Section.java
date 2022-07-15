package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.exception.BadRequestException;

import javax.persistence.*;
import java.util.Objects;

import static java.util.Objects.*;

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
    @JoinColumn(name = "up_station_id", nullable = false)
    private Station upStation;

    @OneToOne
    @JoinColumn(name = "down_station_id", nullable = false)
    private Station downStation;

    @Column(nullable = false)
    private Integer distance;

    @Builder(toBuilder = true)
    public Section(Line line, Station upStation, Station downStation, Integer distance) {
        requireNonNull(line, "line is required");
        requireNonNull(upStation, "upStation is required");
        requireNonNull(downStation, "downStation is required");
        requireNonNull(distance, "distance is required");

        if (upStation.equals(downStation)) {
            throw new BadRequestException("upStation and downStation can not be same");
        }

        if (isNegativeNumber(distance)) {
            throw new BadRequestException("distance required positive number");
        }

        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
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

    private boolean isNegativeNumber(Integer distance) {
        return distance < 0;
    }
}
