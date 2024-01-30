package subway.line;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.section.Section;
import subway.station.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    private Station upstation;

    @ManyToOne
    private Station downstation;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
    private List<Section> sections = new ArrayList<>();

    @Column(nullable = false)
    private int distance;

    @Builder
    public Line(Long id, String color, String name, Station upstation, Station downstation, List<Section> sections, int distance) {
        this.id = id;
        this.color = color;
        this.name = name;
        this.upstation = upstation;
        this.downstation = downstation;
        this.sections = sections != null ? sections : new ArrayList<>();
        this.distance = distance;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateColor(String color) {
        this.color = color;
    }

    public void addSection(Section section) {
        sections.add(section);
        downstation = section.getDownstation();
    }

    public Section popSection() {
        Section removedSection =  sections.remove(sections.size() - 1);
        downstation = sections.get(sections.size() - 1).getDownstation();

        return removedSection;
    }
}
