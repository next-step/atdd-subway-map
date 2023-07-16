package subway.line.domain;

import subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "line_id", nullable = false)
    private Line line;

    @OneToOne
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    public Section() {
    }

    public Section(Long id, Line line, Station station) {
        this.id = id;
        this.line = line;
        this.station = station;
    }

    public Long getId() {
        return id;
    }

    public Line getLine() {
        return line;
    }

    public Station getStation() {
        return station;
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", line=" + line +
                ", station=" + station +
                '}';
    }

}
