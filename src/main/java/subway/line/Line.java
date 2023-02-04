package subway.line;

import subway.section.Section;
import subway.station.Station;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "line")
    private List<Section> sections = new ArrayList<>();


    public Line(String name, String color, Station upStation, Station downStation, Long distance) {
        this.name = name;
        this.color = color;
        addStation(upStation, 0);
        addStation(downStation, distance);
    }

    public Line(Long id) {
        this.id = id;
    }

    protected Line() {
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

    public void addStation(Station downStation, Station upStation, long distance) {
        checkStation(downStation, upStation);
        sections.add(new Section(downStation, this, distance));
    }

    private void checkStation(Station downStation, Station upStation) {
        if (!checkUpStation(upStation.getId())) {
            throw new RuntimeException("하행 종점역과 이어진 지하철 역만 추가할 수 있습니다.");
        }
        if (!checkDownStation(downStation.getId())) {
            throw new RuntimeException("이미 존재하는 지하철역은 추가할 수 없습니다.");
        }
    }

    private boolean checkDownStation(Long downStationId) {
        return getStations().stream().map(Station::getId).allMatch(id -> id.equals(downStationId));
    }

    public void addStation(Station station, long distance) {
        sections.add(new Section(station, this, distance));
    }

    private boolean checkUpStation(Long upStationId) {
        long lastStationId = getLastStationId();
        return Objects.equals(lastStationId, upStationId) || lastStationId == -1 ;
    }

    public Long getDistance() {
        return sections.stream().mapToLong(Section::getDistance).sum();
    }

    private long getLastStationId() {
        int lastIndex = sections.size() - 1;

        if (lastIndex == -1) {
            return -1;
        }
        return sections.get(lastIndex).getStation().getId();
    }

    public List<Station> getStations() {
        return this.sections.stream().map(Section::getStation).collect(Collectors.toList());
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void deleteStation(Long stationId) {
        if (Objects.equals(stationId, getLastStationId())) {
            throw new RuntimeException("하행 종점역이 아닌 지하철 역은 삭제할 수 없다.");
        }
        sections.remove(sections.size() - 1);
    }
}
