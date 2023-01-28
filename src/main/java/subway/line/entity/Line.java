package subway.line.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.section.entity.Section;
import subway.section.entity.Sections;
import subway.station.entity.Station;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_id")
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @Embedded
    private Sections sections;

    @Builder
    public Line(String name, String color, List<Section> values) {
        this.name = name;
        this.color = color;
        this.sections = (values == null) ? new Sections() : Sections.from(values);
    }

    public static Line of(String name, String color) {
        return Line.builder()
                .name(name)
                .color(color)
                .build();
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public List<Station> stations() {
        return sections.stations();
    }

    public void addSection(Section section) {
        validateSectionForSave(section);

        sections.add(section);
        section.changeLine(this);
    }

    private void validateSectionForSave(Section section) {
        // 이미 저장 되어있거나 구간이 없으면 항상 통과하므로 검증할 필요가 없다.
        if (sections.isEmpty() || sections.contains(section)) {
            return;
        }

        long lastSectionDownStationId = sections.get(sections.size() - 1).getDownStation().getId();
        Long newSectionUpStationId = section.getUpStation().getId();

        if (lastSectionDownStationId != newSectionUpStationId) {
            throw new IllegalArgumentException();
        }

        boolean isSavedSectionStation = sections.stations()
                .stream()
                .anyMatch(station -> station.getId() == newSectionUpStationId); // 하행은 위에서 체크했으므로 상행만 체크한다.

        if (isSavedSectionStation) {
            throw new IllegalArgumentException();
        }
    }

    public void removeLastSection() {

    }

    public void removeSection(long removeStationId) {
        long lastSectionDownStationId = sections.get(sections.size() - 1).getDownStation().getId();

        if (lastSectionDownStationId != removeStationId) {
            throw new IllegalArgumentException();
        }

        sections.remove(sections.size() - 1);
    }
}

