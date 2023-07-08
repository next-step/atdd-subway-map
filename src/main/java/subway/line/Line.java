package subway.line;

import subway.Station;
import subway.StationNotFoundException;
import subway.section.Section;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    public Long getDistance() {
        return distance;
    }

    public List<Section> getSections() {
        return sections;
    }

    @Column(nullable = false)
    private Long distance = 0L;

    @OneToMany(mappedBy = "line")
    private List<Section> sections;

    protected Line() {
    }

    public Line(final String name, final String color, final Long distance, final List<Section> sections) {
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.sections = sections;
    }

    public Line(final String name, final String color, final Long distance) {
        this(name, color, distance, new ArrayList<>());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return this.color;
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public boolean isLastStation(Station station) {
        Optional<Section> optionalSection = this.sections
                .stream()
                .max(Comparator.naturalOrder());

        return optionalSection.map(section -> section.getDownStation().equals(station)).orElse(true);
    }

    public Long getNextSequence() {
        Optional<Section> optionalSection = this.sections
                .stream()
                .max(Comparator.naturalOrder());

        return optionalSection.map(section -> section.getSequence() + 1).orElse(1L);
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public Section getLastSection() {
        Optional<Section> optionalSection = this.sections
                .stream()
                .max(Comparator.naturalOrder());

        return optionalSection.orElseThrow();
    }
}
