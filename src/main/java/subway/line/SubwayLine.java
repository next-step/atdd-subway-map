package subway.line;

import subway.section.SubwaySection;

import javax.persistence.*;
import java.util.ArrayList;
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

    @OneToMany(fetch = FetchType.LAZY)
    private List<SubwaySection> sections;

    public SubwayLine() {}

    public SubwayLine(String name, String color) {
        this.name = name;
        this.color = color;
        this.sections = new ArrayList<>();
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