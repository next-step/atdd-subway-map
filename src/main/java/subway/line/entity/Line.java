package subway.line.entity;

import subway.station.entity.Station;

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

    @Column(nullable = false)
    private int distance;

    @Embedded
    private Sections sections;

    protected Line() {
    }

    public Line(final String name, final String color, final Station upStation, final Station downStation, final int distance) {
        this.name = name;
        this.color = color;
        this.distance = distance;

        List<Section> sectionList = new ArrayList<>();
        sectionList.add(new Section(this, upStation, downStation, distance));
        this.sections = Sections.from(sectionList);
    }

    public void updateDetails(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(final Section section) {
        this.sections.addSection(section);
        this.addLineDistance(section.getDistance());
    }

    public void removeSection(final Section section) {
        this.sections.removeSection(section);
        this.subtractLineDistance(section.getDistance());
    }

    private void addLineDistance(final int distance) {
        this.distance += distance;
    }

    private void subtractLineDistance(final int distance) {
        this.distance -= distance;
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

    public int getDistance() {
        return distance;
    }

    public Sections getSections() {
        return sections;
    }

    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", distance=" + distance +
                '}';
    }

}
