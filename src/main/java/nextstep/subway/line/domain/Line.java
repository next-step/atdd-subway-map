package nextstep.subway.line.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public Line(String name, String color, Section section) {

        this.sections = new Sections();

        this.name = name;
        this.color = color;

        if (section !=null) {
            this.sections.addSection(section);
        }
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void extendsLine(Section section) {
        this.sections.addSection(section);
    }

    public Station getLastStation() {
        return this.sections.lastStationOfSections();
    }

    public boolean isRemovableStation(Long stationId) {
        return this.getLastStation().getId().equals(stationId);
    }

    public void removeSection(Long stationId) {
        this.sections.removeSectionByStationId(stationId);
    }

    public boolean isExtensionAvailable(Section section) {

        return isAddSectionAvailable(section);
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

    private boolean isAddSectionAvailable(Section section) {

        return this.sections.isAddSectionAvailable(section);
    }

    public List<Station> getAllLineStations() {
        return this.sections.getAllStations();
    }
}
