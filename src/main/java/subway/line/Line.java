package subway.line;

import javax.persistence.*;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    private String color;

    @Embedded
    private LineStations lineStations;

    protected Line() {
    }

    public Line(final String name, final String color) {
        this.name = name;
        this.color = color;
        this.lineStations = new LineStations();
    }

    public Line addLineStation(final LineStation lineStation) {

        this.lineStations.add(lineStation);
        lineStation.changeLine(this);
        return this;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public LineStations getLineStations() {
        return lineStations;
    }
}
