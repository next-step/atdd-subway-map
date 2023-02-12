package subway.line.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.station.repository.entity.Station;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "line")
@Getter
@NoArgsConstructor
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 10, nullable = false)
    private String name;
    @Column(length = 20, nullable = false)
    private String color;
    @Embedded
    private Sections sections;

    public Line(String name, String color, Sections sections) {
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public Long getLastStationId() {
        return sections.getLastStationId();
    }

    public Section getFirstSection() {
        return sections.getFirstSection();
    }

    public Long getFirstSectionId() {
        return getFirstSection().getId();
    }

    public Section getLastSection() {
        return sections.getLastSection();
    }

    public Long getLastSectionId() {
        return getLastSection().getId();
    }

    public Line modify(String name, String color) {
        this.name = name;
        this.color = color;

        return this;
    }

    public void addSection(Section section) {
        this.sections.addSection(section);
    }

    public List<Station> getAllStations() {
        return sections.getAllStations();
    }

    public void removeSection(Long stationId) {
        sections.remove(stationId);
    }

    public boolean hasStation(Long stationId) {
        return getAllStations().stream()
                .map(Station::getId)
                .collect(Collectors.toList())
                .contains(stationId);
    }

    public int getSectionsCount() {
        return sections.getSectionsCount();
    }

}
