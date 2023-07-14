package subway.line;

import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import subway.line.section.Section;
import subway.line.section.Sections;
import subway.station.Station;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    @Embedded
    private Sections sections;
    private int distance;

    public Line() {}

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this(null, name, color, new Sections(), distance);
        this.sections.add(this, upStation, downStation);
    }

    public Line(Long id, String name, String color, Sections sections, int distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
        this.distance = distance;
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Station upStation, Station downStation) {
        this.sections.add(this, upStation, downStation);
    }

    public Section getLastSection() {
        return this.sections.getLastSection();
    }

    public void validateSection(Station upStation, Station downStation) {
        this.sections.validate(upStation, downStation);
    }

    public void deleteSection(Station station) {
        this.sections.delete(station);
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

    public List<Station> getStations() {
        return this.sections.getStations();
    }

    public List<Section> getSections() {
        return this.sections.getSections();
    }

    public int getDistance() {
        return distance;
    }
}
