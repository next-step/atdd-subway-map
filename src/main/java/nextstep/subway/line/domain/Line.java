package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.station.domain.Station;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public static Line of(
        String name, String color, Station upStation,
        Station downStation, int distance) {
        Line line = new Line();
        line.name = name;
        line.color = color;
        line.sections.add(Section.of(line, upStation, downStation, distance));
        return line;
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void addStations(final Station upStation, final Station downStation, int distance) {
        final Station lastStation = getSections().get(sections.size() - 1).getDownStation();
        if (!upStation.equals(lastStation)) {
            throw new IllegalArgumentException("추가되는 상행역은 마지막 하행역과 같아야 합니다.");
        }
        sections.add(Section.of(this, upStation, downStation, distance));
    }

    public List<Station> getStations() {
        return sections.stream()
            .sorted((section1, section2) -> {
                if (section1.getDownStation().equals(section2.getUpStation())) {
                    return -1;
                }
                if (section1.getUpStation().equals(section2.getDownStation())) {
                    return 1;
                }
                return 0;
            })
            .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
            .distinct()
            .collect(Collectors.toList());
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
}
