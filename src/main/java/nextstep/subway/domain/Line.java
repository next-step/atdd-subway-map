package nextstep.subway.domain;

import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nextstep.subway.exception.AlreadyRegisteredStationException;
import nextstep.subway.exception.RegisterStationException;

@Entity
public class Line extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String color;

    @Embedded
    private Sections sections;

    protected Line() {
    }

    public Line(String name, String color, Sections sections) {
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public void registerSection(Station upStation, Station downStation, int distance) {
        validateUpStation(upStation);
        validateDownStation(downStation);

        sections.register(upStation, downStation, distance);
    }

    private void validateUpStation(Station upStation) {
        if (!sections.isLastStation(upStation)) {
            throw new RegisterStationException();
        }
    }

    private void validateDownStation(Station downStation) {
        if (sections.contianStation(downStation)) {
            throw new AlreadyRegisteredStationException();
        }
    }

    public void update(String updateName, String updateColor) {
        this.name = updateName;
        this.color = updateColor;
    }

    public void deleteSection(Station station) {
        sections.deleteSection(station);
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
        return sections.getStations();
    }

}
