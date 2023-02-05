package subway.section;

import lombok.Getter;

@Getter
public class SectionRequest {

    private Integer downStationId;

    private Integer upStationId;

    private Integer distance;
}
