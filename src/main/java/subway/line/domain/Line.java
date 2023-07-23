package subway.line.domain;

import subway.global.exception.BusinessException;

import javax.persistence.*;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    private String color;

    private Long upStationId;

    private Long downStationId;
    private Integer distance;

    protected Line() {}

    public Line(String name, String color, Integer distance) {
        this.name = name;
        this.color = color;
        this.distance = distance;
    }

    public void changeStations(Long upStationId, Long downStationId){
        this.upStationId = upStationId;
        this.downStationId = downStationId;
    }

    public void changeLine(String name, String color){
        this.name = name;
        this.color = color;
    }

    public void changeDownStation(Long newDownStationId){
        this.downStationId = newDownStationId;
    }

    public void checkIfUpStationIdEqual(Long upStationId){
        if(!this.getDownStationId().equals(upStationId))    throw new BusinessException("새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종착역이어야 한다");
    }

    public void checkIfNotDownStation(Long stationId){
        if(!this.getDownStationId().equals(stationId)) throw new BusinessException("제거하는 지하철 구간이 노선의 마지막 구간이 아닐 경우 삭제할 수 없습니다.");
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

    public Integer getDistance() {
        return distance;
    }
}
