package subway;

import javax.persistence.*;

@Entity
public class LineStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer sequence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id")
    private Station station;

    public Long getId() {
        return id;
    }

    public Integer getSequence() {
        return sequence;
    }

    public Line getLine() {
        return line;
    }

    public Station getStation() {
        return station;
    }

    public LineStation() {
    }

    public LineStation(Integer sequence, Line line, Station station) {
        this.sequence = sequence;
        this.line = line;
        this.station = station;
    }

}
