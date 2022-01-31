package nextstep.subway.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", orphanRemoval = true)
    private List<Section> sectionList = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, List<Section> sectionList) {
        this.name = name;
        this.color = color;
        this.sectionList = sectionList;
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

    public List<Section> getSectionList() {
        return sectionList;
    }

    public void change(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        this.sectionList.add(section);
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();

        for (Section s : sectionList) {
            if(!stations.contains(s.getUpStation())) {
                stations.add(s.getUpStation());
            }
            if(!stations.contains(s.getDownStation())) {
                stations.add(s.getDownStation());
            }
        }

        return stations;
    }
}
