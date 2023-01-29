package subway.domain;

import subway.error.exception.BusinessException;
import subway.error.exception.ErrorCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Table(name = "LINE")
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20)
    private String name;
    @Column(length = 20)
    private String color;
    @OneToMany(mappedBy = "line", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line(final String name, final String color, final Station upStation, final Station downStation, final int distance) {
        this.name = name;
        this.color = color;

        final Section section = new Section(this, upStation, downStation, distance);
        this.sections.add(section);
    }

    protected Line() {
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

    public List<Station> getStations() {
        Station lastUpStation = getLastUpStation();
        final List<Station> stations = this.sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());
        stations.add(0, lastUpStation);
        return stations;
    }

    public void update(final String name, final String color) {
        if (name != null) {
            this.name = name;
        }
        if (color != null) {
            this.color = color;
        }
    }

    public void addSection(final Station upStation, final Station downStation, final int distance) {
        validateSectionBeforeAdd(upStation, downStation);
        final Section section = new Section(this, upStation, downStation, distance);
        this.sections.add(section);
    }

    public void removeSection(final Station station) {
        if (!getStations().contains(station)) {
            throw new BusinessException(ErrorCode.CANNOT_REMOVE_SECTION_WHAT_IS_NOT_EXISTS_STATION_IN_LINE);
        }
        if (!getLastDownStation().equals(station)) {
            throw new BusinessException(ErrorCode.CANNOT_REMOVE_SECTION_WHAT_IS_NOT_LAST_SECTION);
        }
        this.sections.remove(getLastDownStation());
    }

    private void validateSectionBeforeAdd(final Station upStation, final Station downStation) {
        if (!upStation.equals(getLastDownStation())) {
            throw new BusinessException(ErrorCode.CANNOT_ADD_SECTION_WITH_INVALID_UP_STATION);
        }
        if (getStations().contains(downStation)) {
            throw new BusinessException(ErrorCode.CANNOT_ADD_SECTION_WITH_ALREADY_EXISTS_STATION_IN_LINE);
        }
    }

    private Station getLastUpStation() {
        Station lastUpStation = this.sections.stream().findFirst().get().getUpStation();
        while (true) {
            final Station finalUpStation = lastUpStation;
            final Optional<Section> findSection = this.sections.stream()
                    .filter(section -> section.getDownStation().equals(finalUpStation))
                    .findAny();
            if (findSection.isEmpty()) break;
            lastUpStation = findSection.get().getUpStation();
        }
        return lastUpStation;
    }

    private Station getLastDownStation() {
        Station lastDownStation = this.sections.stream().findFirst().get().getDownStation();
        while (true) {
            final Station finalLastDownStation = lastDownStation;
            final Optional<Section> findSection = this.sections.stream()
                    .filter(section -> section.getUpStation().equals(finalLastDownStation))
                    .findAny();
            if (findSection.isEmpty()) break;
            lastDownStation = findSection.get().getDownStation();
        }
        return lastDownStation;
    }
}
