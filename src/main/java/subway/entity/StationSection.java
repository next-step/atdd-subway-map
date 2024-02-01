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
        if(areStationsSame()) {
            return false;
        }
        if(hasUpStation(stationLine)) {
            return false;
        }
        return containsDifferentDownStation(stationLine);
    }

    private boolean containsDifferentDownStation(StationLine stationLine) {
        return stationLine.getSections().stream()
                .noneMatch(stationSection -> this.downStationId.equals(stationSection.getUpStationId()));
    }

    private boolean hasUpStation(StationLine stationLine) {
        return !this.upStationId.equals(stationLine.getDownStationId()); // TODO: 호출 위치 변경
    }

    private boolean areStationsSame() {
        return upStationId.equals(downStationId);
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
