package subway.line;

import subway.exception.ErrorResponseCode;
import subway.exception.SubwayRestApiException;
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

    private int distance;

    @OneToOne
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @OneToOne
    @JoinColumn(name = "down_station_id")
    private Station downStation;

        protected Line() {
    }

    @Embedded
    private Sections sections = new Sections();

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
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
            throw new SubwayRestApiException(ErrorResponseCode.DUPLICATED_DOWN_STATION);
        }

        if (sections.containsStation(downStation)) {
            throw new SubwayRestApiException(ErrorResponseCode.NOT_EQUAL_LAST_STATION);
        }
        sections.addSection(new Section(this, upStation, downStation, distance));
    }

    public void removeSection(Station station) {
        if (!sections.isLastStation(station)) {
            throw new SubwayRestApiException(ErrorResponseCode.NOT_THE_LAST_STATION);
        }
        if (sections.isSingleSection()) {
            throw new SubwayRestApiException(ErrorResponseCode.SINGLE_SECTION_ERROR);
        }
        sections.remove();
    }

    public List<Station> getStations() {
        return sections.getStations();
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

    public int getDistance() {
        return distance;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Sections getSections() {
        return sections;
    }
}
