package subway.domain;

import java.util.List;

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

    public static Section from(Line line, Station upStation, Station downStation, Long distance, List<Section> lineSections) {
        if (lineSections.isEmpty()) {
            return Section.withNoId(downStation, upStation, distance, line);
        }

        Section lineDownSection = lineSections.get(lineSections.size() - 1);

        Station lineDownStation = lineDownSection.getDownStation();

        if (!lineDownStation.equals(upStation)) {
            throw new NotEqualUpStationAndDownStationException();
        }

        if (lineSections.stream().anyMatch(section -> section.hasStation(downStation))) {
            throw new AlreadyRegisteredDownStation();
        }

        return Section.withNoId(downStation, upStation, distance, line);
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

}
