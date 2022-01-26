package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

        return sections.getStations();
    }

    private void sortSections() {
        // 구간순서 보장 안될 경우 정렬
    }

    public boolean containsSection(Section section) {
        return sections.contains(section);
    }

    public void remove(Station station) {
        validateRemovable(station);

        sections.removeFarDownSection();
    }

    private void validateRemovable(Station station) {
        sections.validateRemovalbe(station);
    }
}
