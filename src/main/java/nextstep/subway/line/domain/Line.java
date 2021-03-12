package nextstep.subway.line.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.line.exception.InvalidStationIdException;
import nextstep.subway.station.domain.Station;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    private static final int ONE = 1;
    private static final int FIRST_SECTION_INDEX = 0;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;

    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public Line(final String name, final String color, final Section section) {
        this.name = name;
        this.color = color;
        addSection(section);
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

    public void addSection(final Section section) {
        section.setLine(this);
        this.sections.add(section);
    }

    public boolean isValidUpStation(final Station upStation) {
        return upStation.equals(getFinalStation());
    }

    private Station getFinalStation() {
        return sections.get(sections.size() - ONE).getDownStation();
    }

    public boolean isValidDownStation(final Station downStation) {
        return !sections.stream()
                .anyMatch( section -> section.equalsWithEitherUpOrDown(downStation) );
    }

    public void deleteSection(final Long stationId) {
        Section lastSection = sections.stream()
                .filter(section -> section.hasAsDownStation(stationId))
                .findAny().orElseThrow(InvalidStationIdException::new);

        this.sections.remove(lastSection);
    }

    public boolean isProperStationToDelete(final Long stationId) {
        return getFinalStation().getId().equals(stationId);
    }

    public boolean hasOnlyOneSection() {
        return this.sections.size() == ONE;
    }

    public List<Station> fetchAllStations() {
        List<Station> stations = new ArrayList<>();

        Station firstStation = sections.get(FIRST_SECTION_INDEX).getUpStation();
        stations.add(firstStation);

        sections.stream()
            .map(Section::getDownStation)
            .forEach(station -> stations.add(station));

        return stations;
    }
}
