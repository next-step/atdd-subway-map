package subway.section;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SectionCreateRequest {
    private Long downStationId;
    private Long upStationId;
    private int distance;
}
