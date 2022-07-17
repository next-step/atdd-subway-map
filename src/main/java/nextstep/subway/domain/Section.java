package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;

    @ManyToOne
    @JoinColumn(name = "line_id")
    private Line line;

    @OneToOne
    private Distance distance;

    public Section(Station station, Line line, Distance distance) {
        this.station = station;
        this.line = line;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Station getStation() {
        return station;
    }

}
