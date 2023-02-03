package subway.line;

import subway.exception.StationNotFoundException;
import subway.section.Section;
import subway.station.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Section> sections = new ArrayList<>();

    private Long distance;

    public Line() {
    }

    public Line(String name, String color, Long distance, Station upStation, Station downStation) {
        this.name = name;
        this.color = color;
        this.distance = distance;
        if (upStation == null || downStation == null) {
            throw new StationNotFoundException();
        }
        this.sections = List.of(new Section(upStation, downStation, distance, this));
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

    public Station getUpStation() {
        return this.sections.get(0).getUpStation();
    }

    public Station getDownStation() {
        return this.sections.get(this.sections.size() - 1).getDownStation();
    }

    public List<Station> getStations() {
        return this.getSections().stream()
                .map(Section::getStations)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    public Long getDistance() {
        return distance;
    }

    public void changeNameAndColor(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void breakAllStationRelation() {
        sections.forEach(section -> section.changeLine(null));
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public void plusDistance(long distance) {
        this.distance += distance;
    }

    public boolean hasMinimumStations() {
        return sections.size() <= 1;
    }

    public Section removeLastSection() {
        if (hasMinimumStations()) {
            throw new IllegalArgumentException("구간을 삭제할 수 없습니다.");
        }
        Section section = sections.get(sections.size() - 1);
        section.changeLine(null);
        return sections.remove(sections.size() - 1);
    }

    public Section getLastSection() {
        return sections.get(sections.size() - 1);
    }

    public List<Section> getSections() {
        return sections;
    }
}
