package nextstep.subway.domain;

import org.apache.logging.log4j.util.Strings;

import javax.persistence.*;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    @Embedded
    private Sections sections = new Sections();

    protected Line() {
    }

    public Line(String name, String color) {
        validateBlankName(name);
        validateBlankColor(color);
        this.name = name;
        this.color = color;
    }

    private void validateBlankName(final String name) {
        if (Strings.isBlank(name)) {
            throw new IllegalArgumentException("blank line name occurred");
        }
    }

    private void validateBlankColor(final String color) {
        if (Strings.isBlank(color)) {
            throw new IllegalArgumentException("blank line color occurred");
        }
    }

    public void addSection(final Section section) {
        sections.add(section);
    }

    public void removeSection(final Station downEndStation) {
        sections.remove(downEndStation);
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

    public void update(final String name, final String color) {
        this.name = name;
        this.color = color;
    }
}
