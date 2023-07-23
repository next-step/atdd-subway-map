package subway;

import javax.persistence.*;
import java.util.Comparator;
import java.util.List;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 255, nullable = false)
    private String name;
    @Column(nullable = false)
    private String color;
    @Column(nullable = false)
    private Integer distance;
    @OneToMany(mappedBy = "line", cascade = CascadeType.REMOVE)
    private List<LineStation> lineStations;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<LineStation> getLineStations() {
        return lineStations;
    }

    public Integer getDistance() {
        return distance;
    }

    public Line() {
    }

    public Line(String name, String color, Integer distance) {
        this.name = name;
        this.color = color;
        this.distance = distance;
    }

    public void updateLine(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void updateStations(List<LineStation> lineStations) {
        this.lineStations = lineStations;
    }

    public void isAddableLine(Station upStation, Station downStation) {
        //새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이여야 한다.
        LineStation prevDownStation = lineStations.stream().max(Comparator.comparing(LineStation::getSequence)).get();
        if(!prevDownStation.getStation().equals(upStation)) {
            throw new RuntimeException("새로운 구간의 상행역은 해당 노선에 등록되어 있는 하행 종점역이어야 한다.");
        }
        //새로운 구간 하행역은 해당 노선에 등록되어있는 역일 수 없다.
        if(lineStations.stream().anyMatch(item -> item.getStation().equals(downStation))) {
            throw new RuntimeException("새로운 구간 하행역은 해당 노선에 등록되어있는 역일 수 없다.");
        }
    }

    public void isDeletableLine(Long downStationId) {
        if(lineStations.size() == 2) {
            throw new RuntimeException("구간이 1개인 경우 역을 삭제할 수 없다.");
        }

        if(!lineStations.stream().max(Comparator.comparing(LineStation::getSequence)).get().getStation().getId().equals(downStationId)) {
            throw new RuntimeException("지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. ");
        }
    }
}
