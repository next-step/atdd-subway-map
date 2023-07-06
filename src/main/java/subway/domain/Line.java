package subway.domain;

import antlr.StringUtils;
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

//    private Station getUpStation() {
//        return sections.get(0).getUpStation();
//    }
//
//    private Station getDownStaion() {
//        if(sections.size() == 1) {
//            return sections.get(0).getDownStation();
//        }
//
//        return sections.get(sections.size()).getDownStation();
//    }

    public Long getDistance() {
        return sections.stream().mapToLong(Section::getDistance).sum();
    }

    public void updateNameAndColor(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void registerSection(Section section) {
        validate(section);

        sections.add(section);
    }

    private void validate(Section section) {
        String upStationName = section.getUpStation().getName();
        String downStationName = section.getDownStation().getName();

        Section lastSection = sections.get(sections.size() - 1);

        if(!lastSection.equalsDownStation(section.getDownStation())) {
            throw new SubwayException("새로운 구간의 상행역은 노선에 등록되어 있는 하행 종점역이어야 합니다.");
        }

        long count = sections.stream()
                .filter(s -> s.isContainStation(upStationName) || s.isContainStation(downStationName))
                .count();

        if(count > 0) {
            throw new SubwayException("새로운 구간의 하행역은 노선에 등록되어 있는 역일 수 없습니다.");
        }
    }
}
