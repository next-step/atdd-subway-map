package subway.line.repository.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "section_id")
    private List<Section> sections = new ArrayList<>();

    @Column(nullable = false)
    private int distance;

    protected Line() {
    }

    public Line(final String name, final String color, final Section section) {
        this.name = name;
        this.color = color;
        addSection(section);
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

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

    public int getDistance() {
        return distance;
    }

    public void changeName(final String name) {
        this.name = name;
    }

    public void changeColor(final String color) {
        this.color = color;
    }

    public void addSection(final Section section) {
        this.sections.add(section);
        distance += section.getDistance();
    }
}
