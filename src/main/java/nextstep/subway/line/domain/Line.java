package nextstep.subway.line.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    private static final int FIRST_SECTION_INDEX = 0;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String color;

    @Embedded
    private Sections sections;

    public Line() {
    }

    public Line(final String name, final String color) {
        this.name = name;
        this.color = color;
        this.sections = new Sections();
    }

    public Line(final String name, final String color, final Section section) {
        this.name = name;
        this.color = color;
        this.sections = new Sections();
        section.setLine(this);
        sections.add(section);
    }

    public static Line of(final String name, final String color) {
        return new Line(name, color);
    }

    public static Line of(final String name, final String color, final Section section) {
        return new Line(name, color, section);
    }

    public void update(final Line line) {
        this.name = line.getName();
        this.color = line.getColor();
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

    public void addSection(final Station upStation, final Station downStation, final int distance) {
        Section section = Section.of(upStation, downStation, distance);

        section.setLine(this);
        sections.add(section);
    }

    public boolean isValidUpStation(final Station upStation) {
        return upStation.equals(getFinalStation());
    }

    private Station getFinalStation() {
        return sections.getFinalStation();
    }

    public boolean isValidDownStation(final Station downStation) {
        return sections.isValidDownStation(downStation);
    }

    public void deleteSection(final Long stationId) {
        sections.deleteSection(stationId);
    }

    public boolean isProperStationToDelete(final Long stationId) {
        return getFinalStation().getId().equals(stationId);
    }

    public boolean hasOnlyOneSection() {
        return sections.hasOnlyOneSection();
    }

    public List<Station> fetchAllStations() {
        List<Station> stations = new ArrayList<>();

        Station firstStation = sections.get(FIRST_SECTION_INDEX).getUpStation();

        stations.add(firstStation);
        stations.addAll(sections.fetchAllDownStations());

        return stations;
    }
}
