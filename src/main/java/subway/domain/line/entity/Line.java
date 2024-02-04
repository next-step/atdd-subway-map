package subway.domain.line.entity;

import subway.domain.station.entity.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @OneToMany(mappedBy = "line")
    private List<Section> sections;

    public Line() {

    }

    public Line(String name, String color, Section section) {
        this.name = name;
        this.color = color;
        this.sections = new ArrayList<>();
        this.sections.add(section);
        section.setLine(this);
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
        return sections.get(0).getUpStation();
    }

    public Station getDownStation() {
        return sections.get(sections.size() - 1).getDownStation();
    }

    public void changeName(String name) {
        if (name == null) {
            return;
        }
        if (name.isEmpty()) {
            return;
        }
        if (name.isBlank()) {
            return;
        }
        this.name = name;
    }

    public void changeColor(String color) {
        if (color == null) {
            return;
        }
        if (color.isEmpty()) {
            return;
        }
        if (color.isBlank()) {
            return;
        }
        this.color = color;
    }

    public List<Section> getSections() {
        return this.sections;
    }
}
