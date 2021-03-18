package nextstep.subway.line.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Line extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String color;

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
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

    public Section addSection(Station upStation, Station downStation, int distance) {

        Section section = new Section(this, upStation, downStation, distance);

        if (sections.isEmpty()) {
            sections.addSection(section);
            return section;
        }

        // 새로운 구간의 상행역은 현재 등록되어있는 하행 종점역이어야 한다.
        Station downEndStation = sections.getDownEndStation();
        if (upStation != downEndStation) {
            throw new RuntimeException();
        }

        // 새로운 구간의 하행역은 현재 등록되어있는 역일 수 없다.
        if (sections.isAlreadyAddedStation(downStation)) {
            throw new RuntimeException();
        }

        sections.addSection(section);
        return section;
    }

    public List<Station> getAllStations() {
        return sections.getAllStations();
    }

    private boolean canDeleteSection() {
        return getAllStations().size() <= 2;
    }

    private Section getLastSection() {
        return sections.getLastSection();
    }

    public void deleteLastSection(Station station) {

        if (canDeleteSection()) {
            throw new RuntimeException("상행 종점역과 하행 종점역만 있는 경우 역을 삭제할 수 없습니다.");
        }

        Section lastSection = getLastSection();
        if (!station.equals(lastSection.getDownStation())) {
            throw new UnsupportedOperationException("노선의 마지막 역(하행 종점역)만 제거할 수 있습니다.");
        }

        sections.deleteSection(lastSection);
    }

}
