package nextstep.subway.acceptance.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LineCreationRequest {

    private String name;

    private String color;

    private long upStationId;

    private long downStationId;

    private int distance;

}
