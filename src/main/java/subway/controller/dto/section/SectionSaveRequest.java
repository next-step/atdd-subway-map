package subway.controller.dto.section;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SectionSaveRequest {
    private Long upStationId;
    private Long downStationId;
    private Long distance;
}
