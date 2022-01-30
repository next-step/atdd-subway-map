package nextstep.subway.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    public static final int SECTIONS_MIN_SIZE = 1;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color, Section section) {
        this.name = name;
        this.color = color;
        section.setLine(this);
        this.sections.add(section);
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        verifyConnectable(section);
        section.setLine(this);
        sections.add(section);
    }

    public void removeSection(Station station) {
        Section lastSection = sections.get(sections.size() - 1);

        if (!lastSection.isEqualToDownStation(station) || sections.size() == SECTIONS_MIN_SIZE) {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }

        sections.remove(lastSection);
    }

    private void verifyConnectable(Section section) {
        if (sections.isEmpty()) {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
        Section lastSection = sections.get(sections.size() - 1);
        if (!lastSection.isConnectable(section)) {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
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
        return sections;
    }

}
