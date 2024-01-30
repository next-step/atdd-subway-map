package subway.line;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import subway.section.Section;
import subway.station.Station;

import javax.persistence.*;
import java.util.ArrayList;
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
    @OneToMany(mappedBy = "line", fetch = FetchType.LAZY, orphanRemoval = true)
    List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(final String name, final String color, final Long upStationId, final Long downStationId) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
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
        return sections;
    }

    public void changeName(final String name) {
        this.name = name;
    }

    public void changeColor(final String color) {
        this.color = color;
    }

    public void registerValidate(final Station upStation, final Station downStation) {
        checkLineDownStationAndSectionUpStation(upStation);

        checkLineStationsDuplicate(downStation);
    }

    private void checkLineDownStationAndSectionUpStation(final Station upStation) {
        if (this.downStationId != upStation.getId()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "노선의 하행종점역과 등록하려는 구간의 상행역이 다릅니다.");
        }
    }

    private void checkLineStationsDuplicate(final Station downStation) {
        for (Section section : this.sections) {
            checkSameStation(downStation, section);
        }
    }

    private static void checkSameStation(final Station downStation, final Section section) {
        if (section.getUpStation().getId() == downStation.getId()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 등록되어 있는 지하철역 입니다.");
        }
    }

    public void deleteValidate(final Long stationId) {
        if (this.downStationId != stationId) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제할 수 없는 지하철 역 입니다.");
        }
    }
}
