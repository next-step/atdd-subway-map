package subway.line;

import subway.section.SubwaySection;

import javax.persistence.*;
import java.util.List;

@Entity
public class SubwayLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 15, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @OneToMany
    @JoinColumn(name = "subway_section_id")
    private List<SubwaySection> sections;

    public SubwayLine() {}

    public SubwayLine(String name, String color, List<SubwaySection> sections) {
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateColor(String color) {
        this.color = color;
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public String getColor() { return color; }

    public List<SubwaySection> getSections() { return sections; }
}