package nextstep.subway.domain;

import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class StationLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    @Embedded
    private StationSections stationSections = new StationSections();

    protected StationLine() {
    }

    public StationLine(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getId() {
        return id;
    }

    public void changeNameAndColor(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(StationSection stationSection) {
        stationSections.addSection(this, stationSection);
    }

    public List<Long> getStationIds() {
        return stationSections.getStationIds();
    }

    public StationSection findSectionByDownStationId(Long stationId) {
        return stationSections.findByDownStationId(stationId);
    }

    public void deleteSection(StationSection section) {
        stationSections.delete(section);
    }
}
