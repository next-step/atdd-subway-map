package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.StationLineRequest;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class StationLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    private Long distance;
    private Long upStationId;
    private Long downStationId;
    private String stationsIncluded;

    public StationLine() {
    }

    public StationLine(String name, String color, Long distance, Long upStationId, Long downStationId) {
        this.name = name;
        this.distance = distance;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
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

    public Long getDistance() {
        return distance;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public String getStationsIncluded() {
        return stationsIncluded;
    }

    public void setDownStationId(Long downStationId) {
        this.downStationId = downStationId;
    }

    public void setStationsIncluded(String stationIncluded) {
        this.stationsIncluded = stationIncluded;
    }

    public void updateByStationLineRequest(StationLineRequest stationLineRequest) {
        if (stationLineRequest.getName() != null) this.name = stationLineRequest.getName();
        if (stationLineRequest.getColor() != null) this.color = stationLineRequest.getColor();
        if (stationLineRequest.getUpStationId() != null) this.upStationId = stationLineRequest.getUpStationId();
        if (stationLineRequest.getDownStationId() != null) this.downStationId = stationLineRequest.getDownStationId();
        if (stationLineRequest.getDistance() != null) this.distance = stationLineRequest.getDistance();
    }

    public List<Long> stationIdsIncludedInLine(){
        if(StringUtils.isBlank(this.stationsIncluded)) return new ArrayList<>();
        return Arrays.stream(this.stationsIncluded.split(",")).map(s -> Long.parseLong(s)).collect(Collectors.toList());
    }
}
