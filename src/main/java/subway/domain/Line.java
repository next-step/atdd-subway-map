package subway.domain;

import subway.exception.CannotRemoveNonLastSectionException;
import subway.exception.CannotRemoveUniqueSectionException;
import subway.exception.InvalidSectionDownStationException;
import subway.exception.InvalidSectionUpStationException;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Station upStation, Station downStation, long distance) {
        if (sections.isEmpty()) {
            sections.add(new Section(this, upStation, downStation, distance));
            return;
        }

        if (!sections.isLastStation(upStation)) {
            Station lastStation = sections.getLastStation();
            throw new InvalidSectionUpStationException(lastStation.getName(), upStation.getName());
        }

        if (sections.hasStation(downStation)) {
            throw new InvalidSectionDownStationException(downStation.getName());
        }

        this.sections.add(new Section(this, upStation, downStation, distance));
    }

    public List<Station> getAllStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }
        return sections.getStations();
    }

    public void removeSection(Station station) {
        if (sections.hasSingleSection()) {
            throw new CannotRemoveUniqueSectionException();
        }

        if (!sections.isLastStation(station)) {
            Station lastStation = sections.getLastStation();
            throw new CannotRemoveNonLastSectionException(lastStation.getName(), station.getName());
        }

        sections.remove();
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
}
