package subway.controller.line.dto;

import lombok.Getter;

@Getter
public class AddSectionRequest {

    private Long downStationId;

    private Long upStationId;

    private Integer distance;
}
