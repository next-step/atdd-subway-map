package nextstep.subway.domain;

import nextstep.subway.exception.AlreadyExistStationException;
import nextstep.subway.exception.SectionStationMismatchException;
import nextstep.subway.exception.StationNotRegisteredException;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    @Embedded
    private Sections sections = new Sections();

    protected Line() {
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public Line changeBy(String name, String color) {
        this.name = name;
        this.color = color;
        return this;
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

    public List<Station> allStations() {
        return sections.allStations();
    }

    public Line addSection(Station upStation, Station downStation, int distance) {
        validateAddSection(upStation, downStation, lastStation());
        sections.add(new Section(this, upStation, downStation, distance));
        return this;
    }

    private void validateAddSection(Station upStation, Station downStation, Station lastStation) {
        validateAlreadyExistStation(downStation);
        validateMismatchStations(upStation, lastStation);
    }

    private void validateAlreadyExistStation(Station downStation) {
        if (hasStation(downStation)) {
            throw new AlreadyExistStationException("이미 존재하는 역입니다.");
        }
    }

    boolean hasStation(Station station) {
        return sections.hasStation(station);
    }

    private void validateMismatchStations(Station upStation, Station lastStation) {
        if (!lastStation.equals(upStation)) {
            throw new SectionStationMismatchException(
                    "노선의 하행 마지막역과 추가되는 구간의 상행역이 달라 추가될 수 없습니다. " +
                            "하행 마지막 역 : " + lastStation.getId()
                            + ", 구간 상행역 : " + upStation.getId()
            );
        }
    }

    Station lastStation() {
        if (sections.isEmpty()) {
            throw new StationNotRegisteredException("노선에 등록된 역이 없습니다.");
        }
        return sections.lastStation();
    }

    public void deleteSection(Station station) {
        sections.delete(station);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color) && Objects.equals(sections, line.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, sections);
    }
}
