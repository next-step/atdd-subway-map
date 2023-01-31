package subway.domain;

import java.util.Collections;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public boolean hasLastStation(Station station) {
        return sections.hasLastStation(station);
    }

    public boolean hasStation(Station station) {
        return sections.hasStation(station);
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public List<Station> getAllStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }
        return sections.getStations();
    }

    public Station getLastStation() {
        return sections.getLastStation();
    }

    public void removeSection() {
        sections.remove();
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
