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

    public boolean connectToLastUpStation(Long downStationId) {
        return this.upStationId.equals(downStationId);
    }

    public boolean areStationsSame() {
        return upStationId.equals(downStationId);
    }

    public boolean isUpStationSameAsDownStation(StationSection toSaveSection) {
        return upStationId.equals(toSaveSection.getDownStationId());
    }

    public StationSection setStationLine(StationLine stationLine) {
        this.stationLine = stationLine;
        stationLine.getSections().add(this);

        return this;
    }

    public void updateLineDownStationId() {
        this.stationLine.updateDownStation(this.downStationId);
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
}
