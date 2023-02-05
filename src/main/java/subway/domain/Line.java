package subway.domain;

import lombok.Getter;
import lombok.ToString;
import javax.persistence.*;

@Entity
@Getter
@ToString
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

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void change(String name, String color){
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) throws Exception{
        this.sections.add(section);

        if (section.getLine() != this) {
            section.setLine(this);
        }
    }

    public void deleteSection(Section section) throws Exception{
        this.sections.remove(section);

        if (section.getLine() == this) {
            section.setLine(null);
        }
    }
}
