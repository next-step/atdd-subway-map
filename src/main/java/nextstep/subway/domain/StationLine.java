package nextstep.subway.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.applicaion.dto.StationLineRequest;
import nextstep.subway.applicaion.dto.StationRequest;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class StationLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    private Long upStationId;

    private Long downStationId;

    private Integer distance;

    @Builder
    public StationLine(String name, String color, Long upStationId, Long downStationId, Integer distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static StationLine of(StationLineRequest stationLineRequest) {
        return StationLine.builder()
                .name(stationLineRequest.getName())
                .color(stationLineRequest.getColor())
                .upStationId(stationLineRequest.getUpStationId())
                .downStationId(stationLineRequest.getDownStationId())
                .build();
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

}
