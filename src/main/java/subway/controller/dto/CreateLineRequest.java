package subway.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.domain.contract.LineCommand;

@Getter
@AllArgsConstructor
public class CreateLineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Long distance;

    public LineCommand.CreateLine toCommand() {
        return new LineCommand.CreateLine(name, color, upStationId, downStationId, distance);
    }
}
