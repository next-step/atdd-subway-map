package subway.line;

import subway.section.Section;
import subway.section.SectionRequest;
import subway.station.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    @OneToMany(mappedBy = "line", orphanRemoval = true)
    private List<Station> stations = new ArrayList<>();

    @OneToMany(mappedBy = "line")
    private List<Section> sections = new ArrayList<>();

    public Line() {}

    public Line(String name, String color, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
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

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void changeLineInfo(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void changeDownStationId(SectionRequest sectionRequest) {
        this.downStationId = sectionRequest.getDownStationId();
        this.distance = sectionRequest.getDistance();
    }

    public boolean isExistStation(Station newStation) {
        for(Station station : stations) {
            if(newStation.getId() == station.getId()){
                return true;
            }
        }

        return false;
    }

    public void deleteStation(Long deleteStationId) {
        if(sections.size() == 1) {
            throw new IllegalArgumentException("구간이 1개 남은 경우 삭제할 수 없습니다.");
        }

        if(deleteStationId != downStationId){
            throw new IllegalArgumentException("노선의 하행 종점역이 아닙니다.");
        }

        stations.removeIf(station -> station.getId() == deleteStationId);

        downStationId = sections.stream()
                .filter(v -> v.getDownStationId() == deleteStationId)
                .findAny().get()
                .getUpStationId();
    }
}
