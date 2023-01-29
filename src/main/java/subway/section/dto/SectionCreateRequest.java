package subway.section.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SectionCreateRequest {

    private long lineId;
    private long downStationId;
    private long upStationId;
    private long distance;
}
