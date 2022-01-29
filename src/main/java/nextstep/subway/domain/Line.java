package nextstep.subway.domain;

import javax.persistence.*;
import java.util.ArrayList;
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
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color, List<Section> sections) {
        this.name = name;
        this.color = color;
        this.sections = sections;
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

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        verifyConnectable(section);
        section.setLine(this);
        sections.add(section);
    }

    public void removeSection(Station station){
        Section lastSection = sections.get(sections.size()-1);

        if(!lastSection.isEqualToDownStation(station) || sections.size() == 1){
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

}
