package subway.line;

import subway.exception.ErrorResponseCode;
import subway.exception.SubwayIllegalArgumentException;
import subway.section.Section;
import subway.section.Sections;
import subway.station.Station;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    protected Line() {
    }

    @Embedded
    private Sections sections = new Sections();

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line updateLine(String name, String color) {
        this.name = name;
        this.color = color;
        return this;
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        if (sections.isEmpty()) {
            sections.addSection(new Section(this, upStation, downStation, distance));
            return;
        }

        if (!sections.isLastStation(upStation)) {
            throw new SubwayIllegalArgumentException(ErrorResponseCode.NOT_EQUAL_LAST_STATION);
        }

        if (sections.containsStation(downStation)) {
            throw new SubwayIllegalArgumentException(ErrorResponseCode.DUPLICATED_DOWN_STATION);
        }
        sections.addSection(new Section(this, upStation, downStation, distance));
    }

    public void removeSection(Station station) {
        if (!sections.isLastStation(station)) {
            throw new SubwayIllegalArgumentException(ErrorResponseCode.NOT_THE_LAST_STATION);
        }
        if (sections.isSingleSection()) {
            throw new SubwayIllegalArgumentException(ErrorResponseCode.SINGLE_SECTION_ERROR);
        }
        sections.remove();
    }

    public List<Station> getAllStations() {
        return sections.getStations();
    }

    public int getSumDistance() {
        return sections.calcDistance();
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

    public Sections getSections() {
        return sections;
    }
}
