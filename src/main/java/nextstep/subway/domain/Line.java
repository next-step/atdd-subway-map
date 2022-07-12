package nextstep.subway.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String color;

    @Embedded
    private Sections sections;

    protected Line() {
    }

    public Line(String name, String color, long distance, Station upStation, Station downStation) {
        validation(name, color);
        this.name = name;
        this.color = color;
        this.sections = new Sections(this, distance, upStation, downStation);
    }

    public void changeInfo(String name, String color) {
        validation(name, color);
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        sections.addSection(this,section);
    }

    public void removeSection(Section section) {
        sections.removeSection(section);
    }

    private void validation(String name, String color) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("노선의 이름을 작성해주세요");
        }

        if (color == null || color.isBlank()) {
            throw new IllegalArgumentException("색상을 넣어 주세요");
        }
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

    public List<Station> getStations() {
        return sections.getStation();
    }
}
