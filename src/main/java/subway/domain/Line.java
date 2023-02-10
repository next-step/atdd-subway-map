package subway.domain;

import javax.persistence.*;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    @Embedded
    private Sections sections = new Sections();

    protected Line() {
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

    public Sections getSections() {
        return sections;
    }

    public Line(String name, String color, Section section) {
        this.name = name;
        this.color = color;
        this.sections.addSection(section);
    }

    public void update(String name, String color) {
        this.name = name;
        this.color =color;
    }

    public Long getTotalDistance() {
        return sections.getTotalDistance();
    }

    public Station getDownStation() {
        return sections.getDownStation();
    }

    public Station getUpStation() {
        return sections.getUpStation();
    }

    public void addSection(Section section) {
        sections.addSection(section);
    }
}
