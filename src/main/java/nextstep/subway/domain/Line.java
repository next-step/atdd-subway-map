package nextstep.subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityNotFoundException;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
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

    public List<Section> getSections() { return sections; }

    public void addSection(Section section) {
        sections.add(section);
        section.setLine(this);
    }

    public void update(String name, String color) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        }

        if (color != null && !color.isEmpty()) {
            this.color = color;
        }
    }

    public Section getLastDownStation() {
        return this.getSections()
                .stream()
                .max(Comparator.comparing(Section::getId))
                .orElseThrow(EntityNotFoundException::new);
    }

    public boolean equalsLastDownStation(Station upStation) {
        return getLastDownStation().getDownStation().equals(upStation);
    }
}
