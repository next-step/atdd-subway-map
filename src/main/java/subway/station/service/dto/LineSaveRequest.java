package subway.station.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LineSaveRequest {

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Long distance;
}
