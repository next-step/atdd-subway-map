package nextstep.subway.domain;

import javax.persistence.*;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String color;

    @Embedded
    private final Sections sections = new Sections();

    protected Line() {
    }

    private Line(String name, String color) {
        this.name = name;
        this.color = color;
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

    public static Line of(String name, String color, Station upStation, Station downStation, int distance) {
        Line line = new Line(name, color);
        line.addSection(upStation, downStation, distance);

        return line;
    }

    public Sections getSections() {
        return sections;
    }


    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        sections.add(this, upStation, downStation, distance);
    }

    public boolean isNotEqualDownStation(Station upStation) {
        return upStation.equals(sections.getLastDownStation());
    }
}
