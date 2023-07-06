package subway.domain;

import subway.exception.SubwayException;

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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    @OneToMany
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color, Section section) {
        this.name = name;
        this.color = color;
        sections.add(section);
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

    public List<Station> getContainStations() {
        List<Station> list = sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .collect(Collectors.toList());

        return list.stream()
                .map(station -> station.getName())
                .distinct()
                .map(name -> list.stream()
                        .filter(n -> n.getName().equals(name)).findFirst().orElse(null))
                .collect(Collectors.toList());
    }

    public Long getDistance() {
        return sections.stream().mapToLong(Section::getDistance).sum();
    }

    public void updateNameAndColor(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void registerSection(Section section) {
        validateRegister(section);

        sections.add(section);
    }

    public void deleteSection(Station station) {
        validateDelete(station);

        sections.remove(sections.size() - 1);
    }

    private void validateDelete(Station station) {
        Section lastSection = sections.get(sections.size() - 1);

        if (!lastSection.equalsDownStation(station)) {
            throw new SubwayException("지하철 노선에 등록된 하행 종점역만 제거할 수 있습니다.");
        }

        if (sections.size() == 1) {
            throw new SubwayException("노선에 구간이 한개인 경우 삭제할 수 없습니다.");
        }
    }

    private void validateRegister(Section section) {
        String downStationName = section.getDownStation().getName();
        Section lastSection = sections.get(sections.size() - 1);

        if (!lastSection.equalsDownStation(section.getUpStation())) {
            throw new SubwayException("새로운 구간의 상행역은 노선에 등록되어 있는 하행 종점역이어야 합니다.");
        }

        long count = sections.stream()
                .filter(s -> s.isContainStation(downStationName) || s.isContainStation(downStationName))
                .count();

        if (count > 0) {
            throw new SubwayException("새로운 구간의 하행역은 노선에 등록되어 있는 역일 수 없습니다.");
        }
    }
}
