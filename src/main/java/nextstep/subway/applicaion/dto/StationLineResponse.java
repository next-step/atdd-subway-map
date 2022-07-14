package nextstep.subway.applicaion.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StationLineResponse {
    private Long id;
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;
}
