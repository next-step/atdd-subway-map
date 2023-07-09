package subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.springframework.util.StringUtils;
import subway.section.domain.Section;
import subway.section.domain.SectionStations;
import subway.station.domain.Station;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @Embedded
    private LineLastStations lastStations;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Section> sections;

    protected Line() {}

    public Line(String name, String color, LineLastStations lastStations) {
        if (!StringUtils.hasText(name) || !StringUtils.hasText(color)) {
            throw new IllegalArgumentException();
        }
        this.name = name;
        this.color = color;
        this.lastStations = lastStations;
        this.sections = new ArrayList<>();
    }

    public void updateName(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException();
        }
        this.name = name;
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

    public void updateColor(String color) {
        if (!StringUtils.hasText(color)) {
            throw new IllegalArgumentException();
        }
        this.color = color;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void addBaseSection(Integer distance) {
        if (!sections.isEmpty()) {
            throw new IllegalArgumentException();
        }

        SectionStations stations = SectionStations.createLineBaseSection(lastStations);
        Section section = new Section(this, stations, distance);
        sections.add(section);
    }

    public LineLastStations getLastStations() {
        return lastStations;
    }

    public void addSection(Section section) {
        if (!lastStations.checkCanAddSection(section.getStations())) {
            throw new IllegalArgumentException();
        }

        if (isAlreadyInLineStation(section.getDownwardStation())) {
            throw new IllegalArgumentException();
        }

        sections.add(section);
        lastStations.updateDownLastStation(section.getDownwardStation());
    }

    private boolean isAlreadyInLineStation(Station downwardStation) {
        for (Section section : sections) {
            if (section.checkStationInSection(downwardStation)){
                return true;
            }
        }
        return false;
    }
}
