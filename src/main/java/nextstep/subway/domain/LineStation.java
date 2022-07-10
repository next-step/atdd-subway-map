package nextstep.subway.domain;

import lombok.*;
import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.station.Station;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private LineStation next;

    @Builder
    protected LineStation(Line line, Station station, LineStation next) {
        this.line = line;
        this.station = station;
        this.next = next;
    }
}
