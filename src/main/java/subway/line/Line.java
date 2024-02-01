package subway.line;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import subway.section.Section;
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
    private Long upStationId;
    private Long downStationId;
    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStation.getId();
        this.downStationId = downStation.getId();

        Section section = new Section(upStation, downStation, distance, this);
        this.sections.add(section);
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

    public List<Section> getSections() {
        return this.sections.getSections();
    }

    public void changeName(final String name) {
        this.name = name;
    }

    public void changeColor(final String color) {
        this.color = color;
    }

    public void registerValidate(final Station upStation, final Station downStation) {
        checkEqualsLineDownStation(upStation);

        this.sections.checkLineStationsDuplicate(downStation);
    }

    private void checkEqualsLineDownStation(final Station upStation) {
        if (!this.downStationId.equals(upStation.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "노선의 하행종점역과 등록하려는 구간의 상행역이 다릅니다.");
        }
    }

    public void deleteValidate(final Long stationId) {
        if (!this.downStationId.equals(stationId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제할 수 없는 지하철 역 입니다.");
        }

        if (this.sections.count() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제할 수 없는 지하철 역 입니다.");
        }
    }

    public void changeDownStation(final Station station) {
        this.downStationId = station.getId();
    }

    public void removeSection(final Long stationId) {
        this.sections.removeSection(stationId);
    }
}
