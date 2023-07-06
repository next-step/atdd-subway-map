package subway.domain.line;

import org.springframework.util.Assert;
import subway.domain.station.Station;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private LineDetail lineDetail;

    @Embedded
    private LineStations lineStations;

    protected Line() {
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.lineDetail = new LineDetail(name, color, distance);
        this.lineStations = new LineStations(this, upStation, downStation);
    }

    public void modify(String name, String color) {
        lineDetail.modify(name, color);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return lineDetail.getName();
    }

    public String getColor() {
        return lineDetail.getColor();
    }

    public List<Station> unmodifiableStations() {
        return Collections.unmodifiableList(lineStations.getStations());
    }

}
