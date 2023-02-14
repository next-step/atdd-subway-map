package subway.line.domain;

import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import subway.exception.CannotCreateSectionException;
import subway.exception.CannotDeleteSectionException;
import subway.exception.ErrorMessage;
import subway.line.section.domain.Section;
import subway.station.domain.Station;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 50, nullable = false)
    private String color;

    @Column(nullable = false)
    private Long distance;

    @Embedded
    private Sections sections = new Sections();

    protected Line() {
    }

    public Line(String name, String color, Station upStation, Station downStation, Long distance) {
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.sections.addSection(upStation, downStation, this, distance);
    }

    public Line(String name) {
        this.name = name;
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

    public Long getDistance() {
        return distance;
    }

    public Sections getSections() {
        return this.sections;
    }

    public void changeLine(String name, String color) {
        this.name = name;
        this.color = color;
    }

    private void plusDistance(Long distance) {
        this.distance += distance;
    }

    private void subtractDistance(Long distance) {
        this.distance -= distance;
    }

    public void deleteSection(Station station) {
        validDownStationOfLine(station);
        sections.validSizeOfSection();

        subtractDistance(sections.getLastSectionDistance());
        sections.removeLastSection();
    }

    public List<Station> getAllStations() {
        return sections.getAllStations();
    }

    private void addSection(Section section) {
        sections.addSection(section);

        if (section.getLine() != this) {
            section.setLine(this);
        }
    }

    public void createSection(Section section) {
        validRegisteredDownStation(section.getDownStation());
        validDownStationOfLineEqualsUpStation(section.getUpStation());

        addSection(section);
        plusDistance(section.getDistance());
    }

    private void validRegisteredDownStation(Station downStation) {
        if (sections.hasStation(downStation)) {
            throw new CannotCreateSectionException(
                ErrorMessage.DOWNSTATION_OF_NEW_SECTION_SHOULD_NOT_BE_REGISTERED_THE_LINE);
        }
    }

    private void validDownStationOfLineEqualsUpStation(Station upStation) {
        if (!Objects.equals(sections.getDownStationOfLastSection(), upStation)) {
            throw new CannotCreateSectionException(
                ErrorMessage.UPSTATION_OF_NEW_SECTION_SHOULD_BE_DOWNSTATION_OF_THE_LINE);
        }
    }

    private void validDownStationOfLine(Station station) {
        if (!Objects.equals(sections.getDownStationOfLastSection(), station)) {
            throw new CannotDeleteSectionException(ErrorMessage.SHOULD_DELETE_ONLY_DOWNSTATION_OF_LINE);
        }
    }
}
