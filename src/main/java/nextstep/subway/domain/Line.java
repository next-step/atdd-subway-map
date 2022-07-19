package nextstep.subway.domain;

import nextstep.subway.exception.StationNotRegisteredException;

import javax.persistence.*;
import java.util.List;

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
        sections.add(new Section(this, upStation, downStation, distance));
        return this;
    }

    public Station lastStation() {
        if (sections.isEmpty()) {
            throw new StationNotRegisteredException("노선에 등록된 역이 없습니다.");
        }
        return sections.lastStation();
    }

    public boolean hasStation(long id) {
        return sections.hasStation(id);
    }

    public void deleteSection(Long stationId) {
        sections.delete(stationId);
    }

}
