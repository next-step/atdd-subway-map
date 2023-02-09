package subway.domain;

import subway.exception.SectionNotFoundException;
import subway.exception.StationNotFoundException;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    @ManyToOne(fetch = LAZY, cascade = PERSIST)
    @JoinColumn(name = "downStation_id")
    private Station downStation;

    @ManyToOne(fetch = LAZY, cascade = PERSIST)
    @JoinColumn(name = "upStation_id")
    private Station upStation;

    private Long distance;

    @OneToMany(mappedBy = "line", cascade = CascadeType.REMOVE)
    private List<Section> sections;

    protected Line() {

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

    public Station getDownStation() {
        return downStation;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Long getDistance() {
        return distance;
    }

    private Line(String name, String color, Station downStation, Station upStation, Long distance) {
        this.name = name;
        this.color = color;
        this.downStation = downStation;
        this.upStation = upStation;
        this.distance = distance;
        this.sections = new ArrayList<>();
    }

    private void addFirstSection() {
        sections.add(new Section(this,downStation, upStation, distance));
    }

    public static Line newInstance(String name, String color, Station downStation, Station upStation, Long distance) {
        Line line = new Line(name, color, downStation, upStation, distance);
        line.addFirstSection();
        return line;
    }


    public static void validateStations(Long downStationId, Long upStationId) {
        if (upStationId == null || downStationId == null) {
            throw new StationNotFoundException();
        }
    }

    public void update(String name, String color) {
        this.name = name;
        this.color =color;
    }

    public Long getUpStationId() {
        return upStation.getId();
    }

    public Long getDownStationId() {
        return downStation.getId();
    }

    public List<Section> getSections() {
        return sections;
    }

    public Section getSectionByStations(Station downStation, Station upStation) {
        return sections.stream()
                .filter(section ->
                        section.contains(downStation, upStation))
                .findFirst().orElseThrow(SectionNotFoundException::new);
    }

    public void addSection(Section section) {
        sections.add(section);
        downStation = section.getDownStation();
    }

    public void addDistance(Long distance) {
        this.distance += distance;
    }

    public void removeSection(Section section) {
        downStation = section.getUpStation();
        sections.remove(section);
    }
}
