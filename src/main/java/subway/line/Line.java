package subway.line;

import subway.section.Section;
import subway.station.Station;

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

    @OneToMany(mappedBy = "line", fetch = FetchType.LAZY, orphanRemoval = true)
    List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public Line(final String name, final String color, final Station upStation, final Station downStation) {
        this.name = name;
        this.color = color;
        final Section section = new Section(upStation, downStation);
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

    public List<Section> getSections() {
        return sections;
    }

    public void changeName(final String name) {
        this.name = name;
    }

    public void changeColor(final String color) {
        this.color = color;
    }
}
