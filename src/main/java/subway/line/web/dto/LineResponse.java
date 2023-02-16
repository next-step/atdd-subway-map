package subway.line.web.dto;

import lombok.Builder;
import lombok.Getter;
import subway.station.web.StationResponse;

import java.util.List;

@Getter
@Builder
public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

}
