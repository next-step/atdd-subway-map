package subway.line;

import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.common.exception.line.DownStationCouldNotExistStationExcetion;
import subway.common.exception.line.UpStationMustTermianlStationException;
import subway.station.Station;

import javax.persistence.*;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String color;

    @Embedded
    private final Sections sections = new Sections();

    public Line(final String name, final String color, final Section section) {
        this.name = name;
        this.color = color;
        addSection(section);
    }

    public void addSection(final Section section) {
        this.sections.add(section);
        section.addLine(this);
    }

    public void update(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public void validate(final Station downStation, final Station upStation) {
        if (sections.hasStation(downStation)) {
            throw new DownStationCouldNotExistStationExcetion();
        }

        if (!isUpStationEqualsTerminalStation(upStation)) {
            throw new UpStationMustTermianlStationException();
        }
    }

    private boolean isUpStationEqualsTerminalStation(final Station upStation) {
        return sections.getTerminalStation() == upStation;
    }

    public List<Station> getStations() {
        return this.sections.getStations();
    }
}
