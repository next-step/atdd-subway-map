package subway.section;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import subway.line.LineRequest;

@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionRequest {
    private Long upStationId;

    private Long downStationId;

    private int distance;

    public static SectionRequest of(LineRequest lineRequest) {
        return new SectionRequest(
            lineRequest.getUpStationId(),
            lineRequest.getDownStationId(),
            lineRequest.getDistance()
        );
    }
}
