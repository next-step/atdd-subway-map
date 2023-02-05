package subway.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    @Embedded
    private final Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color, Station upStation, Station downStation, Long distance) {
        this.name = name;
        this.color = color;
        this.sections.init(Section.of(this, upStation, downStation, distance));
    }


    public void updateNameAndColor(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void deleteSection(Station station) {
        this.sections.delete(station);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void addSection(Station upStation, Station downStation, Long distance) {
        this.sections.addSection(Section.of(this, upStation, downStation, distance));
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
}
