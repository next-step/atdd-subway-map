package subway.line.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.station.dto.StationResponse;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LineResponse {
    private Long id;
    private String name;
    private String color;
    List<StationResponse> stations;
}
