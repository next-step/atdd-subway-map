package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;
    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public static Line of(String name, String color) {
        return new Line(name, color);
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
        return sections.get();
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void addSection(Section section) {
        validateAddable(section);

        sections.add(section);
        section.setLine(this);
    }

    private void validateAddable(Section section) {
        sortSections();

        if (sections.isEmpty()) {
            return;
        }

        sections.validateUpStation(section);
        sections.validateDownStation(section);
    }

    public List<Station> getStations() {
        sortSections();

        List<Station> stations = new ArrayList<>();
        for (int i = 0; i < sections.size(); ++i) {
            stations.add(sections.get(i).getUpStation());
            if (i == sections.size()-1) {
                stations.add(sections.get(sections.size()-1).getDownStation());
            }
        }
        return stations;
    }

    private void sortSections() {

    }

    public boolean containsSection(Section section) {
        return sections.contains(section);
    }
}
