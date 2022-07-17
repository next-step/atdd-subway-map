package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
public class Line {

    private static final int NO_INDEX = -1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    @OneToMany(mappedBy = "line", orphanRemoval = true)
    private List<Section> sections = new LinkedList<>();

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line updateNameAndColor(String name, String color) {
        this.name = name;
        this.color = color;
        return this;
    }

    public void addSections(Section upStationSection, Section downStationSection) {
        int index = sectionIndexOf(upStationSection);
        if (index != NO_INDEX) {
            sections.add(index, downStationSection);
            return;
        }
        sections.add(upStationSection);
        sections.add(downStationSection);
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

    public List<Station> getStations() {
        return sections.stream()
                .map(Section::getStation)
                .collect(Collectors.toList());
    }

    private int sectionIndexOf(Section upStationSection) {
        for (int i = 0; i < sections.size(); i++) {
            if (isStationEqualTo(upStationSection, sections.get(i))) {
                return i;
            }
        }
        return NO_INDEX;
    }

    private boolean isStationEqualTo(Section upStationSection, Section section) {
        Long upStationId = upStationSection.getStation()
                .getId();
        Long sectionId = section.getStation().getId();
        return upStationId.equals(sectionId);
    }

}
