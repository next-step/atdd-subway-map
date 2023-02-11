package subway.line;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    private String color;

    @Embedded
    @Builder.Default
    private Sections sections = new Sections();

    public Line addLineStation(final Section section) {

        this.sections.add(section);
        section.changeLine(this);
        return this;
    }

    public Line change(final String name, final String color) {
        this.name = name;
        this.color = color;
        return this;
    }

}
