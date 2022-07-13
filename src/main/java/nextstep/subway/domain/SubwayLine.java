package nextstep.subway.domain;

import javax.persistence.*;
import java.util.Set;

@Entity
public class SubwayLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    @Embedded
    private Sections sections = new Sections();

    public SubwayLine() {
    }

    public SubwayLine(String name, String color, Section section) {
        this.name = name;
        this.color = color;
        this.addSection(section);
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

    public void modify(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        if (this.sections == null) {
            this.sections = new Sections();
        }
        this.sections.add(new Section(section, this));
    }

    public Set<Long> getStationIds() {
        return this.sections.getStationIds();
    }

    public void removeStation(Long stationId) {
        this.sections.remove(stationId);
    }
}
