package nextstep.subway.line.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.station.dto.StationResponse;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;

    private String color;

    @Embedded
    private Sections sections;

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
        this.sections = new Sections();
    }

    public Line(String name, String color, Section section) {
        this.name = name;
        this.color = color;
        this.sections = Sections.of(this, section);
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
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

    public Sections getSections() {
        return sections;
    }

    public void addSection(Section section) {
        sections.isValidSection(section);
        sections.addSection(this, section);

    }

    public List<StationResponse> getStationsAll() {
        return sections.getStationsAll();
    }

    public void deleteLastSection(Long stationId) {
        sections.deleteLastSection(stationId);
    }


}
