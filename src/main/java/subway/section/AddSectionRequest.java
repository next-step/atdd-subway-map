package subway.section;

import lombok.Getter;

@Getter
public class AddSectionRequest {

    private Long downStationId;

    private Long upStationId;

    private Integer distance;

}
