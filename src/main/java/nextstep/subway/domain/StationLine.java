package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.StationLineRequest;

import javax.persistence.*;
import java.util.List;

@Entity
public class StationLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    @Embedded
    private Sections sections = new Sections();

    public StationLine() {
    }

    public StationLine(String name, String color) {
        this.name = name;
        this.color = color;
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
        return this.sections;
    }

    public void addSection(Section section) {
        this.sections.addSection(section);
        section.setStationLine(this);
    }

    public void deleteSection(Section section) {
        this.sections.getSections()
                .remove(section);
    }

    public void updateByStationLineRequest(StationLineRequest stationLineRequest) {
        if (stationLineRequest.getName() != null) {
            this.name = stationLineRequest.getName();
        }
        if (stationLineRequest.getColor() != null) {
            this.color = stationLineRequest.getColor();
        }
    }

    public List<Station> getStationsIncluded() {
        return this.sections.getStationsIncludedInLine();
    }
}
