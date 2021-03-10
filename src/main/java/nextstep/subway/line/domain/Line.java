package nextstep.subway.line.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.line.exception.InvalidDownStationException;
import nextstep.subway.line.exception.InvalidStationIdException;
import nextstep.subway.line.exception.InvalidUpStationException;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line extends BaseEntity {
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

    public void addSection(Section section) {
        section.setLine(this);
        this.sections.add(section);
    }

    public boolean isValidUpstation(Station upStation) {
        return upStation.equals(getFinalStation());
    }

    private Station getFinalStation() {
        return sections.get(sections.size() - 1).getDownStation();
    }

    public boolean isValidDownStation(Station downStation) {
        return !sections.stream()
                .anyMatch( section -> section.getUpStation().equals(downStation) || section.getDownStation().equals(downStation) );
    }

    public void deleteSection(Long stationId) {
        Section lastSection = sections.stream()
                .filter(section -> section.hasAsDownStation(stationId))
                .findAny().orElseThrow(InvalidStationIdException::new);

        this.sections.remove(lastSection);
    }

    public boolean isProperStationToDelete(Long stationId) {
        return getFinalStation().getId().equals(stationId);
    }

    public boolean hasOnlyOneSection() {
        return this.sections.size() == 1;
    }
}
