package nextstep.subway.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    protected Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public boolean isValidNewSection(Long upStationId, Long downStationId) {
        return sections.isEmpty() ||
                doesMatchLastDownStation(upStationId) &&
                doesNotContains(downStationId);
    }

    private Station getLastDownStation() {
        return sections.stream()
                .filter(ds -> sections.stream().noneMatch(us ->
                        us.getUpStation().getId().equals(us.getDownStation().getId())))
                .collect(Collectors.toList())
                .get(0)
                .getDownStation();
    }

    private boolean doesNotContains(Long downStationId) {
        return sections.stream().noneMatch(s ->
                downStationId.equals(s.getUpStation().getId()) ||
                downStationId.equals(s.getDownStation().getId()));
    }

    private boolean doesMatchLastDownStation(Long upStationId) {
        return upStationId.equals(getLastDownStation().getId());
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

    public void edit(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void addSection(Section section) {
        if (!sections.contains(section)) {
            sections.add(section);
        }
        section.applyToLine(this);
    }
}
