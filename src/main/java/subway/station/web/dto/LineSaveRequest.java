package subway.station.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SaveLineRequest {

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Long distance;
}
