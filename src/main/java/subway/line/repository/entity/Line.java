package subway.line.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.line.business.constant.SectionExceptionMessage;
import subway.station.repository.entity.Station;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "line")
@Getter
@NoArgsConstructor
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 10, nullable = false)
    private String name;
    @Column(length = 20, nullable = false)
    private String color;
    @Embedded
    private Sections sections;

    public Line(String name, String color, Sections sections) {
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public Long getFirstSectionId() {
        return sections.getFirstSection().getId();
    }

    public Long getLastSectionId() {
        return sections.getLastSection().getId();
    }

    public Line modify(String name, String color) {
        this.name = name;
        this.color = color;

        return this;
    }

    public void addSection(Section section) {
        if (!sections.getLastStationId().equals(section.getUpStationId())) {
            throw new InvalidParameterException(SectionExceptionMessage.INVALID_UP_STATION);
        }

        if (hasStation(section.getDownStationId())) {
            throw new InvalidParameterException(SectionExceptionMessage.ALREADY_EXIST_DOWN_STATION);
        }

        this.sections.addSection(section);
    }

    public List<Station> getAllStations() {
        return sections.getAllStations();
    }

    public void removeSection(Long stationId) {
        if (!sections.getLastStationId().equals(stationId)) {
            throw new InvalidParameterException(SectionExceptionMessage.CANNOT_REMOVE_SECTION_WHEN_NOT_LAST_STATION);
        }

        if (sections.getSectionsCount() == 1) {
            throw new InvalidParameterException(SectionExceptionMessage.CANNOT_REMOVE_SECTION_WHEN_ONLY_ONE);
        }

        sections.remove();
    }

    private boolean hasStation(Long stationId) {
        return getAllStations().stream()
                .map(Station::getId)
                .collect(Collectors.toList())
                .contains(stationId);
    }

}
