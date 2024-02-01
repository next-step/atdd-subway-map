package subway.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class StationSection {
    public static final int MIN_STATION_ID_VALUE = 1;
    public static final int MIN_DISTANCE_VALUE = 1;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long upStationId;

    private Long downStationId;

    private int distance;

    @JsonIgnore
    @ManyToOne
    private StationLine stationLine;

    protected StationSection() {
    }

    public StationSection(Long upStationId, Long downStationId, int distance) {
        this.upStationId = validateStationId(upStationId, "upStationId");
        this.downStationId = validateStationId(downStationId, "downStationId");
        this.distance = validateDistance(distance);
    }

    private Long validateStationId(Long stationId, String fieldName) {
        if (stationId == null || stationId < MIN_STATION_ID_VALUE) {
            throw new IllegalArgumentException(fieldName + "은(는) 0보다 커야합니다.");
        }
        return stationId;
    }

    private int validateDistance(Integer distance) {
        if(distance == null || distance < MIN_DISTANCE_VALUE) {
            throw new IllegalArgumentException("거리는 0보다 커야합니다.");
        }
        return distance;
    }

    public boolean canSave(StationLine stationLine) {
        // 1. 요청된 상행, 하행역이 같은 번호인지?
        if(this.upStationId.equals(this.downStationId)) {
            return false;
        }

        // 2. 요청된 상행역이 하행 종점역으로 등록되었는지?
        if(!this.upStationId.equals(stationLine.getDownStationId())) {
            return false;
        }

        // 3. 요청된 하행 종점역이 Line에 저장된 구간별 상행역 번호가 아닌지?
        return stationLine.getSections().stream()
                .noneMatch(stationSection -> this.downStationId.equals(stationSection.getUpStationId()));
    }

    public Long getId() {
        return id;
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

    public StationLine getStationLine() {
        return stationLine;
    }

    public void setStationLine(StationLine stationLine) {
        this.stationLine = stationLine;
        stationLine.getSections().add(this);
    }

    public void updateLineDownStationId() {
        this.stationLine.updateDownStation(this.downStationId);
    }
}
