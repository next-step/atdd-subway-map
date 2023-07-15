package subway.line;

import lombok.Builder;
import subway.common.exception.BusinessException;
import subway.section.Section;
import subway.station.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        this.validateCreateSection(section);
        section.setLine(this);
        this.sections.add(section);
    }

    public void deleteSection(Station station) {
        this.validateDeleteSection(station);
        this.sections.remove(this.sections.size() - 1);
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public List<Station> getAllStations() {
        if (this.sections.isEmpty()) {
            return new ArrayList<>();
        }
        List<Station> stations = this.getSections().stream().map(Section::getDownStation).collect(Collectors.toList());
        stations.add(0, this.getSections().get(0).getUpStation());
        return stations;
    }

    private boolean existsStation(Station station) {
        return getAllStations().contains(station);
    }

    private boolean isEndStation(Station station) {
        List<Station> stations = getAllStations();
        return stations.indexOf(station) == stations.size() - 1;
    }

    private void validateCreateSection(Section section) {
        if (existsStation(section.getUpStation()) && !isEndStation(section.getUpStation())) {
            throw new BusinessException("이미 존재하는 역입니다.");
        }
        if (existsStation(section.getDownStation())) {
            throw new BusinessException("이미 존재하는 역입니다.");
        }
        if (!isEndStation(section.getUpStation())) {
            throw new BusinessException("시작역이 하행 종점역이 아닙니다.");
        }
    }

    private void validateDeleteSection(Station station) {
        if (!isEndStation(station)) {
            throw new BusinessException("삭제요청 역이 하행 종점역이 아닙니다.");
        }
        if (sections.size() == 1) {
            throw new BusinessException("역의 구간이 한개입니다.");
        }
    }
}
