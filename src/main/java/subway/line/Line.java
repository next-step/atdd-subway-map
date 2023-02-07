package subway.line;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 30, nullable = false)
    private String name;

    @Column(length = 30, nullable = false)
    private String color;

    private Long upStationId;

    private Long downStationId;


    @Convert(converter = StationListConverter.class)
    private List<Long> stations = new ArrayList<>();

    //우선 단위는 km라고 생각.
    private Long distance;

    public Line() {
    }

    public Line(String name, String color, Long upStationId, Long downStationId, Long distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;

        this.stations.add(upStationId);
        this.stations.add(downStationId);
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

    public Long getDistance() {
        return distance;
    }

    public List<Long> getStations() {
        return stations;
    }

    public void updateLine(String name, String color){
        this.name = name;
        this.color = color;
    }

    public static Line createLine(String name, String color, Long upStationId, Long downStationId, Long distance){
        Line newLine = new Line(name, color, upStationId, downStationId, distance);
        return newLine;
    }

    public void appendSelection(Long upStationId, Long downStationId, Long distance){
        if(!upStationId.equals(this.downStationId)){
            throw new IllegalStateException("추가되는 상행은, 기존 하행과 같아야 됩니다.");
        }

        if(stations.contains(downStationId)){
            throw new IllegalStateException("추가되는 하행은, 기존 노선에 없어야 됩니다. ");
        }

        stations.add(downStationId);
        this.downStationId = downStationId;
        this.distance += distance;
    }

}
