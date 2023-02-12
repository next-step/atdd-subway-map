package subway.line;

import lombok.*;
import subway.*;
import subway.line.section.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    private String color;

    @Embedded
    private Sections sections;

    @Builder
    private Line(
            final Long id,
            final String name,
            final String color,
            final Section section
    ) {

        section.changeLine(this);
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = new Sections(section);
    }

    public Line addSection(final Section section) {

        this.sections.add(section);
        section.changeLine(this);
        return this;
    }

    public Line change(final String name, final String color) {
        this.name = name;
        this.color = color;
        return this;
    }

    public Line removeSection(final Station downStation) {

        this.sections.remove(downStation);
        return this;
    }

}
