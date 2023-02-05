package subway.domain;

import java.util.List;
import java.util.Objects;

public class Section {

    private Long id;
    private Station downStation;
    private Station upStation;
    private Long distance;
    private Line line;

    private Section(Long id, Station downStation, Station upStation, Long distance, Line line) {
        this.id = id;
        this.downStation = downStation;
        this.upStation = upStation;
        this.distance = distance;
        this.line = line;
    }

    public static Section withNoId(Station downStation, Station upStation, Long distance, Line line) {
        return new Section(null, downStation, upStation, distance, line);
    }

    public static Section withId(Long id, Station downStation, Station upStation, Long distance, Line line) {
        return new Section(id, downStation, upStation, distance, line);
    }

    public static Section make(Line line, Station upStation, Station downStation, Long distance, List<Section> lineSections) {
        if (!lineSections.isEmpty()) {
            validateSection(upStation, downStation, lineSections);
        }

        return Section.withNoId(downStation, upStation, distance, line);
    }

    private static void validateSection(Station upStation, Station downStation, List<Section> lineSections) {
        Section lineDownSection = lineSections.get(lineSections.size() - 1);
        Station lineDownStation = lineDownSection.getDownStation();

        if (!lineDownStation.equals(upStation)) {
            throw new NotEqualUpStationAndDownStationException();
        }

        if (lineSections.stream().anyMatch(section -> section.hasStation(downStation))) {
            throw new AlreadyRegisteredDownStation();
        }
    }

    public Long getId() {
        return id;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Long getDistance() {
        return distance;
    }

    public Line getLine() {
        return line;
    }

    public boolean hasStation(Station otherStation) {
        return this.upStation.equals(otherStation) || this.downStation.equals(otherStation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return Objects.equals(id, section.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
