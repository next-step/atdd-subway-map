package nextstep.subway.domain;

import nextstep.subway.exception.BadRequestException;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.List;
import java.util.Objects;

@Entity
public class Section extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    private int distance;

    public Section() {
    }

    private Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section createNewLineSection(Line line, Station upStation, Station downStation, int distance) {
        return new Section(line, upStation, downStation, distance);
    }

    public static Section createAddSection(Line line, Station upStation, Station downStation, int distance) {
        validateDownEndStationNotEqualsNewUpStation(line, upStation);
        validateNewDownEndStationAlReadyStation(line, downStation);

        Section section = new Section(line, upStation, downStation, distance);
        line.addSection(section);

        return section;
    }

    private static void validateDownEndStationNotEqualsNewUpStation(Line line, Station upStation) {
        Station downEndStation = line.getSections().get(line.getSections().size() - 1).getDownStation();
        if (!upStation.equals(downEndStation)) {
            throw new BadRequestException(
                    String.format(Sections.THE_UP_STATION_AND_DOWN_END_STATIONS_DIFFERENT_EXCEPTION_MESSAGE,
                            downEndStation.getName(), upStation.getName()
                    )
            );
        }
    }

    private static void validateNewDownEndStationAlReadyStation(Line line, Station downStation) {
        List<Section> sections = line.getSections();
        for (Section section : sections) {
            if (downStation.equals(section.getUpStation()) || downStation.equals(section.getDownStation())) {
                throw new BadRequestException(
                        String.format(Sections.STATIONS_WHERE_DOWN_END_STATION_ALREADY_EXCEPTION_MESSAGE,
                                downStation.getName()
                        )
                );
            }
        }
    }

    public Long getId() {
        return id;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Section section = (Section) o;
        return getDistance() == section.getDistance() && Objects.equals(getId(), section.getId()) &&
               Objects.equals(line, section.line) && Objects.equals(getUpStation(), section.getUpStation()) &&
               Objects.equals(getDownStation(), section.getDownStation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), line, getUpStation(), getDownStation(), getDistance());
    }
}
