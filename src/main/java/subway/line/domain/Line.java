package subway.line.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import subway.common.BaseEntity;
import subway.section.domain.Section;
import subway.station.domain.Station;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Line extends BaseEntity {
    @Setter
    @Column(length = 20, nullable = false)
    private String name;

    @Setter
    @Column(length = 20, nullable = false)
    private String color;

    @Embedded
    Sections sections = new Sections();

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Long getOriginStationId() {
        return sections.getOriginSectionId();
    }

    public Long getTerminalStationId() {
        return sections.getTerminalSectionId();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int getDistance() {
        return sections.getTotalDistance();
    }

    public void addSection(Section section) {
        this.sections.add(section);
        if (section.getLine() != this) {
            section.setLine(this);
        }
    }

    public boolean hasLessThanTwoSections() {
        return sections.hasLessThanTwoSections();
    }

    public boolean isTerminalStationId(Long id) {
        return getTerminalStationId().equals(id);
    }
}
