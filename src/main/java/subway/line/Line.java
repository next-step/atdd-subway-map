package subway.line;

import lombok.Builder;
import subway.common.exception.BusinessException;
import subway.section.Section;
import subway.station.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    @Builder
    public Line(String name, String color) {
        this.name = name;
        this.color = color;
        this.sections = new ArrayList<>();
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

    public List<Section> getSections() {
        return sections;
    }

    public void addSection(Section section) {
        this.existsStation(section.getUpStation());
        this.existsStation(section.getDownStation());
        this.isEndStation(section.getUpStation());
        this.isEndStation(section.getDownStation());
        section.setLine(this);
        this.sections.add(section);
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    private List<Station> getAllStations() {
        return this.sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .collect(Collectors.toList());
    }

    private void existsStation(Station station) {
        if (getAllStations().contains(station)) {
            throw new BusinessException("이미 존재하는 역입니다.");
        }
    }

    private void isEndStation(Station station) {
        List<Station> stations = this.getAllStations();
        if (stations.indexOf(station) != (stations.size() - 1)) {
            throw new BusinessException("하행 종점역이 아닙니다.");
        }
    }
}
