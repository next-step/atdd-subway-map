package subway;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;
    @Column(length = 20)
    private String color;
    @Embedded
    private Sections sections = new Sections();
    private Long distance;

    public Line() {
    }

    public Line(String name, String color, Sections sections, Long distance) {
        this.name = name;
        this.color = color;
        this.sections = sections;
        this.distance = distance;
    }

    public static Line from(LineRequest lineRequest, Section section) {
        return new Line(lineRequest.getName(),
                lineRequest.getColor(),
                new Sections(section),
                lineRequest.getDistance());
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

    public Long getDistance() {
        return distance;
    }


    public void modify(LineModificationRequest lineModificationRequest) {
        this.name = lineModificationRequest.getName();
        this.color = lineModificationRequest.getColor();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void addSection(Section section) throws WrongSectionCreateException {
        sections.add(section);
    }
}
