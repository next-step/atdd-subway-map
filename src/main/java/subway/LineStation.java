package subway;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import subway.line.Line;
@Entity
@Table(name = "line_station")
public class LineStation {
    @Id
    @Column(name = "line_station_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;
}
