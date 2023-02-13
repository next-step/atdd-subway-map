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
    private Long totalDistance;


    @Convert(converter = StationListConverter.class)
    private List<Long> distances = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color, Long upStationId, Long downStationId, Long totalDistance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.totalDistance = totalDistance;

        this.stations.add(upStationId);
        this.stations.add(downStationId);
        this.distances.add(totalDistance);
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

    public Long getTotalDistance() {
        return totalDistance;
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
        stations.add(downStationId);
        distances.add(distance);
        this.downStationId = downStationId;
        this.totalDistance += distance;
    }

    public void removeSelection(Long stationId){
        int removeIndex = stations.indexOf(stationId);
        int distanceIndex = Math.max(removeIndex - 1, 0);

        Long removeDistance = distances.get(distanceIndex);
        stations.remove(removeIndex);
        distances.remove(distanceIndex);
        downStationId = stations.get(stations.size()-1);
        totalDistance -= removeDistance;
    }

}
