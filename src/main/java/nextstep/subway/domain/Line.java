package nextstep.subway.domain;

import nextstep.subway.applicaion.exception.NotLastSectionException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void update(Line updateLine) {
        this.name = updateLine.getName();
        this.color = updateLine.getColor();
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

    public void addSection(Section section) {
        sections.add(section);
        section.updateLine(this);
    }

    public boolean isDownStation(Long upStationId) {
        for (Section section : sections) {
            if (!section.isSameDownStation(upStationId)) {
                return false;
            }
        }
        return true;
    }

    public void deleteSection(Long stationId) {
        int isLastDownStation = 1;
        int count = validSections(stationId);
        if (count == isLastDownStation) {
            Section findSection = sections.stream().filter(section ->
                    section.isSameDownStation(stationId))
                    .findFirst().orElse(null);
            sections.remove(findSection);
            return;
        }
        throw new NotLastSectionException();
    }

    private int validSections(Long stationId) {
        int count = 0;
        for (Section section : sections) {
            if (section.isSameUpStation(stationId) || section.isSameDownStation(stationId)) {
                count++;
            }
        }
        return count;
    }
}
