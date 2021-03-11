package nextstep.subway.line.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    public Line(String name, String color, Section section, int distance) {
        this.name = name;
        this.color = color;
        this.sections.add(section);
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void extendsLine(Section section) {
        this.sections.add(section);
    }

    public boolean isExtensionAvailable(Section section) {

        if(isLineExtensionAvailable(section)) {

        }

        return true;
        //Long registerdStationCount = sections.stream().filter(section -> section.getUpStation() == newDownStation || section.getDownStation() == newDownStation).count();
//        return registerdStationCount <= 0;
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

    private boolean isLineExtensionAvailable(Section section) {
        long count = this.sections.stream().count();

        if (count <= 0) {
            return true;
        }

        Station lastStation = this.sections.stream().skip(count - 1).findFirst().get().getDownStation();

        if (lastStation.getId().equals(section.getUpStation())) {
            return true;
        }

        return false;
    }
}
