package subway.line.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import subway.common.BaseEntity;
import subway.common.error.InvalidSectionRequestException;
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
        return sections.getOriginStationId();
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

    public void removeSection(Long stationId) {
        if (!sections.isTerminalStationId(stationId)) {
            throw new InvalidSectionRequestException("마지막 구간만 삭제할 수 있습니다.");
        }

        if (sections.hasLessThanTwoSections()) {
            throw new InvalidSectionRequestException("구간이 2개 이상일 때만 삭제할 수 있습니다.");
        }

        this.sections.removeLast();
    }
}
