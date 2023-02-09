package subway.station.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SectionSaveRequest {

    private Long upStationId;
    private Long downStationId;
    private Long distance;
}
