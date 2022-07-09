package nextstep.subway.domain;

import lombok.*;
import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.station.Station;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class LineStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;

    public LineStation(Line line, Station station) {
        this.line = line;
        this.station = station;
    }
}
